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
import org.jeecg.modules.autoapi.entity.SysAutoapiManage;
import org.jeecg.modules.autoapi.service.ISysAutoapiManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 动态接口开发管理
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Tag(name="动态接口开发管理")
@RestController
@RequestMapping("/autoapi/sysAutoapiManage")
@Slf4j
public class SysAutoapiManageController extends JeecgController<SysAutoapiManage, ISysAutoapiManageService> {
	@Autowired
	private ISysAutoapiManageService sysAutoapiManageService;

	/**
	 * 分页列表查询
	 *
	 * @param sysAutoapiManage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "动态接口开发管理-分页列表查询")
	@Operation(summary="动态接口开发管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysAutoapiManage>> queryPageList(SysAutoapiManage sysAutoapiManage,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		QueryWrapper<SysAutoapiManage> queryWrapper = QueryGenerator.initQueryWrapper(sysAutoapiManage, req.getParameterMap());
		Page<SysAutoapiManage> page = new Page<SysAutoapiManage>(pageNo, pageSize);
		IPage<SysAutoapiManage> pageList = sysAutoapiManageService.page(page, queryWrapper);
		System.out.println(pageList);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param sysAutoapiManage
	 * @return
	 */
	@AutoLog(value = "动态接口开发管理-添加")
	@Operation(summary ="动态接口开发管理-添加")
	@RequiresPermissions("autoapi:sys_autoapi_manage:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysAutoapiManage sysAutoapiManage) {
		sysAutoapiManageService.save(sysAutoapiManage);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param sysAutoapiManage
	 * @return
	 */
	@AutoLog(value = "动态接口开发管理-编辑")
	@Operation(summary ="动态接口开发管理-编辑")
	@RequiresPermissions("autoapi:sys_autoapi_manage:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysAutoapiManage sysAutoapiManage) {
		sysAutoapiManageService.updateById(sysAutoapiManage);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "动态接口开发管理-通过id删除")
	@Operation(summary ="动态接口开发管理-通过id删除")
	@RequiresPermissions("autoapi:sys_autoapi_manage:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysAutoapiManageService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "动态接口开发管理-批量删除")
	@Operation(summary ="动态接口开发管理-批量删除")
	@RequiresPermissions("autoapi:sys_autoapi_manage:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAutoapiManageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "动态接口开发管理-通过id查询")
	@Operation(summary ="动态接口开发管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysAutoapiManage> queryById(@RequestParam(name="id",required=true) String id) {
		SysAutoapiManage sysAutoapiManage = sysAutoapiManageService.getById(id);
		if(sysAutoapiManage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysAutoapiManage);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sysAutoapiManage
	 */
	@RequiresPermissions("autoapi:sys_autoapi_manage:exportXls")
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SysAutoapiManage sysAutoapiManage) {
		return super.exportXls(request, sysAutoapiManage, SysAutoapiManage.class, "动态接口开发管理");
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("autoapi:sys_autoapi_manage:importExcel")
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, SysAutoapiManage.class);
	}

}
