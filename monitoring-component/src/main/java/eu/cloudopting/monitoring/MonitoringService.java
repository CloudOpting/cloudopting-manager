package eu.cloudopting.monitoring;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.cloudopting.monitoring.zabbix.DefaultZabbixApi;
import eu.cloudopting.monitoring.zabbix.Request;
import eu.cloudopting.monitoring.zabbix.RequestBuilder;

@Service
public class MonitoringService {
	private final Logger log = LoggerFactory.getLogger(MonitoringService.class);

	@Value("${zabbix.user}")
	private String zabbix_user;

	@Value("${zabbix.password}")
	private String zabbix_password;

	@Value("${zabbix.endpoint}")
	private String zabbix_endpoint;

	private DefaultZabbixApi zabbixApi;

	public void testZabbix() {
		String url = "http://tst-zabbix-opendata.odsp.csi.it/zabbix/api_jsonrpc.php";
		DefaultZabbixApi zabbixApi = new DefaultZabbixApi(url);
		zabbixApi.init();

		boolean login = zabbixApi.login(this.zabbix_user, this.zabbix_password);
		System.err.println("login:" + login);

		String host = "tst-zabbix-opendata.odsp.csi.it";
		JSONObject filter = new JSONObject();

		try {
			filter.put("host", new String[] { host });
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Request getRequest = RequestBuilder.newBuilder().method("host.get").paramEntry("filter", filter).build();
		JSONObject getResponse = this.zabbixApi.call(getRequest);
		System.err.println(getResponse);
		String hostid = null;
		try {
			hostid = getResponse.getJSONArray("result").getJSONObject(0).getString("hostid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(hostid);

	}

	public boolean loginZabbix() {
		this.zabbixApi = new DefaultZabbixApi(this.zabbix_endpoint);
		this.zabbixApi.init();
		boolean login = zabbixApi.login(this.zabbix_user, this.zabbix_password);
		System.err.println("login:" + login);

		return login;
	}

	public JSONArray getHostId(String host) {

		JSONObject filter = new JSONObject();

		try {
			filter.put("host", new String[] { host });
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Request getRequest = RequestBuilder.newBuilder().method("host.get").paramEntry("filter", filter).build();

		JSONObject getResponse = this.zabbixApi.call(getRequest);
		JSONArray hosts = null;
		System.err.println(getResponse);
		String hostid = null;
		try {
//			hostid = getResponse.getJSONArray("result").getJSONObject(0).getString("hostid");
			hosts = getResponse.getJSONArray("result");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(hostid);
		return hosts;
	}

	public JSONArray getItems(String hostid) {
		Request getRequest = RequestBuilder.newBuilder().method("item.get").paramEntry("output", "extend")
				.paramEntry("hostids", hostid).paramEntry("sortfield", "name").build();

		JSONObject getResponse = this.zabbixApi.call(getRequest);
		System.err.println(getResponse);
		JSONArray items = null;

		try {
			items = getResponse.getJSONArray("result");
			for (int i = 0; i < items.length(); i++) {
				JSONObject info = items.getJSONObject(i);
				log.debug(info.getString("description"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return items;
	}
	
	public JSONArray getDataHistory(String itemid) {
		return getDataHistory(itemid, "clock", "DESC", "100");
	}

	public JSONArray getDataHistory(String itemid, String sortfield, String sortorder, String limit) {
		Request getRequest = null;
		
		if (limit == null || limit.isEmpty()) {
			getRequest = RequestBuilder.newBuilder().method("history.get").paramEntry("output", "extend")
					.paramEntry("itemids", itemid).paramEntry("sortfield", sortfield).paramEntry("sortorder", sortorder)
					.build();

		} else {
			getRequest = RequestBuilder.newBuilder().method("history.get").paramEntry("output", "extend")
					.paramEntry("itemids", itemid).paramEntry("sortfield", sortfield).paramEntry("sortorder", sortorder)
					.paramEntry("limit", limit).build();
		}

		JSONObject getResponse = this.zabbixApi.call(getRequest);
		System.err.println(getResponse);
		JSONArray data = null;
		JSONArray dataRet = new JSONArray();

		try {
			data = getResponse.getJSONArray("result");
			for (int i = 0; i < data.length(); i++) {
				JSONObject measure = new JSONObject();
//				log.debug(measure.getString("clock"));
				measure.put("clock", data.getJSONObject(i).optLong("clock")*1000);
				measure.put("value", data.getJSONObject(i).optInt("value"));
				dataRet.put(measure);
//				data.getJSONObject(i).put("clock", data.getJSONObject(i).optLong("clock")*1000);
//				data.getJSONObject(i).put("value", data.getJSONObject(i).optInt("value"));
				log.debug(measure.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataRet;
	}
}
