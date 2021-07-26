package timeplanner.database;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.Resources;
import timeplanner.Utility;
import timeplanner.model.TrackingLog;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class DBConnector {

    private static final DBConnector mInstance = new DBConnector();

    private boolean initialized = false;
    private DataSource dataSource = null;
    private Connection con = null;

    private DBConnector() {
        initialized = false;
    }

    public static DBConnector getInstance() {
        if (!mInstance.isInitialized()) {
            mInstance.initialize();
            mInstance.setInitialized(true);
        }
        return mInstance;
    }

    public TrackingLog loadTrackingLog(int selectedtrackinglog) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TrackingLog trackingLog = new TrackingLog();

        String sqlString = "SELECT * FROM trackinglogs WHERE trackingid = ?";

        try {
            stmt = con.prepareStatement(sqlString);
            stmt.setInt(1, selectedtrackinglog);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("trackingid");
                Integer projectid = rs.getInt("projectid");
                Integer userid = rs.getInt("userid");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime startTime = rs.getTime("starttime").toLocalTime();
                LocalTime endtime = rs.getTime("endtime").toLocalTime();
                String activities = rs.getString("activities");
                String location = rs.getString("location");
                String comments = rs.getString("comments");

                trackingLog.setId(id);
                trackingLog.setProjectID(projectid);
                trackingLog.setUserID(userid);
                trackingLog.setDate(date);
                trackingLog.setStartTime(startTime);
                trackingLog.setEndTime(endtime);
                trackingLog.setActivities(activities);
                trackingLog.setLocation(location);
                trackingLog.setComments(comments);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return trackingLog;
    }

    public void pushLog(TrackingLog trackingLog) {
        String sqlString = "INSERT INTO trackinglogs (projectid, userid, date, starttime, endtime, activities, location, comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sqlString);

            stmt.setInt(1, trackingLog.getProjectID());
            stmt.setInt(2, trackingLog.getUserID());
            stmt.setDate(3, Date.valueOf(trackingLog.getDate()));
            stmt.setTime(4, Time.valueOf(trackingLog.getStartTime()));
            stmt.setTime(5, Time.valueOf(trackingLog.getEndTime()));
            stmt.setString(6, trackingLog.getActivities());
            stmt.setString(7, trackingLog.getLocation());
            stmt.setString(8, trackingLog.getComments());

            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize() {
        try {
            dataSource = DataSourceFactory.getMySQLDataSource();
            con = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } /*finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void shutDown() {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BiMap<String, Integer> getProjects(int userId) {
        BiMap<String, Integer> projects = HashBiMap.create();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sqlString = loadSelectQuerry();
        try {
            stmt = con.prepareStatement(sqlString);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("projectid");
                String name = rs.getString("projectname");
                projects.put(name, id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return projects;
    }

    private String loadSelectQuerry() {

        try {
            URL url = Resources.getResource("select_tracking_list_query.txt");
            File meineFile = new File(url.toURI());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(meineFile)));
            String line = "";
            String fileInput = "";
            while ((line = br.readLine()) != null) {
                fileInput = fileInput + line;
            }
            return fileInput;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*private String loadSelectQuerry() {
        try {
            URL url = Resources.getResource("select_tracking_list_query.txt");
            String query = Resources.toString(url, StandardCharsets.UTF_8);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }*/

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public Map<String, Integer> getTrackings(int userId) {
        Map<String, Integer> trackings = new HashMap<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sqlString = "SELECT trackinglogs.trackingid, trackinglogs.date, projects.projectname FROM trackinglogs LEFT JOIN projects ON trackinglogs.projectid = projects.projectid WHERE trackinglogs.userid = ?";
        try {
            stmt = con.prepareStatement(sqlString);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("trackingid");
                String projectName = rs.getString("projectname");
                Date date = rs.getDate("date");

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = df.format(date);

                String trackingsLabel = dateString + " - " + projectName;

                trackings.put(trackingsLabel, id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return trackings;
    }

    public void updateLog(TrackingLog trackingLog) {
        String sqlString = "UPDATE  trackinglogs SET projectid = ?, date = ?, starttime = ?, endtime = ?, activities = ?, location = ?, comments = ? WHERE trackingid = ?";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sqlString);

            stmt.setInt(1, trackingLog.getProjectID());
            stmt.setDate(2, Date.valueOf(trackingLog.getDate()));
            stmt.setTime(3, Time.valueOf(trackingLog.getStartTime()));
            stmt.setTime(4, Time.valueOf(trackingLog.getEndTime()));
            stmt.setString(5, trackingLog.getActivities());
            stmt.setString(6, trackingLog.getLocation());
            stmt.setString(7, trackingLog.getComments());
            stmt.setInt(8, trackingLog.getId());

            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
