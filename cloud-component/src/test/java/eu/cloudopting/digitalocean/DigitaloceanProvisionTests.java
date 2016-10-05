package eu.cloudopting.digitalocean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.jclouds.ContextBuilder;
import org.jclouds.collect.PagedIterable;
import org.jclouds.digitalocean2.DigitalOcean2Api;
import org.jclouds.digitalocean2.domain.Droplet;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloudopting.cloud.CloudProvider;
import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.config.ProvisionConfig;
import eu.cloudopting.provision.digitalocean.DigitaloceanRequest;
import eu.cloudopting.provision.digitalocean.DigitaloceanResult;

@ContextConfiguration(classes = {ProvisionConfig.class, CloudService.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DigitaloceanProvisionTests {

	@Inject
	ProvisionComponent<DigitaloceanResult, DigitaloceanRequest> digitaloceanProvision;

	@Inject
	CloudService cloudService;

	@Before
	public void setUp(){
		cloudService.setUpCloud("", "1990101366ffe7513d52957a68a090ac453f0ff66bf1f2edfa4d736c97f9c0b2", null, "digitalocean", -1l);
	}

	/**
	 * Method that test the creation of droplet. Prints to console the droplet id. 
	 */
	@Test
	public void testCreateVM() {
		String dropletId = cloudService.createVM(-1l, null, null, null, null);
		System.out.println(dropletId);
	}

	/**
	 * Method that checks if droplet status is ACTIVE 
	 */
	@Test
	public void testCheckVMdeployed() { 
		String dropletId = "27948412";
		boolean vmDeployed = cloudService.checkVM(-1l, dropletId);
		Assert.assertTrue(vmDeployed);
	}

	/**
	 * Method that prints to console the droplet IP based on droplet id
	 */
	@Test
	public void testgetAcquiredIpinfo() { 
		String dropletId = "27948412";
		Map<String, Object> cloudParams = new HashMap<>();
		cloudParams.put("vmId", dropletId);
		cloudParams.put("cloudAccountId", -1l);
		JSONObject result = cloudService.getAssociatedIpinfo(cloudParams);
		System.out.println(result); 
	}

	/**
	 * Delete a droplet by id
	 */
	@Test
	public void deleteDroplet() { 
		int dropletId = 27960183;
		DigitaloceanRequest request = createDigitaloceanRequest();
		DigitalOcean2Api api = getClient(request);
		api.dropletApi().delete(dropletId);
		System.out.println("Deleted:" + dropletId);
	}

	@Test
	public void listAllDroplets() { 
		DigitaloceanRequest request = createDigitaloceanRequest();
		DigitalOcean2Api api = getClient(request);
		PagedIterable<Droplet> list = api.dropletApi().list();
		list.concat().forEach(new Consumer<Droplet>(){
			@Override
			public void accept(Droplet t) {
				System.out.println("Id:" + t.id() + ", Name:" + t.name() + ", Status:" + t.status() + ", CreatedAt:" + t.createdAt());
			}
		});
	}

	private DigitaloceanRequest createDigitaloceanRequest() {
		DigitaloceanRequest request = new DigitaloceanRequest();
		request.setIdentity("");
		request.setCredential("1990101366ffe7513d52957a68a090ac453f0ff66bf1f2edfa4d736c97f9c0b2");

		//Old userdata
		//		String unencodedData = "#cloud-config\n" +
		//				"yum_repos:\n" +
		//				"    docker:\n" +
		//				"        baseurl: https://yum.dockerproject.org/repo/main/centos/7\n" +
		//				"        enabled: 1\n" +
		//				"        gpgcheck: 1\n" +
		//				"        gpgkey: https://yum.dockerproject.org/gpg\n" +
		//				"        name: Docker Repository\n" +
		//				"packages:\n" +
		//				"- docker-engine";

		String unencodedData = "#cloud-config\n" +
				//				"chpasswd:\n" +
				//				"  list: |\n" +
				//				"    root:root\n"+
				//				"users:\n" +
				//				"- name: root\n" +
				//				"    ssh-authorized-keys:\n" +
				//				"        - ssh-rsa AAAAB3NzaC1yc2EAAAABJQAAAQEApQxknOPNuWbu9U16cjuHW2rWG3+GnVd/TujqLaFIjP8s5Y6Zq4QnREl6Cg076TkSsRmXLQzOgw1zj8J8DbquubvtGHGZjVyC3aSxo/ChemblnyPkWvUiOjVJ7bmxy0/SLnNxnoIfpV2crOvADEKPuAquDx3SsNeGprqb5htGqv8rl5MStU1u+Fbe9FGDfsyggog/gC7y0/cjRzE8zuwI5WQVikKAbNTHo+wYtg8RpFhgSrE62bx8ycxUPpwiesk7dT7fsZZ/98fAFmVMekMRF2nlrgmmBechTq35W+d6pQxyvA+b8pojwuvZpjJFYgnQ8wOfb70nyCFcTyBJjDiI6Q== rsa-key-20160629";
				"yum_repos:\n" +
				"    docker:\n" +
				"        baseurl: https://yum.dockerproject.org/repo/main/centos/7\n" +
				"        enabled: 1\n" +
				"        gpgcheck: 1\n" +
				"        gpgkey: https://yum.dockerproject.org/gpg\n" +
				"        name: Docker Repository\n" +
				"packages:\n" +
				"- docker-engine\n" +
				"write_files:\n" +
				"- path: /etc/systemd/system/docker.service.d/docker.conf\n" +
				"  content: |\n" +
				"    [Service]\n" +
				"    ExecStart=\n" +
				"    ExecStart=/usr/bin/docker daemon -H fd:// -H tcp://0.0.0.0:2375\n" +
				"runcmd:\n" +
				"- 'systemctl daemon-reload'\n" +
				"- 'systemctl enable docker'\n" +
				"- 'systemctl start docker'";
		request.setUserData(unencodedData);
		return request;
	}

	private DigitalOcean2Api getClient(DigitaloceanRequest request) {
		ContextBuilder builder = ContextBuilder.newBuilder(CloudProvider.DIGITALOCEAN.getJcloudsName())
				.credentials(request.getIdentity(), request.getCredential());
		return builder.buildApi(DigitalOcean2Api.class);
	}
}
