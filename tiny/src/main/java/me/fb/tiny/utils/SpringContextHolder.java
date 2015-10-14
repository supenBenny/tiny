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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import me.fb.tiny.lang.L;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月9日
 */
public class SpringContextHolder {
	
	/**
	 * 大括号正则{},表达式内取值;如字符串:hello,{name}
	 */
	private final static Pattern PATTERN_BRACKETS = Pattern.compile("\\{([^}]+?)\\}");

	
	private static ApplicationContext applicationContext;
	
	public static MessageSource messageSource;
	
	
	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public static void setApplicationContext(ApplicationContext applicationContext){
		SpringContextHolder.applicationContext = applicationContext;
	}
	
	/**
	 * 从18n文件中获取提示内容
	 * @param code
	 * @return
	 */
	public static String getMessage(String code){
		return getMessage(code,null);
	}
	
	/**
	 * 从18n文件中获取提示内容
	 * @param code
	 * @param args
	 * @return
	 */
	public static String getMessage(String code,Object[] args){
		return applicationContext.getMessage(code, args, null);
	}
	
	public static String replaceMessage(String message){
		if(L.isNotEmpty(message)){
			Matcher matcher = PATTERN_BRACKETS.matcher(message);
			StringBuffer sb = new StringBuffer();
			while(matcher.find()){
				matcher.appendReplacement(sb, getMessage(matcher.group(1)));
			}
			matcher.appendTail(sb);
			return sb.toString();
		}
		return message;
	}
	
	/**
	 * 
	 * @return
	 */
	public static MessageSource getMessageSource(){
		return applicationContext.getBean(MessageSource.class);
	}

}
