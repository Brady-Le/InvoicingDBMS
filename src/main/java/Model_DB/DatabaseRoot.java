package Model_DB;

public class DatabaseRoot {
    //链接数据库的超级用户信息
    public String username;
    public String password;
    public String databaseName;
    public String port;

    public DatabaseRoot(String username, String password, String databaseName, String port) {
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPort() {
        return port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getConnectionString() {
        return "jdbc:mysql://localhost:" + port + "/" + databaseName + "?user=" + username + "&password=" + password;
    }

}
