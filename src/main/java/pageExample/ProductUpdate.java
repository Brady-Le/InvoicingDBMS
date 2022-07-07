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
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class ProductUpdate implements Initializable {
    public DatabaseController DBControl;
    public Button product_save;
    public TextField GoodsName;
    public TextField GoodsCategory;
    public TextField GoodsNumber;
    public TextField GoodsInPrice;
    public Label GoodsNameError;
    public Label GoodsCategoryError;
    public Label GoodsNumberError;
    public Label GoodsInPriceError;
    public TextField GoodsOutPrice;
    public Label GoodsOutPriceError;
    public Label AddDBMessage;
    public Label AddMessage;

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("product_update.fxml"));
        primaryStage.setTitle("修改商品信息");
        primaryStage.setScene(new Scene(root, 580, 400));
        primaryStage.show();

    }//修改商品界面
    public static void main(String[] args) {
        launch(args);
    }

    //重新加载界面
    public void reload() throws Exception {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage) product_save.getScene().getWindow();
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

    public void productSaveBack(ActionEvent actionEvent) throws Exception {
        //获取输入的商品名称
        String GoodsName = this.GoodsName.getText();
        //获取输入的商品类别
        String GoodsCategory = this.GoodsCategory.getText();
        //获取输入的商品数量
        String GoodsNumber = this.GoodsNumber.getText();
        //获取输入的商品进价
        String GoodsInPrice = this.GoodsInPrice.getText();
        //获取输入的商品售价
        String GoodsOutPrice = this.GoodsOutPrice.getText();
        //调用DBControl的getAllGoods方法，获取ArrayList<Goods>类型的商品信息
        ArrayList<Goods> goods = (ArrayList<Goods>) DBControl.getAllGoods().get("result");
        //调用getid方法，遍历商品id信息，寻找最大的商品编号
        int MaxNumberId = 0;
        for (Goods good : goods) {
            if (good.getId() > MaxNumberId) {
                MaxNumberId = good.getId();
            }
        }
        int GoodsId = MaxNumberId + 1;

        //判断商品名称是否为空
        if (GoodsName.equals("")) {
            GoodsNameError.setText("商品名称不能为空");
        } else {
            //判断商品名称是否存在
            for (Goods good : goods) {
                if (good.getName().equals(GoodsName)) {
                    //设置提示信息
                    GoodsNameError.setText("商品名存在");
                    //设置提示信息的颜色
                    GoodsNameError.setStyle("-fx-text-fill: green");
                }else {
                    //设置提示信息
                    GoodsNameError.setText("商品名不存在");
                    //设置提示信息的颜色
                    GoodsNameError.setStyle("-fx-text-fill: red");
                }
            }
        }

        //判断商品类别是否为空
        if (GoodsCategory.equals("")) {
            GoodsCategoryError.setText("商品类别不能为空");
        } else {
            //判断商品类别是否重复
            for (Goods good : goods) {
                if (good.getType().equals(GoodsCategory)) {
                    //设置提示信息
                    GoodsCategoryError.setText("商品类别存在");
                    //设置提示信息的颜色
                    GoodsCategoryError.setStyle("-fx-text-fill: green");
                }else {
                    //设置提示信息
                    GoodsCategoryError.setText("商品类别不存在");
                    //设置提示信息的颜色
                    GoodsCategoryError.setStyle("-fx-text-fill: red");
                }
            }
        }

        //判断商品数量是否为空
        if (GoodsNumber.equals("")) {
            GoodsNumberError.setText("商品数量不能为空");
        } else {
            //判断商品数量是否为数字
            if (!GoodsNumber.matches("[0-9]*")) {
                GoodsNumberError.setText("商品数量必须为数字");
            } else {
                GoodsNumberError.setText("");
            }
        }

        //判断商品进价是否为空
        if (GoodsInPrice.equals("")) {
            GoodsInPriceError.setText("商品进价不能为空");
        } else {
            //判断商品进价是否为数字
            if (!GoodsInPrice.matches("[0-9]*")) {
                GoodsInPriceError.setText("商品进价必须为数字");
            } else {
                GoodsInPriceError.setText("");
            }
        }

        //判断商品售价是否为空
        if (GoodsOutPrice.equals("")) {
            GoodsOutPriceError.setText("商品售价不能为空");
        } else {
            //判断商品售价是否为数字
            if (!GoodsOutPrice.matches("[0-9]*")) {
                GoodsOutPriceError.setText("商品售价必须为数字");
            } else {
                GoodsOutPriceError.setText("");
            }
        }

        //判断商品名称是否为空，商品类别是否重复，商品数量是否为数字，商品进价是否为数字，商品售价是否为数字
        if (!GoodsName.equals("") && !GoodsCategory.equals("") && GoodsNumber.matches("[0-9]*") && GoodsInPrice.matches("[0-9]*") && GoodsOutPrice.matches("[0-9]*")) {
            //调用DBControl的updateGoods方法，修改商品信息
            int result = DBControl.updateGoods(GoodsId, GoodsName, Float.parseFloat(GoodsInPrice), Float.parseFloat(GoodsOutPrice), Integer.valueOf(GoodsNumber), GoodsCategory);

            //判断修改是否成功
            if (result == 0) {
                //设置提示信息
                AddMessage.setText("商品修改成功");
                //设置提示信息的颜色
                AddMessage.setStyle("-fx-text-fill: green");
                System.out.println("商品修改成功");
                //保存修改，并返回至主界面
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
                //设置提示信息
                AddMessage.setText("商品修改失败");
                //设置提示信息的颜色
                AddMessage.setStyle("-fx-text-fill: red");
                System.out.println("商品修改失败");
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
        product_save.setOnAction(actionEvent -> {
            try {
                productSaveBack(actionEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
