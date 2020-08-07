package com.google.spsproject.servlets;

import com.google.spsproject.data.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;
import java.util.*;


@WebServlet("/user")
public class User extends HttpServlet {
    Firestore db;
    public User() throws IOException {
        FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
        .setProjectId("summer20-sps-77")
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build();
        this.db = firestoreOptions.getService();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        DocumentReference docRef = db.collection("users").document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        UserInfo UserObject = new UserInfo();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                UserObject.setExists(true);
                UserObject.setEmail(email);
                UserObject.setName(document.get("name").toString());
                UserObject.setAge(Integer.parseInt(document.get("age").toString()));
                UserObject.setGender(document.get("gender").toString());
            } else {
                UserObject.setExists(false);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(UserObject));
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
		if (br != null){
			json = br.readLine();
			System.out.println(json);
		}
        Gson gson = new Gson();
        UserInfo UserObject = gson.fromJson(json, UserInfo.class);
        String email = UserObject.getEmail();
        String name = UserObject.getName();
        int age = UserObject.getAge();
        String gender = UserObject.getGender();
        DocumentReference docRef = db.collection("users").document(email);
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", name);
        docData.put("age", age);
        docData.put("gender", gender);
        ApiFuture<WriteResult> future = db.collection("users").document(email).set(docData);
        try {
            System.out.println("Update time : " + future.get().getUpdateTime());
        } catch(Exception e) {
            e.printStackTrace();
        }
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(UserObject));
    }
}