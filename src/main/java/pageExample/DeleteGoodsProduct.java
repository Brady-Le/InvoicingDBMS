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

public class DeleteGoodsProduct implements Initializable {
    public DatabaseController DBControl;
    public TextField GoodsId;
    public Label GoodsIdError;
    public Button product_save;
    public Label AddDBMessage;
    public Label AddMessage;

    public void start(Stage primaryStage) throws Exception {
        //编辑商品界面,导入fxml文件
        Parent root = FXMLLoader.load(getClass().getResource("DeleteGoodsProduct.fxml"));
        primaryStage.setTitle("删除商品");
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
                new DeleteGoodsProduct().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void productSaveBack(ActionEvent event) {
        //获取输入的商品编号
        String GoodsId = this.GoodsId.getText();
        //调用DBControl的getAllGoods方法获取goodsList
        ArrayList<Goods> goodsList = (ArrayList<Goods>) DBControl.getAllGoods().get("result");
        //遍历goodsList，调用getid方法获取商品编号，并定义为ArrayList<Integer>
        ArrayList<Integer> GoodsIdList = new ArrayList<Integer>();
        for (Goods goods : goodsList) {
            GoodsIdList.add(goods.getId());
        }
        //判断输入的商品编号是否存在于GoodsIdList中，且不能为空
        if (GoodsId.equals("")){
            GoodsIdError.setText("商品编号不能为空");
        }else {
            if (GoodsIdList.contains(Integer.parseInt(GoodsId))) {
                //设置提示信息
                GoodsIdError.setText("商品编号存在");
                //设置提示信息的颜色
                GoodsIdError.setStyle("-fx-text-fill: green");
                System.out.println("商品编号存在");
            } else {
                //设置提示信息
                GoodsIdError.setText("商品编号不存在");
                //设置提示信息的颜色
                GoodsIdError.setStyle("-fx-text-fill: red");
                System.out.println("商品编号不存在");
            }
        }


        //调用DBControl的deleteGoods方法删除商品订单
        int result = DBControl.deleteGoods(GoodsId);
        //判断删除结果
        if (result == 0) {
            //设置提示信息
            AddMessage.setText("删除成功");
            //设置提示信息的颜色
            AddMessage.setStyle("-fx-text-fill: green");
            System.out.println("删除成功");
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
        } else {
            //设置提示信息
            AddMessage.setText("删除失败");
            //设置提示信息的颜色
            AddMessage.setStyle("-fx-text-fill: red");
            System.out.println("删除失败");
            //线程睡眠1秒
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
