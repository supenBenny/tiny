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
import java.lang.reflect.Type;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年10月9日
 */
public class MappingJackson2JsonpHttpMessageConverter extends MappingJackson2HttpMessageConverter {

	
	
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);        

        try {
            String jsonPadding = "callback";            

            // If the callback doesn't provide, use the default callback
//            if (object instanceof JsonpObject) {
//                String jsonCallback = ((JsonpObject)object).getJsonCallback();
//                if (jsonCallback != null) {
//                    jsonPadding = jsonCallback;
//                }
//            }            

            jsonGenerator.writeRaw(jsonPadding);
            jsonGenerator.writeRaw('(');
            this.getObjectMapper().writeValue(jsonGenerator, object);
            jsonGenerator.writeRaw(");");
            jsonGenerator.flush();
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }
	
}
