package eu.cloudopting.tosca.nodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;

@Service
public class CloudOptingNodeImpl implements CloudOptingNode {
	@Autowired
	ToscaOperationImpl toscaOperationImpl;
	
	@Autowired
	ToscaService toscaService;

	public String execute(HashMap<String, String> data) {
		// TODO Auto-generated method stub
		String id = data.get("id");
		String customizationId = data.get("customizationId");
		String operation = toscaService.getOperationForNode(customizationId, id, "Install");
		System.out.println("Invoking method :"+operation+" on node: "+id);
		Class partypes[] = new Class[1];
        partypes[0] = data.getClass();
        Method meth = null;
        try {
			meth = toscaOperationImpl.getClass().getMethod(operation, partypes);
			System.out.println(meth.toString());
			System.out.println(meth.getParameterTypes().toString());
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return (String) meth.invoke(toscaOperationImpl,data);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String prepare(HashMap<String, String> data) {
		// TODO Auto-generated method stub
		return null;
	}


}
