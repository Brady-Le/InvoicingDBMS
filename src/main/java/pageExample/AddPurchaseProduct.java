package pageExample;

import Model_DB.DatabaseController;
import Model_DB.DatabaseRoot;
import Model_DB.Goods;
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
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class AddPurchaseProduct implements Initializable {
    public DatabaseController DBControl;
    public Button product_save;
    public TextField customerProduct;
    public TextField supplierID;
    public TextField customerAmount;
    public TextField customerPrice;
    public Label customerProductError;
    public Label supplierIDError;
    public Label customerAmountError;
    public Label customerPriceError;
    public Label AddDBMessage;
    public Label AddMessage;

    public void start(Stage primaryStage) throws Exception {
        //编辑商品界面,导入fxml文件
        Parent root = FXMLLoader.load(getClass().getResource("AddPurchaseProduct.fxml"));
        primaryStage.setTitle("添加商品");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) product_save.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new AddPurchaseProduct().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void productSaveBack(ActionEvent actionEvent) throws Exception {
        //获取输入的商品名称
        String product = customerProduct.getText();
        //获取输入的供应商编号
        String supplierid = supplierID.getText();
        //获取输入的商品数量
        String amount = customerAmount.getText();
        //获取输入的商品价格
        String price = customerPrice.getText();
        //打印输入的商品名称，类别，数量，价格
        System.out.println(product + " " + supplierid + " " + amount + " " + price);

        //判断商品名称是否为空
        if (product.isEmpty()) {
            customerProductError.setText("商品名称不能为空");
        } else {
            //调用DBControl的getAllGoods方法，判断商品名称是否存在
            if (DBControl.getAllGoods().containsKey(product)) {
                customerProductError.setText("商品名称已存在");
            }
            else {
                customerProductError.setText("");
            }
        }
        //判断供应商编号是否为空
        if (supplierid.isEmpty()) {
            supplierIDError.setText("供应商编号不能为空");
        } else {
            //调用DBControl的getAllPurchaseOrders方法，判断supplier_id是否存在
            if (DBControl.getAllPurchaseOrders().containsKey(supplierid)) {
                supplierIDError.setText("供应商编号已存在");
            } else {
                supplierIDError.setText("");
            }
        }
        //判断商品数量是否为空
        if (amount.isEmpty()) {
            customerAmountError.setText("商品数量不能为空");
        } else {
            if (!amount.matches("[0-9]+")) {
                customerAmountError.setText("商品数量必须为数字");
            } else {
                customerAmountError.setText("");
            }
        }
        //判断商品价格是否为空
        if (price.isEmpty()) {
            customerPriceError.setText("商品价格不能为空");
        } else {
            if (!price.matches("[0-9]+")) {
                customerPriceError.setText("商品价格必须为数字");
            } else {
                customerPriceError.setText("");
            }
        }

        //调用DBControl的getGoodsByName方法，获取product对应的result对象
        Goods goods = (Goods) DBControl.getGoodsByName(product).get("result");
        //获取goods中的id数据
        int goodsid = goods.getId();

        //判断商品名称是否为空，类别是否为空，数量是否为空，价格是否为空
        if (!product.isEmpty() && !supplierid.isEmpty() && !amount.isEmpty() && !price.isEmpty() && amount.matches("[0-9]+") && price.matches("[0-9]+")) {
            //调用DBControl的newPurchaseOrder方法，返回int类型的result
            int result = DBControl.newPurchaseOrder(goodsid, Integer.parseInt(amount), Float.parseFloat(price), Integer.parseInt(supplierid));
            if (result == 0) {
                //将添加成功信息返回AddMessage的Label控件中
                AddMessage.setText("添加成功");
                //保存修改并返回至主界面
                Platform.runLater(() -> {
                    //获取按钮所在的窗口
                    Stage primaryStage = (Stage) product_save.getScene().getWindow();
                    //当前窗口隐藏
                    primaryStage.hide();
                    //加载主页面窗口
                    try {
                        new MainPage().start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                //将添加失败信息返回AddMessage的Label控件中
                AddMessage.setText("添加失败");
                System.out.println("添加失败");
                //线程休眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重新加载添加界面
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

        //初始化productSaveBack按钮的监听器
        product_save.setOnAction(actionEvent -> {
            try {
                productSaveBack(actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
