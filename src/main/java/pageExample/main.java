package pageExample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(parent,600,400);
        stage.setScene(scene);
        stage.setTitle("老师打个95分吧！");
        stage.show();
    }

}