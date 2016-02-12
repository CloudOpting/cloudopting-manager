package eu.cloudopting.monitoring;

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

	public void testZabbix(){
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
        Request getRequest = RequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);
        System.err.println(getResponse);
        String hostid = null;
		try {
			hostid = getResponse.getJSONArray("result")
			        .getJSONObject(0).getString("hostid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.err.println(hostid);
        
	}
	
	public boolean loginZabbix(){
		this.zabbixApi = new DefaultZabbixApi(this.zabbix_endpoint);
        this.zabbixApi.init();
        boolean login = zabbixApi.login(this.zabbix_user, this.zabbix_password);
        System.err.println("login:" + login);

        return login;
	}
	
	public String get hostId(String host){
		
		JSONObject filter = new JSONObject();

        try {
			filter.put("host", new String[] { host });
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Request getRequest = RequestBuilder.newBuilder()
                .method("host.get").paramEntry("filter", filter)
                .build();
		
		JSONObject getResponse = zabbixApi.call(getRequest);
        System.err.println(getResponse);
        String hostid = null;
		try {
			hostid = getResponse.getJSONArray("result")
			        .getJSONObject(0).getString("hostid");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.err.println(hostid);
        return hostid;
	}
}
