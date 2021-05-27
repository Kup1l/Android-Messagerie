package com.example.le4tp2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
        if(this.getArguments().getString("hash")==null){ // if user is not logged
            menu.getItem(0).setVisible(false); // hide action_create_account menu entry
            menu.getItem(2).setVisible(false); // hide action_account menu entry
        }
    }

    // Gestionnaire d'événement pour le menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings :
                // Changer d'activité pour afficher PrefsActivity
                Intent change2Prefs = new Intent(getActivity(),PrefActivity_.class);
                startActivity(change2Prefs);
                break;
            case R.id.action_account :
                Intent change2Account = new Intent(getActivity(), CompteActivity.class);
                Bundle bdlCompte = this.getArguments();
                change2Account.putExtras(bdlCompte);
                startActivity(change2Account);
                break;
            case R.id.action_create_account :
                Intent change2create = new Intent(getActivity(), CreateCompteActivity.class);
                Bundle bdlCreate = this.getArguments();
                change2create.putExtras(bdlCreate);
                startActivity(change2create);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


}