package pageExample;

import Model_DB.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class ManagementView implements Initializable {
    public DatabaseController DBControl;
    public String titleChoice;
    public Button previous;
    public TabPane TabChoice;
    public TableView<UserProperty> SaleCustomer;
    public TableColumn SaleID;
    public TableColumn SaleUserName;
    public TableColumn SalePassWord;
    public TableColumn SaleCreateTime;
    public TableView<UserProperty> PurchaseCustomer;
    public TableColumn PurchaseID;
    public TableColumn PurchaseUserName;
    public TableColumn PurchasePassWord;
    public TableColumn PurchaseCreateTime;
    public TableView<UserProperty> Customer;
    public TableColumn CustomerID;
    public TableColumn CustomerUserName;
    public TableColumn CustomerPassWord;
    public TableColumn CustomerCreateTime;
    public Label AddDBMessage;
    public Label AddMessage;
    public Button account_add;
    public Tab tab1;
    public Tab tab2;
    public Tab tab3;
    private int result_num;

    public Button account_update;


    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("management_view.fxml"));//导入fxml文件
        primaryStage.setTitle("账户管理系统桌面");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();
    }

    //初始化fxml文件
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) account_add.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new ManagementView().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /*----------------------------------------初始化界面列表----------------------------------------*/
    //初始化基本控件
    public void initialization() {
        //初始化DatabaseController
        DBControl = new DatabaseController();
        //连接数据库
        DatabaseRoot DBRoot = new DatabaseRoot("sa", "123456", "purchase", "1433");
        DBControl.getConnection(DBRoot.databaseName, DBRoot.username, DBRoot.password, DBRoot.port);
        //检查是否连接数据库
        int test_num = DBControl.isConnected();
        if (test_num == 0){
            System.out.println("Connected to database!");
        }
        else {
            System.out.println("Connect failed!");
        }
        //初始化表格导入的列
        //客户账户表格
        SaleID.setCellValueFactory(new PropertyValueFactory<>("username"));
        SaleUserName.setCellValueFactory(new PropertyValueFactory<>("password"));
        SalePassWord.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        //SaleCreateTime.setCellValueFactory(new PropertyValueFactory<User, Data>("SalesCreateTime"));
        //供应商账户表格
        PurchaseID.setCellValueFactory(new PropertyValueFactory<>("username"));
        PurchaseUserName.setCellValueFactory(new PropertyValueFactory<>("password"));
        PurchasePassWord.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        //PurchaseCreateTime.setCellValueFactory(new PropertyValueFactory<>("PurchaseCreateTime"));
        //管理员账户表格
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("username"));
        CustomerUserName.setCellValueFactory(new PropertyValueFactory<>("password"));
        CustomerPassWord.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        //CustomerCreateTime.setCellValueFactory(new PropertyValueFactory<>("CustomerCreateTime"));
    }

    //向客户账户表单添加数据
    public void InsertSaleCustomer() throws Exception {
        try {
            //调用DBControl中的getAllUsers方法获取所有用户信息，并将其存入ArrayList<User>中
            ArrayList<User> users = (ArrayList<User>) DBControl.getAllUsers().get("result");
            //提取ArrayList<User>中的所有用户信息，并将其存入ObservableList<UserProperty>中
            ObservableList<UserProperty> userProperties = FXCollections.observableArrayList();
            for (User user : users) {
                if (user.getPrivilege().equals("customer")) {
                    userProperties.add(new UserProperty(user.getUsername(), user.getPassword(), user.getPrivilege()));
                }
            }

            //提取客户账户表单的数据
            ObservableList<UserProperty> salecustomeritems = SaleCustomer.getItems();
            //清空表格
            salecustomeritems.clear();

            //创建ObservableList<User>对象，将customers添加到ObservableList<User>对象中
            ObservableList<UserProperty> data = FXCollections.observableArrayList(userProperties);

            System.out.println("添加客户账户表单数据");
            //打印data的信息
            for (UserProperty userProperty : data) {
                System.out.println(userProperty.getUsername() + " " + userProperty.getPassword() + " " + userProperty.getPrivilege());
            }

            //在SaleCustomer表格中显示提取的数据
            SaleCustomer.setItems(data);
            //SaleCustomer.getColumns().addAll(SaleID, SaleUserName, SalePassWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向供应商账户表单添加数据
    public void InsertPurchaseCustomer() throws Exception {
        try {
            //调用DBControl中的getAllUsers方法获取所有用户信息，并将其存入ArrayList<User>中
            ArrayList<User> users = (ArrayList<User>) DBControl.getAllUsers().get("result");
            //提取privilege为supplier的用户信息，并将其存入ObservableList<UserProperty>中
            ObservableList<UserProperty> userProperties = FXCollections.observableArrayList();
            for (User user : users) {
                if (user.getPrivilege().equals("supplier")) {
                    userProperties.add(new UserProperty(user.getUsername(), user.getPassword(), user.getPrivilege()));
                }
            }
            //提取客户账户表单的数据
            ObservableList<UserProperty> purchasecustomeritems = PurchaseCustomer.getItems();
            //清空表格
            purchasecustomeritems.clear();

            //创建ObservableList<User>对象，将customers添加到ObservableList<User>对象中
            ObservableList<UserProperty> data = FXCollections.observableArrayList(userProperties);

            System.out.println("添加供应商账户表单数据");
            //打印data的信息
            for (UserProperty userProperty : data) {
                System.out.println(userProperty.getUsername() + " " + userProperty.getPassword() + " " + userProperty.getPrivilege());
            }

            //在SaleCustomer表格中显示提取的数据
            PurchaseCustomer.setItems(data);
            //SaleCustomer.getColumns().addAll(PurchaseID, PurchaseUserName, PurchasePassWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向管理员账户表单添加数据
    public void InsertCustomer() throws Exception {
        try {
            //调用DBControl中的getAllUsers方法获取所有用户信息，并将其存入ArrayList<User>中
            ArrayList<User> users = (ArrayList<User>) DBControl.getAllUsers().get("result");
            //提取privilege为root的用户信息，并将其存入ObservableList<User>中
            ObservableList<UserProperty> userProperties = FXCollections.observableArrayList();
            for (User user : users) {
                userProperties.add(new UserProperty(user.getUsername(), user.getPassword(), user.getPrivilege()));
            }
            //提取客户账户表单的数据
            ObservableList<UserProperty> customeritems = Customer.getItems();
            //清空表格
            customeritems.clear();

            //创建ObservableList<UserProperty>对象，将customers添加到ObservableList<User>对象中
            ObservableList<UserProperty> data = FXCollections.observableArrayList(userProperties);

            System.out.println("添加管理员账户表单数据");
            //打印data的信息
            for (UserProperty userProperty : data) {
                System.out.println(userProperty.getUsername() + " " + userProperty.getPassword() + " " + userProperty.getPrivilege());
            }

            //在SaleCustomer表格中显示提取的数据
            Customer.setItems(data);
            //SaleCustomer.getColumns().addAll(CustomerID, CustomerUserName, CustomerPassWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*----------------------------------------TabPane监听函数----------------------------------------*/

    //设置tab1的onSelectionChanged监听器
    public void ChoiceTab1(Event event) {
        //获取当前选中的Tab
        Tab tab = TabChoice.getSelectionModel().getSelectedItem();
        //获取当前选中的Tab的标题
        String title = tab.getText();
        System.out.println(title);
        //如果选中的是客户账号Tab
        titleChoice = title;
    }

    //设置tab2的onSelectionChanged监听器
    public void ChoiceTab2(Event event) {
        //获取当前选中的Tab
        Tab tab = TabChoice.getSelectionModel().getSelectedItem();
        //获取当前选中的Tab的标题
        String title = tab.getText();
        System.out.println(title);
        //如果选中的是供应商账号Tab
        titleChoice = title;
    }

    //设置tab3的onSelectionChanged监听器
    public void ChoiceTab3(Event event) {
        //获取当前选中的Tab
        Tab tab = TabChoice.getSelectionModel().getSelectedItem();
        //获取当前选中的Tab的标题
        String title = tab.getText();
        System.out.println(title);
        //如果选中的是管理员账号Tab
        titleChoice = title;
    }

    /*----------------------------------------添加账号按钮----------------------------------------*/

    public void AddAccount(ActionEvent event) {
        //Current_User调用setUserTitleChoice方法，将titleChoice传入
        Current_User.setUserTitleChoice(titleChoice);
        //跳转至添加账号页面
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage backStage = (Stage) account_add.getScene().getWindow();
            //当前窗口隐藏
            backStage.hide();
            //加载添加账号窗口
            try {
                new AccountAdd().start(backStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /*----------------------------------------返回主页面按钮----------------------------------------*/

    public void PreviousButtonClick(){
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage backStage = (Stage) previous.getScene().getWindow();
            //当前窗口隐藏
            backStage.hide();
            //加载注册窗口
            try {
                new MainPage().start(backStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /*----------------------------------------修改用户按钮----------------------------------------*/

    public void AccountUpdate(ActionEvent event) {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage backStage = (Stage) previous.getScene().getWindow();
            //当前窗口隐藏
            backStage.hide();
            //加载注册窗口
            try {
                new AccountUpdate().start(backStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化表格
        initialization();
        //客户账户表单初始化
        try {
            InsertSaleCustomer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //供应商账户表单初始化
        try {
            InsertPurchaseCustomer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //管理员账户表单初始化
        try {
            InsertCustomer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //调用ChoiceTab1方法设置按钮tab1的监听器
        tab1.setOnSelectionChanged(event -> {
            try {
                ChoiceTab1((Event) event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //调用ChoiceTab2方法设置按钮tab2的监听器
        tab2.setOnSelectionChanged(event -> {
            try {
                ChoiceTab2((Event) event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //调用ChoiceTab3方法设置按钮tab3的监听器
        tab3.setOnSelectionChanged(event -> {
            try {
                ChoiceTab3((Event) event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //设置添加账号按钮的监听器
        account_add.setOnAction(event -> {
            AddAccount((ActionEvent) event);
        });

        //设置返回主页面按钮的监听器
        previous.setOnAction(event -> {
            PreviousButtonClick();
        });

        //设置修改用户按钮的监听器
        account_update.setOnAction(event -> {
            AccountUpdate((ActionEvent) event);
        });

    }

}
