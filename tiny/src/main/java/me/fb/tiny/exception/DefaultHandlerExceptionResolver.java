/*
 * Copyright 2015- the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.fb.tiny.exception;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.fb.tiny.lang.L;
import me.fb.tiny.utils.Result;
import me.fb.tiny.utils.WebUtils;

/**
 * 
 * 
 * @author fb
 * @version
 * @since 2015年8月12日
 */
public class DefaultHandlerExceptionResolver extends org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver {

	
	@Value("web.error404:404.html")
	private String error404 = "404.html";
	
	@Value("web.error500:500.html")
	private String error500 = "500.html";
	
	public DefaultHandlerExceptionResolver() {
		setOrder(-1);
		//TinyResultMappingJackson2HttpMessageConverter messageCoverter = new TinyResultMappingJackson2HttpMessageConverter();
		//messageCoverter.setSupportedMediaTypes(supportedMediaTypes);
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		if(WebUtils.isAjaxRequest(request)){
			Result result = Result.failure(ex.getMessage());
			response.setContentType("application/json");

			if(ex instanceof BizException){
	

			}else if(ex instanceof NoHandlerFoundException){
				
				
			}else if(ex instanceof MethodArgumentNotValidException){
				MethodArgumentNotValidException e =(MethodArgumentNotValidException)ex;
				Map<String,Object> fieldError = new LinkedHashMap<>(); 
				List<ObjectError> errors = e.getBindingResult().getAllErrors();
				for (ObjectError objectError : errors) {
					if(objectError instanceof FieldError){
						fieldError.put(((FieldError)objectError).getField(), objectError.getDefaultMessage());
					}else {
						fieldError.put(objectError.getObjectName(), objectError.getDefaultMessage());
					}
				}
				
			}else if (ex instanceof Exception) {
				ModelAndView view = super.doResolveException(request, response, handler, ex);
				if(view != null){
					return new ModelAndView();
				}
			}
			try {
				result.toJson(response.getWriter());
			} catch (IOException e) {
			}
			return new ModelAndView();
		}else {
			if(ex instanceof NoHandlerFoundException){
				return new ModelAndView(error404);
			}else {
				ModelAndView view = super.doResolveException(request, response, handler, ex);
				if(view != null){
					return view;
				}
			}
			return new ModelAndView(error500);
		}
		
	}

	public String getError404() {
		return error404;
	}

	public void setError404(String error404) {
		this.error404 = error404;
	}

	public String getError500() {
		return error500;
	}

	public void setError500(String error500) {
		this.error500 = error500;
	}

}
