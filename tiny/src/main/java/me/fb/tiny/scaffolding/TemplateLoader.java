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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.springframework.core.io.ClassPathResource;

import me.fb.tiny.dal.domain.DomainClass;

/**
 * 
 * @author fb
 * @version
 * @since 2015年9月17日
 */
public class TemplateLoader {

	private String templateLocation = "me/fb/tiny/scaffolding/template/";

	public Reader loadTemplate(String templateName) {
		ClassPathResource resource = new ClassPathResource(templateLocation + templateName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(resource.getInputStream(), Charset.forName("UTF-8")));
		} catch (Exception e) {

		}
		
		return br;
	}

	public String mapperPath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}
		sb.append("repository/mapper/").append(domainClass.getSimpleName()).append("Mapper.xml");

		return sb.toString();
	}

	public String repositoryPath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}
		sb.append("repository/").append(domainClass.getSimpleName()).append("Repository.java");

		return sb.toString();
	}
	
	public String repositoryImplPath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}
		sb.append("repository/impl/").append(domainClass.getSimpleName()).append("RepositoryImpl.java");

		return sb.toString();
	}

	public String servicePath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}
		sb.append("service/").append(domainClass.getSimpleName()).append("Service.java");

		return sb.toString();
	}

	public String serviceImplPath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}
		sb.append("service/impl/").append(domainClass.getSimpleName()).append("ServiceImpl.java");

		return sb.toString();
	}
	
	public String controllerPath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}
		sb.append("controller/").append(domainClass.getSimpleName()).append("Controller.java");

		return sb.toString();
	}

	public String testPath(DomainClass domainClass) {
		String[] split = domainClass.getFullName().split("\\.");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length - 2; i++) {
			sb.append(split[i]).append("/");
		}

		return sb.toString();
	}

	public String viewPath(DomainClass domainClass) {

		StringBuilder sb = new StringBuilder();
		sb.append(domainClass.getSimpleName()).append("/").append(domainClass.getSimpleName()).append("Mapper.xml");

		return sb.toString();
	}
}
