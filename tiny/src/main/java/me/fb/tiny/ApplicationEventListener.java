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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import me.fb.tiny.bootstrap.Bootstrap;

/**
 * 监听application 事件
 * 
 * @author fb
 * @version
 * @since 2015年8月12日
 */
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> ,ApplicationContextAware{

	private ApplicationContext applicationContext;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextClosedEvent) {// 如果是容器关闭事件
			//当ApplicationContext被关闭时触发该事件
			logger.info("{}({}) contextClosedEvent event.",applicationContext.getApplicationName(),applicationContext.toString());
			Map<String, Bootstrap> beansOfType = this.applicationContext.getBeansOfType(Bootstrap.class);
			beansOfType.forEach((String name,Bootstrap bootstrap)->{
				bootstrap.stop();
			});
			logger.info("{}({}) contextClosedEvent event end.",applicationContext.getApplicationName(),applicationContext.toString());
			//System.out.println(event.getClass().getSimpleName() + " 事件已发生！");
		} else if (event instanceof ContextRefreshedEvent) {// 如果是容器刷新事件
			//当ApplicationContext初始化或者刷新时触发该事件
			//System.out.println(event.getClass().getSimpleName() + " 事件已发生！");
			logger.info("{}({}) ContextRefreshedEvent event.",applicationContext.getApplicationName(),applicationContext.toString());
			Map<String, Bootstrap> beansOfType = this.applicationContext.getBeansOfType(Bootstrap.class);
			beansOfType.forEach((String name,Bootstrap bootstrap)->{
				bootstrap.start();
			});
		
		} else if (event instanceof ContextStartedEvent) {
			//Spring 2.5当容器调用ConfigurableApplicationContext的Start()方法开始/重新开始容器时触发该事件
			//System.out.println(event.getClass().getSimpleName() + " 事件已发生！");
		
		} else if (event instanceof ContextStoppedEvent) {
			//Spring 2.5当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件
			//System.out.println(event.getClass().getSimpleName() + " 事件已发生！");
		} else {
			//System.out.println("有其它事件发生:" + event.getClass().getName());
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
