package com.example.le4tp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sp;
    EditText edtLogin;
    EditText edtPasse;
    CheckBox cbRemember;
    Button btnOK;
    SharedPreferences.Editor editor;
    APIInterface apiService;
    GlobalState gs;
    String hash="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();


        TextInputLayout loginInput = findViewById(R.id.login_edtLogin);
        edtLogin = loginInput.getEditText();

        TextInputLayout passeInput = findViewById(R.id.login_edtPasse);
        edtPasse = passeInput.getEditText();

        cbRemember = findViewById(R.id.login_cbRemember);
        btnOK = findViewById(R.id.login_btnOK);

        btnOK.setOnClickListener(this);
        gs = (GlobalState) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Au (re)chargement de l'activité,
        // Lire les préférences partagées
        if (sp.getBoolean("remember",false)) {
            // et remplir (si nécessaire) les champs pseudo, passe, case à cocher
            cbRemember.setChecked(true);
            edtLogin.setText(sp.getString("login",""));
            edtPasse.setText(sp.getString("passe",""));
        }

        // Vérifier l'état du réseau
        if (gs.verifReseau()) {
            btnOK.setEnabled(true); // activation du bouton
        } else {
            btnOK.setEnabled(false); // désactivation du bouton
        }
    }


    @Override
    public void onClick(View v) {
        // Lors de l'appui sur le bouton OK
        // si case est cochée, enregistrer les données dans les préférences

        apiService = APIClient.getClient(this).create(APIInterface.class);
        Call<AuthResponse> call1 = apiService.doConnect(edtLogin.getText().toString(),edtPasse.getText().toString());
        call1.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NotNull Call<AuthResponse> call, @NotNull Response<AuthResponse> response) {
                Log.i(gs.TAG,call.toString());
                if (response.code() == 202){
                    Log.i(gs.TAG,response.body().toString());
                    Log.i(gs.TAG,call.request().toString());
                    AuthResponse authResponse = response.body();
                    Log.i(gs.TAG,""+authResponse.status);
                    savePrefs();
                    Intent iVersChoixConv = new Intent(LoginActivity.this,ChoixConvActivity.class);
                    Bundle bdl = new Bundle();
                    bdl.putString("hash",authResponse.hash);
                    hash = authResponse.hash;
                    bdl.putString("login",edtLogin.getText().toString());
                    iVersChoixConv.putExtras(bdl);
                    startActivity(iVersChoixConv);
                }else{
                    Log.i(gs.TAG,response.errorBody().toString());
                    gs.alerter("Identifiant Invalide");
                }
                Log.i(gs.TAG,"Done");
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

    private void savePrefs() {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        if (cbRemember.isChecked()) {
            editor.putBoolean("remember", true);
            editor.putString("login", edtLogin.getText().toString());
            editor.putString("passe", edtPasse.getText().toString());
        } else {
            editor.putBoolean("remember", false);
            editor.putString("login", "");
            editor.putString("passe", "");
        }
        editor.commit();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (sp.getBoolean("remember",false)) {
            // et remplir (si nécessaire) les champs pseudo, passe, case à cocher
            cbRemember.setChecked(true);
            edtLogin.setText(sp.getString("login",""));
            edtPasse.setText(sp.getString("passe",""));
        }
    }

}