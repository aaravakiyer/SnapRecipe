package com.snaprecipe.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.io.IOException;

public class CameraFragment extends Fragment {
    private ImageView imagePreview;
    // FIX: Changed from Button to ImageButton to match the XML layout
    private ImageButton btnCapture, btnGallery;
    private IngredientViewModel ingredientViewModel;
    private Bitmap selectedBitmap;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        // FIX: This line references an ID that doesn't exist and is unused. It has been removed.

        imagePreview = view.findViewById(R.id.image_preview);
        btnCapture = view.findViewById(R.id.btn_capture);
        btnGallery = view.findViewById(R.id.btn_gallery);

        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        setupLaunchers();

        btnCapture.setOnClickListener(v -> checkCameraPermission());
        btnGallery.setOnClickListener(v -> openGallery());

        return view;
    }

    private void setupLaunchers() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(getContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedBitmap = (Bitmap) result.getData().getExtras().get("data");
                        imagePreview.setImageBitmap(selectedBitmap);
                        detectIngredients(selectedBitmap);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().getContentResolver(), imageUri
                            );
                            imagePreview.setImageBitmap(selectedBitmap);
                            detectIngredients(selectedBitmap);
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void detectIngredients(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        labeler.process(image)
                .addOnSuccessListener(labels -> {
                    int detectedCount = 0;
                    for (com.google.mlkit.vision.label.ImageLabel label : labels) {
                        if (label.getConfidence() > 0.7) {
                            long expiryDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L);
                            Ingredient ingredient = new Ingredient(label.getText(), expiryDate);
                            ingredient.setCo2Saved(CO2Calculator.calculateCO2Saved(label.getText()));
                            ingredientViewModel.insert(ingredient);
                            detectedCount++;
                        }
                    }
                    if (detectedCount > 0) {
                        Toast.makeText(getContext(), detectedCount + " ingredients detected!", Toast.LENGTH_SHORT).show();
                        navigateToIngredients();
                    } else {
                        Toast.makeText(getContext(), "No clear ingredients found. Try again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void navigateToIngredients() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new IngredientListFragment())
                .commit();
    }

    public void setCapturedImage(Bitmap imageBitmap) {
        selectedBitmap = imageBitmap;
        imagePreview.setImageBitmap(selectedBitmap);
    }
}
