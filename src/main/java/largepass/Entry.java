package largepass;

/**
 * Entry class used for containing data for each Entry that is in the database. 
 */
public class Entry implements Comparable<Entry> {

    private String username;
    private String password;
    private String service;


    /**
     * Entry contains a username, password, and a service name. 
     * @param username
     * @param password
     * @param service
     */
    public Entry(String username, String password, String service) {
        this.username = username;
        this.password = password;
        this.service = service;
    }

    /**
     * The {@link com.fasterxml.jackson.databind.ObjectMapper#readValue()} needs an empty constructor
     * to construct each Entry from the Json string. 
     */
    public Entry() {

    }

    /**
     * Needs this to display the service name in the ListView in the Vault. 
     * @return {@link String}
     */
    @Override
    public String toString() {
        return this.service;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getService() {
        return service;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Alphabetic sorting of Entries. 
     * Service -> Username
     * @param other string to compare with
     * @return {@link Integer}
     */
    @Override
    public int compareTo(Entry other) {
        if (other == null) return -1;

        String thisService = service == null ? "" : service;
        String otherService = other.service == null ? "" : other.service;

        int serviceCmp = thisService.compareToIgnoreCase(otherService);
        if (serviceCmp != 0) return serviceCmp;

        String thisUsername = username == null ? "" : username;
        String otherUsername = other.username == null ? "" : other.username;

        return thisUsername.compareToIgnoreCase(otherUsername);
    }
    
}
