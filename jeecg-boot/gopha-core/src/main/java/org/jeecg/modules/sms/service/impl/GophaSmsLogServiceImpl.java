package org.jeecg.modules.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.sms.entity.GophaSmsLog;
import org.jeecg.modules.sms.mapper.GophaSmsLogMapper;
import org.jeecg.modules.sms.service.IGophaSmsLogService;
import org.springframework.stereotype.Service;

/**
 * @Description: 短信日志
 * @Author: gopha
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
public class GophaSmsLogServiceImpl extends ServiceImpl<GophaSmsLogMapper, GophaSmsLog> implements IGophaSmsLogService {

}
