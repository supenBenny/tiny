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
package me.fb.tiny.spring.mvc.converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import me.fb.tiny.utils.Result;

/**
 * 
 * @author fb
 * @version
 * @since 2015年10月9日
 */
public class TinyResultMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		if (object instanceof Result) {
//			JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
//			final JsonGenerator jsonGenerator = this.getObjectMapper().getFactory()
//					.createGenerator(outputMessage.getBody(), encoding);
//			try {
//				Result result = (Result) object;
//				jsonGenerator.writeStartObject();
//
//				jsonGenerator.writeObjectField("message", result.getMessage());
//				jsonGenerator.writeObjectField("code", result.getCode());
//
//				result.forEach((String name, Object value) -> {
//					try {
//						jsonGenerator.writeObjectField(name, value);
//					} catch (Exception e) {
//
//					}
//
//				});
//
//				jsonGenerator.writeObjectField("data", result.getData());
//
//				//this.getObjectMapper().writeValue(jsonGenerator, object);
//
//				jsonGenerator.writeEndObject();
//				jsonGenerator.flush();
//			} catch (JsonProcessingException ex) {
//				throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
//			}
			Result result = (Result) object;
			Charset charSet = outputMessage.getHeaders().getContentType().getCharSet();
			if(charSet == null){
				charSet = Charset.forName("UTF-8");
			}
			result.toJson(new OutputStreamWriter(outputMessage.getBody(),charSet));
			
		} else {
			super.writeInternal(object, type, outputMessage);
		}

	}
}
