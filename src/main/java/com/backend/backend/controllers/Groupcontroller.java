package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Addpending;
import com.backend.backend.comps.groups.Creategroup;
import com.backend.backend.functions.Generateid;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/group")
public class Groupcontroller {
    @PostMapping("/creategroup")
    public Object CreateGroup(@RequestBody Creategroup newgroup) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        Creategroup creatingids = new Creategroup();
        Generateid generator = new Generateid();
        creatingids.setGroupid(generator.GenerateId(150));
        creatingids.setBoxfilerid(generator.GenerateId(149));
        creatingids.setGroupapi(generator.GenerateId(50));
        creatingids.setMainchatid(generator.GenerateId(148));
        creatingids.setWallpostid(generator.GenerateId(147));
        creatingids.setSubcomponentsid(generator.GenerateId(146));
        creatingids.setClientid(newgroup.getClientid() + "-" + generator.GenerateId(20));
        Map<String, Object> wholegroupdata = new HashMap<>();
        wholegroupdata.put("groupid", creatingids.getGroupid());
        wholegroupdata.put("clientid", creatingids.getClientid());
        wholegroupdata.put("boxfilerid", creatingids.getBoxfilerid());
        wholegroupdata.put("groupname", newgroup.getGroupname());
        wholegroupdata.put("creator" , newgroup.getCreator());
        wholegroupdata.put("groupdescription", newgroup.getGroupdescription());
        wholegroupdata.put("groupapi", creatingids.getGroupapi());
        wholegroupdata.put("wallpostid", creatingids.getWallpostid());
        wholegroupdata.put("users", newgroup.getUsers());
        wholegroupdata.put("grouptype", newgroup.getGrouptype());
        wholegroupdata.put("adminusers", newgroup.getAdminusers());
        wholegroupdata.put("subgroupsid", creatingids.getSubcomponentsid());
        wholegroupdata.put("mainchatid", creatingids.getMainchatid());
        Map<String, Object> boxfiler = new HashMap<>();
        boxfiler.put("boxfilerid", creatingids.getBoxfilerid());
        Map<String, Object> wallpostid = new HashMap<>();
        wallpostid.put("wallpostid", creatingids.getWallpostid());
        Map<String , Object> subgroupid = new HashMap<>();
        subgroupid.put("subgroupid", creatingids.getSubcomponentsid());
        Map<String , Object> mainchatid = new HashMap<>();
        mainchatid.put("mainchatid", creatingids.getMainchatid());
        Map<String, Object> pending = new HashMap<>();
        pending.put("groupname", newgroup.getGroupname());
        pending.put("groupdescription", newgroup.getGroupdescription());
        pending.put("groupapi", creatingids.getGroupapi());
        pending.put("clientid", creatingids.getClientid());
        pending.put("groupid", creatingids.getGroupid());
        pending.put("grouptype", newgroup.getGrouptype());
        database.collection("GROUPS").document(creatingids.getGroupid()).set(wholegroupdata);
        database.collection("CLIENTGROUPS").document(creatingids.getClientid()).set(pending);
        database.collection("USERS").document(newgroup.getCreator()).collection("GROUPS").document(creatingids.getGroupapi()).set(pending);
        ApiFuture<DocumentSnapshot> query = database.collection("USERS").document(newgroup.getCreator()).get();
            DocumentSnapshot user = query.get();
            Map<String , Object> userref = new HashMap<>();
            userref.put("firstname", user.get("firstname"));
            userref.put("lastname", user.get("lastname"));
            userref.put("email", user.get("email"));
            userref.put("useruid", user.get("useruid"));
            userref.put("title", "Control");
        if(newgroup.getGrouptype().equals("BASICGROUPS") == true) {
            database.collection("BASICGROUPS").document(creatingids.getGroupid()).collection("BOXFILER").document(creatingids.getBoxfilerid()).set(boxfiler);
            database.collection("BASICGROUPS").document(creatingids.getGroupid()).collection("WALLPOSTS").document(creatingids.getWallpostid()).set(wallpostid);   
            database.collection("BASICGROUPS").document(creatingids.getGroupid()).collection("MAINCHAT").document(creatingids.getMainchatid()).set(mainchatid);   
            database.collection("BASICGROUPS").document(creatingids.getGroupid()).collection("USERS").document(newgroup.getCreator()).set(userref);   

        } else if (newgroup.getGrouptype().equals("ORGANIZATIONALGROUPS") == true) {
            database.collection("ORGANIZATIONALGROUPS").document(creatingids.getGroupid()).collection("BOXFILER").document(creatingids.getBoxfilerid()).set(boxfiler);
            database.collection("ORGANIZATIONALGROUPS").document(creatingids.getGroupid()).collection("WALLPOSTS").document(creatingids.getWallpostid()).set(wallpostid);  
            database.collection("ORGANIZATIONALGROUPS").document(creatingids.getGroupid()).collection("SUBGROUPS").document(creatingids.getSubcomponentsid()).set(subgroupid);
            database.collection("ORGANIZATIONALGROUPS").document(creatingids.getGroupid()).collection("MAINCHAT").document(creatingids.getMainchatid()).set(mainchatid);   
            database.collection("ORGANIZATIONALGROUPS").document(creatingids.getGroupid()).collection("USERS").document(newgroup.getCreator()).set(userref);   
        }
        for(int i = 0 ; i < newgroup.getUsersadded().size(); i++) {
           ApiFuture<DocumentSnapshot> ref = database.collection("USERS").document(newgroup.getUsersadded().get(i)).get();
           DocumentSnapshot document = ref.get();
           if(document.exists() && newgroup.getUsersadded().get(i) != newgroup.getCreator()) {
            database.collection("USERS").document(newgroup.getUsersadded().get(i)).collection("PENDINGGROUPS").document(creatingids.getGroupapi()).set(pending);
           } else { 
               System.out.println("Users did not exists");
           } 
        }
        
        Map<String, Object> resp = new HashMap<>();
        resp.put("groupname", newgroup.getGroupname());
        resp.put("groupapi", creatingids.getGroupapi());
        resp.put("clientid", creatingids.getClientid());
        resp.put("groupdescription", newgroup.getGroupdescription());
        resp.put("grouptype", newgroup.getGrouptype());

        return new ResponseEntity<Map<String, Object>>(resp, HttpStatus.OK);
    }

    @GetMapping("/getgroups/{useruid}")
    public Object GetUserGroups(@PathVariable String useruid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> userquery = database.collection("USERS").document(useruid).collection("GROUPS").get();
        List<QueryDocumentSnapshot> docsnaps = userquery.get().getDocuments();
        List<Object> getgroups = new ArrayList<>();
        for (QueryDocumentSnapshot userdocs : docsnaps) {
            Map<String, Object> groupresponse = new HashMap<>();
            groupresponse.put("groupname", userdocs.get("groupname"));
            groupresponse.put("groupapi", userdocs.get("groupapi"));
            groupresponse.put("clientid", userdocs.get("clientid"));
            groupresponse.put("groupdescription", userdocs.get("groupdescription"));
            groupresponse.put("grouptype", userdocs.get("grouptype"));
            getgroups.add(groupresponse);
        }
        return new ResponseEntity<List<Object>>(getgroups , HttpStatus.OK);
    }

    @GetMapping("/getclients/{useruid}")
    public Object GetClientUser(@PathVariable String useruid) throws InterruptedException, ExecutionException {
       Firestore database = FirestoreClient.getFirestore();
       ApiFuture<QuerySnapshot> query = database.collection("USERS").document(useruid).collection("GROUPS").get();
       List<QueryDocumentSnapshot> docs = query.get().getDocuments();
       List<Object> clients = new ArrayList<>();
       for(QueryDocumentSnapshot documents: docs) {
        clients.add(documents.get("clientid"));
       }
        return new ResponseEntity<List<Object>>(clients , HttpStatus.OK);
    }

    @GetMapping("/getclientgroup/{clientid}")
    public Object GetClientGroups(@PathVariable String clientid) throws InterruptedException, ExecutionException {
       Firestore database = FirestoreClient.getFirestore();
       ApiFuture<DocumentSnapshot> query = database.collection("CLIENTGROUPS").document(clientid).get();
       DocumentSnapshot docs = query.get();
       Map<String , Object> response = new HashMap<>();
       response.put("groupname", docs.get("groupname"));
       response.put("groupdescription", docs.get("groupdescription"));
       response.put("groupapi", docs.get("groupapi"));
       response.put("clientid", docs.get("clientid"));
       return new ResponseEntity<Map<String, Object>>(docs.getData(), HttpStatus.OK);
    }

    @PutMapping("/putrequest/{groupid}/{useruid}")
    public Object PutRequest(@PathVariable String groupid, @PathVariable String useruid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> userquery = database.collection("USERS").document(useruid).get();
        DocumentSnapshot userdata = userquery.get();
        database.collection("GROUPS").document(groupid).collection("PENDINGREQUESTS").document(useruid).set(userdata.getData());
        return null;
    }

    @GetMapping("/getgroupdetails/{groupapi}/{useruid}")
    public Object GetGroup(@PathVariable String groupapi , @PathVariable String useruid ) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection("GROUPS").whereEqualTo("groupapi", groupapi).get();
        List<QueryDocumentSnapshot> docs = query.get().getDocuments();
        Map<String, Object> groupobject = new HashMap<>();
        for (QueryDocumentSnapshot documents: docs) {
          ApiFuture<DocumentSnapshot> refquery = database.collection(documents.get("grouptype").toString()).document(documents.get("groupid").toString()).collection("USERS").document(useruid).get();
          DocumentSnapshot ans = refquery.get();
           groupobject.put("boxfilerid", documents.get("boxfilerid"));
           groupobject.put("groupdescription", documents.get("groupdescription"));
           groupobject.put("groupname", documents.get("groupname"));
           groupobject.put("groupid", documents.get("groupid"));
           groupobject.put("wallpostid", documents.get("wallpostid"));
           groupobject.put("users", documents.get("users"));
           groupobject.put("groupapi", documents.get("groupapi"));
           groupobject.put("clientid", documents.get("clientid"));
           groupobject.put("grouptype", documents.get("grouptype"));
           groupobject.put("subgroupsid" , documents.get("subgroupsid"));
           groupobject.put("mainchatid", documents.get("mainchatid"));
           groupobject.put("usertitle", ans.get("title"));
          }
          
        return new ResponseEntity<Map<String , Object>>(groupobject , HttpStatus.OK);
    }

    @GetMapping("/checkuseradmin/{groupid}/{useruid}")
    public Object GetAdminStatus(@PathVariable String groupid , @PathVariable String useruid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> query = database.collection("GROUPS").document(groupid).get();
        DocumentSnapshot ref = query.get();
        Creategroup reference = null;
        reference = ref.toObject(Creategroup.class);
        Boolean res = reference.getAdminusers().contains(useruid.toString());
    
        return new ResponseEntity<Boolean>(res , HttpStatus.OK);
    }

    @GetMapping("/checkusergroup/{groupapi}/{useruid}")
    public Object CheckUser(@PathVariable String groupapi , @PathVariable String useruid)
            throws InterruptedException, ExecutionException {
       Firestore database = FirestoreClient.getFirestore();
       ApiFuture<DocumentSnapshot> query = database.collection("USERS").document(useruid).collection("GROUPS").document(groupapi).get();
       DocumentSnapshot queryres = query.get();
       Map<String, Object> res = new HashMap<>();
       if (queryres.exists() == true) {
        res.put("userresponse", true);
       } else {
        res.put("userresponse", false);
       }
       return new ResponseEntity<Map<String, Object>>(res , HttpStatus.OK);
    }

    @GetMapping("/getusers/{grouptype}/{groupid}")
    public Object GetUsers(@PathVariable String grouptype ,@PathVariable String groupid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> info = database.collection(grouptype).document(groupid).collection("USERS").get();
        List<QueryDocumentSnapshot> data = info.get().getDocuments();
        List<Object> userdata = new ArrayList<>();
        for (QueryDocumentSnapshot docs: data) {
            userdata.add(docs.getData());
        }
        
        return new ResponseEntity<List<Object>>(userdata, HttpStatus.OK);
    }

    @GetMapping("/getportalusers/{groupid}")
    public Object GetPortalUsers(@PathVariable String groupid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> query = database.collection("GROUPS").document(groupid).get();
        DocumentSnapshot ref = query.get();
        Creategroup doc = ref.toObject(Creategroup.class);
        List<String> newuserarray = new ArrayList<>();
        for (int i = 0; i < doc.getUsers().size(); i++) {
            if (doc.getAdminusers().contains(doc.getUsers().get(i)) == false) {
                newuserarray.add(doc.getUsers().get(i));
            }
        }
        List<Object> res = new ArrayList<>();
        for (int k = 0; k < newuserarray.size(); k++) {
          ApiFuture<DocumentSnapshot> userquery = database.collection("USERS").document(newuserarray.get(k)).get();
          DocumentSnapshot userref = userquery.get();
          res.add(userref.getData());
        }

        
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }

    @PutMapping("/makeadmin/{groupid}/{uid}")
    public Object MakeUserAdmin(@PathVariable String groupid, @PathVariable String uid) {
        Firestore database = FirestoreClient.getFirestore();
        database.collection("GROUPS").document(groupid).update("adminusers", FieldValue.arrayUnion(uid));
        return null;
    }

    @DeleteMapping("/removeuser/{groupid}/{uid}")
    public Object RemoveGroupUser(@PathVariable String groupid , @PathVariable String uid) {
        Firestore database = FirestoreClient.getFirestore();
        database.collection("GROUPS").document(groupid).update("users", FieldValue.arrayRemove(uid));
        return null;
    }

    @PutMapping("/giveusertitle/{grouptype}/{groupid}/{uid}")
    public Object GiveUserTitle(@PathVariable String grouptype , @PathVariable String groupid , @PathVariable String uid, @RequestBody String req) {
        Firestore database = FirestoreClient.getFirestore();
        database.collection(grouptype).document(groupid).collection("USERS").document(uid).update("title" , req);
        return null;
    }

    @GetMapping("/getgroupusers/{groupid}")
    public Object GetUsersWithinGroups(@PathVariable String groupid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> query = database.collection("GROUPS").document(groupid).get();
        DocumentSnapshot doc = query.get();
        Map<String, Object> response = new HashMap<>();
        response.put("users" , doc.get("users"));
        return new ResponseEntity<Map<String, Object>>(response , HttpStatus.OK);
    }

    @GetMapping("/pendinggroups/{useruid}")
    public Object GetPendingGroups(@PathVariable String useruid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection("USERS").document(useruid).collection("PENDINGGROUPS").get();
        List<QueryDocumentSnapshot> docs = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot document: docs) {
          Map<String, Object> resjson = new HashMap<>();
          resjson.put("clientid", document.get("clientid"));
          resjson.put("groupapi", document.get("groupapi"));
          resjson.put("groupdescription", document.get("groupdescription"));
          resjson.put("groupname", document.get("groupname"));
          res.add(resjson);
        }
        return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
    }

    
    @GetMapping("/addtogroup/{clientid}/{useruid}")
    public Object Addtogroup(@PathVariable String clientid, @PathVariable String useruid)
            throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection("GROUPS").whereEqualTo("clientid", clientid).get();
        List<QueryDocumentSnapshot> querydocs = query.get().getDocuments();
        for (QueryDocumentSnapshot docs : querydocs) {
            Map<String, Object> jsonres = new HashMap<>();
            jsonres.put("clientid", docs.get("clientid"));
            jsonres.put("groupapi", docs.get("groupapi"));
            jsonres.put("groupdescription", docs.get("groupdescription"));
            jsonres.put("groupname", docs.get("groupname"));
            jsonres.put("groupid", docs.get("groupid"));
            jsonres.put("grouptype", docs.get("grouptype"));
            ApiFuture<DocumentSnapshot> res = database.collection("USERS").document(useruid).get();
            DocumentSnapshot userref = res.get();
            Map<String , Object> userres = new HashMap<>();
            userres.put("firstname", userref.get("firstname"));
            userres.put("lastname", userref.get("lastname"));
            userres.put("email", userref.get("email"));
            userres.put("useruid", userref.get("useruid"));
            userres.put("title", "");
            database.collection("USERS").document(useruid).collection("GROUPS").document(docs.getString("groupapi")).set(jsonres);
            database.collection("USERS").document(useruid).collection("PENDINGGROUPS").document(docs.getString("groupapi")).delete();
            database.collection(docs.get("grouptype").toString()).document(docs.get("groupid").toString()).collection("USERS").document(useruid).set(userres);
            database.collection("GROUPS").document(docs.getId()).update("users", FieldValue.arrayUnion(useruid));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("res", true);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }    

    @PutMapping("/adduser/pending/{groupapi}/{useruid}")
    public Object AddUsersToPending(@PathVariable String groupapi ,@PathVariable String useruid , @RequestBody Addpending req ) throws InterruptedException, ExecutionException {
      Firestore database = FirestoreClient.getFirestore();
      Map<String , Object> response = new HashMap<>();
      response.put("clientid", req.getClientid());
      response.put("groupapi", req.getGroupapi());
      response.put("groupdescription", req.getGroupdescription());
      response.put("groupid", req.getGroupid());
      response.put("groupname", req.getGroupname());
      database.collection("USERS").document(useruid).collection("PENDINGGROUPS").document(groupapi).set(response);
      return null;
    }

    @DeleteMapping("/leavegroup/{groupapi}/{groupid}/{useruid}")
    public Object RemoveUser(@PathVariable String groupapi , @PathVariable String useruid, @PathVariable String groupid) {
        Firestore database = FirestoreClient.getFirestore();
        database.collection("USERS").document(useruid).collection("GROUPS").document(groupapi).delete();
        database.collection("GROUPS").document(groupid).update("users", FieldValue.arrayRemove(useruid));
        return null;
    }

}