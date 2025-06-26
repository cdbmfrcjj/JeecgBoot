package org.jeecg.modules.autoapi.controller;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.autoapi.entity.SysAutoapiHandle;
import org.jeecg.modules.autoapi.entity.SysAutoapiLog;
import org.jeecg.modules.autoapi.entity.SysAutoapiManage;
import org.jeecg.modules.autoapi.service.AfterExecuteHandleService;
import org.jeecg.modules.autoapi.service.ExecuteHandleService;
import org.jeecg.modules.autoapi.service.ISysAutoapiHandleService;
import org.jeecg.modules.autoapi.service.ISysAutoapiLogService;
import org.jeecg.modules.autoapi.service.ISysAutoapiManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import groovy.lang.GroovyClassLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/autoApi/")
public class SysAutoApiExecuteController {

    @Autowired
    private ISysAutoapiManageService sysAutoapiManageService;

    @Autowired
    private ISysAutoapiHandleService sysAutoapiHandleService;

    @Autowired
    private ISysAutoapiLogService sysAutoapiLogService;

    @RequestMapping("/execute/{type}/{name}")
    @Transactional
    public Object execute(HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> header, @PathVariable String type, @PathVariable String name) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            paramsMap.put(paramName, paramValue);
        }
        Map<String, Object> jsonParamMap = new HashMap<String, Object>();
        BufferedReader br;
        try {
            br = request.getReader();
            String str, wholeStr = "";
            while ((str = br.readLine()) != null) {
                wholeStr += str;
            }
            if (StringUtils.isNotEmpty(wholeStr)) {
                jsonParamMap = JSON.parseObject(wholeStr, new TypeReference<Map<String, Object>>(){});
            }
            paramsMap.putAll(jsonParamMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object result = null;
        SysAutoapiLog sal = new SysAutoapiLog();
        QueryWrapper<SysAutoapiManage> queryWrapper = new QueryWrapper<SysAutoapiManage>();
        queryWrapper.eq("name", name);
        SysAutoapiManage sam = this.sysAutoapiManageService.getOne(queryWrapper);
        Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            String permissionFlag = (sam.getPermission().equals("disable") ? "public" : "private");
            if (!type.equals(permissionFlag)) {
                result = Result.error("调用地址错误！");
                sal.setStatus("失败");
                sal.setData(JSON.toJSONString(result));
                return result;
            }
            if (sam.getDoParams() != null && !"".equals(sam.getDoParams())) {
                Map<String, Object> initParams = JSON.parseObject(sam.getDoParams(), new TypeReference<Map<String, Object>>(){});
                paramsMap.putAll(initParams);
            }
            ApplicationContext context = SpringContextUtils.getApplicationContext();

            ClassLoader parent = this.getClass().getClassLoader();
            try (GroovyClassLoader loader = new GroovyClassLoader(parent)) {
                Class<?> dataHandleScript = loader.parseClass(sam.getCodeText());
                ExecuteHandleService executeHandleService = (ExecuteHandleService) dataHandleScript.getDeclaredConstructor().newInstance();
                context.getAutowireCapableBeanFactory().autowireBean(executeHandleService);
                result = executeHandleService.doPress(request, response, paramsMap);

                SysAutoapiHandle sah = this.sysAutoapiHandleService.getById(sam.getHandleId());
                if (StringUtils.isNotEmpty(sam.getHandleId()) && StringUtils.isNotEmpty(sam.getCodeText())) {
                    Class<?> afterHandleScript = loader.parseClass(sah.getCodeText());
                    AfterExecuteHandleService afterExecuteHandleService = (AfterExecuteHandleService) afterHandleScript.getDeclaredConstructor().newInstance();
                    result = afterExecuteHandleService.doPress(request, response, result);
                }
            }

            if (result instanceof Result) {
                sal.setData(JSON.toJSONString(result));
            } else if (result instanceof String) {
                sal.setData(result.toString());
            } else {
//                积木报表不支持Result格式，此处不再强制加
//                result=Result.OK(result);
                sal.setData(JSON.toJSONString(Result.OK(result)));
            }
            sal.setStatus("成功");
            return result;
        } catch (Exception e) {
            System.out.println("错误groovy:" + name);
            e.printStackTrace();
            result = Result.error(e.getMessage());
            sal.setData(JSON.toJSONString(result)).setStatus("失败");
            //动态接口内容大多使用sqlService执行动态sql 所以没有加service层控制事务,此处同意回滚
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return result;
        } finally {
            sal.setName(name).setAddress(request.getRequestURL().toString()).setIp(IpUtils.getIpAddr(request)).setParams(JSON.toJSONString(paramsMap));
            if ("enable".equals(sam.getSaveLog())) {
                this.sysAutoapiLogService.save(sal);
            }
        }
    }


}
