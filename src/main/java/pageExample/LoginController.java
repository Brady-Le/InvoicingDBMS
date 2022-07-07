    package pageExample;

    import Model_DB.*;
    import com.mysql.cj.log.Log;
    import javafx.application.Platform;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.stage.Stage;

    import java.awt.event.ActionListener;
    import java.io.IOException;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.ResourceBundle;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    import static javafx.application.Application.launch;

    public class LoginController implements Initializable {

        private static final Logger logger = Logger.getLogger(LoginController.class.getName());
        public Label PasswordError;
        public Label UserError;
        public javafx.scene.control.PasswordField passwordField;
        public HashMap<String, Object> result_num;
        public Label DataBaseConnect;
        public Label DataBaseMassage;
        private DatabaseController DBControl;
        @FXML
        private TextField NumberTextField;
        @FXML
        private Button signIn;
        @FXML
        private Button Register;

        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.setTitle("Register interface");
            primaryStage.setScene(new Scene(root, 580, 400));
            primaryStage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }

        public void ReloadLogin(){
            Platform.runLater(()->{
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) signIn.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //重新加载登录窗口
                try {
                    new LoginController().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        @FXML
        public void LoginButtonClick(ActionEvent event) throws IOException, InterruptedException {
            logger.log(Level.INFO, "输入用户名为：" + NumberTextField.getText());
            logger.log(Level.INFO, "输入密码为：" + passwordField.getText());
            String username = NumberTextField.getText();
            String password = passwordField.getText();

            //判断username是否为空,返回错误信息->log,UI,Terminal
            if (username.isEmpty()){
                UserError.setText("Error, Username cannot empty!");
                logger.log(Level.INFO, "Error, Username cannot empty!");
                System.out.println("Error, Username cannot empty!");
                Thread.sleep(300);
                ReloadLogin();
            }
            //判断password是否为空，返回错误信息->log,UI,Terminal
            else if (password.isEmpty()){
                PasswordError.setText("Error, Password cannot empty");
                logger.log(Level.INFO, "Error, Password cannot empty!");
                System.out.println("Error, Password cannot empty!");
                Thread.sleep(300);
                ReloadLogin();
            }
            //调用DatabaseController类,登录用户
            else {
                result_num = DBControl.loginUser(username, password);
            }

            //判断用户是否登录数据库
            if ((int)result_num.get("returnCode") == 0) {
                //显示登录成功相关信息
                DataBaseMassage.setText("Login Success!");
                logger.log(Level.INFO, "Login Success!");
                System.out.println("Login Success!");

                System.out.println("Jump MainPage!");

                //调用Current_User类,设置当前用户的登录状态为true
                Current_User.setIsLogin(true);
                //调用Current_User类,设置当前用户的用户名
                Current_User.setCurrentUser(username);
                //get("result")获取登录结果result_num的值,并赋值给User
                User user = (User) result_num.get("result");
                //调用Current_User类，设置当前用户的权限
                Current_User.setPrivilege(user.getPrivilege());
                System.out.println("Current privilege is: " + Current_User.getPrivilege());
                //调用Current_User类，设置SQL返回的结果
                Current_User.setSqlResult(result_num);
                }
                else if ((int)result_num.get("returnCode") == 1){
                    //调用Current_User类,设置当前用户的登录状态为false
                    Current_User.setIsLogin(false);

                    DataBaseMassage.setText("Error, Login Fail!");
                    logger.log(Level.INFO, "Error, Login Fail!");
                    System.out.println("Error, Login Fail!");
                    Thread.sleep(300);
                    ReloadLogin();
                }
                else if ((int)result_num.get("returnCode") == 2){
                    //调用Current_User类,设置当前用户的登录状态为false
                    Current_User.setIsLogin(false);

                    DataBaseMassage.setText("Some errors have occurred!");
                    logger.log(Level.INFO, "Some errors have occurred!");
                    System.out.println("Some errors have occurred!");
                    Thread.sleep(300);
                    ReloadLogin();
                }
                else if ((int)result_num.get("returnCode") == 3){
                    //调用Current_User类,设置当前用户的登录状态为false
                    Current_User.setIsLogin(false);

                    DataBaseMassage.setText("Not Found!");
                    logger.log(Level.INFO, "Not Found!");
                    System.out.println("Not Found!");
                    Thread.sleep(300);
                    ReloadLogin();
                }
                else if ((int)result_num.get("returnCode") == 4){
                    //调用Current_User类,设置当前用户的登录状态为false
                    Current_User.setIsLogin(false);
                    DataBaseMassage.setText("Duplicate results!");
                    logger.log(Level.INFO, "Duplicate results!");
                    System.out.println("Duplicate results!");
                    Thread.sleep(300);
                    ReloadLogin();
                }
                else if ((int)result_num.get("returnCode") == 5){
                    //调用Current_User类,设置当前用户的登录状态为false
                    Current_User.setIsLogin(false);
                    DataBaseMassage.setText("Results do not match!");
                    logger.log(Level.INFO, "Results do not match!");
                    System.out.println("Results do not match!");
                    Thread.sleep(300);
                    ReloadLogin();
                }
                else {
                    //调用Current_User类,设置当前用户的登录状态为false
                    Current_User.setIsLogin(false);
                    System.exit(0);
                }

                //登录成功跳转至主页面
                Platform.runLater(()->{
                    //获取按钮所在的窗口
                    Stage primaryStage = (Stage) signIn.getScene().getWindow();
                    //当前窗口隐藏
                    primaryStage.hide();
                    //加载主窗口
                    try {
                        new MainPage().start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        public void RegisterButtonClick (ActionEvent event) throws IOException{
            Platform.runLater(()->{
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) Register.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载注册窗口
                try {
                    new Register().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            //设置超级用户连接数据库
            DatabaseRoot DBRoot = new DatabaseRoot("sa", "123456", "purchase", "1433");
            System.out.println(DBRoot.databaseName + DBRoot.username + DBRoot.password + DBRoot.port);
            //DatabaseController类
            DBControl = new DatabaseController();
            //调用getConnection()方法，连接数据库
            DBControl.getConnection(DBRoot.databaseName, DBRoot.username, DBRoot.password, DBRoot.port);

            int result_num_2 = DBControl.isConnected();
            //判断数据库是否连接成功
            if (result_num_2 == 0){
                DataBaseConnect.setText("DataBase Connect Success!");
                logger.log(Level.INFO, "Connect Success!");
                System.out.println("Connect Success!");
            }
            else if (result_num_2 == 1){
                DataBaseConnect.setText("Error, DataBase Connect Fail!");
                logger.log(Level.INFO, "Error, Connect Fail!");
                System.out.println("Error, Connect Fail!");
            }
            else if (result_num_2 == 2){
                DataBaseConnect.setText("Some errors have occurred!");
                logger.log(Level.INFO, "Some errors have occurred!");
                System.out.println("Some errors have occurred!");
            }
            else if (result_num_2 == 3){
                DataBaseConnect.setText("Not Found!");
                logger.log(Level.INFO, "Not Found!");
                System.out.println("Not Found!");
            }
            else if (result_num_2 == 4){
                DataBaseConnect.setText("Duplicate results!");
                logger.log(Level.INFO, "Duplicate results!");
                System.out.println("Duplicate results!");
            }
            else if (result_num_2 == 5){
                DataBaseConnect.setText("Results do not match!");
                logger.log(Level.INFO, "Results do not match!");
                System.out.println("Results do not match!");
            }
            else {
                System.exit(0);
            }

            int result_num3 = DBControl.initializeDatabase();
            if (result_num3 == 0){
                logger.log(Level.INFO, "Initialize Database Success!");
                System.out.println("Initialize Success!");
            }
            else if (result_num3 == 1){
                logger.log(Level.INFO, "Error, Initialize Database Fail!");
                System.out.println("Error, Initialize Database Fail!");
            }
            else if (result_num3 == 2){
                logger.log(Level.INFO, "Some errors have occurred!");
                System.out.println("Some errors have occurred!");
            }
            else if (result_num3 == 3){
                logger.log(Level.INFO, "Not Found!");
                System.out.println("Not Found!");
            }
            else if (result_num3 == 4){
                logger.log(Level.INFO, "Duplicate results!");
                System.out.println("Duplicate results!");
            }
            else if (result_num3 == 5){
                logger.log(Level.INFO, "Results do not match!");
                System.out.println("Results do not match!");
            }
            else {
                System.exit(0);
            }

            //设置登录按钮的监听事件
            signIn.setOnAction(event -> {
                try {
                    LoginButtonClick(event);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            //设置注册按钮的监听事件
            Register.setOnAction(event -> {
                try {
                    RegisterButtonClick(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
