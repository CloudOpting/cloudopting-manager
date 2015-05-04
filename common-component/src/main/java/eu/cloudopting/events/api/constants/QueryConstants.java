package eu.cloudopting.events.api.constants;

/**
 * The type Query constants.
 *
 * @author Daniel P.
 *         Date: 8/8/13
 */
public final class QueryConstants {

    /** The constant ID_NEG. */
    public static final String ID_NEG = SearchField.id.toString() + QueryConstants.NEGATION;
    /** The constant NAME_NEG. */
    public static final String NAME_NEG = SearchField.name.toString() + QueryConstants.NEGATION;
    /** The constant LOGIN_NAME_NEG. */
    public static final String LOGIN_NAME_NEG = SearchField.loginName.toString() + QueryConstants.NEGATION;
    /** The constant EMAIL_NEG. */
    public static final String EMAIL_NEG = SearchField.email.toString() + QueryConstants.NEGATION;
    /** The constant TENANT_NEG. */
    public static final String TENANT_NEG = SearchField.tenant.toString() + QueryConstants.NEGATION;
    /** The constant LOCKED_NEG. */
    public static final String LOCKED_NEG = SearchField.locked.toString() + QueryConstants.NEGATION;
    /** The constant DESCRIPTION_NEG. */
    public static final String DESCRIPTION_NEG = SearchField.description.toString() + QueryConstants.NEGATION;
    /** The constant QUESTIONMARK. */
    public static final String QUESTIONMARK = "?";
    /** The constant PAGE. */
    public static final String PAGE = "page";
    /** The constant SIZE. */
    public static final String SIZE = "size";
    /** The constant SORT_BY. */
    public static final String SORT_BY = "sortBy";
    /** The constant SORT_ORDER. */
    public static final String SORT_ORDER = "sortOrder";
    /** The constant Q_SORT_BY. */
    public static final String Q_SORT_BY = QUESTIONMARK + SORT_BY + QueryConstants.OP;
    /** The constant S_ORDER. */
    public static final String S_ORDER = QueryConstants.SEPARATOR_AMPER + QueryConstants.SORT_ORDER + QueryConstants.OP;
    /** The constant S_ORDER_ASC. */
    public static final String S_ORDER_ASC = S_ORDER + "ASC";
    /** The constant S_ORDER_DESC. */
    public static final String S_ORDER_DESC = S_ORDER + "DESC";
    /** note: this character represents the ANY wildcard for the server side (persistence layer). */
    public static final String ANY_SERVER = "%";
    /** note: this character represents the ANY wildcard for the client consumption of the API. */
    public static final String ANY_CLIENT = "*";
    /** The constant QUERY_PREFIX. */
    public static final String QUERY_PREFIX = QUESTIONMARK + "q=";
    /** The constant Q_PARAM. */
    public static final String Q_PARAM = "q";
    /** The constant SEPARATOR. */
    public static final String SEPARATOR = ",";
    /** The constant SEPARATOR_AMPER. */
    public static final String SEPARATOR_AMPER = "&";
    /** The constant OP. */
    public static final String OP = "=";
    /** The constant NEGATION. */
    public static final String NEGATION = "~";
    /** The constant ID. */
    public static final String ID = "id"; // is constant because it's used for the controller mapping
    /** The constant NAME. */
    public static final String NAME = SearchField.name.toString();
    /** The constant UUID. */
    public static final String UUID = "uuid";
    
    public static final String FILTER = "filter";

    /** TODO: ce e asta? */
    private QueryConstants() {
        throw new AssertionError();
    }
}
