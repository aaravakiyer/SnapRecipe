package com.snaprecipe.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // --- Merged from MainActivity2 ---
    public static final int CAMERA_REQUEST_CODE = 1001;
    private static final int CAMERA_PERMISSION_CODE = 1002;
    // --- End Merged ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Initialize ViewModels here to be shared across fragments
        new ViewModelProvider(this).get(IngredientViewModel.class);
        new ViewModelProvider(this).get(RecipeViewModel.class);

        // Start with the CameraFragment by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CameraFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_camera); // Keep the nav bar selection in sync
        }
    }

    private final BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_camera) {
                    selectedFragment = new CameraFragment();
                } else if (itemId == R.id.nav_ingredients) {
                    selectedFragment = new IngredientListFragment();
                } else if (itemId == R.id.nav_recipes) {
                    selectedFragment = new RecipeListFragment();
                } else if (itemId == R.id.nav_co2) {
                    selectedFragment = new CO2DashboardFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            };


    // --- Merged from MainActivity2 ---

    /**
     * Checks for camera permission and requests it if not granted.
     * If permission is already granted, it opens the camera.
     * This method can be called from any fragment.
     */
    public void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    /**
     * Starts an intent to open the device's camera application.
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // The check is implicitly handled by using the deprecated `startActivityForResult`,
        // but for modern practice, it's good to keep it.
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    /**
     * Handles the result of the permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, open the camera
                openCamera();
            }
            // You might want to show a message to the user if permission is denied.
        }
    }

    /**
     * Handles the result from the camera intent.
     * It receives the captured image bitmap and passes it to the active CameraFragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Find the current fragment and pass the bitmap to it
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof CameraFragment) {
                ((CameraFragment) currentFragment).setCapturedImage(imageBitmap);
            }
        }
    }
    // --- End Merged ---
}
