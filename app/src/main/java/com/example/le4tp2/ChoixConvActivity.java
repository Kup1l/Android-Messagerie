package com.example.le4tp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixConvActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences settings;
    private static final String CAT = "LE4-SI";
    APIInterface apiService;
    String hash;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);
        Bundle bdl = this.getIntent().getExtras();
        Log.i(CAT,bdl.getString("hash"));
        hash = bdl.getString("hash");

        apiService = APIClient.getClient(this).create(APIInterface.class);
        Call<ListConversation> call1 = apiService.doGetListConversation(hash);
        call1.enqueue(new Callback<ListConversation>() {
            @Override
            public void onResponse(Call<ListConversation> call, Response<ListConversation> response) {
                ListConversation lc = response.body();
                Log.i(CAT,lc.toString());
                remplirSpinner(lc);
                Log.i(CAT,"Done");
            }

            @Override
            public void onFailure(Call<ListConversation> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {
        //TODO
    }

    private void remplirSpinner(ListConversation lc){
        Spinner s = findViewById(R.id.choixConversation_choixConv);
        List<String> list = new ArrayList<String>();
        for ( Conversation conv : lc.conversations) {
            Log.i(CAT,conv.toString());
            Log.i(CAT,conv.isActive);
            if(conv.isActive.equals("1")){
                list.add(conv.theme);
            }
        }
        Log.i(CAT,list.toString());
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adp1);
        Log.i(CAT,"Fin Spinner");
    }
}
