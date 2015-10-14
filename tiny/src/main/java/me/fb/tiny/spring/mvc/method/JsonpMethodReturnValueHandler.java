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
package me.fb.tiny.spring.mvc.method;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import me.fb.tiny.spring.mvc.annotation.Jsonp;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年10月9日
 */
public class JsonpMethodReturnValueHandler implements HandlerMethodReturnValueHandler{


	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getAnnotatedElement().getAnnotation(Jsonp.class) != null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodReturnValueHandler#handleReturnValue(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest)
	 */
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);

		HttpServletResponse servletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
		servletResponse.getWriter().write(returnValue.toString());
		// servletResponse.flushBuffer();
		servletResponse.getWriter().flush();
		// ServletServerHttpResponse outputMessage = new
		// ServletServerHttpResponse(servletResponse);
		// outputMessage.getBody().write(b);
	}

}
