package eu.cloudopting.bpmn.tasks.publish;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class MySimpleProcessTest {
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	@Deployment(resources={"MySimpleProcess.bpmn20.xml"})
	public void ruleUsageExample() {
		
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration()
			 	.buildProcessEngine();
		
		RuntimeService runtimeService = activitiRule.getRuntimeService();
	    runtimeService.startProcessInstanceByKey("mySimpleProcess");

		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().singleResult();
		//Assert.assertEquals("Service Task", task.getName());

		taskService.complete(task.getId());
		Assert.assertEquals(0, runtimeService.createProcessInstanceQuery().count());
	}
}
