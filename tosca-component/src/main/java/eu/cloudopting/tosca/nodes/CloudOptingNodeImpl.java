package eu.cloudopting.tosca.nodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;
import eu.cloudopting.tosca.nodes.CloudOptingNode;;

@Service
public class CloudOptingNodeImpl implements CloudOptingNode {

	private final Logger log = LoggerFactory.getLogger(CloudOptingNodeImpl.class);

	@Autowired
	ToscaOperationImpl toscaOperationImpl;
	
	@Autowired
	ToscaService toscaService;

	public String execute(HashMap<String, String> data) {

		String id = data.get("id");
		String customizationId = data.get("customizationId");
		String operation = toscaService.getOperationForNode(customizationId, id, "Install");
		log.debug("Invoking method : " + operation + " on node: " + id);
		Class partypes[] = new Class[1];
        partypes[0] = data.getClass();
        Method meth = null;
        try {
			meth = toscaOperationImpl.getClass().getMethod(operation, partypes);
			log.debug(meth.toString());
			log.debug(meth.getParameterTypes().toString());
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException on CloudOptingNodeImpl.execute getting the method");
			e.printStackTrace();
		} catch (SecurityException e) {
			log.error("SecurityException on CloudOptingNodeImpl.execute getting the method");
			e.printStackTrace();
		}
		try {
			return (String) meth.invoke(toscaOperationImpl,data);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException on CloudOptingNodeImpl.execute invoking the method");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException on CloudOptingNodeImpl.execute invoking the method");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException on CloudOptingNodeImpl.execute invoking the method");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String prepare(HashMap<String, String> data) {
		return null;
	}
}
