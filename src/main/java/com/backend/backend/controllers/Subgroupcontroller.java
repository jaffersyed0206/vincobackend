package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Addfile;
import com.backend.backend.comps.groups.Commentpost;
import com.backend.backend.comps.groups.Createfolder;
import com.backend.backend.comps.groups.Createsubgroup;
import com.backend.backend.comps.groups.Filecomment;
import com.backend.backend.comps.groups.Subpost;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/api/subgroup")
@RestController
public class Subgroupcontroller {
    @PutMapping("/createsubggroup/{grouptype}/{groupid}/{subgroupsidentification}/{currentuseruid}")
    public Object CreateSubGroup(@PathVariable String currentuseruid, @PathVariable String subgroupsidentification,
            @PathVariable String grouptype, @PathVariable String groupid, @RequestBody Createsubgroup req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String, Object> response = new HashMap<>();
        response.put("subgroupname", req.getSubgroupname());
        response.put("subgroupdescription", req.getSubgroupdescription());
        response.put("subgroupmembers", req.getSubgroupmembers());
        response.put("subgroupid", req.getSubgroupid());
        response.put("subgrouppost", req.getSubgrouppost());
        response.put("subboxfiler", req.getSubboxfiler());
        response.put("subchat", req.getSubchat());
        response.put("maingroupapi", req.getMaingroupapi());
        response.put("groupid", req.getGroupid());
        response.put("subgroupapi", req.getSubgroupapi());
        response.put("subid", req.getSubid());
        response.put("grouptype", req.getGrouptype());
        response.put("mainboxfilerid", req.getMainboxfilerid());
        response.put("maingroupname", req.getMaingroupname());
        Map<String, Object> userresponse = new HashMap<>();
        userresponse.put("subgroupname", req.getSubgroupname());
        userresponse.put("subgroupdescription", req.getSubgroupdescription());
        userresponse.put("maingroupapi", req.getMaingroupapi());
        userresponse.put("subgroupapi", req.getSubgroupapi());
        userresponse.put("grouptype", req.getGrouptype());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subgroupsidentification)
                .collection("SUBGROUPIDs").document(req.getSubgroupid()).set(response);
        Map<String, Object> boxfiler = new HashMap<>();
        boxfiler.put("subboxfilerid", req.getSubboxfiler());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subgroupsidentification)
                .collection("SUBGROUPIDs").document(req.getSubgroupid()).collection("SUBBOXs").document(req.getSubboxfiler()).set(boxfiler);
        Map<String, Object> posts = new HashMap<>();
        posts.put("postid", req.getSubgrouppost());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subgroupsidentification)
                .collection("SUBGROUPIDs").document(req.getSubgroupid()).collection("SUBPOSTs").document(req.getSubgrouppost()).set(posts);

        if (req.isAreyouinthissubgroup() == true) {
            database.collection(grouptype).document(groupid).collection("USERS").document(currentuseruid)
                    .collection("SUBGROUPS").document(req.getSubgroupid()).set(userresponse);

          database.collection("USERS").document(currentuseruid).collection("SUBGROUPS").document(req.getSubgroupid())
          .set(userresponse);
        }

        for (int i = 0; i < req.getSubgroupmembers().size(); i++) {
            database.collection(grouptype).document(groupid).collection("USERS")
                    .document(req.getSubgroupmembers().get(i)).collection("SUBGROUPS").document(req.getSubgroupid())
                    .set(userresponse);

          database.collection("USERS").document(req.getSubgroupmembers().get(i)).collection("SUBGROUPS").document(req.getSubgroupid())
          .set(userresponse);
        }
        return null;
    }

    @GetMapping("/getusersubgroups/{grouptype}/{groupid}/{currentuser}")
    public Object GetUserSubGroups(@PathVariable String grouptype, @PathVariable String groupid,
            @PathVariable String currentuser) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(grouptype).document(groupid).collection("USERS").document(currentuser).collection("SUBGROUPS").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs : documents) {
            res.add(docs.getData());
        }
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }

    @GetMapping("/getcreds/{grouptype}/{mainapi}/{subapi}")
    public Object GetCreds(@PathVariable String grouptype, @PathVariable String mainapi, @PathVariable String subapi)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> groupquery = database.collection("GROUPS").whereEqualTo("groupapi", mainapi).get();
        List<QueryDocumentSnapshot> groupdoc = groupquery.get().getDocuments();
        Map<String , Object> getGroupCreds = new HashMap<>();
        for  (DocumentSnapshot docs: groupdoc) {
            getGroupCreds.put("groupid", docs.get("groupid"));
            getGroupCreds.put("subgroupid", docs.get("subgroupsid"));
        }

        ApiFuture<QuerySnapshot> subgroupquery =  database.collection(grouptype).document(getGroupCreds.get("groupid").toString()).collection("SUBGROUPS").document(getGroupCreds.get("subgroupid").toString()).collection("SUBGROUPIDs").whereEqualTo("maingroupapi", mainapi).whereEqualTo("subgroupapi", subapi).get();
        List<QueryDocumentSnapshot> subdoc = subgroupquery.get().getDocuments();
        Map<String , Object> subGroupCreds = new HashMap<>();
        for (DocumentSnapshot subdocs: subdoc) {
            subGroupCreds.put("groupid", subdocs.get("groupid"));
            subGroupCreds.put("grouptype", subdocs.get("grouptype"));
            subGroupCreds.put("maingroupapi", subdocs.get("maingroupapi"));
            subGroupCreds.put("subboxfilerid", subdocs.get("subboxfiler"));
            subGroupCreds.put("subchatid", subdocs.get("subchat"));
            subGroupCreds.put("subgroupapi", subdocs.get("subgroupapi"));
            subGroupCreds.put("subgroupdescription", subdocs.get("subgroupdescription"));
            subGroupCreds.put("subgroupid", subdocs.get("subgroupid"));
            subGroupCreds.put("subgroupmembers", subdocs.get("subgroupmembers"));
            subGroupCreds.put("subgroupname", subdocs.get("subgroupname"));
            subGroupCreds.put("subgrouppost", subdocs.get("subgrouppost"));
            subGroupCreds.put("subid", subdocs.get("subid"));
            subGroupCreds.put("mainboxfilerid", subdocs.get("mainboxfilerid"));
            subGroupCreds.put("maingroupname", subdocs.get("maingroupname"));
        }
        
        return new ResponseEntity<Map<String , Object>>(subGroupCreds , HttpStatus.OK);
    }

    @PutMapping("/makepost/{grouptype}/{groupid}/{subid}/{subgroupid}/{subpostid}")
    public Object MakeSubPost(@PathVariable String grouptype , @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subpostid, @RequestBody Subpost req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String , Object> res = new HashMap<>();
        res.put("message" , req.getMessage());
        res.put("file", req.getFile());
        res.put("postid", req.getPostid());
        res.put("creator", req.getCreator());
        res.put("displayName", req.getDisplayName());
        res.put("date", req.getDate());
        res.put("servertimestamp", req.getServertimestamp());
        res.put("filename", req.getFilename());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBPOSTs").document(subpostid).collection("POSTS").document(req.getPostid()).set(res);   
        return null;
    }

    @GetMapping("/getsubposts/{grouptype}/{groupid}/{subid}/{subgroupid}/{subpostid}")
    public Object GetSubPosts(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subpostid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBPOSTs").document(subpostid).collection("POSTS").get();
        List<QueryDocumentSnapshot> ref = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: ref) {
            res.add(docs.getData());
        }
        return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
    }

    @PutMapping("/postcommentonpost/{grouptype}/{groupid}/{subid}/{subgroupid}/{subpostid}/{postid}")
    public Object PostComment(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subpostid , @PathVariable String postid , @RequestBody Commentpost req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String, Object> res = new HashMap<>();
        res.put("creator", req.getCreator());
        res.put("commentid", req.getCommentid());
        res.put("message", req.getMessage());
        res.put("displayname", req.getDisplayname());
        res.put("date", req.getDate());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBPOSTs").document(subpostid).collection("POSTS").document(postid).collection("COMMENTS").document(req.getCommentid()).set(res);
        return null;
    }

    @GetMapping("/getpostcomments/{grouptype}/{groupid}/{subid}/{subgroupid}/{subpostid}/{postid}")
    public Object GetPostComments(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subpostid , @PathVariable String postid ) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBPOSTs").document(subpostid).collection("POSTS").document(postid).collection("COMMENTS").get();
        List<QueryDocumentSnapshot> ref = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: ref) {
            res.add(docs.getData());
        }
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }

    @PutMapping("/createsubfolder/{grouptype}/{groupid}/{subid}/{subgroupid}/{subboxid}")
    public Object CreateSubFolder(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subboxid, @RequestBody Createfolder req){
        Firestore database = FirestoreClient.getFirestore();
        Map<String , Object> newfolder = new HashMap<>();
        newfolder.put("folderid", req.getFolderid());
        newfolder.put("foldername", req.getFoldername());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").document(req.getFolderid())
        .set(newfolder);
        for (int i = 0; i < req.getFiledata().size(); i++) {
            database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").document(req.getFolderid())
            .collection("FILES").document(req.getFiledata().get(i).get("fileid")).set(req.getFiledata().get(i));
        }       
        return null;
    }

    @GetMapping("/getsubfolders/{grouptype}/{groupid}/{subid}/{subgroupid}/{subboxid}")
    public Object GetSubFolders(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subboxid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").get();
        List<QueryDocumentSnapshot> fetch = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: fetch) {
            Map<String , Object> datajson = new HashMap<>();
            datajson.put("foldername", docs.get("foldername"));
            datajson.put("folderid", docs.get("folderid"));
            ApiFuture<QuerySnapshot> ref = database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").document(docs.getId()).collection("FILES").get();
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
            res.add(datajson);
        }
        return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
    }

    @PutMapping("/addfilessubfolder/{grouptype}/{groupid}/{subid}/{subgroupid}/{subboxid}/{folderid}")
    public Object AddFileSubFolder(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subboxid, @PathVariable String folderid, @RequestBody Addfile req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String  , Object> res = new HashMap<>();
        for (int i = 0; i < req.getFiledata().size(); i++ ) {
            res.put("filename", req.getFiledata().get(i).get("filename"));
            res.put("filetype", req.getFiledata().get(i).get("filetype"));
            res.put("filesize", req.getFiledata().get(i).get("filesize"));
            res.put("fileid", req.getFiledata().get(i).get("fileid"));
            database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").document(folderid).collection("FILES").document(req.getFiledata().get(i).get("fileid")).set(res);
        }
        return null;
    }

    @PutMapping("/commentonfile/{grouptype}/{groupid}/{subid}/{subgroupid}/{subboxid}/{folderid}/{fileid}")
    public Object CommentOnFile(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subboxid, @PathVariable String folderid, @PathVariable String fileid , @RequestBody Filecomment req) {
        Firestore database = FirestoreClient.getFirestore();
        Map<String , Object> res = new HashMap<>();
        res.put("comment", req.getComment());
        res.put("date", req.getDate());
        res.put("creator", req.getCreator());
        res.put("displayname", req.getDisplayname());
        res.put("commentid", req.getCommentid());
        database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").document(folderid).collection("FILES").document(fileid).collection("COMMENTS").document(req.getCommentid()).set(res);
        return null;
    }

    @GetMapping("/getcommentsonfile/{grouptype}/{groupid}/{subid}/{subgroupid}/{subboxid}/{folderid}/{fileid}")
    public Object GetCommentsOnFile(@PathVariable String grouptype, @PathVariable String groupid, @PathVariable String subid, @PathVariable String subgroupid, @PathVariable String subboxid, @PathVariable String folderid, @PathVariable String fileid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query =  database.collection(grouptype).document(groupid).collection("SUBGROUPS").document(subid).collection("SUBGROUPIDs").document(subgroupid).collection("SUBBOXs").document(subboxid).collection("FOLDERS").document(folderid).collection("FILES").document(fileid).collection("COMMENTS").get();
        List<QueryDocumentSnapshot> ref = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: ref) {
            res.add(docs.getData());
        }
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }
}