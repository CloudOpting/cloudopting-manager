package eu.cloudopting.dockertests;

import static org.junit.Assert.*;

import org.apache.log4j.spi.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    // Building related test
	
    @Test
    public void testNewContext() {
    	boolean correct = false;
    	String token = "";
    	try {
			token = dockerService.newContext("src/test/resources/testfiles/Puppetfile");
		} catch (DockerError e) {
		}
    	
    	if(token=="exists")
    		correct = true;
    	
        assertTrue("token:."+token, correct);
    }
    
    @Test
    public void testCheckContext() {   
        assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testGetContextInfo() {   
    	assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testRemoveContext() {   
    	assertNotNull("Not yet.", null);
    }
    
    @Test
    public void testBuildImage() {   
    	assertNotNull("Not yet.", null);
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
