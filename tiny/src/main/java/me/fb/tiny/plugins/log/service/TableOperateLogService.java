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
package me.fb.tiny.plugins.log.service;

import javax.validation.constraints.NotNull;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月18日
 */
public interface TableOperateLogService {
	
	public void logSql(String userId,String tableName,String operator,@NotNull String sql,String sysName);
			
}
