package com.project.project.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.project.R;
import com.project.project.api.ApiService;
import com.project.project.api.RetroClient;
import com.project.project.data.RecipeAdapter;
import com.project.project.data.model.MyObject;
import com.project.project.util.InternetConnection;
import com.project.project.util.JSONParser;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText search_et;
    private Button search_btn;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(search_et.getText()!=null) {

                    if (InternetConnection.checkConnection(getApplicationContext())) {

                        //Creating an object for our api interface
                        ApiService api = RetroClient.getRetroClient();

                        //Calling Json
                        Call<JsonObject> jsonArrayCall = api.getObject(search_et.getText().toString());

                        jsonArrayCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                if (response.isSuccessful()) {

                                    String responseBody = response.body().toString();
                                    Log.i("draxvel", responseBody);

                                    Type type = new TypeToken<MyObject>() {}.getType();
                                    MyObject myObject = (MyObject) JSONParser.getFromJSONtoObject(responseBody, type);


                                    if (myObject != null) {
                                        RecipeAdapter recipeAdapter = new RecipeAdapter(myObject.getRecipeList());
                                        recyclerView.setAdapter(recipeAdapter);
                                        recipeAdapter.notifyDataSetChanged();
                                    }

                                    showMessage("Find!");
                                } else {
                                    showMessage("Error in Response");
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.i("onResponse", "fail error");
                                showMessage("Fail error");
                            }
                        });
                    }else {
                        showMessage("Check Internet connection!");
                    }
                }else {
                    showMessage("Empty input!");
                }
            }
        });
    }

    private void initView() {
        search_btn = findViewById(R.id.search_btn);
        search_et = findViewById(R.id.search_et);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void showMessage(final String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //maybe not necessarily
    private String convertInput(String str){
        str = str.replaceAll(" ", "+");
        str = str.replaceAll(",", "");
        return str;
    }
}