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
package me.fb.tiny.utils;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *@author fb
 *@version 
 *@since 2015年10月12日
 */
public class WebUtils {
	
	/**
	 * 是否是Ajax异步请求,请求返回数据为json格式
	 * @param request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		
		String accept = request.getHeader("accept");
		String xRequestedWith = request.getHeader("X-Requested-With");

		return ((accept != null && accept.indexOf("application/json") != -1 
			|| (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)));
	}
	
}
