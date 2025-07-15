package com.tencongty.projectprm.activities.parkingowner;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tencongty.projectprm.MainActivity;
import com.tencongty.projectprm.R;
import com.tencongty.projectprm.activities.common.LoginActivity;
import com.tencongty.projectprm.utils.TokenManager;

public class ParkingOwner_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenManager tokenManager = new TokenManager(this);
        if (!tokenManager.hasToken()) {
            // Chưa đăng nhập → chuyển sang LoginActivity
            Intent intent = new Intent(ParkingOwner_MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Kết thúc MainActivity để không quay lại được
            return;
        }

        setContentView(R.layout.activity_parking_owner_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation2);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment2);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }


}
