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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author fb
 * @version
 * @since 2015年9月8日
 */
public class Result {

	// 执行结果
	private boolean success = true;

	// 结果说明
	// private String msg = null;

	// 结果说明
	private String message = null;

	private Integer code = 0;

	// 结果数据
	private Object data = null;

	private Map<String, Object> args = new LinkedHashMap<>();

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Logger logger = LoggerFactory.getLogger(Result.class);

	public Result() {

	}

	public Result(boolean success, String msg, Object data) {
		this.success = success;
		this.message = msg;
		this.data = data;
	}

	/**
	 * 成功返回值
	 * 
	 * @param data
	 * @return
	 */
	public static Result success(Object data) {
		Result result = new Result();
		result.setData(data);
		return result;
	}

	/**
	 * 失败返回值
	 * 
	 * @param msg
	 * @return
	 */
	public static Result failure(String msg) {
		Result result = new Result();
		result.setSuccess(false);
		result.setMessage(msg);
		return result;
	}

	public static Result failure(String msg, Integer code) {
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(code);
		result.setMessage(msg);
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T) data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 添加结果返回值
	 * 
	 * @param name
	 * @param value
	 */
	public void addResult(String name, Object value) {
		this.args.put(name, value);
	}

	/**
	 * 移出值
	 * 
	 * @param name
	 */
	public void removeResult(String name) {
		this.args.remove(name);
	}

	public void forEach(final BiConsumer<String, Object> action) {
		this.args.forEach((String t, Object u) -> {
			action.accept(t, u);
		});
	}

	/**
	 * 设置分页相关信息
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param totalPages
	 * @param totalResults
	 */
	public void setPagination(Integer currentPage, Integer pageSize, Integer totalPages, Integer totalResults) {
		// private Integer page;//第几页
		// private Integer total;//总页数
		// private Integer records;//总记录数
		// private List<T> rows;//记录列表
		this.addResult("page", currentPage);
		this.addResult("rows", pageSize);
		this.addResult("total", totalPages);
		this.addResult("records", totalResults);

	}

	public String toJson() {
		StringWriter sw = new StringWriter();
		toJson(sw);
		return sw.toString();
	}

	public void toJson(Writer writer) {
		toJson(writer, false);
	}

	public void toJson(Writer writer, boolean prettyPrint) {
		try {
			final JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(writer);

			if (prettyPrint) {
				jsonGenerator.setPrettyPrinter(objectMapper.getSerializationConfig().getDefaultPrettyPrinter());
			}

			jsonGenerator.writeStartObject();

			jsonGenerator.writeObjectField("message", this.getMessage());
			jsonGenerator.writeObjectField("code", this.getCode());

			this.forEach((String name, Object value) -> {
				try {
					jsonGenerator.writeObjectField(name, value);
				} catch (Exception e) {

				}

			});

			jsonGenerator.writeObjectField("data", this.getData());

			// this.getObjectMapper().writeValue(jsonGenerator, object);

			jsonGenerator.writeEndObject();
			jsonGenerator.flush();
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
	public static void setObjectMapper(ObjectMapper om){
		objectMapper = om;
	}
	

}
