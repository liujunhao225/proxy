package com.proxy.worker;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.UpdatableResultSet;
import com.proxy.tool.DBTOOL;
import com.proxy.tool.SQLTYPE;
import com.proxy.util.SystemLocks;

public class ProxySpiderWorker implements Runnable, BaseWorker {

	public void process() {
		SystemLocks.TARGETURLLOCK.lock();

		try {
			// 从数据库取得未处理过的数据
			String findSQL = "SELECT t.`id`,t.`key`,t.`target_url` FROM `proxy_target` t  WHERE t.`is_enable` = 'T' AND STATUS='FREE' limit 0,1";
			JSONObject targetObject = (JSONObject) DBTOOL.execute(SQLTYPE.FINDONE, findSQL, null);
			// 更新这条数据的状态为BUSY
			targetObject.get("id");
			String updateSQL = "update proxy_target set STATUS='BUSY' where t.id=";
			Object obj[] =new Object[1];
			obj[0] = targetObject.get("id");
			DBTOOL.execute(SQLTYPE.UPDATE, updateSQL, obj);
		} finally {
			SystemLocks.TARGETURLLOCK.lock();
		}

	}

	public void run() {
		process();
	}

}
