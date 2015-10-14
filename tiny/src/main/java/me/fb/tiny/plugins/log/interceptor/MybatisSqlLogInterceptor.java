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
package me.fb.tiny.plugins.log.interceptor;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.fb.tiny.plugins.log.DefaultLogInfo;
import me.fb.tiny.plugins.log.LogInfo;
import me.fb.tiny.plugins.log.service.TableOperateLogService;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月19日
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })
//	,@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
//			RowBounds.class, ResultHandler.class }) 
})
public class MybatisSqlLogInterceptor implements Interceptor {

	@SuppressWarnings("unused")
	private Properties properties;
	
	private TableOperateLogService logService;
	//sql 操作表名称映射
	private Map<String,String> tableNames = new HashMap<>();
	
	private Set<String> exculde = new HashSet<>();
	
	private LogInfo logInfo = new DefaultLogInfo();

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		
		Configuration configuration = mappedStatement.getConfiguration();
		Object returnValue = null;
		
		returnValue = invocation.proceed();
		
		String tableName = getTableName(sqlId);
		if(!exculde.contains(tableName)){
			logService.logSql(logInfo.getUserId(tableName),tableName, "", 
					showSql(configuration, boundSql),logInfo.getSystemName(tableName));
		}
		if(logger.isInfoEnabled()){
			logger.info(getSql(configuration, boundSql, sqlId));
		}
		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
		String sql = showSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append(sqlId);
		str.append(":");
		str.append(sql);
		return str.toString();
	}

	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format((Date)obj) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}

		}
		return value;
	}

	public static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}
	
	public String getTableName(String sqlId){
		String tableName = tableNames.get(sqlId);
		if(tableName == null){
			String[] arr = sqlId.split("\\.");
			if(arr.length > 1){
				tableName = "t" + arr[arr.length-2];
			}else {
				tableName = sqlId;
			}
			tableNames.put(sqlId, tableName);
		}
		return tableName;
	}
	
	public void setLogService(TableOperateLogService logService) {
		this.logService = logService;
	}

	public void setExculde(Collection<String> exculde) {
		this.exculde.addAll(exculde);
	}
	
	public void setExculde(String[] exculde) {
		if(exculde != null){
			this.exculde.addAll(Arrays.asList(exculde));
		}
	}

	public void setLogInfo(LogInfo logInfo) {
		this.logInfo = logInfo;
	}
	
	

	
}
