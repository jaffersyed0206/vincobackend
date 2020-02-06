package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Creategroup;
import com.backend.backend.comps.groups.Reminder;
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
@RequestMapping("/api/reminders/")
@RestController
public class Remindercontroller {

/*
@PostMapping("/schedulereminder/{grouptype}/{groupid}/{useruid}") 
    public Object ScheduleEmail(@PathVariable String grouptype , @PathVariable String groupid, @PathVariable String useruid
    , @RequestBody Reminder req) {
        try {
            String reminderemail = req.getBackenddate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            ZonedDateTime dateTime = ZonedDateTime.of(LocalDateTime.parse(reminderemail, formatter), ZoneId.systemDefault());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                return new ResponseEntity<String>("You can not schedule something for right now" , HttpStatus.OK);
            }

        } catch(SchedulingException ex) {
            System.out.println(ex);
        }
        return null;
    }
*/

    @PutMapping("/makeremindermain/{grouptype}/{groupid}")
    public Object MakeReminder(@PathVariable String grouptype, @PathVariable String groupid, @RequestBody Reminder req) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> docquery = database.collection("GROUPS").document(groupid).get();
        DocumentSnapshot docref = docquery.get();
        Creategroup groupdata = docref.toObject(Creategroup.class);
        Map<String, Object> res = new HashMap<>();
        res.put("reminderid", req.getReminderid());
        res.put("reminderpost", req.getReminderpost());
        res.put("postremindertime", req.getPostremindertime());
        res.put("groupname", req.getGroupname());
        res.put("grouptype", req.getGrouptype());
        res.put("groupid", req.getGroupid());
        res.put("creator", req.getCreator());
        res.put("backenddate", req.getBackenddate());
        List<Object> emailarray = new ArrayList<>();
        List<Object> namearray = new ArrayList<>();
        for (int i = 0; i < groupdata.getUsers().size(); i++) {
            ApiFuture<DocumentSnapshot> userquery = database.collection("USERS").document(groupdata.getUsers().get(i)).get();
            DocumentSnapshot userquerydata = userquery.get();
            emailarray.add(userquerydata.get("email"));
            namearray.add(userquerydata.get("firstname") + " " + userquerydata.get("lastname"));
        }
        res.put("emailsincluded", emailarray);
        res.put("namesincluded", namearray);
        for (int j = 0; j < groupdata.getUsers().size(); j++) {
            database.collection("USERS").document(groupdata.getUsers().get(j)).collection("REMINDERS").document(req.getReminderid()).set(res);
        }
        database.collection(grouptype).document(groupid).collection("REMINDERS").document(req.getReminderid()).set(res);
        return null;
    }

    @GetMapping("/fetchreminders/{useruid}")
    public Object FetchReminders(@PathVariable String useruid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> ref = database.collection("USERS").document(useruid).collection("REMINDERS").get();
        List<QueryDocumentSnapshot> query = ref.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: query) {
            Map<String , Object> resarray = new HashMap<>();
            resarray.put("groupname", docs.get("groupname"));
            resarray.put("postremindertime", docs.get("postremindertime"));
            resarray.put("reminderpost", docs.get("reminderpost"));
            resarray.put("usernames", docs.get("namesincluded"));
            res.add(resarray);
        }
        
        return new ResponseEntity<List<Object>>(res , HttpStatus.OK);
    }
}