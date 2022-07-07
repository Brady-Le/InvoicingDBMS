package Model_DB;

import javafx.beans.property.SimpleStringProperty;

public class UserProperty {
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty privilege;

    public UserProperty(String username, String password, String privilege) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.privilege = new SimpleStringProperty(privilege);
    }

    public String getUsername() {
        return this.username.get();
    }

    public String getPassword() {
        return this.password.get();
    }

    public String getPrivilege() {
        return this.privilege.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public void setPrivilege(String privilege) {
        this.privilege.set(privilege);
    }
}
