package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Addfile;
import com.backend.backend.comps.groups.Createfolder;
import com.backend.backend.comps.groups.Filecomment;
import com.backend.backend.comps.groups.Notesfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/boxfiler")
public class Boxfilecontroller {
    @PostMapping("/createfolder/{type}/{groupid}/{boxfilerid}/{folderid}")
    public Object CreateFolder(@PathVariable String type ,@PathVariable String folderid, @PathVariable String boxfilerid,
            @PathVariable String groupid, @RequestBody Createfolder req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String, Object> newfolder = new HashMap<>();
        newfolder.put("folderid", req.getFolderid());
        newfolder.put("foldername", req.getFoldername());
        database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid)
                .collection("FOLDERS").document(folderid).set(newfolder);
        for (int i = 0; i < req.getFiledata().size(); i++) {
            database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid)
                    .collection("FOLDERS").document(folderid).collection("FILES")
                    .document(req.getFiledata().get(i).get("fileid")).set(req.getFiledata().get(i));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("res", true);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/getfolders/{type}/{groupid}/{boxfilerid}")
    public Object GetFolders(@PathVariable String type ,@PathVariable String groupid, @PathVariable String boxfilerid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").get();
        List<QueryDocumentSnapshot> fetch = query.get().getDocuments();
        List<Object> response = new ArrayList<>();
        for (QueryDocumentSnapshot doc : fetch) {
            Map<String, Object> datajson = new HashMap<>();
            datajson.put("foldername", doc.get("foldername"));
            datajson.put("folderid", doc.get("folderid"));
            ApiFuture<QuerySnapshot> ref = database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").document(doc.getId()).collection("FILES").get();
            List<QueryDocumentSnapshot> fetchfiles = ref.get().getDocuments();
            List<Object> filearray = new ArrayList<>();
            for (QueryDocumentSnapshot file: fetchfiles) {
              Map<String , Object> filejson = new HashMap<>();
              filejson.put("filename", file.get("filename"));
              filejson.put("fileid" , file.get("fileid"));
              filejson.put("filesize" , file.get("filesize"));
              filejson.put("filetype", file.get("filetype"));
              filearray.add(filejson);
            }
            datajson.put("files", filearray);
            response.add(datajson);
        }

        return new ResponseEntity<List<Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/addfiles/{type}/{groupid}/{boxfilerid}/{folderid}")
    public Object AddFiles(@PathVariable String type ,@PathVariable String groupid, @PathVariable String boxfilerid, @PathVariable String folderid, @RequestBody Addfile req) {
        Firestore database = FirestoreClient.getFirestore();
        for (int i = 0 ; i < req.getFiledata().size(); i++ ) {
            Map<String, Object> responsejson = new HashMap<>();
            responsejson.put("filename", req.getFiledata().get(i).get("filename"));
            responsejson.put("filetype", req.getFiledata().get(i).get("filetype"));
            responsejson.put("filesize", req.getFiledata().get(i).get("filesize"));
            responsejson.put("fileid", req.getFiledata().get(i).get("fileid"));

            database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").document(folderid).collection("FILES").document(req.getFiledata().get(i).get("fileid")).set(responsejson);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("res", true);
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }

    @PutMapping("/commentonfile/{type}/{groupid}/{boxfilerid}/{folderid}/{fileid}")
    public Object CommentFile(@PathVariable String type ,@PathVariable String groupid, @PathVariable String boxfilerid, @PathVariable String folderid, @PathVariable String fileid, @RequestBody Filecomment req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String, Object> res = new HashMap<>();
        res.put("comment", req.getComment());
        res.put("date", req.getDate());
        res.put("creator", req.getCreator());
        res.put("displayname", req.getDisplayname());
        res.put("commentid", req.getCommentid());
        database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").document(folderid).collection("FILES").document(fileid).collection("FILECOMMENTS").document(req.getCommentid()).set(res);
        return null;
    }

    @GetMapping("/getfilecomments/{type}/{groupid}/{boxfilerid}/{folderid}/{fileid}")
    public Object GetCommentFiles(@PathVariable String type ,@PathVariable String groupid, @PathVariable String boxfilerid, @PathVariable String folderid, @PathVariable String fileid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query =  database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").document(folderid).collection("FILES").document(fileid).collection("FILECOMMENTS").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Object> response = new ArrayList<>();
        for (QueryDocumentSnapshot docs : documents) {
            response.add(docs.getData());
        }
        return new ResponseEntity<List<Object>>(response,  HttpStatus.OK);
    }
    
    @PostMapping("/postnotes/{type}/{groupid}/{boxfilerid}/{folderid}/{fileid}")
    public Object PostNotes(@PathVariable String type , @PathVariable String groupid, @PathVariable String boxfilerid, @PathVariable String folderid, @PathVariable String fileid , @RequestBody Notesfile req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String, Object> res = new HashMap<>();
        res.put("backendpointleft", req.getBackendpointleft());
        res.put("backendpointright", req.getBackendpointright());
        res.put("styleleft", req.getStyleleft());
        res.put("styleright", req.getStyleright());
        res.put("grouptype", req.getGrouptype());
        res.put("groupid", req.getGroupid());
        res.put("boxfilerid", req.getBoxfilerid());
        res.put("folderid", req.getFolderid());
        res.put("fileid", req.getFileid());
        res.put("noteidentification", req.getNoteidentification());
        res.put("notepost", req.getNotepost());
        res.put("notedate", req.getNotedate());
        res.put("creator", req.getCreator());
        res.put("displaydate", req.getDisplaydate());
        res.put("style", req.getStyle());
        database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").document(folderid).collection("FILES").document(fileid).collection("FILENOTES").document(req.getNoteidentification()).set(res);
        Map<String, Object> clientres = new HashMap<>();
        clientres.put("styleleft", req.getStyleleft());
        clientres.put("styleright", req.getStyleright());
        clientres.put("notepost", req.getNotepost());
        clientres.put("displaydate", req.getDisplaydate());
        clientres.put("creator", req.getCreator());
        clientres.put("style", req.getStyle());
        return new ResponseEntity<Map<String , Object>>(clientres , HttpStatus.OK);
    }

    @GetMapping("/getnotes/{type}/{groupid}/{boxfilerid}/{folderid}/{fileid}")
    public Object GetNotes(@PathVariable String type, @PathVariable String groupid, @PathVariable String boxfilerid ,@PathVariable String folderid, @PathVariable String fileid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(type).document(groupid).collection("BOXFILER").document(boxfilerid).collection("FOLDERS").document(folderid).collection("FILES").document(fileid).collection("FILENOTES").get();
        List<QueryDocumentSnapshot> ref = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: ref) {
            Map<String , Object> clientres = new HashMap<>();
            clientres.put("styleleft", docs.get("styleleft"));
            clientres.put("styleright", docs.get("styleright"));
            clientres.put("notepost", docs.get("notepost"));
            clientres.put("displaydate", docs.get("displaydate"));
            clientres.put("creator", docs.get("creator"));
            clientres.put("style", docs.get("style"));
            res.add(clientres);
        }
        return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
    }
}