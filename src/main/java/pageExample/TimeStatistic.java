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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class TimeStatistic implements Initializable {
    public DatabaseController DBControl;
    public Label TimeError;
    public TableView<SaleOrderProperty> sale;
    public TableColumn saleId;
    public TableColumn saleGoodId;
    public TableColumn saleCount;
    public TableColumn saleTotalPrice;
    public TableColumn saleCustomerId;
    public TableView<PurchaseOrderProperty> purchase;
    public TableColumn InPurchaseId;
    public TableColumn InGoodsID;
    public TableColumn InGoodsCount;
    public TableColumn InSupplierId;
    public TableColumn InGoodsTotalPrice;
    @FXML
    private TextField endDay;

    @FXML
    private TextField endMonth;

    @FXML
    private TextField endYear;

    @FXML
    private TextField startDay;

    @FXML
    private TextField startMonth;

    @FXML
    private TextField startYear;

    @FXML
    private Button timeSearch;
    @FXML
    private Button timeSearchBack;

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("time_statistic.fxml"));
        primaryStage.setTitle("入货/出货时间统计查询");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();

    }//根据时间查询信息界面
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) timeSearch.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new TimeStatistic().start(primaryStage);
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


    //根据开始时间和结束时间筛选信息
    @FXML
    public void timeSearchButtonClick(ActionEvent event) throws Exception {
        //获取startYear,startMonth,startDay,拼成字符串
        String startTime = startYear.getText() + "-" + startMonth.getText() + "-" + startDay.getText();
        //获取endYear,endMonth,endDay,拼成字符串
        String endTime = endYear.getText() + "-" + endMonth.getText() + "-" + endDay.getText();

        //调用DBControl的getAllSaleOrders方法,获取所有出货信息
        ArrayList<SaleOrder> saleOrders = (ArrayList<SaleOrder>) DBControl.getAllSaleOrders().get("result");
        //调用DBControl的getAllPurchaseOrders方法,获取所有入货信息
        ArrayList<PurchaseOrder> purchaseOrders = (ArrayList<PurchaseOrder>) DBControl.getAllPurchaseOrders().get("result");

        //创建SimpleDateFormat对象，在SimpleDateFormat(String pattern)构造方法中传入指定的模式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //检查时间是否合法
        if (startTime.compareTo(endTime) > 0) {
            TimeError.setText("开始时间不能大于结束时间！");
            TimeError.setStyle("-fx-text-fill: red");
        }
        else {
            TimeError.setText("输入时间合法！");
            TimeError.setStyle("-fx-text-fill: green");
        }

        //若时间合法，则清空表格中的数据
        if (TimeError.getText().equals("输入时间合法！")) {
            //提取库存信息的数据
            ObservableList<PurchaseOrderProperty> purchaseorderitem = purchase.getItems();
            //清空表格
            purchaseorderitem.clear();
            //创建ArrayList<PurchaseOrderProperty> purchaseorderitem_supplier，用于存放库存信息的数据
            ArrayList<PurchaseOrderProperty> PurchaseOrderItem_supplier = new ArrayList<>();

            //提取库存信息的数据
            ObservableList<SaleOrderProperty> saleorderitem = sale.getItems();
            //清空表格
            saleorderitem.clear();
            //创建ArrayList<SaleOrderProperty> saleorderitem_customer，用于存放库存信息的数据
            ArrayList<SaleOrderProperty> SaleOrderItem_customer = new ArrayList<>();


            //遍历saleOrders和purchaseOrders，获取每个订单的时间
            for (SaleOrder saleOrder : saleOrders) {
                //获取订单的时间
                String saleTime = sdf.format(saleOrder.getDate());
                //判断订单的时间是否在开始时间和结束时间之间
                if (saleTime.compareTo(startTime) >= 0 && saleTime.compareTo(endTime) <= 0) {
                    //将订单添加到SaleOrderItem_customer中
                    SaleOrderItem_customer.add(new SaleOrderProperty(
                            saleOrder.getId(),
                            saleOrder.getGoods_id(),
                            saleOrder.getCount(),
                            saleOrder.getTotal_price(),
                            saleOrder.getCustomer_id()));
                }
            }
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                //获取订单的时间
                String purchaseTime = sdf.format(purchaseOrder.getDate());
                //判断订单的时间是否在开始时间和结束时间之间
                if (purchaseTime.compareTo(startTime) >= 0 && purchaseTime.compareTo(endTime) <= 0) {
                    //将订单添加到PurchaseOrderItem_supplier中
                    PurchaseOrderItem_supplier.add(new PurchaseOrderProperty(
                            purchaseOrder.getId(),
                            purchaseOrder.getGoods_id(),
                            purchaseOrder.getCount(),
                            purchaseOrder.getTotal_price(),
                            purchaseOrder.getSupplier_id()));
                }
            }

            //创建一个ObservableList<PurchaseOrderProperty> data，用于存放库存信息的数据
            ObservableList<PurchaseOrderProperty> data = FXCollections.observableArrayList(PurchaseOrderItem_supplier);
            //将新的ObservableList赋值给表格的items属性
            purchase.setItems(data);

            //创建一个ObservableList<SaleOrderProperty> data，用于存放库存信息的数据
            ObservableList<SaleOrderProperty> data1 = FXCollections.observableArrayList(SaleOrderItem_customer);
            //将新的ObservableList赋值给表格的items属性
            sale.setItems(data1);
        }
    }
    public void SearchBack(){
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) timeSearchBack.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载主页面窗口
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
        //初始化时间查询按钮
        timeSearch.setOnAction(event -> {
            try {
                timeSearchButtonClick(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //初始化返回按钮
        timeSearchBack.setOnAction(event -> {
            SearchBack();
        });

    }
}
