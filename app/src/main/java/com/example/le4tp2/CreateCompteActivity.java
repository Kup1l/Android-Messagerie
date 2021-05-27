package com.example.le4tp2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCompteActivity extends AppCompatActivity implements View.OnClickListener {

    APIInterface apiService;
    String hash;
    EditText edtLogin;
    EditText edtPasse;
    Button btnOK;
    GlobalState gs;
    Bundle bdl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_create_compte);
        edtLogin = findViewById(R.id.create_compte_edtLogin);
        edtPasse = findViewById(R.id.create_compte_edtPasse);
        btnOK = findViewById(R.id.create_compte_btnOK);
        btnOK.setOnClickListener(this);

        bdl = this.getIntent().getExtras();
        hash = bdl.getString("hash");
        if(hash == "" || hash == null){
            Intent change2Login = new Intent(this,LoginActivity.class);
            startActivity(change2Login);
        }
        apiService = APIClient.getClient(this).create(APIInterface.class);
    }

    @Override
    public void onClick(View view) {
        String login = edtLogin.getText().toString();
        String passe = edtPasse.getText().toString();
        if(login != "" || passe != ""){
            Call<ResponseBody> call1 = apiService.doCreateUser(login,passe,hash);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(gs.TAG,response.toString());
                    if(response.code() == 201) {
                        gs.alerter("Compte créé");
                        Intent iVersChoixConv = new Intent(CreateCompteActivity.this,ChoixConvActivity.class);
                        iVersChoixConv.putExtras(bdl);
                        startActivity(iVersChoixConv);
                    }
                    else
                        gs.alerter(response.message());
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

}
