package eu.cloudopting.tosca.nodes;

import java.util.HashMap;

public interface CloudOptingNode {

	public abstract String prepare(HashMap<String, String> data);

}