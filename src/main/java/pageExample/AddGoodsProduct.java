package pageExample;

import Model_DB.DatabaseController;
import Model_DB.DatabaseRoot;
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

public class AddGoodsProduct implements Initializable {
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
    public TextField customerSaleProduct;
    public Label customerSaleProductError;
    public Label AddDBMessage;
    public Label AddMessage;

    public void start(Stage primaryStage) throws Exception {
        //编辑商品界面,导入fxml文件
        Parent root = FXMLLoader.load(getClass().getResource("AddGoodsProduct.fxml"));
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
                new AddGoodsProduct().start(primaryStage);
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
        //获取输入的商品进货价格
        String price = customerPrice.getText();
        //获取输入的商品售价
        String saleProductPrice = customerSaleProduct.getText();

        //打印输入的商品名称，类别，数量，价格
        System.out.println(product + " " + category + " " + amount + " " + price + " " + saleProductPrice);

        //判断商品名称是否为空
        if (product.isEmpty()) {
            customerProductError.setText("商品名称不能为空");
        } else {
            customerProductError.setText("");
            //调用DBControl的getAllGoods方法，判断商品名称是否存在
            if (DBControl.getAllGoods().containsKey(product)) {
                customerProductError.setText("商品名称已存在");
            }
            else {
                customerProductError.setText("");
            }
        }
        //判断商品类别是否为空
        if (category.isEmpty()) {
            customerCategoryError.setText("商品类别不能为空");
        } else {
            //判断商品类型是否为全英文或全中文
            if (category.matches("[a-zA-Z]+") || category.matches("[\\u4e00-\\u9fa5]+")) {
                customerCategoryError.setText("");
            } else {
                customerCategoryError.setText("商品类别只能为英文或中文");
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
            customerAmountError.setText("");
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
            customerPriceError.setText("");
        }
        //判断商品售价是否为空
        if (saleProductPrice.isEmpty()) {
            customerSaleProductError.setText("商品售价不能为空");
        } else {
            if (!saleProductPrice.matches("[0-9]+")) {
                customerSaleProductError.setText("商品售价必须为数字");
            } else {
                customerSaleProductError.setText("");
            }
            customerSaleProductError.setText("");
        }

        //判断商品名称是否为空，类别是否为空，数量是否为空，商品进货价格和商品售价是否为空
        if (!product.isEmpty() && !category.isEmpty() && !amount.isEmpty() && !price.isEmpty() && amount.matches("[0-9]+") && price.matches("[0-9]+") && saleProductPrice.matches("[0-9]+")) {
            //调用DBControl的addGoods方法，添加商品
            int result = DBControl.addGoods(product, Float.parseFloat(price), Float.parseFloat(saleProductPrice), Integer.parseInt(amount), category);
            //判断添加商品是否成功
            if (result == 0){
                //将添加成功信息返回AddMessage的Label控件中
                AddMessage.setText("添加商品成功");
                //保存修改并返回至主界面
                Platform.runLater(()->{
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
            }else if (result == 1){
                //将添加失败信息返回AddMessage的Label控件中
                AddMessage.setText("添加商品失败");
                System.out.println("添加商品失败");
                //线程休眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重新加载增加商品页面
                reload();
            }else if (result == 4){
                //将该商品已存在数据库信息返回AddMessage的Label控件中
                AddMessage.setText("该商品已存在数据库");
                System.out.println("该商品已存在数据库");
                //线程休眠1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重新加载增加商品页面
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
