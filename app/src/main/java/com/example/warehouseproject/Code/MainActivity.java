package com.example.warehouseproject.Code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase database;
    public DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        helper = new DBHelper(this);
        database = helper.getWritableDatabase();


    }

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

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    return true;
                }
            };
}