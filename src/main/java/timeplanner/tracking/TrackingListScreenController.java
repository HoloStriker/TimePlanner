package timeplanner.tracking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import timeplanner.Utility;
import timeplanner.database.DBConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackingListScreenController {
    @FXML
    private ListView lv_Trackings;

    private Map<String, Integer> trackings;

    private ObservableList<String> observableArray;

    public TrackingListScreenController() {
    }

    @FXML
    private void initialize() {
        trackings = DBConnector.getInstance().getTrackings(Utility.USER_ID);
        List<String> trackingTitles = new ArrayList<>(trackings.keySet());
        observableArray = FXCollections.observableArrayList(trackingTitles);
        lv_Trackings.setItems(observableArray);

        lv_Trackings.setCellFactory(lv -> new ListCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setOnMouseClicked(mouseClickedEvent -> {
                        if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 2) {
                            openDetailsForTrackingLog(item);
                        }
                    });
                }
            }
        });
    }

    private void openDetailsForTrackingLog(String item) {
        Utility.SELECTEDTRACKINGLOG = trackings.get(item);

        Stage stage = (Stage) lv_Trackings.getScene().getWindow();
        Scene scene = stage.getScene();

        TabPane tp_Root = (TabPane) scene.lookup("#tp_Root");
        SingleSelectionModel<Tab> selectionModel = tp_Root.getSelectionModel();
        selectionModel.select(0);
    }

    public void refreshList() {
        trackings = DBConnector.getInstance().getTrackings(Utility.USER_ID);
        List<String> trackingTitles = new ArrayList<>(trackings.keySet());
        observableArray.clear();
        observableArray.addAll(trackingTitles);
    }
}
