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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.fb.tiny.dal.domain.DomainClass;
import me.fb.tiny.dal.domain.DomainClassProperty;
import me.fb.tiny.dal.domain.DomainFactory;
import me.fb.tiny.lang.AnnotationUtils;
import me.fb.tiny.lang.L;
import me.fb.tiny.scaffolding.annotation.Template;
import me.fb.tiny.scaffolding.schema.Column;
import me.fb.tiny.scaffolding.schema.Table;

/**
 * 模板生成工具
 *@author fb
 *@version 
 *@since 2015年9月17日
 */
public class TemplateGenerator {
	
	private Configuration configuration;
	
	private TemplateLoader templateLoader;
	
	private final String columnName = "columnName";
	
	private final String fieldName = "fieldName";
	
	private final Logger logger = LoggerFactory.getLogger(TemplateGenerator.class);
	
	
	public TemplateGenerator(){
		this(new Configuration());
	}
	
	public TemplateGenerator(Configuration configuration){
		this.configuration = configuration;
		this.templateLoader = this.configuration.getTemplateLoader();
	}
	
	public void generateView(DomainClass domainClass){
		String path = this.templateLoader.viewPath(domainClass);
		this.contextVariable(domainClass, path);
	}
	
	public void generateController(DomainClass domainClass){

		String path = this.templateLoader.controllerPath(domainClass);
		Map<String, Object> variable = this.contextVariable(domainClass, path);
		String templateName = "Controller.java";
		
		Annotation[] anns = domainClass.getEntityClass().getAnnotations();
		
		Template annotation = AnnotationUtils.getAnnotation(anns, Template.class);
		
		if(annotation != null && Strings.isNotBlank(annotation.mapper())){
			templateName = annotation.controller();
		}
		
		Reader reader = this.templateLoader.loadTemplate(templateName);
		
		File mainSourceDir = this.configuration.getMainSourceDir();
		
		File mapper = new File(mainSourceDir,path);
		
		File parent = mapper.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mapper),"UTF-8")){
			FreemarkerEngine.processTemplate(templateName,reader,variable, out);
		} catch (IOException e) {
			logger.error("",e);
		}
			
	}
	
	
	public void generateService(DomainClass domainClass){

		String path = this.templateLoader.servicePath(domainClass);
		Map<String, Object> variable = this.contextVariable(domainClass, path);
		String templateName = "Service.java";
		
		Annotation[] anns = domainClass.getEntityClass().getAnnotations();
		
		Template annotation = AnnotationUtils.getAnnotation(anns, Template.class);
		
		if(annotation != null && Strings.isNotBlank(annotation.mapper())){
			templateName = annotation.service();
		}
		
		Reader reader = this.templateLoader.loadTemplate(templateName);
		
		File mainSourceDir = this.configuration.getMainSourceDir();
		
		File mapper = new File(mainSourceDir,path);
		
		File parent = mapper.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mapper),"UTF-8")){
			FreemarkerEngine.processTemplate(templateName,reader,variable, out);
		} catch (IOException e) {
			logger.error("",e);
		}
		
		this.generateServiceImpl(domainClass);
	}
	
	protected void generateServiceImpl(DomainClass domainClass) {


		String path = this.templateLoader.serviceImplPath(domainClass);
		Map<String, Object> variable = this.contextVariable(domainClass, path);
		String templateName = "ServiceImpl.java";
		
//		Annotation[] anns = domainClass.getEntityClass().getAnnotations();
		
//		Template annotation = AnnotationUtils.getAnnotation(anns, Template.class);
//		
//		if(annotation != null && Strings.isNotBlank(annotation.mapper())){
//			templateName = annotation.service()
//		}
		
		Reader reader = this.templateLoader.loadTemplate(templateName);
		
		File mainSourceDir = this.configuration.getMainSourceDir();
		
		File mapper = new File(mainSourceDir,path);
		
		File parent = mapper.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mapper),"UTF-8")){
			FreemarkerEngine.processTemplate(templateName,reader,variable, out);
		} catch (IOException e) {
			logger.error("",e);
		}
		
	
	}
	
	
	public void generateMapper(DomainClass domainClass) {
		String path = this.templateLoader.mapperPath(domainClass);

		Map<String, Object> variable = this.contextVariable(domainClass, path);
		String templateName = "Mapper.xml";

		// 设置关联属性
		boolean isJoin = false;
		List<DomainClassProperty> persistentProperties = domainClass.getPersistentProperties();
		for (DomainClassProperty property : persistentProperties) {
			if (property.isDomainType()) {
				isJoin = true;
				DomainClass inner = DomainFactory.instance.getDomainClass(property.getType());
				property.setDomainFieldName(property.getFieldName() + "." + inner.getIdentifier().getFieldName());
			}
		}

		if (isJoin) {
			StringBuilder joinColumn = new StringBuilder();
			StringBuilder searchJoin = new StringBuilder();
			List<Map<String, String>> joinResultMap = new ArrayList<>();
			List<DomainClass> inners = new ArrayList<>();

			for (DomainClassProperty property : persistentProperties) {
				joinColumn.append("domain.").append(property.getColumnName()).append(" as ").append("domain_")
						.append(property.getColumnName()).append(",");

				if (property.isDomainType()) {
					DomainClass inner = DomainFactory.instance.getDomainClass(property.getType());

					inners.add(inner);

					searchJoin.append(" join ").append(inner.getTableName()).append(" as ").append("_")
							.append(inner.getPropertyName()).append(" on ").append("domain").append(".")
							.append(property.getColumnName()).append("=").append("_").append(inner.getPropertyName())
							.append(".").append(inner.getIdentifier().getColumnName());
				}
				if (!property.isIdentifier()) {
					if(property.isDomainType()){
						joinResultMap.add(L.map(columnName, "domain_" + property.getColumnName(), this.fieldName,
								property.getDomainFieldName()));
					}else {
						joinResultMap.add(L.map(columnName, "domain_" + property.getColumnName(), this.fieldName,
								property.getFieldName()));	
					}
					
				}
			}

			for (DomainClass inner : inners) {
				List<DomainClassProperty> properties = inner.getPersistentProperties();
				for (DomainClassProperty property : properties) {

					if (!property.isIdentifier()) {
						joinColumn.append("_").append(inner.getPropertyName()).append(".")
								.append(property.getColumnName()).append(" as ")
								.append(inner.getPropertyName()).append("_").append(property.getColumnName())
								.append(",");
						
						joinResultMap.add(L.map(columnName, inner.getPropertyName() + "_" + property.getColumnName(), 
								this.fieldName,inner.getPropertyName() + "." + property.getFieldName()));
					}

				}

			}
			
			joinColumn.deleteCharAt(joinColumn.length()-1);
			
			variable.put("joinColumn", joinColumn);
			variable.put("searchJoin", searchJoin);
			variable.put("joinResultMap", joinResultMap);
		}

		Annotation[] anns = domainClass.getEntityClass().getAnnotations();

		Template annotation = AnnotationUtils.getAnnotation(anns, Template.class);

		if (annotation != null && Strings.isNotBlank(annotation.mapper())) {
			templateName = annotation.mapper();
		}

		Reader reader = this.templateLoader.loadTemplate(templateName);

		File mainSourceDir = this.configuration.getMainSourceDir();

		File mapper = new File(mainSourceDir, path);

		// 文件已经存在
		if (mapper.exists()) {
			// SAXReader saxReader = new SAXReader();
			// try {
			// Document document = saxReader.read(mapper);
			// } catch (DocumentException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		File parent = mapper.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}

		try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mapper), "UTF-8")) {
			FreemarkerEngine.processTemplate(templateName, reader, variable, out);
		} catch (IOException e) {
			logger.error("", e);
		}

	}
	
	public void generateRepository(DomainClass domainClass){

		String path = this.templateLoader.repositoryPath(domainClass);
		Map<String, Object> variable = this.contextVariable(domainClass, path);
		String templateName = "Repository.java";
		
		Annotation[] anns = domainClass.getEntityClass().getAnnotations();
		
		Template annotation = AnnotationUtils.getAnnotation(anns, Template.class);
		
		if(annotation != null && Strings.isNotBlank(annotation.mapper())){
			templateName = annotation.controller();
		}
		
		Reader reader = this.templateLoader.loadTemplate(templateName);
		
		File mainSourceDir = this.configuration.getMainSourceDir();
		
		File mapper = new File(mainSourceDir,path);
		
		File parent = mapper.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mapper),"UTF-8")){
			FreemarkerEngine.processTemplate(templateName,reader,variable, out);
		} catch (IOException e) {
			logger.error("",e);
		}
		
		this.generateRepositoryImpl(domainClass);
	
	}
	
	protected void generateRepositoryImpl(DomainClass domainClass){

		String path = this.templateLoader.repositoryImplPath(domainClass);
		Map<String, Object> variable = this.contextVariable(domainClass, path);
		String templateName = "RepositoryImpl.java";
		
//		Annotation[] anns = domainClass.getEntityClass().getAnnotations();
//		
//		Template annotation = AnnotationUtils.getAnnotation(anns, Template.class);
//		
//		if(annotation != null && Strings.isNotBlank(annotation.mapper())){
//			templateName = annotation.repository();
//		}
		
		Reader reader = this.templateLoader.loadTemplate(templateName);
		
		File mainSourceDir = this.configuration.getMainSourceDir();
		
		File mapper = new File(mainSourceDir,path);
		
		File parent = mapper.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(mapper),"UTF-8")){
			FreemarkerEngine.processTemplate(templateName,reader,variable, out);
		} catch (IOException e) {
			logger.error("",e);
		}
			
	
	}
	
	public void generateDomain(Table table){

		Map<String, Object> variable = new HashMap<>();
		
		String basePackage = table.getPackageName();
		
		Long serialVersionUID = System.currentTimeMillis();
				
		StringBuilder importClasses = new StringBuilder();
		
		table.getColumns().forEach((Column column)->{
			if(!column.getFieldType().getName().startsWith("java.lang")){
				importClasses.append("import ").append(column.getFieldType().getName()).append(";\r\n");
			}
		});
		
		serialVersionUID = ((Double)(Math.random()*100000000000000000L)).longValue();
		
		variable.put("basePackage", table.getPackageName());
		variable.put("importClasses", importClasses);
		variable.put("table", table);
		variable.put("serialVersionUID", String.valueOf(serialVersionUID));
		
		
		String templateName = "Domain.java";
		
		Reader reader = this.templateLoader.loadTemplate(templateName);
		
		File mainSourceDir = this.configuration.getMainSourceDir();
		
		
		
		File domain = new File(mainSourceDir,basePackage.replace('.', '/') + "/" + table.getClassName() + ".java");
		
		if(domain.exists()){
			System.out.println(basePackage + table.getClassName()+ " exists");
			
		}else {
			File parent = domain.getParentFile();
			if(!parent.exists()){
				parent.mkdirs();
			}
			
			try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(domain),"UTF-8")){
				FreemarkerEngine.processTemplate(templateName,reader,variable, out);
			} catch (IOException e) {
				logger.error("",e);
			}
		}
	
	}
	
	public void generateTest(DomainClass domainClass){
		
	}
	
	/**
	 * 自动生成所有内容
	 * @param domainClass
	 */
	public void generateAll(DomainClass domainClass){
		
		this.generateMapper(domainClass);
		this.generateRepository(domainClass);
		this.generateService(domainClass);
		this.generateController(domainClass);
		this.generateView(domainClass);
		this.generateTest(domainClass);
		
	}
	
	
	
	private Map<String,Object> contextVariable(DomainClass domainClass,String path){
		Map<String,Object> params = new HashMap<>();
		params.put("domainClass", domainClass);
		params.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		params.put("package", path2Package(path));
		params.put("author", "");
		
		String[] split = domainClass.getFullName().split("\\.");
		StringBuilder sb = new StringBuilder();
		//sb.append(str)
		for(int i=0;i<split.length-2;i++){
			sb.append(split[i]).append(".");
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length()-1);
		}
		params.put("basepackage", sb.toString());
		
		return params;
	}
	
	private String path2Package(String path){
		String[] split = path.split("/");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<split.length-1;i++){
			sb.append(split[i]).append(".");
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	protected void processTemplate(String templateName,Reader reader, Map<?, ?> root, Writer out) {
		
	}
	
	
}
