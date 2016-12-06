package eu.cloudopting.provision.azure;

import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jclouds.ContextBuilder;
import org.jclouds.azurecompute.AzureComputeApi;
import org.jclouds.azurecompute.AzureComputeProviderMetadata;
import org.jclouds.azurecompute.compute.AzureComputeServiceAdapter;
import org.jclouds.azurecompute.domain.CloudService;
import org.jclouds.azurecompute.domain.CreateStorageServiceParams;
import org.jclouds.azurecompute.domain.Deployment;
import org.jclouds.azurecompute.domain.DeploymentParams;
import org.jclouds.azurecompute.domain.NetworkConfiguration;
import org.jclouds.azurecompute.domain.StorageService;
import org.jclouds.azurecompute.features.DeploymentApi;
import org.jclouds.azurecompute.features.VirtualNetworkApi;
import org.jclouds.azurecompute.util.ConflictManagementPredicate;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.providers.ProviderMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Module;

import eu.cloudopting.provision.AbstractProvision;

/**
 * Main class used to provision on azure.
 */
public class AzureProvision extends AbstractProvision<AzureResult, AzureRequest> {

    private NetworkConfiguration.VirtualNetworkSite virtualNetworkSite;
    private final Set<Module> modules = ImmutableSet.<Module>of(new ExecutorServiceModule(newDirectExecutorService()));
    AzureRequest request;
    private StorageService storageService;
    DeploymentApi deploymentApi;

    @Override
    public AzureResult provision(AzureRequest request) {
        this.request = request;
        CloudService cloudService = getOrCreateCloudService(request.getCloudService(), request.getLocation());
        storageService = getOrCreateStorageService(request.getStorageServiceName(), storageServiceParams());
        virtualNetworkSite = getOrCreateVirtualNetworkSite(request.getVirtualNetworkName(), request.getLocation());
        DeploymentApi deploymentApi = api().getDeploymentApiForService(cloudService.name());
        this.deploymentApi = deploymentApi;
        final String requestId = deploymentApi.create(deploymentParams());
        assert (operationSucceeded().apply(requestId));
        AzureResult azureResult = new AzureResult();
        azureResult.setRequestId(requestId);
        azureResult.setDeplymentId(requestId);
        azureResult.setDeplymentName(request.getDeployment());
        request.setAzureResult(azureResult);
        return azureResult;
    }

    private DeploymentParams deploymentParams() {
    	List<String> subnetNames = new LinkedList<String>();
    	subnetNames.add(Iterables.get(virtualNetworkSite.subnets(), 0).name());
    	
    	Set<DeploymentParams.ExternalEndpoint> endpoints = new HashSet<DeploymentParams.ExternalEndpoint>();
    	endpoints.add(DeploymentParams.ExternalEndpoint.inboundTcpToLocalPort(22, 22));
    	endpoints.addAll(request.getExternalEndpoints());
        return DeploymentParams.builder()
                .name(request.getDeployment())
                .os(request.getOsImageType())
                .sourceImageName(request.getImageName())
                .mediaLink(AzureComputeServiceAdapter.createMediaLink(storageService.serviceName(), request.getDeployment()))
                .username(request.getUsername())
                .password(request.getPassword())
                .size(request.getRoleSizeType())
                .subnetNames(subnetNames)
                .virtualNetworkName(virtualNetworkSite.name())
                .externalEndpoints(endpoints)
                .build();
    }

    private AzureComputeApi api() {
        Properties properties = new Properties();
        ProviderMetadata metadata = new AzureComputeProviderMetadata();

        return ContextBuilder.newBuilder(metadata).credentials(request.getIdentity(), request.getCredential()).endpoint(request.getUrl() + request.getSubscriptionId())
                .modules(modules).overrides(properties).buildApi(AzureComputeApi.class);
    }

    private Predicate<String> operationSucceeded() {
        return new ConflictManagementPredicate(api(), 600, 5, 5, TimeUnit.SECONDS);
    }

    protected CloudService getOrCreateCloudService(final String cloudServiceName, final String location) {
        CloudService cloudService = api().getCloudServiceApi().get(cloudServiceName);
        if (cloudService != null) {
            return cloudService;
        }

        String requestId = api().getCloudServiceApi().createWithLabelInLocation(
                cloudServiceName, cloudServiceName, location);

        assert (operationSucceeded().apply(requestId));
        Logger.getAnonymousLogger().log(Level.INFO, "operation succeeded: {0}", requestId);
        cloudService = api().getCloudServiceApi().get(cloudServiceName);
        Logger.getAnonymousLogger().log(Level.INFO, "created cloudService: {0}", cloudService);
        return cloudService;
    }

    protected StorageService getOrCreateStorageService(String storageServiceName, CreateStorageServiceParams params) {
        StorageService ss = api().getStorageAccountApi().get(storageServiceName);
        if (ss != null) {
            return ss;
        }
        String requestId = api().getStorageAccountApi().create(params);
        assert (operationSucceeded().apply(requestId));
        Logger.getAnonymousLogger().log(Level.INFO, "operation succeeded: {0}", requestId);
        ss = api().getStorageAccountApi().get(storageServiceName);
        Logger.getAnonymousLogger().log(Level.INFO, "created storageService: {0}", ss);
        return ss;
    }

    private CreateStorageServiceParams storageServiceParams() {
        return CreateStorageServiceParams.builder().
                serviceName(request.getStorageServiceName()).
                label(request.getStorageServiceName()).
                location(request.getLocation()).
                accountType(request.getStorageAccountType()).
                build();
    }


    private NetworkConfiguration.VirtualNetworkSite getOrCreateVirtualNetworkSite(final String virtualNetworkSiteName, String location) {
        final List<NetworkConfiguration.VirtualNetworkSite> current = getVirtualNetworkSite(api());

        final com.google.common.base.Optional<NetworkConfiguration.VirtualNetworkSite> optionalVirtualNetworkSite = tryFind(
                current,
                new SameVirtualNetworkSiteNamePredicate(virtualNetworkSiteName));

        if (optionalVirtualNetworkSite.isPresent()) {
            return optionalVirtualNetworkSite.get();
        }

        current.add(NetworkConfiguration.VirtualNetworkSite.create(UUID.randomUUID().toString(),
                virtualNetworkSiteName,
                location,
                NetworkConfiguration.AddressSpace.create(request.getAddressSpace()),
                ImmutableList.of(NetworkConfiguration.Subnet.create(request.getSubnetName(), request.getSubnetAddressSpace(), null))));

        final NetworkConfiguration networkConfiguration
                = NetworkConfiguration.create(NetworkConfiguration.VirtualNetworkConfiguration.create(null, current));

        NetworkConfiguration.VirtualNetworkSite vns;
        try {
            vns = find(
                    api().getVirtualNetworkApi().getNetworkConfiguration().virtualNetworkConfiguration().
                            virtualNetworkSites(),
                    new SameVirtualNetworkSiteNamePredicate(virtualNetworkSiteName));
        } catch (Exception e) {
            assert (new ConflictManagementPredicate(api()) {

                @Override
                protected String operation() {
                    return api().getVirtualNetworkApi().set(networkConfiguration);
                }
            }.apply(virtualNetworkSiteName));

            vns = find(
                    api().getVirtualNetworkApi().getNetworkConfiguration().virtualNetworkConfiguration().
                            virtualNetworkSites(),
                    new SameVirtualNetworkSiteNamePredicate(virtualNetworkSiteName));

            Logger.getAnonymousLogger().log(Level.INFO, "created virtualNetworkSite: {0}", vns);
        }
        return vns;
    }

    public static List<NetworkConfiguration.VirtualNetworkSite> getVirtualNetworkSite(AzureComputeApi api) {
        final VirtualNetworkApi vnapi = api.getVirtualNetworkApi();
        final NetworkConfiguration netConf = vnapi.getNetworkConfiguration();

        return netConf == null
                ? new ArrayList<NetworkConfiguration.VirtualNetworkSite>()
                : new ArrayList<NetworkConfiguration.VirtualNetworkSite>(netConf.virtualNetworkConfiguration().
                virtualNetworkSites());

    }

    public static class SameVirtualNetworkSiteNamePredicate
            implements Predicate<NetworkConfiguration.VirtualNetworkSite> {

        private final String virtualNetworkSiteName;

        public SameVirtualNetworkSiteNamePredicate(final String virtualNetworkSiteName) {
            this.virtualNetworkSiteName = virtualNetworkSiteName;
        }

        @Override
        public boolean apply(final NetworkConfiguration.VirtualNetworkSite input) {
            return input.name().equals(virtualNetworkSiteName);
        }
    }

	@Override
	public String provisionVM(AzureRequest request, HashMap<String, String> vmdata) {
		return provision(request).getRequestId();
	}

	@Override
	public boolean checkVMdeployed(AzureRequest myRequest, String taskId) {
        Deployment.Status status = deploymentApi.get(myRequest.getDeployment()).status();
        return status != null && status.toString().equals("RUNNING");
    }

	@Override
	public JSONObject getVMinfo(AzureRequest myRequest, String taskId) {
		JSONObject jsonObject = new JSONObject();
//        Deployment deployment = deploymentApi.get(myRequest.getDeployment());
        try {
            jsonObject.put("vmId", deploymentApi.get(myRequest.getDeployment()).name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
	}

	@Override
	public String acquireIp(AzureRequest myRequest) {
		return myRequest.getDeployment()+".cloudapp.net";
	}

	@Override
	public boolean checkIpAcquired(AzureRequest myRequest, String taskId) {
		return true;
	}

	@Override
	public JSONObject getAcquiredIpinfo(AzureRequest myRequest, String taskId) {
        JSONObject ipData = new JSONObject();
        try {
            ipData.put("ip", myRequest.getDeployment()+".cloudapp.net");
            ipData.put("ipId", "");
        } catch (JSONException e) {

            e.printStackTrace();
        }
		return ipData;
	}

	@Override
	public String portForward(AzureRequest myRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkPortForward(AzureRequest myRequest, String taskId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSONObject getVMinfoById(AzureRequest myRequest) {
        JSONObject jsonObject = new JSONObject();
//        Deployment deployment = deploymentApi.get(myRequest.getDeployment());
        try {
            jsonObject.put("vmId", deploymentApi.get(myRequest.getDeployment()).name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
	}

	@Override
	public String removeISO(AzureRequest myRequest) {
		return "";
	}

	@Override
	public boolean checkIso(AzureRequest myRequest, String taskId) {
		return true;
	}

	@Override
	public String startVM(AzureRequest myRequest) {
		return myRequest.getDeployment();
	}


}
