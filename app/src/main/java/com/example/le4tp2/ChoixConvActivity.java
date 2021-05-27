package com.example.le4tp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixConvActivity extends AppCompatActivity implements View.OnClickListener {

    APIInterface apiService;
    String hash = "";
    String login = "";
    Spinner spinner;
    Button btnOK;
    Bundle bdl;
    GlobalState gs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_choix_conversation);
        spinner = findViewById(R.id.choixConversation_choixConv);
        btnOK = findViewById(R.id.choixConversation_btnOK);
        btnOK.setOnClickListener(this);


        bdl = this.getIntent().getExtras();
        Log.i(CAT,bdl.getString("hash"));

        hash = bdl.getString("hash");
        login = bdl.getString("login");

        apiService = APIClient.getClient(this).create(APIInterface.class);
        Call<ListConversation> call1 = apiService.doGetListConversation(hash);
        call1.enqueue(new Callback<ListConversation>() {
            @Override
            public void onResponse(Call<ListConversation> call, Response<ListConversation> response) {
                ListConversation lc = response.body();
                Log.i(gs.TAG,lc.toString());
                remplirSpinner(lc);
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
    public void onClick(View view) {
        Log.i(gs.TAG,"Lancement");
        Conversation conv = (Conversation) spinner.getSelectedItem();
        Log.i(gs.TAG,conv.getId().toString());
        Log.i(gs.TAG,conv.toString());
        Intent iVersShowConv = new Intent(ChoixConvActivity.this,ShowConvActivity.class);
        Bundle bdl = new Bundle();
        bdl.putString("hash",hash);
        bdl.putString("idConv",conv.getId());
        iVersShowConv.putExtras(bdl);
        Log.i(gs.TAG,"GO");
        startActivity(iVersShowConv);
    }

    private void remplirSpinner(ListConversation lc){
        ArrayAdapter<Conversation> adp1 = new ArrayAdapter<Conversation>(this,android.R.layout.simple_spinner_dropdown_item, lc.getConversations());
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp1);
        Log.i(gs.TAG,"Fin Spinner");
    }

    // Afficher les éléments du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Utiliser menu.xml pour créer le menu (Préférences, Mon Compte)
        getMenuInflater().inflate(R.menu.menu, menu);
        if(hash == "" || hash == null) {
            MenuItem item = menu.findItem(R.id.action_account);
            item.setVisible(false);
        }
        return true;
    }
    // Gestionnaire d'événement pour le menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                gs.alerter("Préférences");
                // Changer d'activité pour afficher PrefsActivity
                Intent change2Prefs = new Intent(this,PrefActivity_.class);
                startActivity(change2Prefs);
                break;
            case R.id.action_account :
                gs.alerter("Compte");
                Intent change2Compte = new Intent(ChoixConvActivity.this,CompteActivity.class);
                Bundle bdl = new Bundle();
                bdl.putString("hash",hash);
                bdl.putString("login",login);
                change2Compte.putExtras(bdl);
                startActivity(change2Compte);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
