package com.ust.poll.model;


import java.io.Serializable;

public class NewPoll implements Serializable {
    private static final long serialVersionUID = 1L;
    private String date;
    private String time;
    private String title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String friend;


    public NewPoll(){

    }
    public NewPoll(String date, String time, String title, String option1, String option2, String option3, String option4, String friend){
        this.date = date;
        this.time = time;
        this.title = title;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.friend = friend;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

/*
    public String toDisplayString() {
        return activity + " " + date + " " + bktype;
    }
    public String toString() {
        return "[DT:" + date + ",T:" + time + ",Centre:" + centre + ",CRT:"
                + court + ",ACT:" + activity + ",PRI:" + price + ",USERID:"
                + userid + ",ID:" + id + "]";
    }
*/

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String time) {
        this.title = title;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }


    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }


    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }


    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }


    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }



}

