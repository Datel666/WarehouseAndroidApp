package com.example.warehouseproject.Code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.warehouseproject.Fragments.HistoryFragment;
import com.example.warehouseproject.Fragments.HomeFragment;
import com.example.warehouseproject.Fragments.ItemListFragment;
import com.example.warehouseproject.Fragments.NewItemFragment;
import com.example.warehouseproject.Fragments.SearchFragment;
import com.example.warehouseproject.R;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity class
 *
 * Класс содержит функционал для activity_main layout
 */
public class MainActivity extends AppCompatActivity {


    //region variables
    public SQLiteDatabase database;
    public DBHelper helper;
    public Intent intent;
    public Activity act;
    public Context con;
    public BottomNavigationView bottomNav;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeValues();
        initializeViews();

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    /**
     * Инициализация значений переменных
     */
    private void initializeValues(){
        act = this;
        con = getApplicationContext();
        helper = new DBHelper(this);
        database = helper.getWritableDatabase();
    }

    /**
     * Инициализация форм
     */
    private void initializeViews(){
        bottomNav = findViewById(R.id.bottom_navbar);
    }

    /**
     * Обработчик взаимодействия с навигационной панелью
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_itemlist:
                            selectedFragment = new ItemListFragment();
                            break;
                        case R.id.nav_add:
                            selectedFragment = new NewItemFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_history:
                            selectedFragment = new HistoryFragment();
                            break;
                        }
                        if(selectedFragment!=null)
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();
                        }
                    return true;
                }
            };
}