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

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class ProductEdit implements Initializable {
    public DatabaseController DBControl;
    public Button product_save;
    public TextField customerProduct;
    public TextField customerCategory;
    public TextField customerAmount;
    public TextField customerPrice;
    public Label customerProductError;
    public Label customerCategoryError;
    public Label customerAmountError;
    public Label customerPriceError;
    public Label AddDBMessage;
    public Label AddMessage;
    public TextField SalePrice;
    public Label SalePriceError;

    public void start(Stage primaryStage) throws Exception {
        //编辑商品界面,导入fxml文件
        Parent root = FXMLLoader.load(getClass().getResource("product_edit.fxml"));
        primaryStage.setTitle("添加商品");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload(ActionEvent actionEvent) {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) product_save.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new ProductEdit().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void productSaveBack(ActionEvent actionEvent) throws Exception {
        //获取输入的商品名称
        String product = customerProduct.getText();
        //获取输入的商品类别
        String category = customerCategory.getText();
        //获取输入的商品数量
        String amount = customerAmount.getText();
        //获取输入的商品价格
        String price = customerPrice.getText();
        //获取输入的销售价格
        String salePrice = SalePrice.getText();
        //打印输入的商品名称，类别，数量，价格
        System.out.println(product + " " + category + " " + amount + " " + price);

        //判断商品名称是否为空
        if (product.isEmpty()) {
            customerProductError.setText("商品名称不能为空");
        } else {
            customerProductError.setText("");
        }
        //判断商品类别是否为空
        if (category.isEmpty()) {
            customerCategoryError.setText("商品类别不能为空");
        } else {
            customerCategoryError.setText("");
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
        //判断销售价格是否为空
        if (salePrice.isEmpty()) {
            SalePriceError.setText("销售价格不能为空");
        } else {
            if (!salePrice.matches("[0-9]+")) {
                SalePriceError.setText("销售价格必须为数字");
            } else {
                SalePriceError.setText("");
            }
        }

        //调用DBControl的getAllGoods方法获取所有商品信息，获取对应的商品名称的id
        ArrayList<Goods> goods = (ArrayList<Goods>) DBControl.getAllGoods().get("result");
        int id = 0;
        for (Goods good : goods) {
            if (good.getName().equals(product)) {
                id = good.getId();
            }
        }

        //判断商品名称是否为空，类别是否为空，数量是否为空，价格是否为空
        if (!product.isEmpty() && !category.isEmpty() && !amount.isEmpty() && !price.isEmpty() && amount.matches("[0-9]+") && price.matches("[0-9]+")) {
            //调用DBControl的updateGoods方法，更新商品信息
            int result =  DBControl.updateGoods(id,product, Float.parseFloat(price) , Float.parseFloat(salePrice) ,Integer.parseInt(amount) ,category);

            //判断更新是否成功
            if (result == 0) {
                //更新成功，提示消息
                AddMessage.setText("更新成功");
                //保存修改并返回至主界面
                Platform.runLater(()->{
                    //获取按钮所在的窗口
                    Stage primaryStage = (Stage) product_save.getScene().getWindow();
                    //当前窗口隐藏
                    primaryStage.hide();
                    //加载注册窗口
                    try {
                        new MainPage().start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                //更新失败，提示消息
                AddMessage.setText("更新失败");
                reload(actionEvent);
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
        product_save.setOnAction(actionEvent -> {
            try {
                productSaveBack(actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
