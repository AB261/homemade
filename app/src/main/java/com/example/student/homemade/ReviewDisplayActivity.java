package com.example.student.homemade;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewDisplayAdapter reviewDisplayAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    //    public String myproviderID = "13";//        FirebaseAuth.getInstance().getUid()
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);

        recyclerView = findViewById(R.id.reviewrv);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reviewDisplayAdapter = new ReviewDisplayAdapter(this, new ArrayList<ReviewInfo>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(reviewDisplayAdapter);

        Log.d("user", mAuth.getUid() + "!");
        mAuth = FirebaseAuth.getInstance();

        fetch();
        Log.d("ME", mAuth.getUid());


    }

    public void fetch() {
        firebaseFirestore.collection("Reviews and Ratings").whereEqualTo("reviewee", mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("BLAH", "kjhsdkh");
                ReviewInfo reviewInfo;
                if (task.isSuccessful()) {
                    Log.d("SUS", "SUC");
                    Log.d("Sze task", task.getResult().size() + " ");
                    Log.d("Sze task", task.getResult().getDocuments() + " ");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("GH", "gh");
//                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d("USER : ", document.get("reviewer").toString());
                        String reviewer = document.get("reviewer").toString();

                        username = findusername(document);
//                        Log.d("Username",username);
//                        reviewInfo = new ReviewInfo(Integer.parseInt(map.get("ratings").toString()), map.get("review").toString(), map.get("reviewID").toString(), map.get("reviewee").toString(), username, map.get("date").toString());
//                        reviewDisplayAdapter.added(reviewInfo);
                    }
                } else {
                    Log.d("ERROR", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    public String findusername(QueryDocumentSnapshot document) {
        String reviewer = document.get("reviewer").toString().trim();
        Log.d("USERHERE","+"+ reviewer);
        final String[] usernamearray = new String[1];
        firebaseFirestore.collection("Consumer").document(reviewer).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                ReviewInfo reviewInfo;
                Log.d("Comp", "co");
                if (task2.isSuccessful()) {
                    Log.d("SKDFHKJDS", "askjfgh");
                    DocumentSnapshot document2 = task2.getResult();
                    Log.d("Task",task2.getResult().get("username").toString()+"!");
                    Log.d("USERINFO ", document2.get("username").toString());
                    username = document2.get("username").toString();
                    Log.d("HERE", username);
                    HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                    Log.d("MAP",map.toString());
                    Log.d("Ratings : ", map.get("ratings").toString());
                    Log.d("Review : ", map.get("review").toString());
                    Log.d("Ratings : ", map.get("reviewID").toString());
                    Log.d("Ratings : ", map.get("reviewee").toString());
                    Log.d("Ratings : ", map.get("reviewer").toString());
                    Log.d("Ratings : ", map.get("timeAndDate").toString());
                    reviewInfo = new ReviewInfo(Integer.parseInt(map.get("ratings").toString()), map.get("review").toString(), map.get("reviewID").toString(), map.get("reviewee").toString(), username, map.get("timeAndDate").toString());
                    reviewDisplayAdapter.added(reviewInfo);
////                         username[0] = document2.get("username").toString();

//                                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                } else {
                    Log.d("INSIDE ERROR", "Error getting documents: ", task2.getException());
                }
            }
        });
//        Log.d("BROOOROROOROR",username);
        return null;
    }
//    public String findusername(QueryDocumentSnapshot document, final HashMap<String, Object> map){
//
//        final String[] usernamearray = new String[1];
//        firebaseFirestore.collection("Consumer").whereEqualTo("id", document.get("reviewer")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
//                ReviewInfo reviewInfo;
//                if (task2.isSuccessful()) {
//                    for (QueryDocumentSnapshot document2 : task2.getResult()) {
//                        Log.d("USERINFO ",document2.get("username").toString());
//                         username = document2.get("username").toString();
//                         Log.d("HERE",username);
//                        reviewInfo = new ReviewInfo(Integer.parseInt(map.get("ratings").toString()), map.get("review").toString(), map.get("reviewID").toString(), map.get("reviewee").toString(), username, map.get("date").toString());
//                        reviewDisplayAdapter.added(reviewInfo);
////                         username[0] = document2.get("username").toString();
//
////                                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
//
//                    }
//                } else {
//                    Log.d("INSIDE ERROR", "Error getting documents: ", task2.getException());
//                }
//            }
//        });
////        Log.d("BROOOROROOROR",username);
//        return null;
//    }
}
