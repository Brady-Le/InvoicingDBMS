package pageExample;

import Model_DB.DatabaseController;
import Model_DB.DatabaseRoot;
import Model_DB.SaleOrder;
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

public class DeleteSaleProduct implements Initializable {
    public DatabaseController DBControl;
    public TextField SaleOrderId;
    public Label SaleOrderIdError;
    public Button product_save;
    public Label AddDBMessage;
    public Label AddMessage;

    public void start(Stage primaryStage) throws Exception {
        //编辑商品界面,导入fxml文件
        Parent root = FXMLLoader.load(getClass().getResource("DeleteSaleProduct.fxml"));
        primaryStage.setTitle("删除出货订单");
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
                new DeleteSaleProduct().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void productSaveBack(ActionEvent event) {
        //获取输入的出货编号
        String SaleOrderId = this.SaleOrderId.getText();
        //调用DBControl的getAllSaleOrders方法获取sellOrderList
        ArrayList<SaleOrder> sellOrderList = (ArrayList<SaleOrder>) DBControl.getAllSaleOrders().get("result");
        //遍历sellOrderList，调用getid方法获取出货编号，并定义为ArrayList<Integer>
        ArrayList<Integer> SaleOrderIdList = new ArrayList<>();
        for (SaleOrder saleOrder : sellOrderList) {
            SaleOrderIdList.add(saleOrder.getId());
        }
        //判断输入的出货编号是否存在于sellOrderIdList中，且不能为空
        if (SaleOrderId.equals("")){
            SaleOrderIdError.setText("出货编号不能为空");
        }else {
            if (SaleOrderIdList.contains(Integer.parseInt(SaleOrderId))) {
                //设置提示信息
                SaleOrderIdError.setText("出货编号存在");
                //设置提示信息的颜色
                SaleOrderIdError.setStyle("-fx-text-fill: green");
                System.out.println("出货编号存在");
            }else {
                //设置提示信息
                SaleOrderIdError.setText("出货编号不存在");
                //设置提示信息的颜色
                SaleOrderIdError.setStyle("-fx-text-fill: red");
                System.out.println("出货编号不存在");
            }
        }


        //调用DBControl的deleteSaleOrder方法删除出货订单
        int result = DBControl.deleteSaleOrder(Integer.parseInt(SaleOrderId));
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
        }else {
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
            //调用reload方法重新加载界面
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
