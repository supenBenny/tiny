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
package me.fb.tiny.bootstrap;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月16日
 */
public interface Bootstrap {
	
	/**
	 * 系统启动时调用
	 */
	default void start(){}

	/**
	 * 系统关闭时调用
	 */
	default void stop(){}
}
