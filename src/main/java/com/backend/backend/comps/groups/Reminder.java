package com.backend.backend.comps.groups;

import java.time.ZoneId;

public class Reminder {
  
    private String reminderid;
    private String reminderpost;
    private String postremindertime;
    private String groupname;
    private String grouptype;
    private String groupid;
    private String creator;
    private String backenddate;
    private ZoneId timezone;


    public String getReminderid() {
        return reminderid;
    }

    public void setReminderid(String reminderid) {
        this.reminderid = reminderid;
    }

    public String getReminderpost() {
        return reminderpost;
    }

    public void setReminderpost(String reminderpost) {
        this.reminderpost = reminderpost;
    }

    

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

  
    public String getPostremindertime() {
        return postremindertime;
    }

    public void setPostremindertime(String postremindertime) {
        this.postremindertime = postremindertime;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }


    public String getBackenddate() {
        return backenddate;
    }

    public void setBackenddate(String backenddate) {
        this.backenddate = backenddate;
    }

}