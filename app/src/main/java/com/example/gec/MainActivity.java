package com.example.gec;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    //declaring int variables for fragment positions

    private static final int POS_CLOSE = 0;
    private static final int POS_FIELD = 1;
    private static final int POS_GROUP = 2;
    private static final int POS_PHOTO = 3;
    private static final int POS_SET = 4;
    private static final int POS_VC = 5;
    private static final int POS_PROFILE = 6;
    private static final int POS_SETTING = 7;
    private static final int POS_LOG = 9;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //setting the sliding root navigation properties

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        //loading the icons from the array values
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        //setting their position in the navigation

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_FIELD).setChecked(true),
                createItemFor(POS_GROUP),
                createItemFor(POS_PHOTO),
                createItemFor(POS_SET),
                createItemFor(POS_VC),
                createItemFor(POS_PROFILE),
                createItemFor(POS_SETTING),
                new SpaceItem(170),
                createItemFor(POS_LOG)
        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_FIELD);

    }
// Assigning the color property of selected and non selected variables

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.green))
                .withTextTint(color(R.color.green))
                .withSelectedIconTint(color(R.color.blue))
                .withSelectedTextTint(color(R.color.blue));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

   private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        finish();
        onStop();
        onDestroy();
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    // setting fragment activies upon clicking choice from navigation
    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position){
            case 1:
                DashboardFragment dashboardFragment = new DashboardFragment();
                transaction.replace(R.id.main_container, dashboardFragment);
                break;
            case 2:
                WGFragment wgFragment = new WGFragment();
                transaction.replace(R.id.main_container,wgFragment);
                break;
            case 3:
                Intent plant = new Intent(MainActivity.this,PlantDisease.class);
                startActivity(plant);
                break;
            case 5:
                VirConFragment virConFragment = new VirConFragment();
                transaction.replace(R.id.main_container, virConFragment);
                break;
            case 6:
                UserFragment userFragment = new UserFragment();
                transaction.replace(R.id.main_container, userFragment);
                break;

            case 9:
                finish();
                onStop();
                onDestroy();
                break;
        }
        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }
}