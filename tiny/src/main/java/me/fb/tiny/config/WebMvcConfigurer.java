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
package me.fb.tiny.config;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.fb.tiny.spring.mvc.converter.TinyResultMappingJackson2HttpMessageConverter;

/**
 * 
 * @author fb
 * @version
 * @since 2015年10月9日
 */
public class WebMvcConfigurer extends WebMvcConfigurerAdapter implements ApplicationContextAware,InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed",
			WebMvcConfigurationSupport.class.getClassLoader());

	private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder",
			WebMvcConfigurationSupport.class.getClassLoader());

	// private static final boolean jackson2Present =
	// ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
	// WebMvcConfigurationSupport.class.getClassLoader()) &&
	// ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
	// WebMvcConfigurationSupport.class.getClassLoader());

	private static final boolean jackson2XmlPresent = ClassUtils.isPresent(
			"com.fasterxml.jackson.dataformat.xml.XmlMapper", WebMvcConfigurationSupport.class.getClassLoader());

	private ApplicationContext applicationContext;

	/**
	 * 转换
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		stringConverter.setWriteAcceptCharset(false);

		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(stringConverter);
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new SourceHttpMessageConverter<Source>());
		converters.add(new AllEncompassingFormHttpMessageConverter());

		if (romePresent) {
			// converters.add(new AtomFeedHttpMessageConverter());
			// converters.add(new RssChannelHttpMessageConverter());
		}

		if (jackson2XmlPresent) {
			ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.xml().applicationContext(this.applicationContext)
					.build();
			converters.add(new MappingJackson2XmlHttpMessageConverter(objectMapper));
		} else if (jaxb2Present) {
			converters.add(new Jaxb2RootElementHttpMessageConverter());
		}

		MappingJackson2HttpMessageConverter jacksonMessageConverter = new TinyResultMappingJackson2HttpMessageConverter(); // new
																															// MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		jacksonMessageConverter.setObjectMapper(objectMapper);
		List<MediaType> supportedMediaTypes = new ArrayList<>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		jacksonMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		converters.add(jacksonMessageConverter);
		//
		// MappingJackson2JsonpHttpMessageConverter jsonpMessageConverter = new
		// MappingJackson2JsonpHttpMessageConverter();
		//
		// jsonpMessageConverter.setSupportedMediaTypes(Arrays.asList(new
		// MediaType("application", "x-javascript"),
		// new MediaType("application", "javascript"), new MediaType("text",
		// "javascript")));

	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		
		super.addArgumentResolvers(argumentResolvers);
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		super.addReturnValueHandlers(returnValueHandlers);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// configurer.ignoreAcceptHeader(true).defaultContentType(
		// MediaType.TEXT_HTML);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/").setCachePeriod(31536000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		RequestMappingHandlerMapping mapping = this.applicationContext.getBean(RequestMappingHandlerMapping.class);
		logger.info("requestMappingHandlerMapping:{}",mapping);
	}

}
