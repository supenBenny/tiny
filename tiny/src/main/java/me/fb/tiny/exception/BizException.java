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

import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *@author fb
 *@version 
 *@since 2015年9月25日
 */
public class BizException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5902345831786825397L;

	private String code = "";

	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public BizException(String message) {
		super(message);
	}

	public BizException(){
		
	}
	
	public BizException(String message,String code) {
		super(message);
		this.code = code;
	}

	public BizException(String message, String code,HttpStatus status) {
		super(message);
		this.code = code;
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
