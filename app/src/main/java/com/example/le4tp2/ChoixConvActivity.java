package com.example.le4tp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixConvActivity extends AppCompatActivity implements RecyclerViewListener {

    APIInterface apiService;
    String hash = "";
    String login = "";
    Bundle bdl;
    RecyclerView recyclerView;
    GlobalState gs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_choix_conversation);
        recyclerView = findViewById(R.id.choixConversation_choixConv2);


        bdl = this.getIntent().getExtras();
        Log.i(gs.TAG,bdl.getString("hash"));

        hash = bdl.getString("hash");
        login = bdl.getString("login");

        apiService = APIClient.getClient(this).create(APIInterface.class);
        Call<ListConversation> call1 = apiService.doGetListConversation(hash);
        call1.enqueue(new Callback<ListConversation>() {
            @Override
            public void onResponse(Call<ListConversation> call, Response<ListConversation> response) {
                ListConversation lc = response.body();
                Log.i(gs.TAG,lc.toString());
                remplirRecycler(lc);
                Log.i(gs.TAG,"Done");
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
        getSupportFragmentManager().findFragmentById(R.id.menu_fragment).setArguments(bdl); //setting menu fragment argument to notify it that we're logged

    }

    @Override
    public void recyclerViewListClicked(Conversation conversation){
        Log.i(gs.TAG,"Lancement");
        Conversation conv = conversation;
        Log.i(gs.TAG,conv.getId().toString());
        Log.i(gs.TAG,conv.toString());
        Intent iVersShowConv = new Intent(ChoixConvActivity.this,ShowConvActivity.class);
        Bundle bdl = new Bundle();
        bdl.putString("hash",hash);
        bdl.putString("login",login);
        bdl.putString("idConv",conv.getId());
        iVersShowConv.putExtras(bdl);
        Log.i(gs.TAG,"GO");
        startActivity(iVersShowConv);
    }


    private void remplirRecycler(ListConversation lc){
        ConversationAdapter adp = new ConversationAdapter(this,this,lc.conversations);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



}
