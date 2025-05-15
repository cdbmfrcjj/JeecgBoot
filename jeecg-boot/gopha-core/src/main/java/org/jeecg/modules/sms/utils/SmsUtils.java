package org.jeecg.modules.sms.utils;

public interface SmsUtils {
    public String sendSms(String tel) throws Exception;
    public void smsStatusJob() throws Exception;
}
