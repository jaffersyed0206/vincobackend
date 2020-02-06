package com.backend.backend.firebaseserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.backend.backend.HiddenApi;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

//import org.springframework.core.io.ClassPathResource;

public class Firebaseapp {
    public FirebaseApp getContextApp() throws IOException, ClassNotFoundException, JSONException {
        JSONObject json = new JSONObject();
        json.put("type", "service_account");
        json.put("project_id", "vinco-6a779");
        json.put("private_key_id", "366fc6d2bb0c3e1da3b025154990cfe7828c2576");
        json.put("private_key", "-----BEGIN PRIVATE KEY-----\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQD9NnkYEzAmGv1v\naEcRC7oiIzrNDskxzPjoi3DLNKBiDyN00Vq8olE3gKQX07NZU33SwCJfy5176Z+8\n2yVlJVpjwTOoGpCzm0HR/PVKcY071c3UKPU2vz9LMylD46+2UBg0XnZQ1gDIgA29\naL9WEJJG3loXnHdF8lZx+eJ1wEVbctLKrm4P8YGl6I49I9rQTBuhCl69H6AOzbE5\niTC4oU57H2z045nId3iT9kZZ2zc/KzrQKw077dL2u0Y88+N7Y3ZD1XXZgEBPA0rg\nYrsxAZupoEZcr+cVr7w4MvXFKYg1ZGcpQwcWllKK5D1W+6ZLG5YT+pT9OREJQzat\nlrNjkj3LAgMBAAECggEALxuRxsVXGVe/G5liU00DuXMlbt3X13CDRDwg5WUqi2O2\nnHaQc3SdOH6xyI8FmFI8n9dH7duDNnjd03RPzjP3T4SzmAwtLITvZ2IwvkNG28Fo\nlBZ0nFVTMKnZfPxghcH75s5GI77UkXilVb4ljpzxocafKzJcTEKMC5NKKs70+FCT\nI+Q5b0j0QeLUtWLPT40eUt5+JfMQABJiqgUfCsAKni5xiVqt2FHS/nqZigQfUssg\n9OgUyYdJBfSqpyzfZO0/NMsj7Cu8U+wXEpYQ40JtUqXNtKlvwBwn/ZhW7QaV5HCe\nW/1OQuHhMqNMZasdKk2FFHQ4FH21DMVLHc92upKtvQKBgQD+2MUiBAcKgipNtFFk\nhxhVhMtzvNgSrgYtSwtA2zIeGbozLzyC+iOvs9ey6YzLO3Ei0fdAD+2XNk06gj9L\nxQ0ToqOHQvQeuuzr/2HgJzTWygOu5zZVeM+KnVDaqyPwfHdpt+dyiF4PMmX4NDRA\nlB/V/nPBZ2P1/8C7eMpOAhCGrwKBgQD+W89haQmqUm1UXHISzABjNN432XvoeqZD\nLisau2hg3gIo17c10V5EDj8u4ksDbBSizNloSrTwVm97VvChdXc7RVapLXTd92Ri\nHoJ9F9GmJL10SlsFTXgJ18QktduUkoXb/jr6WmsdbPRVLYoUm2PdL5EKZGvu5yb1\nt/5TLRdBpQKBgQDPj7vXPZKCg4ty6DcWelVeaZ7O7/LIHAaZ4zISK7UBARE/WOye\n/J4OH5rBdCig6hP2OS54dcCb1LVHvwdQbfh9kow/wU9UrAgFGmYcC7KkcbpYPKZr\nLM9UZVaSEqtd1fHoH8mY5eo91F1qrxITutDHGMwsrEmk9R2DUxjqOhI9rwKBgQCF\nMnC7svSDw5xK9Aose7WsPdAscPApAqk0PuYFnqP/LeL1nljrjtMNUmSV2ZyskxmP\nUzR6H7z2qlzAuvtRoaCpYhsKhqpPXjCsDQwKE8IOfejUD+fwM+wrQ1kMXEs7VytP\n06bw2Q6EnzynqixHtEOsXS9XwNSgi11kiVBeIBuwnQKBgQDlXUGvEYUzFVcrbLHy\ndmFF4xaQMsGlc00quyvHheFQj+i1qpKO1l7fsdJ3W7oGfS3f9WzHvxdYrpLLHGdK\nTCoYhi4AJFCZkg0HQqZIos1LIEXu6PdRvHQJpUY0y4x4fR4DA1f8OqFZgdahMgOg\nk7ig7GRlqWOVxV7VyvSfZmd0aA==\n-----END PRIVATE KEY-----\n");
        json.put("client_email", "firebase-adminsdk-nce3i@vinco-6a779.iam.gserviceaccount.com");
        json.put("client_id", "102893559198084253394");
        json.put("auth_uri", "https://accounts.google.com/o/oauth2/auth");
        json.put("token_uri", "https://oauth2.googleapis.com/token");
        json.put("auth_provider_x509_cert_url", "https://www.googleapis.com/oauth2/v1/certs");
        json.put("client_x509_cert_url", "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-nce3i%40vinco-6a779.iam.gserviceaccount.com");
        String str = json.toString();
        InputStream inputstr = new ByteArrayInputStream(str.getBytes());
        FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(inputstr))
        .setStorageBucket("vinco-6a779.appspot.com")
        .setProjectId("vinco-6a779")
        .setServiceAccountId("firebase-adminsdk-nce3i@vinco-6a779.iam.gserviceaccount.com")
        .setDatabaseUrl(HiddenApi.databaseurl)
        .build();
        FirebaseApp myApp = null;
        List<FirebaseApp> fbapps = FirebaseApp.getApps();
        if (fbapps != null && !fbapps.isEmpty()) {
            for (FirebaseApp app : fbapps) {
             if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                myApp = app;
             }
            }
        } else {
            myApp = FirebaseApp.initializeApp(options);
        }
        return myApp;
    }
}