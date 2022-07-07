package Model_DB;

import java.sql.Timestamp;
import java.util.Date;

public class User {
    String username;
    String password;
    String privilege;
    Date create_time;

    User(String username, String password, String privilege, Date create_time) {
        this.username = username;
        this.password = password;
        this.privilege = privilege;
        this.create_time = create_time;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrivilege() {
        return privilege;
    }

    public Date getCreate_time() {
        return new Date(create_time.getTime());
    }
}
