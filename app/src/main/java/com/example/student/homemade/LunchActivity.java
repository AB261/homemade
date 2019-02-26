package com.example.student.homemade;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunchActivity extends AppCompatActivity {
    Button lunch_upload;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int menuID;
    int flag=0;

    private ArrayList<String> menu_items = new ArrayList<String>();
    public boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            // s is not numeric
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);


        //PLEASE PASS provider ID to this activity from login page
        final int myproviderID = 13;
        //PLEASE PASS provider ID to this activity from login page


        final Spinner spinner = (Spinner) findViewById(R.id.appetizer_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_array,  android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText spinner_quantity = (EditText) findViewById(R.id.appetizer_spinner_quantity);



        final Spinner spinner1 = (Spinner) findViewById(R.id.main_course_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.food_array,  android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        final EditText spinner1_quantity = (EditText) findViewById(R.id.main_course_spinner_quantity);


        final Spinner spinner2 = (Spinner) findViewById(R.id.dessert_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.food_array,  android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        final EditText spinner2_quantity = (EditText) findViewById(R.id.dessert_spinner_quantity);

        final Spinner spinner3 = (Spinner) findViewById(R.id.lunch_drinks_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.food_array,  android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        final EditText spinner3_quantity = (EditText) findViewById(R.id.lunch_drinks_spinner_quantity);

        final EditText additional_menu = (EditText) findViewById(R.id.additional_lunch);

        db.collection("Menu").orderBy("menuID", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("THIS IS  ", document.getId() + " => " + document.getData());
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                        Log.d("MENUID =  ", "" + map.get("menuID"));

                        menuID = Integer.parseInt(map.get("menuID").toString());
                    }
                } else {
                    Log.d("R", "Error getting documents: ", task.getException());
                }
            }
        });


        lunch_upload = findViewById(R.id.lunch_upload);

        lunch_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                CharSequence text;

                String item = spinner.getSelectedItem().toString();
                String item_price = spinner_quantity.getText().toString().trim().length() + "";
                String item1 = spinner1.getSelectedItem().toString();
                String item1_price = spinner1_quantity.getText().toString().trim().length() + "";
                String item2 = spinner2.getSelectedItem().toString();
                String item2_price = spinner2_quantity.getText().toString().trim().length() + "";
                String item3 = spinner3.getSelectedItem().toString();
                String item3_price = spinner3_quantity.getText().toString().trim().length() + "";
                String item4 = additional_menu.getText().toString();

                String item_cost = spinner_quantity.getText().toString();
                String item1_cost = spinner1_quantity.getText().toString();
                String item2_cost = spinner2_quantity.getText().toString();
                String item3_cost = spinner3_quantity.getText().toString();

                Log.d("item", item);
                Log.d("item_price_length", "" + item_price);
                Log.d("item_price", "" + item_cost);
                Log.d("item1", item1);
                Log.d("item1_quantity", "" + item1_price);
                Log.d("item2", item2);
                Log.d("item2_quantity", "" + item2_price);
                Log.d("item3", item3);
                Log.d("item3_quantity", "" + item3_price);
                Log.d("additional_menu", item4);
                String[] elements = item4.split(",");
                List<String> fixedLenghtList = Arrays.asList(elements);
                if (!item4.equals(""))
                    menu_items = new ArrayList<String>(fixedLenghtList);

                if (!item.equals("None") && !item_price.equals("0") && isNumeric(item_cost))
                    menu_items.add(item + "( " + item_cost + " )");
                else if ((!item.equals("None") && item_price.equals("0")) || !isNumeric(item_cost)) {
                    text = "Price not valid";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item.equals("None") && !item_price.equals(("0"))){
                    text = "Menu not entered with respective price";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }


                if (!item1.equals("None") && !item1_price.equals("0") && isNumeric(item1_cost))
                    menu_items.add(item1 + "( " + item1_cost + " )");
                else if ((!item1.equals("None") && item1_price.equals("0")) || !isNumeric(item1_cost)) {
                    text = "Price not valid";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item1.equals("None") && !item1_price.equals(("0"))){
                    text = "Menu not entered with respective price";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                if (!item2.equals("None") && !item2_price.equals("0") && isNumeric(item2_cost))
                    menu_items.add(item2 + "( " + item2_cost + " )");
                else if ((!item2.equals("None") && item2_price.equals("0")) || !isNumeric(item2_cost)) {
                    text = "Price not valid";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item2.equals("None") && !item2_price.equals(("0"))){
                    text = "Menu not entered with respective price";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                if (!item3.equals("None") && !item3_price.equals("0") && isNumeric(item3_cost))
                    menu_items.add(item3 + "( " + item3_cost + " )");
                else if ((!item3.equals("None") && item3_price.equals("0")) || !isNumeric(item3_cost)) {
                    text = "Price not valid";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(item3.equals("None") && !item3_price.equals(("0"))){
                    text = "Menu not entered with respective price";
                    flag = 1;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                Log.d("ACTUAL MENU ID ", "" + menuID);

                Log.d("Array : ", menu_items.toString());

                Map<String, Object> menu_item = new HashMap<>();
                menu_item.put("content", menu_items);
                menu_item.put("menuID", menuID + 1);
                menu_item.put("provider", myproviderID);
                menu_item.put("menu_type", "Lunch");

                db.collection("Menu").document("" + (menuID + 1)).set(menu_item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Su", "DocumentSnapshot successfully written!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Fa", "Error writing document", e);
                            }
                        });


                db.collection("Menu").document("" + menuID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Delete", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Error Delete", "Error deleting document", e);
                            }
                        });

                text = "Menu uploaded to database";

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(LunchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
