package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Connection;
import com.backend.backend.comps.groups.Creategroup;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/connection")
public class Connectioncontroller {
    @GetMapping("/getgroup/{connectionid}")
    public Object GetGroup(@PathVariable String connectionid) throws InterruptedException, ExecutionException {
      Firestore database = com.google.firebase.cloud.FirestoreClient.getFirestore();
      ApiFuture<DocumentSnapshot> query =database.collection("CLIENTGROUPS").document(connectionid).get();
      DocumentSnapshot file = query.get();
      Map<String, Object> res = new HashMap<>();
      res.put("clientid", file.get("clientid"));
      res.put("groupapi", file.get("groupapi"));
      res.put("groupdescription", file.get("groupdescription"));
      res.put("groupname", file.get("groupname"));
      return new ResponseEntity<Map<String, Object>>(res , HttpStatus.OK);
  }

  @PutMapping("/requestconnection/{requestedclientid}/{responseclientid}")
  public Object RequestConnection(@PathVariable String requestedclientid , @PathVariable String responseclientid , @RequestBody Connection req) throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    Map<String, Object> res = new HashMap<>();
    Connection connection = new Connection();
    Generateid generator = new Generateid();
    ApiFuture<DocumentSnapshot> reqquery = database.collection("CLIENTGROUPS").document(requestedclientid).get();
    DocumentSnapshot reqdata = reqquery.get();
    ApiFuture<DocumentSnapshot> resquery = database.collection("CLIENTGROUPS").document(responseclientid).get();
    DocumentSnapshot resdata = resquery.get();
    connection.setConnectionid(generator.GenerateId(160));
    connection.setConnectiongroupapi(generator.GenerateId(55));
    connection.setPathconnectionid(generator.GenerateId(80));
    res.put("connectionid", connection.getConnectionid());
    res.put("requestedgroupid", reqdata.get("groupid"));
    res.put("responsegroupid", resdata.get("groupid"));
    res.put("requestedclientid", reqdata.get("clientid"));
    res.put("responseclientid", resdata.get("clientid"));
    res.put("requestedgroupname", reqdata.get("groupname"));
    res.put("responsegroupname", resdata.get("groupname"));
    res.put("connectiongroupapi", connection.getConnectiongroupapi());
    res.put("pathconnectionid", connection.getPathconnectionid());
    res.put("connectionname", req.getConnectionname());
    database.collection("GROUPS").document(resdata.get("groupid").toString()).collection("PENDINGCONNECTIONS").document(connection.getPathconnectionid()).set(res);
    return null;
  }

  @GetMapping("/getpendingconnections/{groupid}")
  public Object GetPendingGroups(@PathVariable String groupid) throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> query = database.collection("GROUPS").document(groupid).collection("PENDINGCONNECTIONS").get();
    List<QueryDocumentSnapshot> documents = query.get().getDocuments();
    List<Object> res = new ArrayList<>();
    for(QueryDocumentSnapshot docs: documents) {
      Map<String, Object> resjson = new HashMap<>();
      resjson.put("requestedclientid", docs.get("requestedclientid"));
      resjson.put("responseclientid", docs.get("responseclientid"));
      resjson.put("pathconnectionid" , docs.get("pathconnectionid"));
      resjson.put("connectionname", docs.get("connectionname"));
      res.add(resjson);
    }
    return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
  }

  @GetMapping("/createconnection/{responseclientid}/{requestedclientid}/{connectionapi}")
  public Object CreateConnection(@PathVariable String responseclientid, @PathVariable String requestedclientid, @PathVariable String connectionapi) throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    ApiFuture<DocumentSnapshot> resquery = database.collection("CLIENTGROUPS").document(responseclientid).get();
    DocumentSnapshot resdata = resquery.get();
    ApiFuture<DocumentSnapshot> reqquery = database.collection("CLIENTGROUPS").document(requestedclientid).get();
    DocumentSnapshot reqdata = reqquery.get();
    ApiFuture<DocumentSnapshot> query = database.collection("GROUPS").document(resdata.get("groupid").toString()).collection("PENDINGCONNECTIONS").document(connectionapi).get();
    DocumentSnapshot response = query.get();
    ApiFuture<DocumentSnapshot> getusersres = database.collection("GROUPS").document(resdata.get("groupid").toString()).get();
    DocumentSnapshot resgroupdata = getusersres.get();
    ApiFuture<DocumentSnapshot> getusersreq = database.collection("GROUPS").document(reqdata.get("groupid").toString()).get();
    DocumentSnapshot reqgroupdata = getusersreq.get();
    Creategroup dataresponse = null;
    dataresponse = resgroupdata.toObject(Creategroup.class);
    Creategroup datarequest = null;
    datarequest = reqgroupdata.toObject(Creategroup.class);
    Map<String, Object> connectiondata = new HashMap<>();
    connectiondata.put("connectionname", response.get("connectionname"));
    connectiondata.put("connectiongroupapi", response.get("connectiongroupapi"));
    for (int i = 0; i < dataresponse.getUsers().size(); i++) {
      database.collection("USERS").document(dataresponse.getUsers().get(i)).collection("CONNECTIONS").document(response.get("connectionid").toString()).set(connectiondata);
    }

    for (int j = 0; j < datarequest.getUsers().size(); j++) {
      database.collection("USERS").document(datarequest.getUsers().get(j)).collection("CONNECTIONS").document(response.get("connectionid").toString()).set(connectiondata);
    }

    database.collection("GROUPS").document(resdata.get("groupid").toString()).collection("CONNECTIONS").document(response.get("connectionid").toString()).set(response.getData());
    database.collection("GROUPS").document(reqdata.get("groupid").toString()).collection("CONNECTIONS").document(response.get("connectionid").toString()).set(response.getData());
    database.collection("CONNECTIONS").document(response.get("connectionid").toString()).set(response.getData());
    database.collection("GROUPS").document(resdata.get("groupid").toString()).collection("PENDINGCONNECTIONS").document(connectionapi).delete();
    
    return new ResponseEntity<Map<String, Object>>(connectiondata, HttpStatus.OK);
  }

  @GetMapping("/fetchconnections/{groupid}")
  public Object FetchConnections (@PathVariable String groupid) throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> query = database.collection("GROUPS").document(groupid).collection("CONNECTIONS").get();
    List<QueryDocumentSnapshot> response = query.get().getDocuments();
    List<Object> array = new ArrayList<>();
    for(QueryDocumentSnapshot docs: response) {
      Map<String, Object> res = new HashMap<>();
      res.put("connectionname", docs.get("connectionname"));
      res.put("connectiongroupapi", docs.get("connectiongroupapi"));
      array.add(res);
    }
    return new ResponseEntity<List<Object>>(array , HttpStatus.OK);
  }

  @GetMapping("/fetchconnectioncredentials/{connectionapi}")
  public Object ConnectionFetch(@PathVariable String connectionapi) throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> query = database.collection("CONNECTIONS").whereEqualTo("connectiongroupapi", connectionapi).get();
    List<QueryDocumentSnapshot> res = query.get().getDocuments();
    Map<String, Object> response = new HashMap<>();
    for (QueryDocumentSnapshot docs: res) {
      response.put("connectionname", docs.get("connectionname"));
      response.put("connectionid", docs.get("connectionid"));
    }
    
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
  }

  /*
  @PutMapping("/makepost/{connectionid}")
  public Object MakePostConnection(@PathVariable String connectionid, @RequestBody Connectionpost req) {
    Firestore database = FirestoreClient.getFirestore();
    Map<String, Object> res = new HashMap<>();
    res.put("message" , req.getMessage());
    res.put("file", req.getFile());
    res.put("postid", req.getPostid());
    res.put("creator", req.getCreator());
    res.put("displayName", req.getDisplayName());
    res.put("date", req.getDate());
    res.put("servertimestamp" , req.getServertimestamp());
    res.put("fileboxfilerid", req.getFileboxfilerid());
    res.put("groupid", req.getGroupid());
    res.put("fileid", req.getFileid());
    res.put("fileurl", req.getFileurl());
    database.collection("CONNECTIONS").document(connectionid).collection("POSTS").document(req.getPostid()).set(res);
    return null;
  }

  @GetMapping("/getposts/{connectionid}")
  public Object GetPosts(@PathVariable String connectionid) throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> query = database.collection("CONNECTIONS").document(connectionid).collection("POSTS").get();
    List<QueryDocumentSnapshot> documents = query.get().getDocuments();
    List<Object> res = new ArrayList<>();
    for (QueryDocumentSnapshot docs: documents) {
      res.add(docs.getData());
    }
    return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
  }

  @PutMapping("/comment/{connectionid}/{postid}")
  public Object SetComment(@PathVariable String connectionid , @PathVariable String postid , @RequestBody Commentpost req) {
    Firestore database = FirestoreClient.getFirestore();
    Map<String, Object> res = new HashMap<>();
    res.put("commentid", req.getCommentid());
    res.put("creator", req.getCreator());
    res.put("date", req.getDate());
    res.put("displayname", req.getDisplayname());
    res.put("message", req.getMessage());
    database.collection("CONNECTIONS").document(connectionid).collection("POSTS").document(postid).collection("COMMENTS").document(req.getCommentid()).set(res);
    return null;
  }

  @GetMapping("/getcomments/{connectionid}/{postid}")
  public Object GetComments(@PathVariable String connectionid , @PathVariable String postid)
      throws InterruptedException, ExecutionException {
    Firestore database = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> query = database.collection("CONNECTIONS").document(connectionid).collection("POSTS").document(postid).collection("COMMENTS").get();
    List<QueryDocumentSnapshot> documents = query.get().getDocuments();
    List<Object> res = new ArrayList<>();
    for (QueryDocumentSnapshot docs: documents) {
      res.add(docs.getData());
    }
    return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
  }
  */

}