package eu.cloudopting.web.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParser;

import eu.cloudopting.tosca.ToscaService;
import eu.cloudopting.tosca.utils.ZipDirectory;

@RestController
@RequestMapping("/api")
public class CytoscapeController {
	private final Logger log = LoggerFactory.getLogger(CytoscapeController.class);

	@Autowired
	ToscaService toscaService;

	@Autowired
	ZipDirectory zipDirectory;

	@RequestMapping(value = "/edges", method = RequestMethod.GET)
	@ResponseBody
	public String edgeArray() {
		log.debug("in edge");
		JSONArray ret = new JSONArray(toscaService.getEdgeTypeList());
		log.debug(ret.toString());
		return ret.toString();

	}

	@RequestMapping(value = "/edgeTypes", method = RequestMethod.GET)
	@ResponseBody
	public String getEdgesJsonList() {
		log.debug("in edgeTypes");
		String ret = toscaService.getEdgeTypeJsonList().toString();
		log.debug(ret);
		return ret;
	}

	@RequestMapping(value = "/nodes", method = RequestMethod.GET)
	@ResponseBody
	public String getNodesList() {
		JSONArray ret = new JSONArray(toscaService.getNodeTypeList());
		log.debug(ret.toString());
		return ret.toString();
	}

	@RequestMapping(value = "/nodeTypes", method = RequestMethod.GET)
	@ResponseBody
	public String getNodesJsonList() {
		String ret = toscaService.getNodeTypeJsonList().toString();
		log.debug(ret);
		return ret;
	}

	@RequestMapping(value = "/sendData", method = RequestMethod.POST, consumes = "text/plain")
	@ResponseBody
	public String sendData(@RequestBody String payload) {
		log.debug("the received payload");
		log.debug(payload);
		JSONObject toscadata = null;
		try {
			toscadata = new JSONObject(payload);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// process definition

		// copy template dir in tmp
		File srcDir = new File("toscaTemplate");
		File destDir = new File("/tmp/tosca");
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String destDirPath = "/tmp/tosca/Definitions/Service-Definitions.xml";
		toscaService.writeToscaDefinition(toscadata, destDirPath);

		List<File> fileList = new ArrayList<File>();
		zipDirectory.getAllFiles(destDir, fileList);
		zipDirectory.writeZipFile(destDir, fileList);

		return null;
	}

	@RequestMapping(value = "/saveData", method = RequestMethod.POST, consumes = "text/plain")
	@ResponseBody
	public String saveData(@RequestBody String payload) {
		log.debug("the received payload");
		log.debug(payload);

		JSONObject toscadata = null;
		try {
			toscadata = new JSONObject(payload);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			// Writing to a file
			File file = new File(toscadata.getString("serviceName") + ".json");
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			// System.out.print(payload);

			fileWriter.write(payload);
			fileWriter.flush();
			fileWriter.close();

		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/loadTopology", method = RequestMethod.POST, consumes = "text/plain")
	@ResponseBody
	public String loadTopology(@RequestBody String payload) {
		log.debug("the received payload");
		log.debug(payload);

		JSONObject toscadata = null;
		try {
			toscadata = new JSONObject(payload);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String topologyContent = null;

		try {

			// Writing to a file
			File file = new File(toscadata.getString("serviceName") + ".json");
			String filePath = toscadata.getString("serviceName") + ".json";
			topologyContent = new String(Files.readAllBytes(Paths.get(filePath)));
			// JSONObject topology = new JSONObject(topologyContent);

		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return topologyContent;
	}
}
