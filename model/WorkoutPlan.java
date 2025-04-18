package model;

public class WorkoutPlan {
    private String activity;
    private String time;

    public WorkoutPlan(String activity, String time) {
        this.activity = activity;
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return activity + " at " + time;
    }
}
