package pageExample;

import Model_DB.DatabaseController;
import Model_DB.DatabaseRoot;
import Model_DB.Goods;
import Model_DB.GoodsProperty;
import javafx.application.Platform;
import javafx.beans.property.Property;
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

public class TypeStatistic implements Initializable {
    public DatabaseController DBControl;
    public TableView<GoodsProperty> Goods;
    public TableColumn GoodsID;
    public TableColumn GoodsName;
    public TableColumn GoodsSalePrice;
    public TableColumn GoodsPurchasePrice;
    public TableColumn GoodsCount;
    public TableColumn GoodsType;
    public Label typeError;
    @FXML
    private TextField typeField;
    @FXML
    private Button typeSearch;
    @FXML
    private Button typeSearchBack;


    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("type_statistic.fxml"));
        primaryStage.setTitle("商品类型统计查询");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();

    }//商品类型检索界面
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) typeSearch.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载类型查询窗口
            try {
                new TypeStatistic().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initialization() {
        //初始化表格导入的列
        //库存表单
        GoodsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        GoodsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        GoodsSalePrice.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        GoodsPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchase_price"));
        GoodsCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        GoodsType.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    public void typeSearchBack(ActionEvent actionEvent) throws Exception {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) typeSearchBack.getScene().getWindow();
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

    public void typeSearch(ActionEvent actionEvent) throws Exception {
        //获取输入的商品类别
        String type = typeField.getText();
        //调用DBControl的getAllGoods方法获取goodsList
        ArrayList<Goods> goodsList = (ArrayList<Goods>) DBControl.getAllGoods().get("result");

        //判断输入是否为空
        if(type.equals("")){
            typeError.setText("输入商品类别为空");
            typeError.setStyle("-fx-text-fill: red");
        }else{
            //判断输入的商品类别是否存在
            boolean flag = false;
            for(Goods goods:goodsList){
                if(goods.getType().equals(type)){
                    flag = true;
                    break;
                }
            }
            //如果不存在，则提示错误信息
            if(!flag){
                typeError.setText("输入商品类别不存在");
                typeError.setStyle("-fx-text-fill: red");
            }else{
                typeError.setText("输入商品类别存在");
                typeError.setStyle("-fx-text-fill: green");
            }
        }

        if (typeError.getText().equals("输入商品类别存在")) {
            //获取商品类别为type的商品
            ArrayList<Goods> typeGoods = new ArrayList<>();
            for (Goods goods : goodsList) {
                if (goods.getType().equals(type)) {
                    typeGoods.add(goods);
                }
            }

            //提取库存信息的数据
            ObservableList<GoodsProperty> goodsitems = Goods.getItems();
            //清空表格
            goodsitems.clear();

            //创建ArrayList<GoodsProperty>用于存储商品信息
            ArrayList<GoodsProperty> goodsPropertyList = new ArrayList<>();
            //将typeGoods中的商品信息提取出来，存储到goodsPropertyList中
            for (Goods goods : typeGoods) {
                GoodsProperty goodsProperty = new GoodsProperty(goods.getId(), goods.getName(), goods.getSelling_price(), goods.getPurchase_price(), goods.getCount(), goods.getType());
                goodsPropertyList.add(goodsProperty);
            }
            //创建一个新的ObservableList，用于存储商品类别为type的商品的库存信息
            ObservableList<GoodsProperty> data = FXCollections.observableArrayList(goodsPropertyList);
            //将新的ObservableList赋值给表格的items属性
            Goods.setItems(data);
            Goods.getColumns().addAll(GoodsID, GoodsName, GoodsSalePrice, GoodsPurchasePrice, GoodsCount, GoodsType);
        }
        else {
            //线程睡眠一秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //重新加载界面
            reload();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        initialization();

        //设置typeSearch的监听器
        typeSearch.setOnAction(event -> {
            try {
                typeSearch(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //设置typeSearchBack的监听器
        typeSearchBack.setOnAction(event -> {
            try {
                typeSearchBack(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
