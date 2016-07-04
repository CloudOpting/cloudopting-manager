package eu.cloudopting.digitalocean;

import javax.inject.Inject;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloudopting.provision.ProvisionComponent;
import eu.cloudopting.provision.config.ProvisionConfig;
import eu.cloudopting.provision.digitalocean.DigitaloceanRequest;
import eu.cloudopting.provision.digitalocean.DigitaloceanResult;

@ContextConfiguration(classes = {ProvisionConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DigitaloceanProvisionTests {

    @Inject
    ProvisionComponent<DigitaloceanResult, DigitaloceanRequest> digitaloceanProvision;

    @Test
    public void testCreateVM() { 
    	DigitaloceanRequest request = createDigitaloceanRequest();
    	String result = digitaloceanProvision.provisionVM(request);
    	System.out.println(result); 
    }
    
    @Test
    public void testCheckVMdeployed() { 
    	DigitaloceanRequest request = createDigitaloceanRequest();
    	boolean result = digitaloceanProvision.checkVMdeployed(request, String.valueOf(18456480));
    	System.out.println(result); 
    }
    
    @Test
    public void testgetVMinfo() { 
    	DigitaloceanRequest request = createDigitaloceanRequest();
    	JSONObject result = digitaloceanProvision.getVMinfo(request, String.valueOf(18456480));
    	System.out.println(result); 
    }
    
    @Test
    public void testgetVMinfoById() { 
    	DigitaloceanRequest request = createDigitaloceanRequest();
    	request.setVirtualMachineId(String.valueOf(18456480));
    	JSONObject result = digitaloceanProvision.getVMinfoById(request);
    	System.out.println(result); 
    }
    
    @Test
    public void testgetAcquiredIpinfo() { 
    	DigitaloceanRequest request = createDigitaloceanRequest();
    	JSONObject result = digitaloceanProvision.getAcquiredIpinfo(request, String.valueOf(18456480));
    	System.out.println(result); 
    }
    
    private DigitaloceanRequest createDigitaloceanRequest() {
		DigitaloceanRequest request = new DigitaloceanRequest();
		request.setIdentity("75:05:8b:b9:a4:78:9c:c5:be:b6:da:ad:87:14:62:df");
		request.setCredential("f4ee736a6df30ef54ef51039e9cd0d9bc868c3da28d79c7a2118de962b6cfb2f");
		return request;
	}
}
