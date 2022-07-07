package pageExample;

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

public class AccountUpdate implements Initializable {
    public DatabaseController DBControl;
    public Button account_save;
    public Label UserNameError;
    public Label OldPasswordError;
    public Label NewPasswordError;
    public Label AddDBMessage;
    public Label AddMessage;
    public TextField UserName;
    public TextField OldPassword;
    public TextField NewPassword;

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("account_update.fxml"));//导入fxml文件
        primaryStage.setTitle("修改账户");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();

    }//修改账户界面
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
            //加载用户修改窗口
            try {
                new AccountUpdate().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void AccountSave(ActionEvent actionEvent) {
        //获取输入的用户名
        String UserName = this.UserName.getText();
        //获取输入的旧密码
        String OldPassword = this.OldPassword.getText();
        //获取输入的新密码
        String NewPassword = this.NewPassword.getText();
        //调用DBControl的getAllUsers方法获取所有用户信息
        ArrayList<User> AllUsers = (ArrayList<User>) DBControl.getAllUsers().get("result");
        //遍历AllUsers，返回用户名ArrayList<String>
        ArrayList<String> UserNameList = new ArrayList<>();
        for (User user : AllUsers) {
            UserNameList.add(user.getUsername());
        }


        //检查输入的用户名是否为空
        if (UserName.equals("")) {
            UserNameError.setText("用户名不能为空");
        } else {
            //检查输入的用户名是否存在
            if (UserNameList.contains(UserName)) {
                UserNameError.setText("用户名存在");
                UserNameError.setStyle("-fx-text-fill: green");
            } else {
                UserNameError.setText("用户名不存在");
                UserNameError.setStyle("-fx-text-fill: red");
            }
        }
        //检查输入的旧密码是否为空
        if (OldPassword.equals("")) {
            OldPasswordError.setText("旧密码不能为空");
            OldPasswordError.setStyle("-fx-text-fill: red");
        } else {
            //检查输入的旧密码是否跟用户名对应
            if (UserNameList.contains(UserName) && OldPassword.equals(AllUsers.get(AllUsers.indexOf(UserName)).getPassword())) {
                OldPasswordError.setText("");
            } else {
                OldPasswordError.setText("旧密码错误");
                OldPasswordError.setStyle("-fx-text-fill: red");
            }
        }
        //检查输入的新密码是否为空
        if (NewPassword.equals("")) {
            NewPasswordError.setText("新密码不能为空");
            NewPasswordError.setStyle("-fx-text-fill: red");
        } else {
            //检查输入的新密码是否跟旧密码相同
            if (OldPassword.equals(NewPassword)) {
                NewPasswordError.setText("新密码不能与旧密码相同");
                NewPasswordError.setStyle("-fx-text-fill: red");
            } else {
                NewPasswordError.setText("");
            }
        }

        //如果输入的用户名为空，旧密码为空，新密码为空，则不修改密码
        if (UserName.equals("") && OldPassword.equals("") && NewPassword.equals("")) {
            AddMessage.setText("修改失败");
            //线程休眠1秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //重新加载界面
            reload();
        } else {
            //调用DBControl的getAllUsers方法获取idList
            ArrayList<Integer> idList = (ArrayList<Integer>) DBControl.getAllUsers().get("idList");
            //返回用户名对应的id
            int id = idList.get(UserNameList.indexOf(UserName));
            System.out.println("修改用户的id" + id);
            //调用DBControl的changePassword方法修改用户密码
            int result =  DBControl.changePassword(id, OldPassword, NewPassword);
            //如果修改成功，则提示修改成功
            if (result == 0) {
                AddMessage.setText("修改成功");
                //线程休眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //跳转到用户管理页面
                Platform.runLater(()->{
                    //获取按钮所在的窗口
                    Stage primaryStage = (Stage) account_save.getScene().getWindow();
                    //当前窗口隐藏
                    primaryStage.hide();
                    //加载用户管理窗口
                    try {
                        new ManagementView().start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                AddMessage.setText("修改失败");
                //线程休眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重新加载界面
                reload();
            }
        }

    }//修改账户并返回至账户管理界面

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
