package pageExample;

import Model_DB.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class ManStatistic implements Initializable {
    public DatabaseController DBControl;
    public Label UserError;
    public TableView<PurchaseOrderProperty> purchase;
    public TableView<SaleOrderProperty> sale;
    @FXML
    private Button peopleSearch;
    @FXML
    private Button peopleSearchBack;
    @FXML
    private TextField people;

    //出货统计列
    public TableColumn saleId;
    public TableColumn saleDate;
    public TableColumn saleGoodId;
    public TableColumn saleCount;
    public TableColumn saleTotalPrice;
    public TableColumn saleCustomerId;
    //入货统计列
    public TableColumn InPurchaseId;
    public TableColumn InPurchaseDate;
    public TableColumn InGoodsID;
    public TableColumn InGoodsCount;
    public TableColumn InGoodsTotalPrice;
    public TableColumn InSupplierId;


    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("man_statistic.fxml"));
        primaryStage.setTitle("用户统计查询");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();

    }//用户（顾客/供应商）查询界面
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) peopleSearch.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载客户查询窗口
            try {
                new ManStatistic().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

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
        //出货表单
        saleId.setCellValueFactory(new PropertyValueFactory<>("id"));
        saleGoodId.setCellValueFactory(new PropertyValueFactory<>("goods_id"));
        saleCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        saleTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        saleCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        //入货表单
        InPurchaseId.setCellValueFactory(new PropertyValueFactory<>("id"));
        InGoodsID.setCellValueFactory(new PropertyValueFactory<>("goods_id"));
        InGoodsCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        InGoodsTotalPrice.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        InSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));
    }

    public void peopleSearch(ActionEvent actionEvent) throws Exception {
        //获取TextField的值
        String people_name = people.getText();
        //调用DBControl的getAllPurchaseOrders函数获取用户所有的出货订单
        ArrayList<PurchaseOrder> purchaseOrders = (ArrayList<PurchaseOrder>) DBControl.getAllPurchaseOrders().get("result");
        //调用DBControl的getAllSaleOrders函数获取用户所有的入货订单
        ArrayList<SaleOrder> saleOrders = (ArrayList<SaleOrder>) DBControl.getAllSaleOrders().get("result");

        //判断用户名是否为空
        if (people_name.equals("")){
            UserError.setText("用户名不能为空！");
            UserError.setStyle("-fx-text-fill: red");
        }
        else {
            //调用DBControl的getUserId函数获取用户id
            int people_id = DBControl.getUserId(people_name);
            System.out.println("people_id: " + people_id);
            //遍历purchaseOrders的supplier_id属性，判断supplier_id与people_id相等
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                if (purchaseOrder.getSupplier_id() == people_id) {
                    //UserError输出用户名为supplier
                    UserError.setText("查询结果：" + people_name + "为供应商");
                    UserError.setStyle("-fx-text-fill: green");
                    break;
                }
            }
            //遍历saleOrders的customer_id属性，判断customer_id与people_id相等
            for (SaleOrder saleOrder : saleOrders) {
                if (saleOrder.getCustomer_id() == people_id) {
                    //UserError输出用户名为customer
                    UserError.setText("查询结果：" + people_name + "为顾客");
                    UserError.setStyle("-fx-text-fill: green");
                    break;
                }
            }
            //如果上面的循环没有找到，则输出用户名不存在
            if (UserError.getText().equals("")){
                UserError.setText("查询结果：" + people_name + "不存在");
                UserError.setStyle("-fx-text-fill: red");
            }
        }

        //若查询结果为供应商，则调用DBControl的getAllPurchaseOrders函数获取用户所有的出货订单
        if (UserError.getText().equals("查询结果：" + people_name + "为供应商")){
            //调用DBControl的getUserId函数获取用户id
            int people_id = DBControl.getUserId(people_name);
            //调用DBControl的getAllPurchaseOrders函数获取用户所有的出货订单
            ArrayList<PurchaseOrder> purchaseOrders_supplier = new ArrayList<>();
            //遍历purchaseOrders_supplier的supplier_id属性，判断supplier_id与people_id相等
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                if (purchaseOrder.getSupplier_id() == people_id) {
                    //将purchaseOrders_supplier添加到purchaseOrders中
                    purchaseOrders_supplier.add(purchaseOrder);
                }
            }

            //提取库存信息的数据
            ObservableList<PurchaseOrderProperty> purchaseorderitem = purchase.getItems();
            //清空表格
            purchaseorderitem.clear();

            //创建ArrayList<PurchaseOrderProperty> purchaseorderitem_supplier，用于存放库存信息的数据
            ArrayList<PurchaseOrderProperty> PurchaseOrderItem_supplier = new ArrayList<>();
            //将purchaseOrders_supplier中的数据添加到purchaseorderitem_supplier中
            for (PurchaseOrder purchaseOrder : purchaseOrders_supplier) {
                PurchaseOrderItem_supplier.add(new PurchaseOrderProperty(purchaseOrder.getId(), purchaseOrder.getGoods_id(), purchaseOrder.getCount(), purchaseOrder.getTotal_price(), purchaseOrder.getSupplier_id()));
            }
            //创建一个ObservableList<PurchaseOrderProperty> data，用于存放库存信息的数据
            ObservableList<PurchaseOrderProperty> data = FXCollections.observableArrayList(PurchaseOrderItem_supplier);
            //将新的ObservableList赋值给表格的items属性
            purchase.setItems(data);
            //purchase.getColumns().addAll(saleId, saleGoodId, saleCount, saleTotalPrice, saleCustomerId);
        }

        //若查询结果为顾客，则调用DBControl的getAllSaleOrders函数获取用户所有的入货订单
        if (UserError.getText().equals("查询结果：" + people_name + "为顾客")){
            //调用DBControl的getUserId函数获取用户id
            int people_id = DBControl.getUserId(people_name);
            //调用DBControl的getAllSaleOrders函数获取用户所有的入货订单
            ArrayList<SaleOrder> saleOrders_customer = new ArrayList<>();
            //遍历saleOrders_customer的customer_id属性，判断customer_id与people_id相等
            for (SaleOrder saleOrder : saleOrders) {
                if (saleOrder.getCustomer_id() == people_id) {
                    //将saleOrders_customer添加到saleOrders中
                    saleOrders_customer.add(saleOrder);
                }
            }
            //提取库存信息的数据
            ObservableList<SaleOrderProperty> saleorderitem = sale.getItems();
            //清空表格
            saleorderitem.clear();

            //创建ArrayList<SaleOrderProperty> saleorderitem_customer，用于存放库存信息的数据
            ArrayList<SaleOrderProperty> SaleOrderItem_customer = new ArrayList<>();
            //将saleOrders_customer中的数据添加到saleorderitem_customer中
            for (SaleOrder saleOrder : saleOrders_customer) {
                SaleOrderItem_customer.add(new SaleOrderProperty(saleOrder.getId(), saleOrder.getGoods_id(), saleOrder.getCount(), saleOrder.getTotal_price(), saleOrder.getCustomer_id()));
            }
            //创建一个ObservableList<SaleOrderProperty> data，用于存放库存信息的数据
            ObservableList<SaleOrderProperty> data = FXCollections.observableArrayList(SaleOrderItem_customer);
            //将新的ObservableList赋值给表格的items属性
            sale.setItems(data);
            //sale.getColumns().addAll(saleId, saleGoodId, saleCount, saleTotalPrice, saleCustomerId);
        }

    }//输入用户名查找对应的购买/库存信息

    public void peopleSearchBack(ActionEvent actionEvent) {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) peopleSearchBack.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new MainPage().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }//返回至主界面

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化界面
        initialization();

        //按钮绑定事件
        peopleSearch.setOnAction(event -> {
            try {
                peopleSearch(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //按钮绑定事件
        peopleSearchBack.setOnAction(this::peopleSearchBack);
    }
}
