package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Window;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.CinemaOrdersFragment;

public class CinemaOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cinema_orders);
        String cinemaId = getIntent().getStringExtra(MainActivity.EXTRA_CINEMA_ID);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.cinema_orders_container);
        if (fragment == null) {
            fragment = CinemaOrdersFragment.newInstance(cinemaId);
            manager.beginTransaction().add(R.id.cinema_orders_container, fragment).commit();
        }

    }
}