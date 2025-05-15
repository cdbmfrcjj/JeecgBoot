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
import org.jeecg.modules.autoapi.entity.SysAutoapiHandle;
import org.jeecg.modules.autoapi.service.ISysAutoapiHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 通用处理
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Tag(name="通用处理")
@RestController
@RequestMapping("/autoapi/sysAutoapiHandle")
@Slf4j
public class SysAutoapiHandleController extends JeecgController<SysAutoapiHandle, ISysAutoapiHandleService> {
	@Autowired
	private ISysAutoapiHandleService sysAutoapiHandleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysAutoapiHandle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "通用处理-分页列表查询")
	@Operation(summary ="通用处理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysAutoapiHandle>> queryPageList(SysAutoapiHandle sysAutoapiHandle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysAutoapiHandle> queryWrapper = QueryGenerator.initQueryWrapper(sysAutoapiHandle, req.getParameterMap());
		Page<SysAutoapiHandle> page = new Page<SysAutoapiHandle>(pageNo, pageSize);
		IPage<SysAutoapiHandle> pageList = sysAutoapiHandleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysAutoapiHandle
	 * @return
	 */
	@AutoLog(value = "通用处理-添加")
	@Operation(summary="通用处理-添加")
	@RequiresPermissions("autoapi:sys_autoapi_handle:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysAutoapiHandle sysAutoapiHandle) {
		sysAutoapiHandleService.save(sysAutoapiHandle);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysAutoapiHandle
	 * @return
	 */
	@AutoLog(value = "通用处理-编辑")
	@Operation(summary ="通用处理-编辑")
	@RequiresPermissions("autoapi:sys_autoapi_handle:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysAutoapiHandle sysAutoapiHandle) {
		sysAutoapiHandleService.updateById(sysAutoapiHandle);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "通用处理-通过id删除")
	@Operation(summary ="通用处理-通过id删除")
	@RequiresPermissions("autoapi:sys_autoapi_handle:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysAutoapiHandleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "通用处理-批量删除")
	@Operation(summary ="通用处理-批量删除")
	@RequiresPermissions("autoapi:sys_autoapi_handle:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAutoapiHandleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "通用处理-通过id查询")
	@Operation(summary ="通用处理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysAutoapiHandle> queryById(@RequestParam(name="id",required=true) String id) {
		SysAutoapiHandle sysAutoapiHandle = sysAutoapiHandleService.getById(id);
		if(sysAutoapiHandle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysAutoapiHandle);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysAutoapiHandle
    */
    @RequiresPermissions("autoapi:sys_autoapi_handle:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysAutoapiHandle sysAutoapiHandle) {
        return super.exportXls(request, sysAutoapiHandle, SysAutoapiHandle.class, "通用处理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("autoapi:sys_autoapi_handle:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysAutoapiHandle.class);
    }

}
