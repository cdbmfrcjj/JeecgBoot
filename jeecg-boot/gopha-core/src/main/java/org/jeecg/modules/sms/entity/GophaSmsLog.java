package org.jeecg.modules.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 短信日志
 * @Author: gopha
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Data
@TableName("gopha_sms_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="短信日志")
public class GophaSmsLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
	/**发送人*/
    @Schema(description = "发送人")
    private String createBy;
	/**发送日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发送日期")
    private Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private String updateBy;
	/**状态更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "状态更新日期")
    private Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private String sysOrgCode;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @Schema(description = "内容")
    private String content;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @Schema(description = "电话")
    private String tel;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private String status;
	/**短信标识*/
	@Excel(name = "短信标识", width = 15)
    @Schema(description = "短信标识")
    private String identifier;
	/**发送次数*/
	@Excel(name = "发送次数", width = 15)
    @Schema(description = "发送次数")
    private Integer sendTmes;
}
