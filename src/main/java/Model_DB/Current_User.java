package Model_DB;

import javax.swing.text.StyledEditorKit;
import java.util.HashMap;

//定义当前用户的全局变量
public class Current_User {
    //当前用户权限
    public static String privilege;
    public static HashMap<String, Object> sqlResult;
    public static String username;
    public static Boolean isLogin;

    //用户管理页面的Tabchoice全局变量
    public static String UserTitleChoice;

    public static String getUserTitleChoice() {
        return UserTitleChoice;
    }

    public static void setUserTitleChoice(String UserTitleChoice) {
        Current_User.UserTitleChoice = UserTitleChoice;
    }

    public static String getPrivilege() {
        return privilege;
    }

    public static void setPrivilege(String privilege) {
        Current_User.privilege = privilege;
    }

    public static HashMap<String, Object> getSqlResult() {
        return sqlResult;
    }

    public static void setSqlResult(HashMap<String, Object> sqlResult) {
        Current_User.sqlResult = sqlResult;
    }


    public static void setCurrentUser(String username) {
        Current_User.username = username;
    }

    public static void setIsLogin(Boolean isLogin) {
        Current_User.isLogin = isLogin;
    }

    public static Boolean getIsLogin() {
        return isLogin;
    }
}
