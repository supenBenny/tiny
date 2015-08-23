/*
 * Copyright 2002-2015 the original author or authors.
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

/**
 * 
 */
package me.fb.tiny.example.test;

import java.util.Iterator;
import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import me.fb.tiny.config.TinyConfig;
import me.fb.tiny.config.TinyWebConfig;
import me.fb.tiny.example.config.AppConfig;
import me.fb.tiny.example.config.WebApplicationInitializer;
import me.fb.tiny.example.config.WebConfig;

/**
 *
 *@author fb
 *@since 2015年8月11日
 */
@ContextConfiguration(classes = {WebApplicationInitializer.class,
		TinyConfig.class,TinyWebConfig.class,AppConfig.class,WebConfig.class})
@WebAppConfiguration
public class AbstractContextTest {
	@Autowired
    private WebApplicationContext webApplicationContext;
    public MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected MockHttpServletRequestBuilder postWithJson(String url,String json){
        return post(url,"json").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(json.getBytes());
    }

    protected MockHttpServletRequestBuilder putWithJson(String url,String json){
        return put(url,"json").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(json.getBytes());
    }

    protected MockHttpServletRequestBuilder postAlone(String url){
        return post(url);
    }
    protected MockHttpServletRequestBuilder deleteAlone(String uri){
        return delete(uri);
    }

    protected MockHttpServletRequestBuilder getAlone(String uri){
        return get(uri);
    }

    protected MockHttpServletRequestBuilder getWithRequest(String uri,Map<?,?> attr){
        MockHttpServletRequestBuilder builder = get(uri);
        if (attr!=null){
            Iterator<?> it = attr.keySet().iterator();
            while (it.hasNext()){
                Object key = it.next();
                builder.param((String)key,attr.get(key).toString());
            }
        }
        return builder;
    }

    protected String resultString(ResultActions actions) throws Exception{
        return actions.andReturn().getResponse().getContentAsString();
    }

    protected void exceptStatusOk(ResultActions actions)throws Exception{
        actions.andExpect(status().isOk());
    }

}
