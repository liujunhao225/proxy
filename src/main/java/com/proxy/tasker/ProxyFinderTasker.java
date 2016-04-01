package com.proxy.tasker;

import com.alibaba.fastjson.JSONObject;
import com.proxy.tasker.base.Tasker;
import com.proxy.tool.DBTOOL;
import com.proxy.tool.SQLTYPE;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * get proxy_target and use some method extract ip and port
 * 
 * @author liujunhao
 *
 */
public class ProxyFinderTasker implements Tasker {

	@Test
	public void execute() {
		// get target

		String sql = "";
		@SuppressWarnings("unchecked")
		List<JSONObject> list = (List<JSONObject>) DBTOOL.execute(SQLTYPE.FINDLIST, sql, null);
		System.out.println(list);
		// iterator list
		ExecutorService service = Executors.newFixedThreadPool(2);
		while(){
			
		}
		// run target and extract ip port

	}

}
