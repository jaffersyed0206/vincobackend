package com.backend.backend.comps.groups;

import java.util.List;
import java.util.Map;

public class Createfolder {
    private String foldername;
    private String folderid;
    private List<Map<String, String>> filedata;

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getFolderid() {
        return folderid;
    }

    public void setFolderid(String folderid) {
        this.folderid = folderid;
    }

    public List<Map<String, String>> getFiledata() {
        return filedata;
    }

    public void setFiledata(List<Map<String, String>> filedata) {
        this.filedata = filedata;
    }

   

   
   
}