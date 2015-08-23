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

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * 监听application 事件
 * 
 * @author fb
 * @version
 * @since 2015年8月12日
 */
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// 如果是容器刷新事件
		if (event instanceof ContextClosedEvent) {
			//当ApplicationContext被关闭时触发该事件
			//System.out.println(event.getClass().getSimpleName() + " 事件已发生！");
		} else if (event instanceof ContextRefreshedEvent) {// 如果是容器关闭事件
			//当ApplicationContext初始化或者刷新时触发该事件
			//System.out.println(event.getClass().getSimpleName() + " 事件已发生！");
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

}
