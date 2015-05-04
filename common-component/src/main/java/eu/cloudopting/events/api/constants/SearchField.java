package eu.cloudopting.events.api.constants;

/**
 * @author Daniel P.
 *         Date: 8/8/13
 */
// TODO: documentat enumul, nu stiu pentru ce e
public enum SearchField {
    id, name, // common
    uuid, // for Tenant only
    loginName, email, tenant, locked, // for User only
    description // for Privilege only
}
