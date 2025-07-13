package com.tencongty.projectprm.activities.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.activities.admin.fragments.AdminHomeFragment;
import com.tencongty.projectprm.activities.admin.fragments.AdminProfileFragment;
import com.tencongty.projectprm.activities.admin.fragments.OwnerManagementFragment;
import com.tencongty.projectprm.activities.admin.fragments.ParkingLotManagementFragment;
import com.tencongty.projectprm.activities.admin.fragments.UserManagementFragment;

public class AdminHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_home);

        bottomNavigationView = findViewById(R.id.admin_bottom_nav);

        // Mặc định hiển thị fragment quản lý người dùng
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, new AdminHomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new AdminHomeFragment();
            } else if (id == R.id.nav_user) {
                selectedFragment = new UserManagementFragment();
            } else if (id == R.id.nav_owner) {
                selectedFragment = new OwnerManagementFragment();
            } else if (id == R.id.nav_parking) {
                selectedFragment = new ParkingLotManagementFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new AdminProfileFragment();
            } else {
                return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, selectedFragment)
                    .commit();

            return true;
        });


    }
}
