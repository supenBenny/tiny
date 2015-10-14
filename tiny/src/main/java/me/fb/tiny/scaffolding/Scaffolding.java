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
package me.fb.tiny.scaffolding;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.fb.tiny.dal.domain.DomainClass;
import me.fb.tiny.dal.domain.DomainFactory;
import me.fb.tiny.scaffolding.schema.SqlServerSchemaExport;
import me.fb.tiny.scaffolding.schema.Table;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月18日
 */
public class Scaffolding {
	
	private Set<Class<?>> classes = new HashSet<>();
	
	private TemplateGenerator generator = new TemplateGenerator(); 
	
	public void addClass(Class<?> cls){
		DomainFactory.instance.register(cls);
		classes.add(cls);
	}
	
	public void generateDomain(Connection conn,String basePackage,String tableName){
		SqlServerSchemaExport export = new SqlServerSchemaExport(conn);
		Table table = export.getTable(tableName);
		if(table != null){
			table.setPackageName(basePackage);
			generator.generateDomain(table);
		}
	}
	
	public void generateMapper(Class<?> cls){
		DomainClass domainClass = DomainFactory.instance.getDomainClass(cls);
		if(domainClass == null){
			domainClass = DomainFactory.instance.register(cls);
		}
		classes.add(cls);
		
		generator.generateMapper(domainClass);
	}

	
	public List<Table> getTables(Connection conn){
		SqlServerSchemaExport export = new SqlServerSchemaExport(conn);
		return export.getTables();
	}
	
	
	
	public void generate(Class<?> cls){
		DomainClass domainClass = DomainFactory.instance.getDomainClass(cls);
		if(domainClass == null){
			domainClass = DomainFactory.instance.register(cls);
		}
		classes.add(cls);
		
		generator.generateAll(domainClass);
		
	}
	
	public void generateAll(){
		for (Class<?> cls : classes) {
			DomainClass domainClass = DomainFactory.instance.getDomainClass(cls);
			if(domainClass == null){
				domainClass = DomainFactory.instance.register(cls);
			}
			generator.generateAll(domainClass);
		}
	}

}
