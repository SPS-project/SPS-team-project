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
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@WebServlet("/user")
public class User extends HttpServlet {
    private Firestore db;
    private static final Logger logger = Logger.getLogger(User.class.getName());
    private FileHandler fh;
    public User() throws IOException {
        FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
        .setProjectId("summer20-sps-77")
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build();
        this.db = firestoreOptions.getService();
        filehandler = new FileHandler("errorfile.log");
        logger.addHandler(filehandler);
        SimpleFormatter formatter = new SimpleFormatter();
        filehandler.setFormatter(formatter);
    }
    public static boolean ValidateString(String str) {
        if (str != null && !str.trim().isEmpty()) return true;
        return false;
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
                UserObject.setEmail(email);
                UserObject.setName(document.get("name").toString());
                UserObject.setAge(Integer.parseInt(document.get("age").toString()));
                UserObject.setGender(document.get("gender").toString());
            }
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error from User Class ", e);
        }
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(UserObject));
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if (br != null) {
            json = br.readLine();
        }
        Gson gson = new Gson();
        UserInfo UserObject = gson.fromJson(json, UserInfo.class);
        String email = UserObject.getEmail();
        String name = UserObject.getName();
        int age = UserObject.getAge();
        String gender = UserObject.getGender();
        if (ValidateString(email) == false || ValidateString(name) == false || ValidateString(gender) == false || age == 0) {
            response.sendError(400, "Enter valid parameters" );
            return;
        }
        DocumentReference docRef = db.collection("users").document(email);
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", name);
        docData.put("age", age);
        docData.put("gender", gender);
        ApiFuture<WriteResult> future = db.collection("users").document(email).set(docData);
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(UserObject));
    }
}