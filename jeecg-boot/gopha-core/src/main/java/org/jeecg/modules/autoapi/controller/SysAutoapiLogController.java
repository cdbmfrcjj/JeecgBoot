package org.jeecg.modules.autoapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.autoapi.entity.SysAutoapiLog;
import org.jeecg.modules.autoapi.service.ISysAutoapiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 数据交互日志
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Tag(name="数据交互日志")
@RestController
@RequestMapping("/autoapi/sysAutoapiLog")
@Slf4j
public class SysAutoapiLogController extends JeecgController<SysAutoapiLog, ISysAutoapiLogService> {
	@Autowired
	private ISysAutoapiLogService sysAutoapiLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysAutoapiLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "数据交互日志-分页列表查询")
	@Operation(summary ="数据交互日志-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysAutoapiLog>> queryPageList(SysAutoapiLog sysAutoapiLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysAutoapiLog> queryWrapper = QueryGenerator.initQueryWrapper(sysAutoapiLog, req.getParameterMap());
		Page<SysAutoapiLog> page = new Page<SysAutoapiLog>(pageNo, pageSize);
		IPage<SysAutoapiLog> pageList = sysAutoapiLogService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysAutoapiLog
	 * @return
	 */
	@AutoLog(value = "数据交互日志-添加")
	@Operation(summary ="数据交互日志-添加")
	@RequiresPermissions("autoapi:sys_autoapi_log:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysAutoapiLog sysAutoapiLog) {
		sysAutoapiLogService.save(sysAutoapiLog);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysAutoapiLog
	 * @return
	 */
	@AutoLog(value = "数据交互日志-编辑")
	@Operation(summary ="数据交互日志-编辑")
	@RequiresPermissions("autoapi:sys_autoapi_log:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysAutoapiLog sysAutoapiLog) {
		sysAutoapiLogService.updateById(sysAutoapiLog);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据交互日志-通过id删除")
	@Operation(summary ="数据交互日志-通过id删除")
	@RequiresPermissions("autoapi:sys_autoapi_log:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysAutoapiLogService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据交互日志-批量删除")
	@Operation(summary ="数据交互日志-批量删除")
	@RequiresPermissions("autoapi:sys_autoapi_log:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAutoapiLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "数据交互日志-通过id查询")
	@Operation(summary ="数据交互日志-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysAutoapiLog> queryById(@RequestParam(name="id",required=true) String id) {
		SysAutoapiLog sysAutoapiLog = sysAutoapiLogService.getById(id);
		if(sysAutoapiLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysAutoapiLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysAutoapiLog
    */
    @RequiresPermissions("autoapi:sys_autoapi_log:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysAutoapiLog sysAutoapiLog) {
        return super.exportXls(request, sysAutoapiLog, SysAutoapiLog.class, "数据交互日志");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("autoapi:sys_autoapi_log:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysAutoapiLog.class);
    }

}
