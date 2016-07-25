package com.example.rotem.beats;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.rotem.beats.Model.Model;

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


}
