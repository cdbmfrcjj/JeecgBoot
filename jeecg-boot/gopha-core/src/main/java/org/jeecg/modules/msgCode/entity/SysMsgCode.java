package org.jeecg.modules.msgCode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 手机验证码
 * @Author: gopha
 * @Date:   2022-10-08
 * @Version: V1.0
 */
@Data
@TableName("sys_msg_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_msg_code对象", description="手机验证码")
public class SysMsgCode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String mobile;
	/**验证码*/
	@Excel(name = "验证码", width = 15)
    @ApiModelProperty(value = "验证码")
    private String vcode;
	/**当日获取数量*/
	@Excel(name = "当日获取数量", width = 15)
    @ApiModelProperty(value = "当日获取数量")
    private Integer count;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    // vo 短信内容
    private transient String content;

    // vo 手机号码
    private transient List<String> tel;


}
