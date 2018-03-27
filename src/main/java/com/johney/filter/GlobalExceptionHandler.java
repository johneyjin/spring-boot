package com.johney.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获类
 * 
 * @author Johney
 * @ControllerAdvice是 controller 的一个辅助类为最常用的就是作为全局异常处理的切面类； 约定了几种可行的返回值，如果是直接返回
 *                    model 类的话，需要使用 @ResponseBody 进行 json 转换
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger("info");
	private static Logger loggerError = LoggerFactory.getLogger("error");

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Map<String, Object> exceptionHandle(RuntimeException ex, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("抛异常了！！\n" + ex.getStackTrace()[0]+"  行报错\n错误为："+ex.getClass().toString()+":"+ex.getMessage());
		loggerError.error("异常捕获"+ex.getStackTrace()[0]);
		Map<String, Object> map = new HashMap<String, Object>();
		// 判断异常时属于哪个类
		if (ex.getClass().equals(ArithmeticException.class)) {
			map.put("errorCode", "500");
			// 定位到具体是哪行报错
			map.put("errorLine", ex.getStackTrace()[0]);
			map.put("errorMsg", "除数为0");
			logger.error("抛异常了！" + map);
		} else if (ex.getClass().equals(NullPointerException.class)) {
			map.put("errorCode", "500");
			// 定位到具体是哪行报错
			map.put("errorLine", ex.getStackTrace()[0]);
			map.put("errorMsg", "空指针");
			logger.error("抛异常了！" + map);
		}
		if (logger.isDebugEnabled()){
			logger.debug("System …..");
		}
		return map;
	}
}
