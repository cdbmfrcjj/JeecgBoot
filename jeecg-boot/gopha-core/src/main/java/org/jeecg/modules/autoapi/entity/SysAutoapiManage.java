package org.jeecg.modules.autoapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 动态接口开发管理
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Data
@TableName("sys_autoapi_manage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="动态接口开发管理")
public class SysAutoapiManage implements Serializable {
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
	/**名称*/
	@Excel(name = "名称", width = 15)
    @Schema(description = "名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @Schema(description = "描述")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String description;
	/**参数描述*/
	@Excel(name = "参数描述", width = 15)
    @Schema(description = "参数描述")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String paramsDescription;
	/**执行参数*/
	@Excel(name = "执行参数", width = 15)
    @Schema(description = "执行参数")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String doParams;
	/**身份验证*/
	@Excel(name = "身份验证", width = 15, dicCode = "autoapiPermissions")
    @Schema(description = "身份验证")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String permission;
    /**执行日志*/
	@Excel(name = "执行日志", width = 15, dicCode = "autoapiPermissions")
    @Schema(description = "执行日志")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String saveLog;
	/**通用处理*/
	@Excel(name = "通用处理", width = 15, dictTable = "sys_autoapi_handle", dicText = "name", dicCode = "id")
	@Dict(dictTable = "sys_autoapi_handle", dicText = "name", dicCode = "id")
    @Schema(description = "通用处理")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String handleId;
	/**代码*/
	@Excel(name = "代码", width = 15)
    @Schema(description = "代码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String codeText;
}
