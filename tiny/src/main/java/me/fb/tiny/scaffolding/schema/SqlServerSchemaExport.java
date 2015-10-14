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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.fb.tiny.lang.Strings;

/**
 * 
 * @author fb
 * @version
 * @since 2015年9月29日
 */
public class SqlServerSchemaExport implements SchemaExport {
	
	//sqlType中数据类型映射
	private final static Map<Integer,String> sqlTypes = new HashMap<>();
	
	//sqlType映射为java类型
	private final static Map<Integer,Class<?>> javaTypes = new HashMap<>();
	
	private Connection connection;

	// 查找所有表
	private final String FIND_TABLE_ALL = "SELECT t.id AS [id],t.name AS [name],td.VALUE AS [comment] "
			+ "FROM sysobjects t INNER JOIN sysusers u ON u.uid = t.uid "
			+ "LEFT OUTER JOIN sys.extended_properties td ON td.major_id = t.id "
			+ "AND td.minor_id = 0 AND td.name = 'MS_Description'" + "WHERE t.type = 'u' ";

	// 根据名称查找表
	private final String FIND_TABLE_BY_NAME = FIND_TABLE_ALL + " and t.name=?";

	// 查找表的所有列
	private final String FIND_COLUMNS_BY_TABLE_NAME = "SELECT c.name AS [name],cd.VALUE AS [comment],c.xtype AS [sqlTypeCode] "
			+ "FROM sysobjects t INNER JOIN sysusers u ON u.uid = t.uid "
			+ "LEFT OUTER JOIN sys.extended_properties td ON td.major_id = t.id "
			+ "AND td.minor_id = 0 AND td.name = 'MS_Description' " 
			+ "INNER JOIN syscolumns c ON c.id = t.id "
			+ "LEFT OUTER JOIN sys.extended_properties cd ON cd.major_id = c.id "
			+ "AND cd.minor_id = c.colid AND cd.name = 'MS_Description' "
			+ "WHERE t.type = 'u' and t.name=?";

	private final Logger logger = LoggerFactory.getLogger(SqlServerSchemaExport.class);
	
	static {
		sqlTypes.put(34,"image");
		sqlTypes.put(35,"text");
		sqlTypes.put(36,"uniqueidentifier");
		sqlTypes.put(40,"date");
		sqlTypes.put(41,"time");
		sqlTypes.put(42,"datetime2");
		sqlTypes.put(43,"datetimeoffset");
		sqlTypes.put(48,"tinyint");
		sqlTypes.put(52,"smallint");
		sqlTypes.put(56,"int");
		sqlTypes.put(58,"smalldatetime");
		sqlTypes.put(59,"real");
		sqlTypes.put(60,"money");
		sqlTypes.put(61,"datetime");
		sqlTypes.put(62,"float");
		sqlTypes.put(98,"sql_variant");
		sqlTypes.put(99,"ntext");
		sqlTypes.put(104,"bit");
		sqlTypes.put(106,"decimal");
		sqlTypes.put(108,"numeric");
		sqlTypes.put(122,"smallmoney");
		sqlTypes.put(127,"bigint");
		sqlTypes.put(240,"hierarchyid");
		sqlTypes.put(240,"geometry");
		sqlTypes.put(240,"geography");
		sqlTypes.put(165,"varbinary");
		sqlTypes.put(167,"varchar");
		sqlTypes.put(173,"binary");
		sqlTypes.put(175,"char");
		sqlTypes.put(189,"timestamp");
		sqlTypes.put(231,"nvarchar");
		sqlTypes.put(239,"nchar");
		sqlTypes.put(241,"xml");
		sqlTypes.put(231,"sysname");
		
		javaTypes.put(34, byte[].class);
		javaTypes.put(35, String.class);
		javaTypes.put(36, Integer.class);
		javaTypes.put(40, Date.class);
		javaTypes.put(41, Date.class);
		javaTypes.put(42, Date.class);
		javaTypes.put(43, Date.class);
		
		javaTypes.put(48, Integer.class);
		javaTypes.put(52, Integer.class);
		javaTypes.put(56, Integer.class);
		javaTypes.put(58, Date.class);
		javaTypes.put(59, Float.class);
		javaTypes.put(60, BigDecimal.class);
		javaTypes.put(61, Date.class);
		javaTypes.put(62, Float.class);
		javaTypes.put(98, String.class);
		javaTypes.put(99, String.class);
		javaTypes.put(104, Integer.class);
		javaTypes.put(106, Double.class);
		javaTypes.put(108, Integer.class);
		javaTypes.put(122, BigDecimal.class);
		javaTypes.put(127, Long.class);

		
	}

	public SqlServerSchemaExport(Connection conn){
		this.connection = conn;
	}
	
	/**
	 * 获取数据库所有表
	 * @return
	 */
	public List<Table> getTables() {
		List<Table> tables = new ArrayList<>();
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_TABLE_ALL);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				Table table = new Table();
				table.setTableName(name);
				table.setComment(rs.getString("comment"));
				table.setColumns(this.findColumns(name));
				table.setClassName(tableName2ClassName(table.getTableName()));
				tables.add(table);
			}

		} catch (SQLException e) {
			logger.error("", e);
		}
		return tables;
	}

	/**
	 * 查找一张表
	 * @param tableName
	 * @return
	 */
	public Table getTable(String tableName) {
		Table table = null;
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_TABLE_BY_NAME);

			ps.setString(1, tableName);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String name = rs.getString("name");
				table = new Table();
				table.setTableName(name);
				table.setComment(rs.getString("comment"));
				table.setColumns(this.findColumns(name));
				table.setClassName(tableName2ClassName(table.getTableName()));
			}

		} catch (SQLException e) {
			logger.error("", e);
		}

		return table;
	}

	
	private List<Column> findColumns(String tableName) {
		List<Column> columns = new ArrayList<>();
		try {
			PreparedStatement ps = connection.prepareStatement(FIND_COLUMNS_BY_TABLE_NAME);
			ps.setString(1, tableName);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Column column = new Column();
				column.setColumnName(rs.getString("name"));
				column.setFieldName(columnName2FieldName(column.getColumnName()));
				column.setComment(rs.getString("comment"));
				column.setSqlTypeCode(rs.getInt("sqlTypeCode"));
				Class<?> javaType = getJavaType(column.getSqlTypeCode());
				column.setFieldType(javaType);
				column.setFieldTypeName(javaType.getSimpleName());
				columns.add(column);
			}
			
		} catch (SQLException e) {
			logger.error("", e);
		}

		return columns;
	}
	
	private Class<?> getJavaType(Integer sqlType){
		Class<?> cls = javaTypes.get(sqlType);
		if(cls == null){
			cls = String.class;
		}
		return cls;
	}
	
	private String tableName2ClassName(String tableName){
		if(tableName.startsWith("t")){
			tableName = tableName.substring(1);
		}
		return Strings.capitalize(Strings.camelCaseNamed(tableName));
	}

	private String columnName2FieldName(String columnName){
		return Strings.uncapitalize(Strings.camelCaseNamed(columnName));
	}
}
