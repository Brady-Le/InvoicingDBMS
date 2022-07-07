package pageExample;

import Model_DB.Current_User;
import Model_DB.DatabaseController;
import Model_DB.DatabaseRoot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import static javafx.application.Application.launch;

public class Register implements Initializable {
    public int result_num;
    public DatabaseController DBControl;
    public PasswordField passwordField;
    public Label UserError;
    public Label PasswordError;
    public Label DataBaseMassage;
    @FXML
    private Button confirm;
    @FXML
    private String customer;
    @FXML
    private ChoiceBox<String> identify;
    @FXML
    private String root;
    @FXML
    private String supplier;
    @FXML
    private TextField userNameField;

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        primaryStage.setTitle("Register interface");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载注册页面
    public void ReloadRegister() {
        Platform.runLater(()->{
            //获取注册按钮所在的位置
            Stage primaryStage = (Stage) confirm.getScene().getWindow();
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
    
    public void ConfirmButtonClick(ActionEvent event) throws IOException, InterruptedException {
        String username = userNameField.getText();
        String password = passwordField.getText();
        String privilege = identify.getValue();
        System.out.println(username);
        System.out.println(password);
        System.out.println(privilege);

        //判断username是否为空,返回错误信息->log,UI,Terminal
        if (username.isEmpty()){
            UserError.setText("Error, Username cannot empty!");
            System.out.println("Error, Username cannot empty!");
            Thread.sleep(300);
            ReloadRegister();
        }
        //判断password是否为空，返回错误信息->log,UI,Terminal
        else if (password.isEmpty()){
            PasswordError.setText("Error, Password cannot empty");
            DataBaseMassage.setText("Register failed");
            System.out.println("Error, Password cannot empty!");
            Thread.sleep(300);
            ReloadRegister();
        }
        //调用DatabaseController类,注冊用户
        else {
            System.out.println(privilege);
            //出现DBControl传值为NULL, 出错原因为：跳转页面数据连接不继承。
            //解决办法：双线程，建立UI线程， 建立数据库线程， 建立终端线程。
            result_num = DBControl.registerUser(username, password, privilege);
        }

        //判断用户是否登录数据库
        if (result_num == 0){
            DataBaseMassage.setText("Register Success!");
            System.out.println("Register Success!");
            Platform.runLater(()->{
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) confirm.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载注册窗口
                try {
                    new MainPage().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (result_num == 1){
            DataBaseMassage.setText("Error, Register Fail!");
            System.out.println("Error, Register Fail!");
            Thread.sleep(300);
            ReloadRegister();
        }
        else if (result_num == 2){
            DataBaseMassage.setText("Some errors have occurred!");
            System.out.println("Some errors have occurred!");
            Thread.sleep(300);
            ReloadRegister();
        }
        else if (result_num == 3){
            DataBaseMassage.setText("Not Found!");
            System.out.println("Not Found!");
            Thread.sleep(300);
            ReloadRegister();
        }
        else if (result_num == 4){
            DataBaseMassage.setText("Duplicate results!");
            System.out.println("Duplicate results!");
            Thread.sleep(300);
            ReloadRegister();
        }
        else if (result_num == 5){
            DataBaseMassage.setText("Results do not match!");
            System.out.println("Results do not match!");
            Thread.sleep(300);
            ReloadRegister();
        }
        else {
            System.exit(0);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化数据库控制器
        DBControl = new DatabaseController();
        //连接数据库
        DatabaseRoot DBRoot = new DatabaseRoot("sa", "123456", "purchase", "1433");
        DBControl.getConnection(DBRoot.databaseName, DBRoot.username, DBRoot.password, DBRoot.port);
        //检查是否连接数据库
        int test_num = DBControl.isConnected();
        if (test_num == 0){
            DataBaseMassage.setText("Connected to database!");
            System.out.println("Connected to database!");
        }
        else {
            DataBaseMassage.setText("Connect failed!");
            System.out.println("Connect failed!");
        }

        //设置监听器
        identify.setOnAction(event -> {
            Current_User.setPrivilege(identify.getValue());
            System.out.println("Current privilege is:" + Current_User.getPrivilege());
        });

        confirm.setOnAction(event -> {
            try {
                ConfirmButtonClick(event);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
}

