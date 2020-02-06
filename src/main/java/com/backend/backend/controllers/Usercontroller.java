package com.backend.backend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.usercomps.Createuser;
import com.backend.backend.firebaseserver.Firebaseapp;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;

import org.json.JSONException;
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
@RequestMapping("/user")
public class Usercontroller {
  Firebaseapp application = new Firebaseapp();

  @PostMapping(value = "/createuser")
  public Object CreateUser(@RequestBody Createuser userdata)
      throws FirebaseAuthException, IOException, ClassNotFoundException, JSONException {
    Firestore database = FirestoreClient.getFirestore(application.getContextApp());
    CreateRequest requestnew = new CreateRequest();
    requestnew.setEmail(userdata.getEmail());
    requestnew.setPassword(userdata.getPassword());
    requestnew.setDisplayName(userdata.getFirstname());
    UserRecord recorduser = FirebaseAuth.getInstance().createUser(requestnew);
    Map<String, Object> newuser = new HashMap<>();
    newuser.put("email", userdata.getEmail());
    newuser.put("firstname", userdata.getFirstname());
    newuser.put("lastname", userdata.getLastname());
    newuser.put("useruid", recorduser.getUid());
    database.collection("USERS").document(recorduser.getUid()).set(newuser);
    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
  }

  @GetMapping("/getallusers")
  public Object GetAllUsers() throws InterruptedException, ExecutionException {
     Firestore database = FirestoreClient.getFirestore();
     ApiFuture<QuerySnapshot> query = database.collection("USERS").get();
     List<QueryDocumentSnapshot> docs = query.get().getDocuments();
     List<Object> responsearray = new ArrayList<>();
     for(QueryDocumentSnapshot document: docs) {
      responsearray.add(document.getData());
     }
     return new ResponseEntity<List<Object>>(responsearray , HttpStatus.OK);
    }


    @GetMapping("/getusersubs/{useruid}")
    public Object GetUserSubs(@PathVariable String useruid) throws InterruptedException, ExecutionException {
      Firestore database = FirestoreClient.getFirestore();
      ApiFuture<QuerySnapshot> query = database.collection("USERS").document(useruid).collection("SUBGROUPS").get();
      List<QueryDocumentSnapshot> ref = query.get().getDocuments();
      List<Object> res = new ArrayList<>();
      for (QueryDocumentSnapshot docs: ref) {
        res.add(docs.getData());
      }
      return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }

    @GetMapping("/getuserconnections/{useruid}")
    public Object GetUserConnections(@PathVariable String useruid) throws InterruptedException, ExecutionException {
      Firestore database = FirestoreClient.getFirestore();
      ApiFuture<QuerySnapshot> query = database.collection("USERS").document(useruid).collection("CONNECTIONS").get();
      List<QueryDocumentSnapshot> resquery = query.get().getDocuments();
      List<Object> response = new ArrayList<>();
      for (QueryDocumentSnapshot docs: resquery) {
        response.add(docs.getData());
      }
      return new ResponseEntity<List<Object>>(response, HttpStatus.OK);
    }

}