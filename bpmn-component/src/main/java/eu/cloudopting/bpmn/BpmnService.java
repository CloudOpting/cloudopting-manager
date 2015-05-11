package eu.cloudopting.bpmn;

import java.util.HashMap;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.Status;
import eu.cloudopting.service.CustomizationService;


@Service
public class BpmnService {
	private final Logger log = LoggerFactory.getLogger(BpmnService.class);


	@Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    protected ProcessEngineConfiguration processEngineConfiguration;
    
    @Autowired
    protected CustomizationService customizationS;

	public String startDeployProcess(String customizationId, String cloudId){
		log.debug("Before activating process");
		// the UI will pass the customization ID and the cloud ID since is in that page
		// will need to retrieve the:
		// TOSCA csar to unzip it
		// tosca customization to send it to the tosca service
		// the customer name to build folders
		// all this need to go in the process so I send the customization ID to the process
//		log.debug("\ncustomerId:"+customerId);
//		System.out.println("\ncloudId:"+cloudId);
		// We recover the Customization and chack if processId is null otherwise we need to throw exception since we cannot execute another deploy for the same Customization
		Customizations theCust = customizationS.findOne(Long.parseLong(customizationId));
		if(!theCust.getProcessId().isEmpty()){
			log.debug("Customization "+customizationId+" has already a deployment process");
			return theCust.getProcessId();
		}
		
		HashMap<String, Object> v = new HashMap<String, Object>();
//		v.put("toscaFile", toscaId);
		v.put("customizationId", customizationId);
		v.put("cloudId", cloudId);
		// TODO the process string has to go in a constant
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("cloudoptingProcess",v);
        System.out.println("ProcessID:"+pi.getProcessInstanceId());
        return pi.getProcessInstanceId();

	}
	
	public String startTestDeployProcess(){
		return null;
	
	}
	
	public String startAddApplicationProcess(){
		return null;
	
	}
	
	public byte[] getProcessStatusImage(String id){
		return null;
	
	}

}
