package org.jeecg.modules.autoapi.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import groovy.lang.GroovyClassLoader;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.autoapi.entity.SysAutoapiLog;
import org.jeecg.modules.autoapi.entity.SysAutoapiManage;
import org.jeecg.modules.autoapi.service.ExecuteHandleService;
import org.jeecg.modules.autoapi.service.ISysAutoapiLogService;
import org.jeecg.modules.autoapi.service.ISysAutoapiManageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class AutoGroovyJob implements Job {

    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Autowired
    private ISysAutoapiManageService sysAutoapiManageService;

    @Autowired
    private ISysAutoapiLogService sysAutoapiLogService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        QueryWrapper queryWrapper = new QueryWrapper<SysAutoapiManage>();
        queryWrapper.eq("name",parameter);
        SysAutoapiManage sam=this.sysAutoapiManageService.getOne(queryWrapper);
        SysAutoapiLog sal=new SysAutoapiLog();
        sal.setName("定时任务"+parameter);
        Object result=null;
        ApplicationContext context= SpringContextUtils.getApplicationContext();
        ClassLoader parent = this.getClass().getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);
        Class dataHandleScript = loader.parseClass(sam.getCodeText());
        ExecuteHandleService executeHandleService= null;
        try {
            executeHandleService = (ExecuteHandleService)dataHandleScript.newInstance();

            context.getAutowireCapableBeanFactory().autowireBean(executeHandleService);
            Object resutl=executeHandleService.doPress(null,null,null);
            System.out.println(resutl);

            if(sam==null){
                result= Result.error("调用地址错误！");
                sal.setStatus("失败");
                sal.setData(JSON.toJSONString(result));
            }
            if(result instanceof Result){
                sal.setData(JSON.toJSONString(result));
            }else if(result instanceof String){
                sal.setData(result.toString());
            }else{
                result=Result.OK(result);
                sal.setData(JSON.toJSONString(Result.OK(result)));
            }
            sal.setStatus("成功");

        } catch (Exception e) {
            e.printStackTrace();
            result=Result.error(e.getMessage());
            sal.setData(JSON.toJSONString(result)).setStatus("失败");
        }finally {
            this.sysAutoapiLogService.save(sal);
        }
    }
}
