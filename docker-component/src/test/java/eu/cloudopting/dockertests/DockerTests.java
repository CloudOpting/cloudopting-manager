package eu.cloudopting.dockertests;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.log4j.spi.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloudopting.docker.DockerError;
import eu.cloudopting.docker.DockerService;
import eu.cloudopting.docker.config.DockerConfig;


/**
 * Tests for cloudopting-crane
 */
@ContextConfiguration(classes = {DockerConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DockerTests {

    @Autowired
    private DockerService dockerService;
	
	// Connections tests
    
    @Test
    public void testConnection() {   
        assertTrue("Crane is not alive or it is not responding as expected.", dockerService.isCraneAlive());
    }
    
    // Building related tests
	
    @Test
    public void testNewContext() {
    	boolean correct = false;
    	String token = "";
    	
    	try {
			token = dockerService.newContext("src/test/resources/testfiles/Puppetfile");
		} catch (DockerError e) {
		}
    	
    	if(token!=null && token!="")
    		correct = true;
    	
        assertTrue("Can't obtain a valid context token from Crane.", correct);
    }
    
    @Test
    public void testCheckContext() {   
       	boolean correct = false;
    	
    	try {
    		String token = dockerService.newContext("src/test/resources/testfiles/Puppetfile");
			dockerService.isContextReady(token);
			correct = true;
		} catch (DockerError e) {
		}
    	
        assertTrue("Error while consulting context build status.", correct);
    }
    
    @Test
    public void testGetContextInfo() {   
       	boolean correct = false;
    	
    	try {
    		String token = dockerService.newContext("src/test/resources/testfiles/Puppetfile");
			String response = dockerService.getContextInfo(token);
			
			BasicJsonParser parser = new BasicJsonParser();
			Map<String, Object> map = parser.parseMap(response);
			
			if(map.get("status").toString() != null && map.get("description").toString() != null){
				correct = true;
			}
			
		} catch (DockerError e) {
		}
    	
        assertTrue("Error while consulting context build status.", correct);
    }
    
    @Test
    public void testRemoveContext() {   
       	boolean correct = false;
    	String token;
    	
    	try {
    		token = dockerService.newContext("src/test/resources/testfiles/Puppetfile");
			dockerService.removeContext(token);
			
			try{
				dockerService.isContextReady(token);
			}catch (DockerError e){
				correct = true;
			}
			
		} catch (DockerError e) {
		}
    	
        assertTrue("Error while removing context.", correct);
    }
    
    @Test
    public void testBuildAndCheckImage() {   
       	boolean correct = false;
       	
    	try {
    		// Prepare context
    		String contextToken = dockerService.newContext("src/test/resources/testfiles/Puppetfile");
			
    		Thread.sleep(1500);
    		while(dockerService.isContextReady(contextToken)!=true)
    			Thread.sleep(2000);

			// Launch image builder
    		String imageToken = dockerService.buildDockerImage("directory", "src/test/resources/testfiles/Dockerfile", "src/test/resources/testfiles/directorymanifest.pp", contextToken);
    		
    		assertNotNull("No token returned by image builder.", imageToken);
    		
    		// Wait for image
    		Thread.sleep(1500);
    		while(dockerService.isBuilt(imageToken)!=true)
    			Thread.sleep(2000);
    		
    		String response = dockerService.getBuildInfo(imageToken);
    		
    		assertNotNull("No response returned when asking information about the process.", response);
    		
    		// Check response
			BasicJsonParser parser = new BasicJsonParser();
			Map<String, Object> map = parser.parseMap(response);
			
			if(map.get("status").toString().equals("finished")){
				correct = true;
			}
			
		} catch (DockerError e) {
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
    	
        assertTrue("Error while building image.", correct);
    }
    
    @Test
    public void testCheckImage() {   
    	assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testGetImageInfo() {   
    	assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testStopBuild() {   
    	assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testCommitImage() {   
    	assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testCheckCommitImage() {   
    	assertNotNull("Not yet.", null);
    }
    


}
