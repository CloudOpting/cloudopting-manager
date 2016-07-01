package eu.cloudopting.provision.digitalocean;

import java.util.Comparator;

import org.jclouds.ContextBuilder;
import org.jclouds.digitalocean2.DigitalOcean2Api;
import org.jclouds.digitalocean2.domain.Action;
import org.jclouds.digitalocean2.domain.Droplet;
import org.jclouds.digitalocean2.domain.DropletCreate;
import org.jclouds.digitalocean2.domain.Image;
import org.jclouds.digitalocean2.domain.Key;
import org.jclouds.digitalocean2.domain.Region;
import org.jclouds.digitalocean2.domain.Size;
import org.jclouds.digitalocean2.domain.options.CreateDropletOptions;
import org.jclouds.digitalocean2.domain.options.ImageListOptions;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.primitives.Floats;

import eu.cloudopting.cloud.CloudProvider;
import eu.cloudopting.provision.AbstractProvision;

public class DigitaloceanProvision extends AbstractProvision<DigitaloceanResult, DigitaloceanRequest> {

	@Override
	public DigitaloceanResult provision(DigitaloceanRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String provisionVM(DigitaloceanRequest request) {
		DigitalOcean2Api api = getClient(request);
		Size machineType = getMachineType(api); 
		Region region = getRegion(api, machineType);
		Image image = getImage(api, region);
		Key sshKey = api.keyApi().get(request.getIdentity());
		if(sshKey == null){
			throw new RuntimeException("Ssh Key not found for fingerprint " + request.getIdentity());
		}
		
		CreateDropletOptions digitalOceanSpecificParams = 
				CreateDropletOptions.builder().backupsEnabled(false).privateNetworking(false).addSshKeyId(sshKey.id()).build();
		DropletCreate result = api.dropletApi().create("testOcean", region.slug(), machineType.slug(), image.slug(), digitalOceanSpecificParams);
		
		return String.valueOf(result.droplet().id());
	}

	// Method returns the cheapest machine by hourly price
	private Size getMachineType(DigitalOcean2Api api){
		Size size = api.sizeApi().list().concat().toSortedList(new Comparator<Size>() {
			@Override
			public int compare(Size machine1, Size machine2) {
				if(!machine1.available()){
					return 1;
				}
				if(!machine2.available()){
					return -1;
				}	
				return Floats.compare(machine1.priceHourly(), machine2.priceHourly());
			}
		}).get(0);
		return size;
	}
	
	// Method returns first available region for starting a vm of type machineType
	private Region getRegion(DigitalOcean2Api api, Size machineType){
		Optional<? extends Region> region = api.regionApi().list().concat().firstMatch(isRegionOk(machineType));
		return region.orNull();
	}
	
	private static Predicate<Region> isRegionOk(Size machineType) {
		return new Predicate<Region>() {
			@Override
			public boolean apply(Region region) {
				return region.available() && machineType.regions().contains(region.slug());
			}
		};
	}
	
	private Image getImage(DigitalOcean2Api api, Region region){
		Optional<? extends Image> image = api.imageApi().list(ImageListOptions.Builder.type("distribution")).firstMatch(isImageOk(region));
		return image.orNull();
	}
	
	private static Predicate<Image> isImageOk(Region region) {
		return new Predicate<Image>() {
			@Override
			public boolean apply(Image image) {
				return image.regions().contains(region.slug()) && image.distribution().equals("CentOS");
			}
		};
	}
	
	@Override
	public boolean checkVMdeployed(DigitaloceanRequest request, String taskId) {
		DigitalOcean2Api api = getClient(request);
		Droplet droplet = api.dropletApi().get(Integer.valueOf(taskId));
		if(droplet != null && droplet.status() == Droplet.Status.ACTIVE){
			return true;
		}
		return false;
	}

	private DigitalOcean2Api getClient(DigitaloceanRequest request) {
		ContextBuilder builder = ContextBuilder.newBuilder(CloudProvider.DIGITALOCEAN.getJcloudsName())
				.credentials(request.getIdentity(), request.getCredential()); //.endpoint(request.getEndpoint());
		return builder.buildApi(DigitalOcean2Api.class);
	}
	
	@Override
	public JSONObject getVMinfo(DigitaloceanRequest request, String taskId) {
		DigitalOcean2Api api = getClient(request);
		Droplet droplet = api.dropletApi().get(Integer.valueOf(taskId));
		if(droplet == null){
			return null;
		}
		JSONObject vmData = new JSONObject();
		try {
			vmData.put("vmId", droplet.id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vmData;
	}

	@Override
	public String acquireIp(DigitaloceanRequest myRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkIpAcquired(DigitaloceanRequest myRequest, String taskId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject getAcquiredIpinfo(DigitaloceanRequest request, String vmId) {
		DigitalOcean2Api api = getClient(request);
		Droplet droplet = api.dropletApi().get(Integer.valueOf(vmId));
		if(droplet == null){
			return null;
		}
		JSONObject ipData = new JSONObject();
		try {
			ipData.put("ip", droplet.getPublicAddresses().iterator().next().ip());
			ipData.put("ipId", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ipData;
	}

	@Override
	public String portForward(DigitaloceanRequest myRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkPortForward(DigitaloceanRequest myRequest, String taskId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject getVMinfoById(DigitaloceanRequest request) {
		DigitalOcean2Api api = getClient(request);
		Droplet droplet = api.dropletApi().get(Integer.valueOf(request.getVirtualMachineId()));
		System.out.println("VM:" + droplet.toString());

		JSONObject vmData = new JSONObject();
		try {
			// vmData.put("vmId", vm.getId());
			vmData.put("vmId", droplet.id());
			vmData.put("created", droplet.createdAt());
			vmData.put("state", droplet.status());
			System.out.println("VM STATE:" + droplet.status());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vmData;
	}

	@Override
	public String removeISO(DigitaloceanRequest myRequest) {
		return "";
	}

	@Override
	public boolean checkIso(DigitaloceanRequest myRequest, String taskId) {
		return true;
	}

	@Override
	public String startVM(DigitaloceanRequest request) {
		DigitalOcean2Api api = getClient(request);
		Action powerOnaction = api.dropletApi().powerOn(Integer.valueOf(request.getVirtualMachineId()));
		return String.valueOf(powerOnaction.id());
	}
}
