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
package me.fb.tiny.scaffolding.schema;

import java.lang.reflect.Type;

/**
 * 对应表中的列
 *@author fb
 *@version 
 *@since 2015年9月29日
 */
public class Column {
	
	private String columnName;
	
	private String sqlType;
	
	private Integer sqlTypeCode;
	
	private String fieldName;
	
	private Class<?> fieldType;
	
	private String fieldTypeName;
	
	private String comment="";

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String name) {
		this.columnName = name;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public Integer getSqlTypeCode() {
		return sqlTypeCode;
	}

	public void setSqlTypeCode(Integer sqlTypeCode) {
		this.sqlTypeCode = sqlTypeCode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?>  fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldTypeName() {
		return fieldTypeName;
	}

	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
