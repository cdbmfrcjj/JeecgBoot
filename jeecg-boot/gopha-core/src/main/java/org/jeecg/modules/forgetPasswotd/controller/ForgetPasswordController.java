package org.jeecg.modules.forgetPasswotd.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.SqlService;
import org.jeecg.modules.msgCode.entity.SysMsgCode;
import org.jeecg.modules.msgCode.service.ISysMsgCodeService;
import org.jeecg.modules.sms.entity.GophaSmsLog;
import org.jeecg.modules.sms.service.IGophaSmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Description: app忘记密码
* @Author: gopha
* @Date:   2020-12-23
* @Version: V1.0
*/
@Tag(name="app忘记密码")
@RestController
@RequestMapping("/app/fp")
@Slf4j
public class ForgetPasswordController extends JeecgController<GophaSmsLog, IGophaSmsLogService> {

    @Autowired
    private ISysMsgCodeService sysMsgCodeService;

    @Autowired
    private IGophaSmsLogService gophaSmsLogService;

   @Autowired
   private SqlService sqlService;


    // 手机端忘记密码
    @RequestMapping(value = "/forgetPasswotd", method = RequestMethod.POST)
    public Result<?>  forgetPasswotd(@RequestBody JSONObject jsonObject) {

        String mobile = jsonObject.getString("mobile");
        String vcode = jsonObject.getString("vcode");
        String password = jsonObject.getString("password");
        String type = jsonObject.getString("type");

        List<Map<String,Object>> list = sqlService.selectList(" select * from sys_user where username = '"+mobile+"'");

        if(list.size() > 0) {
            Map<String,Object> map = list.get(0);
            String id = map.get("id").toString();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            QueryWrapper<SysMsgCode> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("mobile",mobile);
            // queryWrapper2.eq("vcode",vcode);
            queryWrapper2.like("create_time",df.format(new Date()));
            queryWrapper2.orderByDesc("create_time");
            List<SysMsgCode> list2 = sysMsgCodeService.list(queryWrapper2);
            if(list2.size() > 0) {
                SysMsgCode smc = list2.get(0);
                String code = smc.getVcode();
                if(!code.equals(vcode)) {
                    return Result.error("验证码错误！");
                }else {
                    String salt = oConvertUtils.randomGen(8);
                    String passwordEncode = PasswordUtil.encrypt(mobile, password, salt);
                    this.sqlService.update("update sys_user set salt = '"+salt+"',password = '"+passwordEncode+"' where id = '"+id+"' ");
                    return Result.OK("密码修改成功！");
                }
            }else {
                return Result.error("验证码错误！");
            }
        }else {
            return Result.error("手机号码不正确！");
        }
    }

}
