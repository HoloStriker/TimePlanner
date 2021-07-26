package timeplanner.projects;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import timeplanner.Utility;
import timeplanner.database.DBConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectsListScreenController {
    @FXML
    private ListView lv_Projects;

    private Map<String, Integer> projects;

    public ProjectsListScreenController () {}

    @FXML
    private void initialize() {
        projects = DBConnector.getInstance().getProjects(Utility.USER_ID);
        List<String> projectNames = new ArrayList<>(projects.keySet());
        lv_Projects.setItems(FXCollections.observableArrayList(projectNames));
    }
}
