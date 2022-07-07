package pageExample;


import Model_DB.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class MainPage implements Initializable {
    public DatabaseController DBControl;
    public String titleChoice;
    public Button userManage;
    public  Button account_add;
    public  Button account_delete;
    public ChoiceBox<String> choiceBox;
    public Button product_update;
    public TableColumn GoodsID;
    public TableColumn GoodsName;
    public TableColumn GoodsSalePrice;
    public TableColumn GoodsPurchasePrice;
    public TableColumn GoodsCount;
    public TableColumn GoodsType;
    public TableColumn saleId;
    public TableColumn saleDate;
    public TableColumn saleGoodId;
    public TableColumn saleCount;
    public TableColumn saleTotalPrice;
    public TableColumn saleCustomerId;
    public TableColumn InPurchaseId;
    public TableColumn InPurchaseDate;
    public TableColumn InGoodsID;
    public TableColumn InGoodsCount;
    public TableColumn InGoodsTotalPrice;
    public TableColumn InSupplierId;
    //库存表单
    public TableView<GoodsProperty> AllGoods;
    //入货表单
    public TableView<PurchaseOrderProperty> purchase;
    //出货表单
    public TableView<SaleOrderProperty> sale;
    public TabPane TabChoice;
    public Tab tab1;
    public Tab tab2;
    public Tab tab3;
    @FXML
    private Button chooseStatistic;
    @FXML
    private String man_statistic;
    @FXML
    private String time_statistic;
    @FXML
    private String type_statistic;

    //导入fxml文件
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_page.fxml"));
        primaryStage.setTitle("进销存系统桌面");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();
    }

    //初始化fxml文件
    public static void main(String[] args) {
        launch(args);
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
        //库存表单
        GoodsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        GoodsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        GoodsSalePrice.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        GoodsPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchase_price"));
        GoodsCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        GoodsType.setCellValueFactory(new PropertyValueFactory<>("type"));
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

    //向出货表单添加数据
    public void InsertSaleOrder() throws Exception{
        try {
            //调用getAllSaleOrders方法获取所有出货订单
            ArrayList<SaleOrder> saleorder = (ArrayList<SaleOrder>) DBControl.getAllSaleOrders().get("result");
            //提取saleorder中的(id,goods_id,count,total_price,customer_id)信息，并将其存入ArrayList<SaleOrder>中
            ArrayList<SaleOrder> saleorder_list = new ArrayList<>();
            for (int i = 0; i < saleorder.size(); i++) {
                SaleOrder saleorder_temp = new SaleOrder(
                        saleorder.get(i).getId(),
                        null,
                        saleorder.get(i).getGoods_id(),
                        saleorder.get(i).getCount(),
                        saleorder.get(i).getTotal_price(),
                        saleorder.get(i).getCustomer_id());
                saleorder_list.add(saleorder_temp);
            }

            //提取出货订单的数据
            ObservableList<SaleOrderProperty> saleorderList = sale.getItems();
            //清空表格
            saleorderList.clear();

            //创建一个新的ArrayList<SaleOrderProperty>
            ArrayList<SaleOrderProperty> saleorderListProperty = new ArrayList<>(saleorder_list.size());
            //将saleorder_list中的数据，存入saleorderListProperty中
            for (SaleOrder saleOrder:saleorder_list) {
                saleorderListProperty.add(
                        new SaleOrderProperty(
                                saleOrder.getId(),
                                saleOrder.getGoods_id(),
                                saleOrder.getCount(),
                                saleOrder.getTotal_price(),
                                saleOrder.getCustomer_id()
                        )
                );
            }
            //创建ObservableList<SaleOrderProperty>对象，将saleorderListProperty添加到ObservableList<SaleOrderProperty>对象中
            ObservableList<SaleOrderProperty> data = FXCollections.observableArrayList(saleorderListProperty);
            //在Sales表单中显示数据
            sale.setItems(data);
            //sale.getColumns().addAll(saleId,saleGoodId,saleCount,saleTotalPrice,saleCustomerId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向入货表单添加数据
    public void InsertPurchaseOrder() throws Exception{
        try {
            //调用getAllPurchaseOrders方法获取所有入货订单
            ArrayList<PurchaseOrder> purchaseorder = (ArrayList<PurchaseOrder>) DBControl.getAllPurchaseOrders().get("result");
            //提取purchaseorder中的(id,goods_id,count,total_price,supplier_id)信息，并将其存入ArrayList<PurchaseOrder>中
            ArrayList<PurchaseOrder> purchaseorder_list = new ArrayList<>();
            for (PurchaseOrder order : purchaseorder) {
                PurchaseOrder purchaseorder_temp = new PurchaseOrder(
                        order.getId(),
                        null,
                        order.getGoods_id(),
                        order.getCount(),
                        order.getTotal_price(),
                        order.getSupplier_id()
                );
                purchaseorder_list.add(purchaseorder_temp);
            }

            //提取入货订单的数据
            ObservableList<PurchaseOrderProperty> purchaseorderList = purchase.getItems();
            //清空表格
            purchaseorderList.clear();

            //创建一个新的ObservableList<PurchaseOrderProperty>
            ArrayList<PurchaseOrderProperty> purchaseorderListProperty = new ArrayList<>(purchaseorder_list.size());
            //将purchaseorder_list中的数据，存入purchaseorderListProperty中
            for (PurchaseOrder purchaseOrder:purchaseorder_list) {
                purchaseorderListProperty.add(new PurchaseOrderProperty(
                        purchaseOrder.getId(),
                        purchaseOrder.getGoods_id(),
                        purchaseOrder.getCount(),
                        purchaseOrder.getTotal_price(),
                        purchaseOrder.getSupplier_id()));
            }
            //创建ObservableList<PurchaseOrderProperty>对象，将purchaseorderListProperty添加到ObservableList<PurchaseOrderProperty>对象中
            ObservableList<PurchaseOrderProperty> data = FXCollections.observableArrayList(purchaseorderListProperty);
            //在Purchase表单中显示数据
            purchase.setItems(data);
            //purchase.getColumns().addAll(InPurchaseId,InGoodsID,InGoodsCount,InGoodsTotalPrice,InSupplierId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向库存表单添加数据
    public void InsertAllGoods() throws Exception{
        try {
            //调用getAllGoods方法获取所有库存信息
            ArrayList<Goods> goods = (ArrayList<Goods>) DBControl.getAllGoods().get("result");
            //提取goods中的(id,name,price,count)信息，并将其存入ArrayList<Goods>中
            ArrayList<Goods> goods_list = new ArrayList<>();
            for (Goods value : goods) {
                Goods goods_temp = new Goods(
                        value.getId(),
                        value.getName(),
                        value.getPurchase_price(),
                        value.getSelling_price(),
                        value.getCount(),
                        value.getType()
                );
                goods_list.add(goods_temp);
            }

            //提取库存信息的数据
            ObservableList<GoodsProperty> goodsList = AllGoods.getItems();
            //清空表格
            goodsList.clear();

            //创建一个新的ObservableList<GoodsProperty>
            ArrayList<GoodsProperty> goodsListProperty = new ArrayList<>(goods_list.size());
            //将goods_list中的数据，存入goodsListProperty中
            for (Goods good:goods_list) {
                goodsListProperty.add(new GoodsProperty(good.getId(),good.getName(),good.getPurchase_price(),good.getSelling_price(),good.getCount(),good.getType()));
            }
            //创建ObservableList<GoodsProperty>对象，将goodsListProperty添加到ObservableList<GoodsProperty>对象中
            ObservableList<GoodsProperty> data = FXCollections.observableArrayList(goodsListProperty);
            //在AllGoods表单中显示数据
            AllGoods.setItems(data);
            //AllGoods.getColumns().addAll(GoodsID,GoodsName,GoodsPurchasePrice,GoodsSalePrice,GoodsCount,GoodsType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*----------------------------------------查询表单按钮----------------------------------------*/

    //跳转至查询界面
    public void SearchButton() {
        String value = choiceBox.getValue();
        System.out.println(value);
        if (Objects.equals(value, "time")) {
            System.out.println("connect to chooseStatistic,time");
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) chooseStatistic.getScene().getWindow();
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
        else if (Objects.equals(value, "type")) {
            Platform.runLater(() -> {
                System.out.println("connect to chooseStatistic,type");
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) chooseStatistic.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载注册窗口
                try {
                    new TypeStatistic().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (Objects.equals(value, "people")) {
            System.out.println("connect to chooseStatistic,man");
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) chooseStatistic.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载注册窗口
                try {
                    new ManStatistic().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /*----------------------------------------TabPane监听函数----------------------------------------*/

    //设置tab1的onSelectionChanged监听器
    public void ChoiceTab1(Event MouseEvent) {
        //获取当前选中的Tab
        Tab tab = TabChoice.getSelectionModel().getSelectedItem();
        //获取当前选中的Tab的标题
        String title = tab.getText();
        System.out.println(title);
        //如果选中的是出货Tab
        titleChoice = title;
    }

    //设置tab2的onSelectionChanged监听器
    public void ChoiceTab2(Event event) {
        //获取当前选中的Tab
        Tab tab = TabChoice.getSelectionModel().getSelectedItem();
        //获取当前选中的Tab的标题
        String title = tab.getText();
        System.out.println(title);
        //如果选中的是出货Tab
        titleChoice = title;
    }

    //设置tab3的onSelectionChanged监听器
    public void ChoiceTab3(Event event) {
        //获取当前选中的Tab
        Tab tab = TabChoice.getSelectionModel().getSelectedItem();
        //获取当前选中的Tab的标题
        String title = tab.getText();
        System.out.println(title);
        //如果选中的是出货Tab
        titleChoice = title;
    }

    /*----------------------------------------添加表单按钮----------------------------------------*/

    //根据titleChoice的选择跳转至出货，入货和库存界面的增加商品界面
    public void AccountAdd(ActionEvent event) throws Exception{
        String value = titleChoice;
        if (Objects.equals(value, "出货单")) {
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) account_add.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载AddSaleProduct窗口
                try {
                    new AddSaleProduct().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (Objects.equals(value, "入货单")) {
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) account_add.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载AddPurchaseProduct窗口
                try {
                    new AddPurchaseProduct().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (Objects.equals(value, "库存表")) {
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) account_add.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载AddGoodsProduct窗口
                try {
                    new AddGoodsProduct().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /*----------------------------------------删除表单按钮----------------------------------------*/

    //根据titleChoice的选择跳转至出货，入货和库存界面的删除商品界面
    public void AccountDelete(ActionEvent event) throws Exception{
        String value = titleChoice;
        if (Objects.equals(value, "出货单")) {
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) account_delete.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载DeleteSaleProduct窗口
                try {
                    new DeleteSaleProduct().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (Objects.equals(value, "入货单")) {
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) account_delete.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载DeletePurchaseProduct窗口
                try {
                    new DeletePurchaseProduct().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        else if (Objects.equals(value, "库存表")) {
            Platform.runLater(() -> {
                //获取按钮所在的窗口
                Stage primaryStage = (Stage) account_delete.getScene().getWindow();
                //当前窗口隐藏
                primaryStage.hide();
                //加载DeleteGoodsProduct窗口
                try {
                    new DeleteGoodsProduct().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /*----------------------------------------修改表单按钮----------------------------------------*/
    //跳转至修改商品界面
    public void productUpdate(ActionEvent actionEvent) {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage)product_update.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new ProductUpdate().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /*----------------------------------------用户管理按钮----------------------------------------*/
    //跳转至用户管理界面
    public void UserManage(){
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) userManage.getScene().getWindow();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化商品类型下拉框
        initialization();
        //出货表单初始化
        try {
            InsertSaleOrder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //入货表单初始化
        try {
            InsertPurchaseOrder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //库存表单初始化
        try {
            InsertAllGoods();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //设置account_add按钮的监听器
        account_add.setOnAction(event -> {
            try {
                AccountAdd(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //设置product_update按钮的监听器
        product_update.setOnAction(event -> {
            try {
                productUpdate(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //设置account_delete按钮的监听器
        account_delete.setOnAction(event -> {
            try {
                AccountDelete(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

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

        //调用Current_User的getPrivilege方法获取当前用户的权限
        String privilege = Current_User.getPrivilege();
        //如果权限为customer或者supplier,则将tab3和userManage按钮禁用
        if (Objects.equals(privilege, "customer") || Objects.equals(privilege, "supplier")) {
            tab3.setDisable(true);
            userManage.setDisable(true);
        }

    }

}