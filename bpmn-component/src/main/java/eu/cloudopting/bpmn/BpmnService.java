package eu.cloudopting.bpmn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.cloudopting.domain.Applications;
import eu.cloudopting.domain.Status;
import eu.cloudopting.dto.ActivitiDTO;
import eu.cloudopting.dto.ApplicationDTO;
import eu.cloudopting.service.ApplicationService;
import eu.cloudopting.service.StatusService;
import eu.cloudopting.service.util.StatusConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.service.CustomizationService;

import javax.inject.Inject;
import javax.transaction.Transactional;


@Service
@Transactional
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

	@Inject
	private StatusService statusService;

	@Inject
	private ApplicationService applicationService;

	public String startDeployProcess(String customizationId, String cloudId){
		log.info("Before activating process");
		log.info("customizationId: "+customizationId);
		log.info("cloudId: "+cloudId);
		
		
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
		log.info("theCust: "+theCust.toString());

		if(theCust.getProcessId()!= null){
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
	
	/**
	 * Starts the process with the provided id and the provided initial input parameters.
	 * <strong>For testing purposes, might be removed at any time</strong>.
	 * @param processId the Identifier of the process (not null)
	 * @param startParams the input variables (might be null)
	 * @return the process instance id
	 */
	public String startGenericProcess(String processId, Map<String, Object> startParams){
		log.debug("Starting Process with id:'"+processId+"'");
		// TODO the process string has to go in a constant
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId, startParams);
        System.out.println("ProcessID:"+pi.getProcessInstanceId());
        return pi.getProcessInstanceId();

	}
	
	/**
	 * Gets the list of active Process Definitions
	 * For testing purposes, <strong>might be removed at any time</strong>.
	 * @param processId the Identifier of the process (not null)
	 * @param startParams the input variables (might be null)
	 * @return
	 */
	public List<String> getAvailableProcessDefinitions(){
		log.debug("Retrieving available process definitions");
		RepositoryService rs = processEngine.getRepositoryService();
		List<String> result = new LinkedList<String>();
		for (ProcessDefinition currentDefinition : rs.createProcessDefinitionQuery().active().list()) {
			result.add(currentDefinition.getId()+" - "+currentDefinition.getName()+" - DeploymentId: "+currentDefinition.getDeploymentId());
		}
        return result;
	}
	
	public void deleteDeploymentById(String deploymentId){
		log.debug("Deleting Process Deployment with id:"+ deploymentId);
		RepositoryService rs = processEngine.getRepositoryService();
		rs.deleteDeployment(deploymentId, true);
	}
	
	/**
	 * Unlocks the process instance with the provided instance id, waiting on the message event with the provided name.
	 * <strong>For testing purposes, might be removed at any time</strong>.
	 * @param processInstanceId The id of the instance of the target process
	 * @param messageName The name of the intermediate message the instance is waiting for
	 * @return A set containing the execution ids of unlocked process instances
	 */
	public Set<String> unlockProcess(String processInstanceId, String messageName){
		Set<String> exIds = new HashSet<String>();
		log.debug("Unlocking Process with processInstanceId:'"+processInstanceId+"'");
		List<Execution> executions = runtimeService.createExecutionQuery()
			      .messageEventSubscriptionName(messageName).processInstanceId(processInstanceId)
			      .list();
			      
		for (Execution execution2 : executions) {
			String curExId = execution2.getId();
			exIds.add(curExId);
			runtimeService.messageEventReceived(messageName, curExId);
		}
		return exIds;
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

	public ActivitiDTO startPublish(ApplicationDTO application) {
	    HashMap<String, Object> v = new HashMap<>();
		v.put("application",application);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess",v);
		ActivitiDTO activitiDTO = new ActivitiDTO();
		Map map = ((ExecutionEntity) pi).getVariableInstances();
		activitiDTO.setApplicationId(((VariableInstanceEntity)map.get("applicationId")).getTextValue());
		activitiDTO.setProcessInstanceId(pi.getProcessInstanceId());
		return activitiDTO;
	}
}
