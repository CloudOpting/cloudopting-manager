package eu.cloudopting.provision.digitalocean;

import java.lang.reflect.Field;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Comparator;

import org.bouncycastle.asn1.eac.RSAPublicKey;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.primitives.Floats;

import eu.cloudopting.cloud.CloudProvider;
import eu.cloudopting.provision.AbstractProvision;

@Service
public class DigitaloceanProvision extends AbstractProvision<DigitaloceanResult, DigitaloceanRequest> {
	private final Logger log = LoggerFactory.getLogger(DigitaloceanProvision.class);

	private static final String OS_TYPE = "CentOS";
	
	private enum OS_VERSION{
		V7("7"), V6("6"), V5("5");
		
		private String name;
		private OS_VERSION(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	@Override
	public DigitaloceanResult provision(DigitaloceanRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String provisionVM(DigitaloceanRequest request) {
		log.debug("in DO ProvisionVM");
		DigitalOcean2Api api = getClient(request);
		Size machineType = getMachineType(api); 
		log.debug("Size: " + machineType.toString());
		Region region = getRegion(api, machineType);
		log.debug("Region null? " + (region == null));
		log.debug("Region: " + region);
		Image image = getImage(api, region);
		log.debug("Image: " + image.name());
		CreateDropletOptions digitalOceanSpecificParams = 
				CreateDropletOptions.builder().backupsEnabled(false).privateNetworking(false).build();
		
		//TODO create public/private key
		/******/		
//		try {
//			KeyPairGenerator generator;
//			generator = KeyPairGenerator.getInstance("RSA", "BC");
//			generator.initialize(1024);
//			KeyPair keyPair = generator.generateKeyPair();
//			RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
//			RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
//		} catch (NoSuchAlgorithmException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NoSuchProviderException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		/*****/

		//Key key = Key.create("id", "name", "fingerprint", publicKey);
		
		//TODO: per Luca Gioppo: qui viene settato il campo userData nell'oggetto che rappresenta le opzioni delle droplte DigitalOcean
		// CreateDropletOptions builder does not have a method for setting userData field.
		// We set userData field by reflection
		
		try {
			Field userData = CreateDropletOptions.class.getDeclaredField("userData");
			userData.setAccessible(true);
			userData.set(digitalOceanSpecificParams, request.getUserData());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error setting user data info at digitalocean provision");
		}
		
		
		
		log.debug("Before calling create");
		DropletCreate result = api.dropletApi().create("testOcean", region.slug(), machineType.slug(), image.slug(), digitalOceanSpecificParams);
		log.debug("After calling create");
		
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
		FluentIterable<Image> images = api.imageApi().
				list(ImageListOptions.Builder.type("distribution")).
				filter(new Predicate<Image>() {
					@Override
					public boolean apply(Image image) {
						return image.regions().contains(region.slug()) && image.distribution().equals(OS_TYPE);
					}
				});
		for(OS_VERSION osVersion : OS_VERSION.values()){
			Optional<? extends Image> image = images.firstMatch(isVersionOk(osVersion));
			if(image.isPresent()){
				return image.get();
			}
		}
		throw new RuntimeException("Cannot find image for OS " + OS_TYPE + " in region " + region.name());
	}
	
	private static Predicate<Image> isVersionOk(OS_VERSION osVersion) {
		return new Predicate<Image>() {
			@Override
			public boolean apply(Image image) {
				return image.name().startsWith(osVersion.getName());
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