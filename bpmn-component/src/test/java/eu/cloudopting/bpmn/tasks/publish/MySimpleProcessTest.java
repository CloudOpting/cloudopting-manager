package eu.cloudopting.bpmn.tasks.publish;

import javax.inject.Inject;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MySimpleProcessTest {
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("eu/cloudopting/bpmn/tasks/publish/activiti.cfg.xml");
	
	@Inject
	private ProcessEngine engine;
	
	@Test
	@Deployment(resources={"eu/cloudopting/bpmn/tasks/publish/MySimpleProcess.bpmn20.xml"})
	public void ruleUsageExample() {
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
	    ProcessInstance pi = runtimeService.startProcessInstanceByKey("mySimpleProcess");

		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		//Assert.assertEquals("Service Task", task.getName());
		
		//String taskId = task.getId();
		//taskService.complete(taskId);
		Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().count());
	}
}
