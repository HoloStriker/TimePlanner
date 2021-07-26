package timeplanner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Utility {
    public static int USER_ID = 1;
    public static boolean ADMIN_RIGHTS = false;
    public static int SELECTEDTRACKINGLOG = -1;

    private Utility() {}

    public static void switchScene(URL urlToFXML, Node anchorNode, boolean resizable) throws IOException {
        Stage stageTheNodeBelongs = (Stage) anchorNode.getScene().getWindow();

        // Swap screen

        FXMLLoader fxmlLoader = new FXMLLoader(urlToFXML);
        Parent root = fxmlLoader.load();

        Scene startScene = new Scene(root);
        stageTheNodeBelongs.setScene(startScene);
        stageTheNodeBelongs.setResizable(resizable);
    }
}
