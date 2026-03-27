package largepass;

public class Entry {

    private String username;
    private String password;
    private String service;
    private String id;


    public Entry(String username, String password, String service) {
        this.username = username;
        this.password = password;
        this.service = service;
        // generate ID
        
    }

    private String generateId() {
        return "service:number";
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
    public String getId() {
        return id;
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
    
}
