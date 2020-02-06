package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Commentpost;
import com.backend.backend.comps.groups.Makepost;
import com.backend.backend.functions.Generateid;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/wallpostapi")
public class Wallpostscontroller {
    @PostMapping("/makepost/{type}/{groupid}/{wallpostid}")
    public Object MakePost(@PathVariable String type ,@PathVariable String wallpostid, @PathVariable String groupid,
     @RequestBody Makepost req) throws InterruptedException, ExecutionException {
        Generateid generator = new Generateid();
        Firestore database = FirestoreClient.getFirestore();
        Makepost newpost = new Makepost();
        newpost.setPostid(generator.GenerateId(130));
        Map<String, Object> setdata = new HashMap<>();
        setdata.put("creator", req.getCreator());
        setdata.put("postid", newpost.getPostid());
        setdata.put("message", req.getMessage());
        setdata.put("displayname", req.getDisplayname());
        setdata.put("date", req.getDate());
        setdata.put("displaydate", req.getDisplaydate());
        setdata.put("posttype", req.getPosttype());
        database.collection(type).document(groupid).collection("WALLPOSTS").document(wallpostid).collection("POSTS").document(newpost.getPostid()).set(setdata);
        ApiFuture<DocumentSnapshot> responsequery = database.collection(type).document(groupid).collection("USERS").document(req.getCreator()).get();
        DocumentSnapshot responsedata = responsequery.get();
        setdata.put("usertitle", responsedata.get("title"));
        return new ResponseEntity<Map<String, Object>>(setdata , HttpStatus.OK);
    }

    @GetMapping("/getposts/{type}/{groupid}/{wallpostid}")
    public Object GetPosts(@PathVariable String type ,@PathVariable String groupid, @PathVariable String wallpostid)
    throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(type).document(groupid).collection("WALLPOSTS").document(wallpostid).collection("POSTS").get();
        List<QueryDocumentSnapshot> querylist = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for(QueryDocumentSnapshot docs : querylist) {
            Map<String, Object> response = new HashMap<>();
            ApiFuture<DocumentSnapshot> resquery = database.collection(type).document(groupid).collection("USERS").document(docs.get("creator").toString()).get();
            DocumentSnapshot usertitle = resquery.get();
            response.put("creator", docs.get("creator"));
            response.put("postid", docs.get("postid"));
            response.put("message", docs.get("message"));
            response.put("displayname", docs.get("displayname"));
            response.put("date", docs.get("date"));
            response.put("displaydate",docs.get("displaydate"));
            response.put("posttype", docs.get("posttype"));
            response.put("usertitle", usertitle.get("title"));
            res.add(response);
        }

        Collections.reverse(res);
        
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }

    @PostMapping("/comment/{type}/{groupid}/{wallpostid}/{postid}")
    public Object PostComment(@PathVariable String type ,@PathVariable String groupid, @PathVariable String wallpostid, @PathVariable String postid, @RequestBody Commentpost req) {
       Firestore database = FirestoreClient.getFirestore();
       Generateid generator = new Generateid();
       Commentpost ref = new Commentpost();
       ref.setCommentid(generator.GenerateId(90));
       Map<String, Object> res = new HashMap<>();
       res.put("creator", req.getCreator());
       res.put("commentid", ref.getCommentid());
       res.put("message", req.getMessage());
       res.put("displayname", req.getDisplayname());
       res.put("date", req.getDate());
       database.collection(type).document(groupid).collection("WALLPOSTS").document(wallpostid).collection("POSTS").document(postid).collection("COMMENTS").document(ref.getCommentid()).set(res);
       Map<String, Object> response = new HashMap<>();
       response.put("message", req.getMessage());
       response.put("displayname", req.getDisplayname());
       response.put("date", req.getDate());
       return new ResponseEntity<Map<String, Object>>(response , HttpStatus.OK);
    }

    @GetMapping("/getcomments/{type}/{groupid}/{wallpostid}/{postid}")
    public Object GetComments(@PathVariable String type ,@PathVariable String groupid, @PathVariable String wallpostid, @PathVariable String postid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(type).document(groupid).collection("WALLPOSTS").document(wallpostid).collection("POSTS").document(postid).collection("COMMENTS").get();
        List<QueryDocumentSnapshot> querydocs = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs : querydocs) {
            Map<String, Object> resjson = new HashMap<>();
            resjson.put("message", docs.get("message"));
            resjson.put("displayname", docs.get("displayname"));
            resjson.put("date", docs.get("date"));
            res.add(resjson);
        }
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }
}