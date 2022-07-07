package pageExample;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

    public class AlertView {
        private static boolean res;

        public static boolean display(String title, String msg){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Label label = new Label();
            label.setText(msg);

            stage.setTitle(title);
            stage.showAndWait();

            return res;
        }

    }

