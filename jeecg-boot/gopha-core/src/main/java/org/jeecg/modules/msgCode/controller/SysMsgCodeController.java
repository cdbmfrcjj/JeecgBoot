package org.jeecg.modules.msgCode.controller;

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
import org.jeecg.modules.msgCode.entity.SysMsgCode;
import org.jeecg.modules.msgCode.service.ISysMsgCodeService;
import org.jeecg.modules.msgCode.utils.MsgCodeUtils;
import org.jeecg.modules.sms.entity.GophaSmsLog;
import org.jeecg.modules.sms.service.IGophaSmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

 /**
 * @Description: 手机验证码
 * @Author: gopha
 * @Date:   2022-10-08
 * @Version: V1.0
 */
@Tag(name="手机验证码")
@RestController
@RequestMapping("/msgCode/sysMsgCode")
@Slf4j
public class SysMsgCodeController extends JeecgController<SysMsgCode, ISysMsgCodeService> {

	 @Value(value="${jeecg.sms.url}")
	 private String urls;

	 @Value(value="${jeecg.sms.username}")
	 private String username;

	 @Value(value="${jeecg.sms.password}")
	 private String password;

	 @Value(value="${jeecg.sms.limit}")
	 private Integer limit;

	 @Autowired
	 private ISysMsgCodeService sysMsgCodeService;

	 @Autowired
	 private IGophaSmsLogService gophaSmsLogService;


	 // 发短信
	 private boolean sendMessage(String tel, String content) {
		 PrintWriter out = null;
		 BufferedReader in = null;
		 String result = "";
		 try {
			 URL realUrl = new URL(urls);
			 // 打开和URL之间的连接
			 URLConnection conn = realUrl.openConnection();
			 // 设置通用的请求属性
			 conn.setRequestProperty("accept", "*/*");
			 conn.setRequestProperty("connection", "Keep-Alive");
			 conn.setRequestProperty("user-agent",
					 "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			 // 发送POST请求必须设置如下两行
			 conn.setDoOutput(true);
			 conn.setDoInput(true);
			 // 获取URLConnection对象对应的输出流
			 out = new PrintWriter(conn.getOutputStream());
			 // 发送请求参数
			 StringBuffer sb = new StringBuffer();
			 sb.append("username="+username+"&password="+password);
			 sb.append("&numbers=");
			 sb.append(tel);
			 sb.append("&content=");
			 sb.append(URLEncoder.encode(content, "UTF-8"));
			 //sb.append(content);
			 String param = sb.toString();
			 System.out.println(param.toString());
			 out.print(param);
			 // flush输出流的缓冲
			 out.flush();
			 // 定义BufferedReader输入流来读取URL的响应
			 in = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
			 String line;
			 while ((line = in.readLine()) != null) {
				 result += line;
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
			 GophaSmsLog smsRecord = new GophaSmsLog();
			 smsRecord.setContent(content);
			 smsRecord.setTel(tel);
			 smsRecord.setCreateTime(new Date());
			 smsRecord.setContent("短信发送失败：服务器异常");
			 gophaSmsLogService.save(smsRecord);
			 return false;
		 }
		 // 使用finally块来关闭输出流、输入流
		 finally {
			 try {
				 if (out != null) {
					 out.close();
				 }
				 if (in != null) {
					 in.close();
				 }
			 } catch (IOException ex) {
				 ex.printStackTrace();
				 GophaSmsLog smsRecord = new GophaSmsLog();
				 smsRecord.setContent(content);
				 smsRecord.setTel(tel);
				 smsRecord.setCreateTime(new Date());
				 smsRecord.setContent("短信发送失败：服务器异常");
				 gophaSmsLogService.save(smsRecord);
				 return false;
			 }
		 }
		 System.out.println(result.toString());
		 int count = result.indexOf("\"success\":\"true\"");
		 String code = "";
		 // 状态码
		 GophaSmsLog smsRecord = new GophaSmsLog();
		 smsRecord.setContent(content);
		 smsRecord.setTel(tel);
		 smsRecord.setCreateTime(new Date());
		 if (count == -1) {
			 smsRecord.setContent(result);
		 } else {
			 smsRecord.setContent("短信发送成功");
		 }
		 gophaSmsLogService.save(smsRecord);
		 if ("0".equals(code)) {
			 return true;
		 }
		 return false;
	 }




	 /**
	  * 发送预警信息
	  *
	  * @param sysMsgCode
	  * @return
	  */
	 @AutoLog(value = "发送预警信息-发送预警信息")
	 @Operation(summary="发送预警信息-发送预警信息")
	 @PostMapping(value = "/sendWaringMessage")
	 public Result<?> sendWaringMessage(@RequestBody SysMsgCode sysMsgCode) {
		 String  content = sysMsgCode.getContent();
		 List<String> tels = sysMsgCode.getTel();
		 for(String tel : tels) {
			 this.sendMessage(tel, content);
		 }
		 return Result.OK("发送成功！");
	 }


	 /**
	  * 发送验证码
	  *
	  * @param sysMsgCode
	  * @return
	  */
	 @AutoLog(value = "手机验证码-发送验证码")
	 @Operation(summary ="手机验证码-发送验证码")
	 @PostMapping(value = "/getMsgCode")
	 public Result<?> getMsgCode(@RequestBody SysMsgCode sysMsgCode) {

		 Date date = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String datef = sdf.format(date);

	 	 String mobile = sysMsgCode.getMobile();

		 QueryWrapper<SysMsgCode> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("mobile",mobile);
		 queryWrapper.like("create_time",datef);

		 List list = sysMsgCodeService.list(queryWrapper);

		 if(list.size() >= limit) {
			 return Result.error(500,"获取验证码超过次数限制！");
		 }

		 if(list.size() > 0) {
			 sysMsgCode.setCount(list.size()+1);
		 }else {
			 sysMsgCode.setCount(1);
		 }
		 String value = MsgCodeUtils.randString(6);
		 sysMsgCode.setVcode(value);

		 String content = "您当前获取的验证码是:" + value;
		 this.sendMessage(mobile, content);

		 sysMsgCodeService.save(sysMsgCode);
		 return Result.OK("发送验证码成功！");
	 }




	/**
	 * 分页列表查询
	 *
	 * @param sysMsgCode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "手机验证码-分页列表查询")
	@Operation(summary ="手机验证码-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysMsgCode sysMsgCode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysMsgCode> queryWrapper = QueryGenerator.initQueryWrapper(sysMsgCode, req.getParameterMap());
		Page<SysMsgCode> page = new Page<SysMsgCode>(pageNo, pageSize);
		IPage<SysMsgCode> pageList = sysMsgCodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysMsgCode
	 * @return
	 */
	@AutoLog(value = "手机验证码-添加")
	@Operation(summary ="手机验证码-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysMsgCode sysMsgCode) {
		sysMsgCodeService.save(sysMsgCode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysMsgCode
	 * @return
	 */
	@AutoLog(value = "手机验证码-编辑")
	@Operation(summary ="手机验证码-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysMsgCode sysMsgCode) {
		sysMsgCodeService.updateById(sysMsgCode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "手机验证码-通过id删除")
	@Operation(summary ="手机验证码-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysMsgCodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "手机验证码-批量删除")
	@Operation(summary ="手机验证码-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysMsgCodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "手机验证码-通过id查询")
	@Operation(summary ="手机验证码-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysMsgCode sysMsgCode = sysMsgCodeService.getById(id);
		if(sysMsgCode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysMsgCode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysMsgCode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysMsgCode sysMsgCode) {
        return super.exportXls(request, sysMsgCode, SysMsgCode.class, "手机验证码");
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
        return super.importExcel(request, response, SysMsgCode.class);
    }

}
