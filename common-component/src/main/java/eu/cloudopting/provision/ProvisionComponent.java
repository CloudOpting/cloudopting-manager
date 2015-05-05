package eu.cloudopting.provision;

/**
 * Main interface for all provision components.
 */
public interface ProvisionComponent<Resp extends ProvisionResult,Req extends ProvisionRequest> {
    public Resp provision(Req request);
}
