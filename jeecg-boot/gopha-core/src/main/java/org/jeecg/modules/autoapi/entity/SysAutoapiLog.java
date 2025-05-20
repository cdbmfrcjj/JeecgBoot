package org.jeecg.modules.autoapi.entity;

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

/**
 * @Description: 数据交互日志
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Data
@TableName("sys_autoapi_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="数据交互日志")
public class SysAutoapiLog implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
	/**创建人*/
    @Schema(description = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private String sysOrgCode;
	/**接口名称*/
	@Excel(name = "接口名称", width = 15)
    @Schema(description = "接口名称")
    private String name;
	/**调用地址*/
	@Excel(name = "调用地址", width = 15)
    @Schema(description = "调用地址")
    private String address;
	/**调用参数*/
	@Excel(name = "调用参数", width = 15)
    @Schema(description = "调用参数")
    private String params;
	/**调用者IP*/
	@Excel(name = "调用者IP", width = 15)
    @Schema(description = "调用者IP")
    private String ip;
	/**数据内容*/
	@Excel(name = "数据内容", width = 15)
    @Schema(description = "数据内容")
    private String data;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private String status;
}
