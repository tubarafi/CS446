package com.example.refresh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.refresh.util.fragmentCallbackListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements fragmentCallbackListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        String menuFragment = getIntent().getStringExtra("menuFragment");
        if (menuFragment != null && menuFragment.equals("RecipeFragment")) {
            bottomNav.setSelectedItemId(R.id.nav_recipe);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = new HomeFragment();

                switch (item.getItemId()) {
                    case R.id.nav_recipe:
                        Bundle bundle = new Bundle();
                        bundle.putString("loadLocation", "main");
                        selectedFragment = new RecipeFragment();
                        selectedFragment.setArguments(bundle);
                        break;
                    case R.id.nav_shop:
                        selectedFragment = new ShoppingFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };

    @Override
    public void onCallback(String fragmentName, @Nullable Bundle param) {
        Fragment frag;
        switch (fragmentName) {
            default:
                frag = new HomeFragment();
            case "home":
                frag = new HomeFragment();
                break;
            case "recipe":
                frag = new RecipeFragment();
                frag.setArguments(param);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
    }
}
