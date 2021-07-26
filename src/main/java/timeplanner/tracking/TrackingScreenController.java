package timeplanner.tracking;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import timeplanner.Utility;
import timeplanner.database.DBConnector;
import timeplanner.model.TrackingLog;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TrackingScreenController {

    @FXML
    private  Button btn_Confirm;

    @FXML
    private  Button btn_Abort;

    @FXML
    private ChoiceBox cb_Project;

    @FXML
    private DatePicker dp_Date;

    @FXML
    private  TimeSpinner sp_StartTime;

    @FXML
    private  TimeSpinner sp_EndTime;

    @FXML
    private TextArea ta_Activities;

    @FXML
    private  TextField tf_Location;

    @FXML
    private TextArea ta_Comments;

    private DBConnector mDBConnector;

    private BiMap<String, Integer> projects;

    public TrackingScreenController() {

    }

    @FXML
    private void initialize() {
        mDBConnector = DBConnector.getInstance();
        loadProjects();
        dp_Date.setValue(LocalDate.now());
    }

    private void loadProjects() {
        projects = mDBConnector.getProjects(Utility.USER_ID);
        List<String> projectNames = new ArrayList<>(projects.keySet());
        cb_Project.setItems(FXCollections.observableArrayList(projectNames));
    }

    public void onConfirmPressed() {
        if (cb_Project.getSelectionModel().getSelectedItem() != null) {
            String projectName = cb_Project.getValue().toString();
            int projectID = projects.get(projectName);
            LocalDate date = dp_Date.getValue();
            LocalTime startTime = sp_StartTime.getValue();
            LocalTime endTime = sp_EndTime.getValue();
            String activities = ta_Activities.getText();
            String location = tf_Location.getText();
            String comments = ta_Comments.getText();

            startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
            endTime = endTime.truncatedTo(ChronoUnit.MINUTES);
            boolean allCorrect = true;

            long diff = MINUTES.between(startTime, endTime);

            if(endTime.isBefore(startTime) || diff == 0)
                allCorrect = false;

            if(activities.length() == 0)
                allCorrect = false;

            if(allCorrect)
            {
                TrackingLog trackingLog = new TrackingLog(projectID, Utility.USER_ID, date, startTime, endTime, activities, location, comments);
                if(Utility.SELECTEDTRACKINGLOG > -1){
                    trackingLog.setId(Utility.SELECTEDTRACKINGLOG);
                    mDBConnector.updateLog(trackingLog);
                } else {
                    mDBConnector.pushLog(trackingLog);
                }
            }
        }
        clearInputs();
    }

    private void clearInputs() {
        cb_Project.getSelectionModel().clearSelection();
        cb_Project.setValue(null);
        dp_Date.setValue(LocalDate.now());
        sp_StartTime.getValueFactory().setValue(LocalTime.now());
        sp_EndTime.getValueFactory().setValue(LocalTime.now());
        ta_Activities.clear();
        tf_Location.clear();
        ta_Comments.clear();
        Utility.SELECTEDTRACKINGLOG = -1;
    }

    public void onAbortPressed() {
        clearInputs();
    }

    public void gotFocus() {
        if(Utility.SELECTEDTRACKINGLOG > -1){
            loadTrackingLog();
        }
    }

    private void loadTrackingLog() {
        TrackingLog log = DBConnector.getInstance().loadTrackingLog(Utility.SELECTEDTRACKINGLOG);
        cb_Project.setValue(projects.inverse().get(log.getProjectID()));
        dp_Date.setValue(log.getDate());
        sp_StartTime.getValueFactory().setValue(log.getStartTime());
        sp_EndTime.getValueFactory().setValue(log.getEndTime());
        ta_Activities.setText(log.getActivities());
        tf_Location.setText(log.getLocation());
        ta_Comments.setText(log.getComments());
    }
}
