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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.fb.tiny.ApplicationEventListener;
import me.fb.tiny.exception.DefaultHandlerExceptionResolver;
import me.fb.tiny.utils.Result;
import me.fb.tiny.utils.SpringContextHolder;

/**
 * 默认webconfig
 *@author fb
 *@since 2015年8月11日
 */
public final class TinyWebConfig implements ApplicationContextAware,InitializingBean{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Bean
	public DefaultHandlerExceptionResolver exceptionHandler(){
		return new DefaultHandlerExceptionResolver();
	}
	
	@Bean
	public ApplicationEventListener applicationEventListener(){
		return new ApplicationEventListener();
	}

	@Bean
	public MessageSource messageSource(){
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:i18n/message","classpath:i18n/validationMessages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		//messageSource.setCacheSeconds(60);
		return messageSource;
	}
	

    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
 
        List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
 
        resolvers.add(this.internalResourceViewResolver());
        
//        resolvers.add(jaxb2MarshallingXmlViewResolver());
//        resolvers.add(jsonViewResolver());
//        resolvers.add(jspViewResolver());
//        resolvers.add(pdfViewResolver());
//        resolvers.add(excelViewResolver());
         
        resolver.setViewResolvers(resolvers);
        return resolver;
    }

	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setContentType("text/html;charset=UTF-8");
		// viewResolver.setOrder(10);
		return viewResolver;
	}

	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.setApplicationContext(applicationContext);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// SpringContextHolder.messageSource =
		// SpringContextHolder.applicationContext.getBean(MessageSource.class);
		ObjectMapper objectMapper = null;
		try {
			objectMapper = SpringContextHolder.getApplicationContext().getBean(ObjectMapper.class);
		} catch (Exception e) {
			logger.info("objectMapper is not found,use default");
		}

		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		Result.setObjectMapper(objectMapper);

	}

}
