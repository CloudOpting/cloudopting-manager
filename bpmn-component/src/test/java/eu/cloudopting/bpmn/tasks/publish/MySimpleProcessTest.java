package eu.cloudopting.bpmn.tasks.publish;

import javax.inject.Inject;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MySimpleProcessTest {
	
	@Inject
	private ProcessEngine engine;
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	
	@Test
	@Deployment(resources={"MySimpleProcess.bpmn20.xml"})
	public void ruleUsageExample() {
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
	    ProcessInstance pi = runtimeService.startProcessInstanceByKey("mySimpleProcess");

		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		//Assert.assertEquals("Service Task", task.getName());
		
		//String taskId = task.getId();
		//taskService.complete(taskId);
		ProcessInstanceQuery piq = runtimeService.createProcessInstanceQuery();
		long count = piq.count();
		Assert.assertEquals(1, count);
	}
}
