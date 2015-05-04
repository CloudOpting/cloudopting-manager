package eu.cloudopting.events.api.exceptions.messages;

/**
 * Obiect utilizat pentru transmiterea detaliilor despre erori catre client.
 */
public class ServerErrorInfo {
    /**
     * Mesajul de eroare.
     */
    private String message;
    /**
     * URL-ul request-ului pentru care s-a inregistrat eroarea.
     */
    private String requestUrl;

    /**
     * Impachetam un mesaj cu detalii eroare pentru client.
     *
     * @param message    mesajul
     * @param requestUrl url-ul
     */
    public ServerErrorInfo(String message, String requestUrl) {
        this.message = message;
        this.requestUrl = requestUrl;
    }

    /**
     * Mesajul de eroare.
     *
     * @return un mesaj transmis clientului
     */
    public String getMessage() {
        return message;
    }

    /**
     * URL-ul request-ului pentru care s-a inregistrat eroarea.
     *
     * @return url-ul request-ului
     */
    public String getRequestUrl() {
        return requestUrl;
    }
}
