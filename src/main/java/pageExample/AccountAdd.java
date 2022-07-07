package pageExample;

import Model_DB.Current_User;
import Model_DB.DatabaseController;
import Model_DB.DatabaseRoot;
import Model_DB.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class AccountAdd implements Initializable {
    public DatabaseController DBControl;
    public Button account_save;
    public TextField UserName;
    public Label UserNameError;
    public TextField Password;
    public Label PasswordError;
    public Label AddDBMessage;
    public Label AddMessage;
    public String privilege;
    public Label privilegeError;

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("account_add.fxml"));
        primaryStage.setTitle("添加账户");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();

    }//添加账户界面
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) account_save.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new AccountAdd().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void AccountSave(ActionEvent actionEvent) {
        //获取输入的用户名
        String UserName = this.UserName.getText();
        //获取输入的密码
        String Password = this.Password.getText();

        //检查输入的用户名是否为空
        if (UserName.equals("")) {
            UserNameError.setText("用户名不能为空");
        } else {
            //调用DBControl的getAllUsers方法获取所有用户信息，并判断用户名是否已存在
            ArrayList<User> users = (ArrayList<User>) DBControl.getAllUsers().get("result");
            for (User user : users) {
                if (user.getUsername().equals(UserName)) {
                    UserNameError.setText("用户名已存在");
                }
            }
            UserNameError.setText("");
        }
        //检查输入的密码是否为空
        if (Password.equals("")) {
            PasswordError.setText("密码不能为空");
        } else {
            PasswordError.setText("");
        }

        //调用Current_User的getUserTitleChoice方法获取添加用户的权限
        String UserTitleChoice = Current_User.getUserTitleChoice();
        privilege = UserTitleChoice;

        //检查输入的权限是否为空
        if (privilege.equals("")) {
            System.out.println("权限不能为空");
        } else {
            //检查输入的权限是否为(root,customer,supplier)
            if (privilege.equals("root") || privilege.equals("customer") || privilege.equals("supplier")) {
                System.out.println("权限正确");
            } else {
                System.out.println("权限错误,必须为(root,customer,supplier)");
            }
        }

        //如果用户名为空或密码为空或密码不是数字或用户名已存在，则不添加用户
        if (UserName.equals("") || Password.equals("") || !AddMessage.getText().equals("")) {
            AddMessage.setText("添加失败");
            //线程休眠1秒，让用户看到添加失败的提示信息
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //重新加载界面
            reload();
        }
        else {
            //调用DBControl的registerUser方法，添加用户
            int result =  DBControl.registerUser(UserName, Password, privilege);
            //如果添加成功，则提示添加成功，并跳回用户管理界面
            if (result == 0) {
                AddMessage.setText("添加成功");
                Platform.runLater(()->{
                    //获取按钮所在的窗口
                    Stage backStage = (Stage)account_save.getScene().getWindow();
                    //当前窗口隐藏
                    backStage.hide();
                    //加载用户管理窗口
                    try {
                        new ManagementView().start(backStage);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                });
            } else {
                AddMessage.setText("添加失败");
                //线程睡眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reload();
            }
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
            AddDBMessage.setText("Connected to database!");
            System.out.println("Connected to database!");
        }
        else {
            AddDBMessage.setText("Connect failed!");
            System.out.println("Connect failed!");
        }

        //初始化product_save按钮的监听事件
        account_save.setOnAction(actionEvent -> {
            try {
                AccountSave(actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
