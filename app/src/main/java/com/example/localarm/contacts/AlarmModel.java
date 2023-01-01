package com.example.localarm.contacts;

public class AlarmModel {
    private int id;
    private String name;
    private String desc;


    public AlarmModel(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public AlarmModel(String alarmName, String alarmDesc) {
        this.name = alarmName;
        this.desc = alarmDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
