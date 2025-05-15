package org.jeecg.modules.autoapi.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ExecuteHandleService {

    public Object doPress(HttpServletRequest request, HttpServletResponse response, Map<String,Object> paramsMap) throws Exception;
}
