package org.jeecg.modules.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.sms.entity.GophaSmsLog;
import org.jeecg.modules.sms.service.IGophaSmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 短信日志
 * @Author: gopha
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Tag(name="短信日志")
@RestController
@RequestMapping("/modules.sms/gophaSmsLog")
@Slf4j
public class GophaSmsLogController extends JeecgController<GophaSmsLog, IGophaSmsLogService> {
	@Autowired
	private IGophaSmsLogService gophaSmsLogService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gophaSmsLog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "短信日志-分页列表查询")
	@Operation(summary ="短信日志-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GophaSmsLog gophaSmsLog,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GophaSmsLog> queryWrapper = QueryGenerator.initQueryWrapper(gophaSmsLog, req.getParameterMap());
		Page<GophaSmsLog> page = new Page<GophaSmsLog>(pageNo, pageSize);
		IPage<GophaSmsLog> pageList = gophaSmsLogService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param gophaSmsLog
	 * @return
	 */
	@AutoLog(value = "短信日志-添加")
	@Operation(summary ="短信日志-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GophaSmsLog gophaSmsLog) {
		gophaSmsLogService.save(gophaSmsLog);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param gophaSmsLog
	 * @return
	 */
	@AutoLog(value = "短信日志-编辑")
	@Operation(summary ="短信日志-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GophaSmsLog gophaSmsLog) {
		gophaSmsLogService.updateById(gophaSmsLog);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "短信日志-通过id删除")
	@Operation(summary ="短信日志-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gophaSmsLogService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "短信日志-批量删除")
	@Operation(summary ="短信日志-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gophaSmsLogService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "短信日志-通过id查询")
	@Operation(summary ="短信日志-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GophaSmsLog gophaSmsLog = gophaSmsLogService.getById(id);
		if(gophaSmsLog==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(gophaSmsLog);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param gophaSmsLog
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GophaSmsLog gophaSmsLog) {
        return super.exportXls(request, gophaSmsLog, GophaSmsLog.class, "短信日志");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GophaSmsLog.class);
    }

}
