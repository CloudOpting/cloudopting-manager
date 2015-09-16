package eu.cloudopting.provision.cloudstack;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.io.ByteSource;
import com.google.common.net.HostAndPort;
import com.google.common.net.HostSpecifier;
import com.google.inject.Module;

import eu.cloudopting.cloud.CloudService;
import eu.cloudopting.provision.AbstractProvision;
import eu.cloudopting.provision.ProvisionComponent;
import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.cloudstack.CloudStackApi;
import org.jclouds.cloudstack.CloudStackContext;
import org.jclouds.cloudstack.compute.strategy.CloudStackComputeServiceAdapter;
import org.jclouds.cloudstack.domain.*;
import org.jclouds.cloudstack.domain.AsyncJob.Status;
import org.jclouds.cloudstack.domain.Template;
import org.jclouds.cloudstack.options.DeployVirtualMachineOptions;
import org.jclouds.cloudstack.options.ListTemplatesOptions;
import org.jclouds.cloudstack.predicates.*;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.*;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.rest.config.CredentialStoreModule;
import org.jclouds.ssh.SshClient;
import org.jclouds.util.InetAddresses2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Predicates.in;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Sets.filter;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jclouds.cloudstack.options.ListNetworksOptions.Builder.isDefault;
import static org.jclouds.util.Predicates2.retry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main class to provision on cloudstack.
 */
public class CloudstackProvision extends AbstractProvision<CloudstackResult, CloudstackRequest> {

	private final Logger log = LoggerFactory.getLogger(CloudstackProvision.class);

	@Autowired
	Environment env;

	protected org.jclouds.compute.domain.Template template;

	protected TemplateBuilderSpec templateSpec;

	protected Module credentialStoreModule = new CredentialStoreModule(new ConcurrentHashMap<String, ByteSource>());

	protected Predicate<String> jobComplete;
	protected Predicate<VirtualMachine> virtualMachineRunning;
	protected CloudStackApi client;
	private VirtualMachine vm = null;
	protected LoginCredentials loginCredentials = LoginCredentials.builder().user("admin").password("password").build();
	CloudstackRequest request;
	protected String identity;
	protected String credential;
	protected String endpoint;
	protected String apiVersion;
	protected String provider = "cloudstack";
	protected Predicate<HostAndPort> socketTester;
	protected SshClient.Factory sshFactory;
	CloudStackContext context;

	/* String cloudstackEndpoint; */

	Properties overrides = new Properties();
	/*
	 * @PostConstruct private void init(){
	 * 
	 * }
	 */

	protected void setupProperties() {
		this.overrides.setProperty(Constants.PROPERTY_TRUST_ALL_CERTS, "true");
		this.overrides.setProperty(Constants.PROPERTY_RELAX_HOSTNAME, "true");
		this.identity = request.getIdentity();
		this.credential = request.getCredential();
		this.endpoint = request.getEndpoint();
		this.apiVersion = env.getProperty("cloudstack.api-version");
		log.debug("this.credential:" + this.credential);
		log.debug("this.identity:" + this.identity);
		log.debug("this.endpoint:" + this.endpoint);
		log.debug("this.apiVersion:" + this.apiVersion);

	}

	/**
	 * This will be the new refactored provision method (will be tested and than
	 * renamed) All variables will have to be local since we have multiple
	 * instances of process running and we cannot run the risk of changing the
	 * data of the cloudstack client using global vars
	 * 
	 * @param request
	 * @return
	 */
	public String provisionVM(CloudstackRequest request) {
		ContextBuilder builder = ContextBuilder.newBuilder(provider)
				.credentials(request.getIdentity(), request.getCredential()).endpoint(request.getEndpoint());
		CloudStackContext theContext = builder.buildView(CloudStackContext.class);
		CloudStackApi theClient = theContext.getApi();
		ComputeService theCompute = theContext.getComputeService();
		TemplateBuilder templateBuilder = theCompute.templateBuilder();
		org.jclouds.compute.domain.Template theTemplate = templateBuilder.osFamily(request.getOsFamily())
				.minRam(request.getMinRam()).build();
		// theTemplate = template;
		Set<ServiceOffering> theOffering = theClient.getOfferingApi().listServiceOfferings();
		Set<Zone> theZones = theClient.getZoneApi().listZones(null);
		log.debug(theZones.toString());
		DeployVirtualMachineOptions options = new DeployVirtualMachineOptions();
		options.displayName("testmachine");
		options.name("test_machine");
		AsyncCreateResponse job = theClient.getVirtualMachineApi().deployVirtualMachineInZone(
				theZones.iterator().next().getId(), theOffering.iterator().next().getId(), request.defaultTemplate,
				options);
		System.out.println("JOB RESPONSE:" + job.toString());

		return job.getJobId();
	}

	public boolean checkVMdeployed(CloudstackRequest request, String jobId) {
		ContextBuilder builder = ContextBuilder.newBuilder(provider)
				.credentials(request.getIdentity(), request.getCredential()).endpoint(request.getEndpoint());
		CloudStackContext theContext = builder.buildView(CloudStackContext.class);
		CloudStackApi theClient = theContext.getApi();
		AsyncJob<VirtualMachine> jobWithResult;

		jobWithResult = theClient.getAsyncJobApi().<VirtualMachine> getAsyncJob(jobId);
		System.out.println("JOB WITH RESULT RESPONSE:" + jobWithResult.toString());
		if (jobWithResult.getError() != null) {
			// we have an error to manage
			log.debug(jobWithResult.getError().toString());
		}

		boolean theCheck = false;
		switch (jobWithResult.getStatus()) {
		case IN_PROGRESS:
			theCheck = false;
			break;

		case FAILED:

			break;
		case SUCCEEDED:
			theCheck = true;
			break;
		default:
			break;
		}
//		VirtualMachine vm = jobWithResult.getResult();
//		System.out.println("VM:" + vm.toString());

		return true;
	}

	public JSONObject getVMinfo(CloudstackRequest request, String jobId) {
		ContextBuilder builder = ContextBuilder.newBuilder(provider)
				.credentials(request.getIdentity(), request.getCredential()).endpoint(request.getEndpoint());
		CloudStackContext theContext = builder.buildView(CloudStackContext.class);
		CloudStackApi theClient = theContext.getApi();
		AsyncJob<VirtualMachine> jobWithResult;

		jobWithResult = theClient.getAsyncJobApi().<VirtualMachine> getAsyncJob(jobId);
		VirtualMachine vm = jobWithResult.getResult();
		System.out.println("VM:" + vm.toString());
		

		JSONObject vmData = new JSONObject();
		try {
			vmData.put("ipaddress", vm.getIPAddress());
			vmData.put("vmId", vm.getId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vmData;

	}

	@Override
	public CloudstackResult provision(CloudstackRequest request) {
		this.request = request;
		setupProperties();
		setupContext();
		setupClient();
		setupCompute();
		setupTemplate();
		log.debug("after setup");

		jobComplete = retry(new JobComplete(client), 1200, 1, 5, SECONDS);
		virtualMachineRunning = retry(new VirtualMachineRunning(client), 600, 5, 5, SECONDS);

		log.debug("before calling create");
		vm = createVirtualMachine(client, request.getDefaultTemplate(), jobComplete, virtualMachineRunning);
		if (vm.getPassword() != null) {
			conditionallyCheckSSH();
		}
		assert in(ImmutableSet.of("ROOT", "NetworkFilesystem", "IscsiLUN", "VMFS", "PreSetup"))
				.apply(vm.getRootDeviceType()) : vm;
		checkVm(vm);

		return new CloudstackResult();
	}

	private void conditionallyCheckSSH() {
		if (vm.getPassword() != null && !loginCredentials.getOptionalPassword().isPresent())
			loginCredentials = loginCredentials.toBuilder().password(vm.getPassword()).build();
		assert HostSpecifier.isValid(vm.getIPAddress());
		if (!InetAddresses2.isPrivateIPAddress(vm.getIPAddress())) {
			// not sure if the network is public or not, so we have to test
			HostAndPort socket = HostAndPort.fromParts(vm.getIPAddress(), 22);
			System.err.printf("testing socket %s%n", socket);
			System.err.printf("testing ssh %s%n", socket);
			checkSSH(socket);
		} else {
			System.err.printf("skipping ssh %s, as private%n", vm.getIPAddress());
		}
	}

	protected void checkSSH(HostAndPort socket) {
		socketTester.apply(socket);
		SshClient client = sshFactory.create(socket, loginCredentials);
		try {
			client.connect();
			ExecResponse exec = client.exec("echo hello");
			System.out.println(exec);
			assert(exec.getOutput().trim().equals("hello"));
		} finally {
			if (client != null)
				client.disconnect();
		}
	}

	public static VirtualMachine createVirtualMachine(CloudStackApi client, String defaultTemplate,
			Predicate<String> jobComplete, Predicate<VirtualMachine> virtualMachineRunning) {
		Set<Network> networks = client.getNetworkApi().listNetworks(isDefault(true));
		if (!networks.isEmpty()) {
			Network network = get(filter(networks, new Predicate<Network>() {
				@Override
				public boolean apply(Network network) {
					return network != null && network.getState().equals("Implemented");
				}
			}), 0);
			return createVirtualMachineInNetwork(network,
					defaultTemplateOrPreferredInZone(defaultTemplate, client, network.getZoneId()), client, jobComplete,
					virtualMachineRunning);
		} else {
			String zoneId = find(client.getZoneApi().listZones(), new Predicate<Zone>() {

				@Override
				public boolean apply(Zone arg0) {
					return arg0.isSecurityGroupsEnabled();
				}

			}).getId();
			return createVirtualMachineWithSecurityGroupInZone(zoneId,
					defaultTemplateOrPreferredInZone(defaultTemplate, client, zoneId),
					get(client.getSecurityGroupApi().listSecurityGroups(), 0).getId(), client, jobComplete,
					virtualMachineRunning);
		}
	}

	public static VirtualMachine createVirtualMachineWithSecurityGroupInZone(String zoneId, String templateId,
			String groupId, CloudStackApi client, Predicate<String> jobComplete,
			Predicate<VirtualMachine> virtualMachineRunning) {
		return createVirtualMachineWithOptionsInZone(new DeployVirtualMachineOptions().securityGroupId(groupId), zoneId,
				templateId, client, jobComplete, virtualMachineRunning);
	}

	public static String defaultTemplateOrPreferredInZone(String defaultTemplate, CloudStackApi client, String zoneId) {
		String templateId = defaultTemplate != null ? defaultTemplate : getTemplateForZone(client, zoneId);
		return templateId;
	}

	public static String getTemplateForZone(CloudStackApi client, String zoneId) {
		// TODO enum, as this is way too easy to mess up.
		Set<String> acceptableCategories = ImmutableSet.of("Ubuntu", "CentOS");

		final Predicate<Template> hypervisorPredicate = new CorrectHypervisorForZone(client).apply(zoneId);
		final Predicate<Template> osTypePredicate = new OSCategoryIn(client).apply(acceptableCategories);

		@SuppressWarnings("unchecked")
		Predicate<Template> templatePredicate = Predicates.<Template> and(TemplatePredicates.isReady(),
				hypervisorPredicate, osTypePredicate);
		Iterable<Template> templates = Iterables.filter(
				client.getTemplateApi().listTemplates(ListTemplatesOptions.Builder.zoneId(zoneId)), templatePredicate);
		if (Iterables.any(templates, TemplatePredicates.isPasswordEnabled())) {
			templates = Iterables.filter(templates, TemplatePredicates.isPasswordEnabled());
		}
		if (Iterables.size(templates) == 0) {
			throw new NoSuchElementException(templatePredicate.toString());
		}
		String templateId = get(templates, 0).getId();
		return templateId;
	}

	public static VirtualMachine createVirtualMachineInNetwork(Network network, String templateId, CloudStackApi client,
			Predicate<String> jobComplete, Predicate<VirtualMachine> virtualMachineRunning) {
		DeployVirtualMachineOptions options = new DeployVirtualMachineOptions();
		String zoneId = network.getZoneId();
		// options.networkId(network.getId());
		return createVirtualMachineWithOptionsInZone(options, zoneId, templateId, client, jobComplete,
				virtualMachineRunning);
	}

	static final Ordering<ServiceOffering> DEFAULT_SIZE_ORDERING = new Ordering<ServiceOffering>() {
		public int compare(ServiceOffering left, ServiceOffering right) {
			return ComparisonChain.start().compare(left.getCpuNumber(), right.getCpuNumber())
					.compare(left.getMemory(), right.getMemory()).result();
		}
	};

	public static VirtualMachine createVirtualMachineWithOptionsInZone(DeployVirtualMachineOptions options,
			String zoneId, String templateId, CloudStackApi client, Predicate<String> jobComplete,
			Predicate<VirtualMachine> virtualMachineRunning) {
		String serviceOfferingId = DEFAULT_SIZE_ORDERING.min(client.getOfferingApi().listServiceOfferings()).getId();

		System.out.printf("serviceOfferingId %s, templateId %s, zoneId %s, options %s%n", serviceOfferingId, templateId,
				zoneId, options);
		AsyncCreateResponse job = client.getVirtualMachineApi().deployVirtualMachineInZone(zoneId, serviceOfferingId,
				templateId, options);
		System.out.println("JOB RESPONSE:" + job.toString());

		assert(jobComplete.apply(job.getJobId()));
		AsyncJob<VirtualMachine> jobWithResult;
		do {
			jobWithResult = client.getAsyncJobApi().<VirtualMachine> getAsyncJob(job.getJobId());
			System.out.println("JOB WITH RESULT RESPONSE:" + jobWithResult.toString());
			if (jobWithResult.getError() != null)
				Throwables.propagate(new ExecutionException(String.format("job %s failed with exception %s",
						job.getId(), jobWithResult.getError().toString())) {
				});
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (jobWithResult.getStatus() == Status.IN_PROGRESS);
		VirtualMachine vm = jobWithResult.getResult();
		System.out.println("VM:" + vm.toString());
		if (vm.isPasswordEnabled()) {
			assert vm.getPassword() != null : vm;
		}
		assert(virtualMachineRunning.apply(vm));
		assert(vm.getServiceOfferingId().equals(serviceOfferingId));
		assert(vm.getTemplateId().equals(templateId));
		assert(vm.getZoneId().equals(zoneId));
		return vm;
	}

	protected void checkVm(VirtualMachine vm) {
		assert(vm.getId().equals(client.getVirtualMachineApi().getVirtualMachine(vm.getId()).getId()));
		assert vm.getId() != null : vm;
		assert vm.getName() != null : vm;
		// vm.getDisplayName() can be null, so skip that check.
		assert vm.getAccount() != null : vm;
		assert vm.getDomain() != null : vm;
		assert vm.getDomainId() != null : vm;
		assert vm.getCreated() != null : vm;
		assert vm.getState() != null : vm;
		assert vm.getZoneId() != null : vm;
		assert vm.getZoneName() != null : vm;
		assert vm.getTemplateId() != null : vm;
		assert vm.getTemplateName() != null : vm;
		assert vm.getServiceOfferingId() != null : vm;
		assert vm.getServiceOfferingName() != null : vm;
		assert vm.getCpuCount() > 0 : vm;
		assert vm.getCpuSpeed() > 0 : vm;
		assert vm.getMemory() > 0 : vm;
		assert vm.getGuestOSId() != null : vm;
		assert vm.getRootDeviceId() != null : vm;
		// assert vm.getRootDeviceType() != null : vm;
		if (vm.getJobId() != null)
			assert vm.getJobStatus() != null : vm;
		assert vm.getNICs() != null && !vm.getNICs().isEmpty() : vm;
		for (NIC nic : vm.getNICs()) {
			assert nic.getId() != null : vm;
			assert nic.getNetworkId() != null : vm;
			assert nic.getTrafficType() != null : vm;
			assert nic.getGuestIPType() != null : vm;
			switch (vm.getState()) {
			case RUNNING:
				assert nic.getNetmask() != null : vm;
				assert nic.getGateway() != null : vm;
				assert nic.getIPAddress() != null : vm;
				break;
			case STARTING:
				assert nic.getNetmask() == null : vm;
				assert nic.getGateway() == null : vm;
				assert nic.getIPAddress() == null : vm;
				break;
			default:
				assert nic.getNetmask() != null : vm;
				assert nic.getGateway() != null : vm;
				assert nic.getIPAddress() != null : vm;
			}

		}
		assert vm.getSecurityGroups() != null && vm.getSecurityGroups().size() >= 0 : vm;
		assert vm.getHypervisor() != null : vm;
	}

	private void setupContext() {
		ContextBuilder builder = ContextBuilder.newBuilder(provider).credentials(this.identity, this.credential)
				.endpoint(this.endpoint);
		this.context = builder.buildView(CloudStackContext.class);
	}

	private void setupClient() {
		client = context.getApi();
	}

	private ComputeService compute;

	private void setupCompute() {
		this.compute = context.getComputeService();
	}

	private void setupTemplate() {
		TemplateBuilder templateBuilder = compute.templateBuilder();
		org.jclouds.compute.domain.Template template = templateBuilder.osFamily(request.getOsFamily())
				.minRam(request.getMinRam()).build();
		this.template = template;
	}
}
