package eu.cloudopting.monitoring.zabbix;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.cloudopting.monitoring.MonitoringService;
import flexjson.JSONTokener;



public class DefaultZabbixApi implements ZabbixApi {
	private final Logger log = LoggerFactory.getLogger(DefaultZabbixApi.class);

	CloseableHttpClient httpClient;

	URI uri;

	String auth;

	public DefaultZabbixApi(String url) {
		try {
			uri = new URI(url.trim());
		} catch (URISyntaxException e) {
			throw new RuntimeException("url invalid", e);
		}
	}

	public DefaultZabbixApi(URI uri) {
		this.uri = uri;
	}

	public DefaultZabbixApi(String url, CloseableHttpClient httpClient) {
		this(url);
		this.httpClient = httpClient;
	}

	public DefaultZabbixApi(URI uri, CloseableHttpClient httpClient) {
		this(uri);
		this.httpClient = httpClient;
	}

	@Override
	public void init() {
		if (this.httpClient == null) {
			this.httpClient = HttpClients.custom().build();
		}
	}

	@Override
	public void destory() {
		if (this.httpClient != null) {
			try {
				this.httpClient.close();
			} catch (Exception e) {
				log.error("close httpclient error!", e);
			}
		}
	}

	@Override
	public boolean login(String user, String password) {
		Request request = RequestBuilder.newBuilder().paramEntry("user", user)
				.paramEntry("password", password).method("user.login").build();
		log.debug("user"+user);
		log.debug("password"+password);
		log.debug("request in login"+request.toString());
		
		JSONObject response = call(request);
		log.debug("response in login"+response.toString());
		String auth = null;
		try {
			auth = response.getString("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (auth != null && !auth.isEmpty()) {
			this.auth = auth;
			return true;
		}
		return false;
	}

	@Override
	public String apiVersion() {
		Request request = RequestBuilder.newBuilder().method("apiinfo.version")
				.build();
		JSONObject response = call(request);
		try {
			return response.getString("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean hostExists(String name) {
		Request request = RequestBuilder.newBuilder().method("host.exists")
				.paramEntry("name", name).build();
		JSONObject response = call(request);
		try {
			return response.getBoolean("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public String hostCreate(String host, String groupId) {
		JSONArray groups = new JSONArray();
		JSONObject group = new JSONObject();
		try {
			group.put("groupid", groupId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		groups.put(group);
		Request request = RequestBuilder.newBuilder().method("host.create")
				.paramEntry("host", host).paramEntry("groups", groups).build();
		JSONObject response = call(request);
		try {
			return response.getJSONObject("result").getJSONArray("hostids")
					.getString(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean hostgroupExists(String name) {
		Request request = RequestBuilder.newBuilder()
				.method("hostgroup.exists").paramEntry("name", name).build();
		JSONObject response = call(request);
		try {
			return response.getBoolean("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 * @return groupId
	 */
	public String hostgroupCreate(String name) {
		Request request = RequestBuilder.newBuilder()
				.method("hostgroup.create").paramEntry("name", name).build();
		JSONObject response = call(request);
		try {
			return response.getJSONObject("result").getJSONArray("groupids")
					.getString(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JSONObject call(Request request) {
		if (request.getAuth() == null) {
			request.setAuth(auth);
		}
		log.debug("request in call"+request.toString());
		CloseableHttpResponse response = null;
		try {
			HttpUriRequest httpRequest = org.apache.http.client.methods.RequestBuilder
					.post().setUri(uri)
					.addHeader("Content-Type", "application/json")
					.setEntity(new StringEntity(request.toString()))
					.build();
			try{
				response = this.httpClient.execute(httpRequest);
			}catch(ClientProtocolException e){
/*				if (response != null){ 
					response.close();
				}
	*/			log.debug(e.getMessage());
			}catch(IOException e){
	/*			if (response != null){ 
					response.close();
				}
		*/		log.debug(e.getMessage());
			}
			log.debug("response"+response.toString());
//			HttpEntity entity = response.getEntity();
//			byte[] data = EntityUtils.toByteArray(entity);
			String responseContent = IOUtils.toString(response.getEntity().getContent());
			log.debug("responseContent"+responseContent);
			JSONObject jresult = new JSONObject(responseContent);
			log.debug("test"+jresult.toString());
//			JSONTokener tokener = new JSONTokener(responseContent);
			response.close();
			return jresult;
		} catch (IOException e) {
			
			throw new RuntimeException("DefaultZabbixApi call exception!", e);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
		return null;
	}

}
