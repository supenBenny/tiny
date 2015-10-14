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

/**
 * 自动生成配置文件
 *@author fb
 *@version 
 *@since 2015年9月17日
 */
public class Configuration {
	
	/**
	 * project根目录
	 */
	private String basedir;
	
	private String mainSouceDir = "/src/main/java";
		
	private String testSourceDir = "/src/main/test";
	
	private TemplateLoader templateLoader;

	public Configuration(){
		
		this.init();
	}
	
	//初始化默认目录
	private void init(){
		basedir = new File(new File(this.getClass().getClassLoader().getResource("").getFile()).getParent()).getParent();
		basedir.replace('\\', '/');
		
		this.templateLoader = new TemplateLoader();
	}
	
	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		System.out.println(cfg.getMainSourceDir());
	}
	
	/**
	 * 获取源码目录,子文件为包开始目录
	 * @return
	 */
	public File getMainSourceDir(){
		return new File(this.basedir,this.mainSouceDir);
	}
	
	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public TemplateLoader getTemplateLoader() {
		return templateLoader;
	}

	public void setTemplateLoader(TemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	public String getMainSouceDir() {
		return mainSouceDir;
	}

	public void setMainSouceDir(String mainSouceDir) {
		this.mainSouceDir = mainSouceDir;
	}

	public String getTestSourceDir() {
		return testSourceDir;
	}

	public void setTestSourceDir(String testSourceDir) {
		this.testSourceDir = testSourceDir;
	}

}
