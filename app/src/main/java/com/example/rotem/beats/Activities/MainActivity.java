package com.example.rotem.beats.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rotem.beats.Fragments.HomeFragmentTab;
import com.example.rotem.beats.Fragments.MyBeatsFragmentTab;
import com.example.rotem.beats.Model.Model;
import com.example.rotem.beats.MyApplication;
import com.example.rotem.beats.R;

public class MainActivity extends AppCompatActivity {

    Model model = Model.getInstance();

    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab homeTab, myBeatsTab;

    HomeFragmentTab homeFragmentTab = new HomeFragmentTab();
    Fragment myBeatsFragmentTab = new MyBeatsFragmentTab();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getSupportActionBar();




        // Screen handling while hiding ActionBar icon.
        //actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.
        //actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.
        homeTab = actionBar.newTab().setText(R.string.title_home_fragment);
        myBeatsTab = actionBar.newTab().setText(R.string.title_my_beats_fragment);

        // Setting tab listeners.
        homeTab.setTabListener(new TabListener(homeFragmentTab));
        myBeatsTab.setTabListener(new TabListener(myBeatsFragmentTab));




        // Adding tabs to the ActionBar.
        actionBar.addTab(homeTab);
        actionBar.addTab(myBeatsTab);

        // Get current user
        model.getUserNameById(model.getUserId(), new Model.GetUserListener() {
            @Override
            public void onResult(String userName) {
                MyApplication.setCurrentUser(userName);
            }

            @Override
            public void onCancel() {}
        });
    }

    public class TabListener implements ActionBar.TabListener {

        private Fragment fragment;

        // The constructor.
        public TabListener(Fragment fragment) {
            this.fragment = fragment;
        }

        // When a tab is tapped, the FragmentTransaction replaces
        // the content of our main layout with the specified fragment;
        // that's why we declared an id for the main layout.
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.activity_main, fragment);
        }

        // When a tab is unselected, we have to hide it from the user's view.
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

        // Nothing special here. Fragments already did the job.
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }





}
