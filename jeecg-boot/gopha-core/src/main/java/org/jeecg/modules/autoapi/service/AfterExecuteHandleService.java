package org.jeecg.modules.autoapi.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AfterExecuteHandleService {

    public Object doPress(HttpServletRequest request, HttpServletResponse response,Object result);
}
