package com.backend.backend.comps.groups;

import java.util.List;

public class Creategroup {
    private String groupid;
    private String clientid;
    private String boxfilerid;
    private String groupname;
    private String groupdescription;
    private List<String> usersadded;
    private String creator;
    private String groupapi;
    private String wallpostid;
    private String grouptype;
    private List<String> adminusers;
    private List<String> users;
    private String subcomponentsid;
    private String mainchatid;


    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupdescription() {
        return groupdescription;
    }

    public void setGroupdescription(String groupdescription) {
        this.groupdescription = groupdescription;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getBoxfilerid() {
        return boxfilerid;
    }

    public void setBoxfilerid(String boxfilerid) {
        this.boxfilerid = boxfilerid;
    }

    public List<String> getUsersadded() {
        return usersadded;
    }

    public void setUsersadded(List<String> usersadded) {
        this.usersadded = usersadded;
    }

    public String getGroupapi() {
        return groupapi;
    }

    public void setGroupapi(String groupapi) {
        this.groupapi = groupapi;
    }

    public String getWallpostid() {
        return wallpostid;
    }

    public void setWallpostid(String wallpostid) {
        this.wallpostid = wallpostid;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

  

    public List<String> getAdminusers() {
        return adminusers;
    }

    public void setAdminusers(List<String> adminusers) {
        this.adminusers = adminusers;
    }

    public String getSubcomponentsid() {
        return subcomponentsid;
    }

    public void setSubcomponentsid(String subcomponentsid) {
        this.subcomponentsid = subcomponentsid;
    }

    public String getMainchatid() {
        return mainchatid;
    }

    public void setMainchatid(String mainchatid) {
        this.mainchatid = mainchatid;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }

   
}