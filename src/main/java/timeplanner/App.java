package timeplanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import timeplanner.database.DBConnector;
import timeplanner.database.DataSourceFactory;

public class App extends Application {
    public static void main(String[] args){
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainFrameScreen.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("TimePlanner");

        primaryStage.setOnCloseRequest(e -> {
            DBConnector.getInstance().shutDown();
            Platform.exit();
            System.exit(0);
        });

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        DataSourceFactory.getMySQLDataSource();
    }
}
