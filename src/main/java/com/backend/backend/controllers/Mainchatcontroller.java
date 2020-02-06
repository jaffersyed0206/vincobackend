package com.backend.backend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.backend.backend.comps.groups.Chatmessage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@CrossOrigin
@RestController
@RequestMapping("/api/mainchatid")
public class Mainchatcontroller {
    @GetMapping("/getmainchatmessages/{grouptype}/{groupid}/{mainchatid}")
    public Object GetMainChatMessages(@PathVariable String grouptype, @PathVariable String groupid,
    @PathVariable String mainchatid) throws InterruptedException, ExecutionException {
        Firestore database = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = database.collection(grouptype).document(groupid).collection("MAINCHAT").document(mainchatid).collection("MESSAGES").get();
        List<QueryDocumentSnapshot> ref = query.get().getDocuments();
        List<Object> res = new ArrayList<>();
        for (QueryDocumentSnapshot docs: ref) {
            res.add(docs.getData());
        }
        return new ResponseEntity<List<Object>>(res, HttpStatus.OK);
    }

    @MessageMapping("/mainchat/{grouptype}/{groupid}/{mainchatid}")
    @SendTo("/chat/{grouptype}/{groupid}/{mainchatid}")
    public Map<String, Object> MainchatConfig(@DestinationVariable String grouptype, @DestinationVariable String groupid, @DestinationVariable String mainchatid, Chatmessage message) throws InterruptedException {
        Thread.sleep(500);
        Firestore database = FirestoreClient.getFirestore();
        Map<String, Object> res = new HashMap<>();
        res.put("message", message.getMessage());
        res.put("messageid", message.getMessageid());
        res.put("displayname", message.getDisplayname());
        res.put("date", message.getDate());
        database.collection(grouptype).document(groupid).collection("MAINCHAT").document(mainchatid).collection("MESSAGES").document(message.getMessageid()).set(res);
        return res;
    }

    

}