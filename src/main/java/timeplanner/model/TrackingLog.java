package timeplanner.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TrackingLog {
    private int id = -1;
    private int projectID;
    private int userID;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String activities;
    private String location;
    private String comments;

    public TrackingLog(int projectID, int userID, LocalDate date, LocalTime startTime, LocalTime endTime, String activities, String location, String comments) {
        this.projectID = projectID;
        this.userID = userID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activities = activities;
        this.location = location;
        this.comments = comments;
    }

    public TrackingLog() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
