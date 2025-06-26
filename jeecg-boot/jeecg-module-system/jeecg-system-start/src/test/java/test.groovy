package org.jeecg


import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.jeecg.modules.autoapi.service.AfterExecuteHandleService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CommonHandleScript implements  AfterExecuteHandleService{


	@Override
	public Object doPress(HttpServletRequest request, HttpServletResponse response,Object result) {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			Map map=new HashMap();
			map.put("item",result);
			String resultJson = JSON.toJSONString(map);
			String xml = "<xml><item>" + resultJson + "</item></xml>";
			buffer.append(xml);
			return buffer.toString();
		}catch(Exception e){
			return "";
		}
	}
}