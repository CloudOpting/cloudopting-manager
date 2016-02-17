package eu.cloudopting.monitoring;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.CustomizationDeployInfo;
import eu.cloudopting.domain.Customizations;
import eu.cloudopting.monitoring.zabbix.DefaultZabbixApi;
import eu.cloudopting.monitoring.zabbix.Request;
import eu.cloudopting.monitoring.zabbix.RequestBuilder;
import eu.cloudopting.service.CustomizationService;

@Transactional
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

	@Autowired
	CustomizationService customizationService;

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

	public JSONArray getHostId(Long customizationId) {
		Customizations theCust = customizationService.findOne(customizationId);
		Set<CustomizationDeployInfo> dinfo = theCust.getDeployInfos();
		String fqdns[] = new String[dinfo.size()];
		String fqdn = null;
		int idx = 0;
		for (CustomizationDeployInfo di : dinfo) {
			fqdn = di.getFqdn();
			fqdns[idx] = fqdn;
			idx++;
		}

		JSONObject filter = new JSONObject();

		try {
			filter.put("host", fqdns);
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
			// hostid =
			// getResponse.getJSONArray("result").getJSONObject(0).getString("hostid");
			hosts = getResponse.getJSONArray("result");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(hostid);
		return hosts;
	}

	public JSONArray getItems(String hostid) {

		// just get integer values
		JSONObject filter = new JSONObject();
		try {
			filter.put("value_type", "3");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Request getRequest = RequestBuilder.newBuilder().method("item.get").paramEntry("output", "extend")
				.paramEntry("hostids", hostid).paramEntry("sortfield", "name").paramEntry("search", filter).build();

		JSONObject getResponse = this.zabbixApi.call(getRequest);
		System.err.println(getResponse);
		JSONArray items = null;

		try {
			items = getResponse.getJSONArray("result");
			for (int i = 0; i < items.length(); i++) {
				JSONObject info = items.getJSONObject(i);
				Pattern theKey = Pattern.compile("^[0-9a-zA-Z_.-]+\\[(.*)\\]$");
				String[] keys = theKey.split(info.getString("key_"));
				Matcher match = theKey.matcher(info.getString("key_"));
				while (match.find()) {
					if (match.groupCount() > 0) {
						String[] splitted = match.group(1).split(",");
						String expanded_name = info.getString("name");
						for (int s = 0; s < splitted.length; s++) {
							String search = "\\$" + (s + 1);
							expanded_name = expanded_name.replaceAll(search, splitted[s]);
						}
						items.getJSONObject(i).put("name", expanded_name);
					}
				}
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
				// log.debug(measure.getString("clock"));
				measure.put("clock", data.getJSONObject(i).optLong("clock") * 1000);
				measure.put("value", data.getJSONObject(i).optInt("value"));
				dataRet.put(measure);
				// data.getJSONObject(i).put("clock",
				// data.getJSONObject(i).optLong("clock")*1000);
				// data.getJSONObject(i).put("value",
				// data.getJSONObject(i).optInt("value"));
				log.debug(measure.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataRet;
	}
}
