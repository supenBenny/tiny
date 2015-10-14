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

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月17日
 */
public class FreemarkerEngine {
	
	private static freemarker.template.Configuration config = new freemarker.template.Configuration(
			freemarker.template.Configuration.getVersion());

	private final static Logger logger = LoggerFactory.getLogger(FreemarkerEngine.class);

	static {
		//config.setTemplateLoader(templateLoader);
	}
	
	/**
	 * 
	 * @param templateName 模板名称
	 * @param reader 模板内容
	 * @param root
	 * @param out
	 */
	public static void processTemplate(String templateName,Reader reader, Map<?, ?> root, Writer out) {
		Template template = null;
		try {
			template = new Template(templateName, reader, config);
			template.process(root, out);
			out.flush();
		} catch (Exception e) {
			logger.error("",e);
		}
		
//		try {
//			// 获得模板
//			Template template = config.getTemplate(templateName, "utf-8");
//			// 生成文件（这里是我们是生成html）
//			template.process(root, out);
//			out.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (TemplateException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				out.close();
//				out = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
