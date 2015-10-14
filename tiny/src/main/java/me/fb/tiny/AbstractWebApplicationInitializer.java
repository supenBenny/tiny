/*
 * Copyright 2015 the original author or authors.
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

package me.fb.tiny;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import me.fb.tiny.config.TinyConfig;
import me.fb.tiny.config.TinyWebConfig;

/**
 * web 应用初始化
 * 
 * @author fb
 *
 * @since 2015年8月11日
 */
public abstract class AbstractWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//before startup,启动前
		
		super.onStartup(servletContext);
		
		//WebApplicationContextUtils.getWebApplicationContext(sc)
		//系统启动,初始化数据init
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		List<Class<?>> configs = new ArrayList<>();
		
		configs.add(TinyConfig.class);
		
		Class<?>[] configClasses = this.getRootApplicationContextConfigClasses();
		for (Class<?> clazz : configClasses) {
			if (clazz != null) {
				configs.add(clazz);
			}
		}
		return configs.toArray(new Class[] {});
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		List<Class<?>> configs = new ArrayList<>();

		configs.add(TinyWebConfig.class);
		
		Class<?>[] configClasses = this.getWebApplicationContextConfigClasses();
		for (Class<?> clazz : configClasses) {
			if (clazz != null) {
				configs.add(clazz);
			}
		}
		return configs.toArray(new Class[] {});
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	

	/**
	 * 添加rootApplicationConfig,以注解形式的class配置文件
	 * @param classes
	 * @return
	 */
	protected abstract Class<?>[] getRootApplicationContextConfigClasses();

	/**
	 * webApplicationConfig
	 * @param classes
	 * @return
	 */
	protected abstract Class<?>[] getWebApplicationContextConfigClasses();

	@Override
	protected void customizeRegistration(Dynamic registration) {
		super.customizeRegistration(registration);
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}

}
