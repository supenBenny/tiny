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
package me.fb.tiny.plugins.log.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import me.fb.tiny.plugins.log.domain.TableOperateLog;
import me.fb.tiny.plugins.log.service.TableOperateLogService;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月18日
 */
public class TableOperateLogServiceImpl implements TableOperateLogService,DisposableBean {
	
	private ArrayBlockingQueue<TableOperateLog> queue = new ArrayBlockingQueue<>(2 << 15);

	private DataSource dataSource;
	
	private ExecutorService  executor;  
	
	private final Logger logger = LoggerFactory.getLogger(TableOperateLogServiceImpl.class);

	@PostConstruct
	protected void init() {
		// CPU 核心数
		int coreCpuNum = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(coreCpuNum);

		new Thread(() -> {
			List<TableOperateLog> logs = new ArrayList<>();
			while (true) {
				try {
					TableOperateLog log = queue.poll(1L, TimeUnit.SECONDS);

					if (log != null) {
						logs.add(log);
					}

					if (log == null || logs.size() > 20) {
						if (!logs.isEmpty()) {
							executor.submit(new LogTask(logs));
							logs = new ArrayList<>();
						}
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.fb.tiny.plugins.log.service.TableOperateLogService#log(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public void logSql(String userId,String tableName, String operator, String sql,String sysName) {
		TableOperateLog log = new TableOperateLog();
		log.setUserId(userId);
		log.setTableName(tableName);
		log.setMessage(sql);
		//log.setUserId(operator);
		log.setSysName(sysName);
		
		String tmp = sql.toLowerCase();
		if(tmp.indexOf("insert") != -1){
			log.setActionLog("insert");
		}else if(tmp.indexOf("update") != -1){
			log.setActionLog("update");
		}else if(tmp.indexOf("delete") != -1){
			log.setActionLog("delete");
		}else {
			log.setActionLog("select");
		}
		
		log.setDate(new Date());
		
		try {
			queue.add(log);
		} catch (IllegalStateException e) {
			logger.error("",e);
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	class LogTask implements Runnable{
		
		private List<TableOperateLog> logs = null;
	
		public LogTask(List<TableOperateLog> logs){
			this.logs = logs;
		}
		
		@Override
		public void run() {
			if(this.logs == null || this.logs.isEmpty()){
				return;
			}
			Connection con = null;
			PreparedStatement statement = null;
			Boolean auto = null;
			try {
				con = dataSource.getConnection();
				auto = con.getAutoCommit();
				con.setAutoCommit(false);
				statement = con.prepareStatement("insert into tTabOperateLog (userId,tableName,actionlog,message,sysName,date)values(?,?,?,?,?,?)");
				for (TableOperateLog log : logs) {
					statement.setString(1, log.getUserId());
					statement.setString(2, log.getTableName());
					statement.setString(3, log.getActionLog());
					statement.setString(4, log.getMessage());
					statement.setString(5, log.getSysName());
					statement.setTimestamp(6, new java.sql.Timestamp(log.getDate().getTime()));
					statement.addBatch();	
				}
				statement.executeBatch();
				con.commit();
				
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
			
				}
				logger.error("",e);
			}finally{
				if(statement != null){
					try {
						statement.close();
					} catch (SQLException e) {
			
					}
				}
				
				if(con != null){
					try {
						con.setAutoCommit(auto);
						con.close();
					} catch (SQLException e) {
					}
				}
			}
			
		}
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		
	} 
}
