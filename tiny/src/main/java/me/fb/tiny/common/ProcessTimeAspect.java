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
package me.fb.tiny.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import me.fb.tiny.lang.Stopwatch;

/**
 * 
 *@author fb
 *@version 
 *@since 2015年9月24日
 */
public class ProcessTimeAspect {
	
	private Map<String, Method> timer = new HashMap<String, Method>();

	// @Around("execution(* me.fb.*.*(..))")
	public Object process(ProceedingJoinPoint pjp) throws Throwable {

		Stopwatch sw = Stopwatch.begin();
		Object o = pjp.proceed();
		sw.stop();
		String id = pjp.getSignature().toLongString();
		Method m = timer.get(id);
		if (m == null) {
			m = new Method(id);
			timer.put(id, m);
		}
		m.increase();
		m.addDuration(sw.getDuration());
		return o;

	}
	
	public List<Method> getMethods(){
		return new ArrayList<>(this.timer.values());
	}

	public class Method {
		
		//方法执行次数
		private int times = 0;
		
		//执行总耗时时间
		private long total = 0L;
		
		private long max = 0L;
		
		//方法唯一标识
		private String id = null;

		public Method() {

		}

		public Method(String id) {
			this.id = id;
		}

		public Method(String id, int time) {
			this.increase();
			this.addDuration(time);
		}

		/**
		 * 方法执行一次耗时时间
		 * 
		 * @param time
		 * @return
		 */
		public long addDuration(long time) {
			total = total + time;
			//最长执行时间
			max = max > time?max:time;
			return total;
		}

		public int increase() {
			return ++this.times;
		}

		public long getAverage() {
			if (times != 0) {
				return total / this.times;
			}
			return 0;
		}

		public long getTotal() {
			return total;
		}

		public String getId() {
			return id;
		}

		public int getTimes() {
			return times;
		}
		
		
		
		
	}
}
