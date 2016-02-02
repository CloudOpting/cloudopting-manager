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
			token = dockerService.newContext("","src/test/resources/testfiles/test1/Puppetfile");
		} catch (DockerError e) {
		}
    	
    	if(token!=null && token!="")
    		correct = true;
    	
        assertTrue("Can't obtain a valid context token from Crane.", correct);
    }

        @Test
    public void contextFull() {
        boolean correct = false;
        String token = "";
        
        try {
            token = dockerService.newContext("test","src/test/resources/testfiles/test1/Puppetfile");
        } catch (DockerError e) {
        }
        
        if(token!=null && token!="")
            correct = true;
        
        assertTrue("Can't obtain a valid context token from Crane.", correct);
    }

        @Test
    public void contextWithoutPuppetfile() {
        boolean correct = false;
        String token = "";
        
        try {
            token = dockerService.newContext("test","");
        } catch (DockerError e) {
        }
        
        if(token!=null && token!="")
            correct = true;
        
        assertTrue("Can't obtain a valid context token from Crane.", correct);
    }
    
        @Test
    public void contextEmpty() {
        boolean correct = false;
        String token = "";
        
        try {
            token = dockerService.newContext("","");
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
    		String token = dockerService.newContext("","src/test/resources/testfiles/test1/Puppetfile");
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
    		String token = dockerService.newContext("","src/test/resources/testfiles/test1/Puppetfile");
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
    public void testContextDetail() {   
        boolean correct = false;
        
        try {
            String token = dockerService.newContext("","src/test/resources/testfiles/test1/Puppetfile");
            String response = dockerService.contextDetail(token);
            
            BasicJsonParser parser = new BasicJsonParser();
            Map<String, Object> map = parser.parseMap(response);
            
            if(map.get("status").toString() != null && map.get("description").toString() != null && map.get("log").toString() != null){
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
    		token = dockerService.newContext("","src/test/resources/testfiles/test1/Puppetfile");
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
    		String contextToken = dockerService.newContext("","src/test/resources/testfiles/test1/Puppetfile");
			
    		Thread.sleep(1500);
    		while(dockerService.isContextReady(contextToken)!=true)
    			Thread.sleep(2000);

			// Launch image builder
    		String imageToken = dockerService.buildDockerImage("directory","", "src/test/resources/testfiles/test1/directory/Dockerfile", "src/test/resources/testfiles/test1/directory/directorymanifest.pp", contextToken);
    		
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
    public void testStopBuild() {   
    	boolean correct = false;
        
        try {
            // Prepare context
            String contextToken = dockerService.newContext("erased","src/test/resources/testfiles/test1/Puppetfile");
            
            Thread.sleep(1500);
            while(dockerService.isContextReady(contextToken)!=true)
                Thread.sleep(2000);

            // Launch image builder
            String imageToken = dockerService.buildDockerImage("directory","", "src/test/resources/testfiles/test1/directory/Dockerfile", "src/test/resources/testfiles/test1/directory/directorymanifest.pp", contextToken);
            
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

            dockerService.stopBuild(imageToken);
            String responseStop = dockerService.getBuildInfo(imageToken);
            assertNotNull("No response returned when asking information about the process.", responseStop);
            BasicJsonParser parserStop = new BasicJsonParser();
            Map<String, Object> mapStop = parserStop.parseMap(responseStop);
            
            Thread.sleep(1500);
            while(mapStop.get("error").toString().equals("notFound")!=true){
                responseStop = dockerService.getBuildInfo(imageToken);
                mapStop = parserStop.parseMap(responseStop);
                Thread.sleep(2000);
            }

        } catch (DockerError e) {
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        assertTrue("Error while building image.", correct);
    }
    
@Test
    public void testBuildWithoutPuppet(){
    boolean correct = false;
        
        try {
            // Prepare context
            String contextToken = dockerService.newContext("withoutpuppet","");
            
            Thread.sleep(1500);
            while(dockerService.isContextReady(contextToken)!=true)
                Thread.sleep(2000);
              
            // Launch image builder
            String imageToken1 = dockerService.buildDockerImage("redis","", "src/test/resources/testfiles/test3/img/redis/Dockerfile", "", contextToken);
            
            assertNotNull("No token returned by image builder.", imageToken1);
            
            // Wait for image
            Thread.sleep(1500);
            while(dockerService.isBuilt(imageToken1)!=true)
                Thread.sleep(2000);
            
            String response1 = dockerService.getBuildInfo(imageToken1);
            
            assertNotNull("No response returned when asking information about the process.", response1);
            
            // Check response
            BasicJsonParser parser1 = new BasicJsonParser();
            Map<String, Object> map1 = parser1.parseMap(response1);
            
            assertTrue("Can't build image", map1.get("status").toString().equals("finished"));
            
        } catch (DockerError e) {
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        correct = true;
        
        assertTrue("Error while building image.", correct);
    }

    @Test
    public void testDeployCompositionLocally(){
	boolean correct = false;
       	
    	try {
    		// Prepare context
    		String contextToken = dockerService.newContext("test1","src/test/resources/testfiles/test1/Puppetfile");
			
    		Thread.sleep(1500);
    		while(dockerService.isContextReady(contextToken)!=true)
    			Thread.sleep(2000);

			// Launch image builder
    		String imageToken = dockerService.buildDockerImage("apache","", "src/test/resources/testfiles/test1/apache/Dockerfile", "src/test/resources/testfiles/test1/apache/apache.pp", contextToken);
    		
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
			
			assertTrue("Can't build image", map.get("status").toString().equals("finished"));
			
            // cluster
            String clusterToken = dockerService.addMachine("emulatedhost", 4243);
            assertNotNull("No can't add machine", clusterToken);
			
            String responseCluster = dockerService.getClusterInfo(clusterToken);
            assertNotNull ("No response returned when asking information about the process.", responseCluster);
            
            // Wait for cluster
            Thread.sleep(1500);
            while(dockerService.isClusterReady(clusterToken)!=true)
                Thread.sleep(2000);
            
            // Check response
            BasicJsonParser parserCluster = new BasicJsonParser();
            Map<String, Object> mapCluster = parserCluster.parseMap(responseCluster);
            
            assertTrue("Can't access to cluster", mapCluster.get("status").toString().equals("ready")); 

            // Launch composition
			String compositionToken = dockerService.deployComposition("src/test/resources/testfiles/test1/docker-compose.yml", clusterToken);
			
			if(compositionToken.equals(""))
				compositionToken = null;
			
			assertNotNull("Erroneous response for deploy composition.", compositionToken);
			
		} catch (DockerError e) {
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
    	
    	correct = true;
    	
        assertTrue("Error while building image.", correct);
    }

@Test
    public void testFull(){
    boolean correct = false;
        
        try {
            // Prepare context
            String contextToken = dockerService.newContext("testfull","src/test/resources/testfiles/test3/img/puppetfile");
            // check crane engine
            assertTrue("Crane is not alive or it is not responding as expected.", dockerService.checkCraneEngine());
            // info about VM docker daemon
            String info = dockerService.dockerInfo("emulatedhost",4243);
            assertNotNull("No response returned when asking information about the process.", info);
            // Build base
            Thread.sleep(1500);
            while(dockerService.isContextReady(contextToken)!=true)
                Thread.sleep(2000);
            String bodyBase = dockerService.buildBase("ubuntubase", "src/test/resources/testfiles/test3/bases/ubuntubase");
            
            // Wait for image base
            Thread.sleep(1500);
            while(dockerService.isBuiltBase("ubuntubase")!=true)
                Thread.sleep(2000);

            String responseBase = dockerService.getBuildBaseInfo("ubuntubase");
            assertNotNull("No response returned when asking information about the process.", responseBase);

            // Check response
            BasicJsonParser parserBase = new BasicJsonParser();
            Map<String, Object> mapBase = parserBase.parseMap(responseBase);
            
            assertTrue("Can't build image base", mapBase.get("status").toString().equals("finished"));
            
            // Launch image builder
            String imageToken1 = dockerService.buildDockerImage("redis","", "src/test/resources/testfiles/test3/img/redis/Dockerfile", "", contextToken);
            
            assertNotNull("No token returned by image builder.", imageToken1);
            
            // Wait for image
            Thread.sleep(1500);
            while(dockerService.isBuilt(imageToken1)!=true)
                Thread.sleep(2000);
            
            String response1 = dockerService.getBuildInfo(imageToken1);
            
            assertNotNull("No response returned when asking information about the process.", response1);
            
            // Check response
            BasicJsonParser parser1 = new BasicJsonParser();
            Map<String, Object> map1 = parser1.parseMap(response1);
            
            assertTrue("Can't build image", map1.get("status").toString().equals("finished"));

            //Launch image builder
            String imageToken2 = dockerService.buildDockerImage("webconsumer","", "src/test/resources/testfiles/test3/img/webconsumer/Dockerfile", "src/test/resources/testfiles/test3/img/webconsumer/consumer.pp", contextToken);
            
            assertNotNull("No token returned by image builder.", imageToken2);
            
            // Wait for image
            Thread.sleep(1500);
            while(dockerService.isBuilt(imageToken2)!=true)
                Thread.sleep(2000);
            
            //Launch image builder
            String imageToken3 = dockerService.buildDockerImage("webproducer","ubuntubase", "", "src/test/resources/testfiles/test3/img/webproducer/producer.pp", contextToken);
            
            assertNotNull("No token returned by image builder.", imageToken3);
            
            // Wait for image
            Thread.sleep(1500);
            while(dockerService.isBuilt(imageToken3)!=true)
                Thread.sleep(2000);
            
            String response3 = dockerService.getBuildInfo(imageToken3);
            
            assertNotNull("No response returned when asking information about the process.", response3);
            
            // Check response
            BasicJsonParser parser3 = new BasicJsonParser();
            Map<String, Object> map3 = parser3.parseMap(response3);
            
            assertTrue("Can't build image", map3.get("status").toString().equals("finished"));    
            
            // Access cluster
            String clusterToken = dockerService.addMachine("emulatedhost", 4243);
            assertNotNull("No can't add machine", clusterToken);

            String responseCluster = dockerService.clusterDetail(clusterToken);
            assertNotNull ("No response returned when asking information about the process.", responseCluster);
            
            // Wait for cluster
            Thread.sleep(1500);
            while(dockerService.isClusterReady(clusterToken)!=true)
                Thread.sleep(2000);
            
            // Check response
            BasicJsonParser parserCluster = new BasicJsonParser();
            Map<String, Object> mapCluster = parserCluster.parseMap(responseCluster);
            
            assertTrue("Can't access to cluster", mapCluster.get("status").toString().equals("ready")); 
            
            // Launch composition
            String compositionToken = dockerService.deployComposition("src/test/resources/testfiles/test3/run/compose.yml", clusterToken);
            
            if(compositionToken.equals(""))
                compositionToken = null;
            
            assertNotNull("Erroneous response for deploy composition.", compositionToken);
            
        } catch (DockerError e) {
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        correct = true;
        
        assertTrue("Error while building image.", correct);
    }

}
