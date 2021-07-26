package timeplanner.MainFrame;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import timeplanner.tracking.TrackingListScreenController;
import timeplanner.tracking.TrackingScreenController;

public class MainFrameScreenController {

    @FXML
    private TabPane tp_Root;

    @FXML
    private Tab tab_TrackingScreen;

    @FXML
    private TrackingScreenController tab_TrackingScreenPageController;

    @FXML
    private Tab tab_Trackingslist;

    @FXML
    private TrackingListScreenController tab_TrackingslistPageController;

    public MainFrameScreenController() {

    }

    @FXML
    private void initialize() {
        tab_TrackingScreen.setOnSelectionChanged(e -> {
            if(tab_TrackingScreen.isSelected())
                tab_TrackingScreenPageController.gotFocus();
        });
        tab_Trackingslist.setOnSelectionChanged(e -> {
            if(tab_Trackingslist.isSelected())
                tab_TrackingslistPageController.refreshList();
        });

    }
}
