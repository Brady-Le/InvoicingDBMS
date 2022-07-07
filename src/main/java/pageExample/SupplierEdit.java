package pageExample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class SupplierEdit{
    public Button account_save;

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("supplierEdit.fxml"));
        primaryStage.setTitle("R");
        primaryStage.setScene(new Scene(root, 350, 300));
        primaryStage.show();

    }

    public void AccountSave(ActionEvent actionEvent) {
        Platform.runLater(()->{
            //获取按钮所在的窗口
            Stage primaryStage = (Stage)account_save.getScene().getWindow();
            //当前窗口隐藏
            primaryStage.hide();
            //加载注册窗口
            try {
                new MainPage().start(primaryStage);
            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }

}

