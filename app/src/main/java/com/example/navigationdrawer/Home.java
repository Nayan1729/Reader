package com.example.navigationdrawer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends Fragment {
    private static final int FILE_SELECT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private FloatingActionButton fabAdd;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the Floating Action Button
        fabAdd = view.findViewById(R.id.fabAdd);

        // Handle button click to open file chooser
        fabAdd.setOnClickListener(v -> openFileChooser());

        return view;
    }

//    private void checkPermissions() {
//        // Check if the READ_EXTERNAL_STORAGE permission is granted
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//        } else {
//            // Permission is granted, open the file chooser
//            openFileChooser();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, open the file chooser
                openFileChooser();
            } else {
                // Permission was denied
                Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/epub+zip");  // Only allow ePub files

        String[] mimeTypes = {"application/epub+zip"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, FILE_SELECT_CODE);  // Trigger file chooser
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                Uri selectedFileUri = data.getData();
                Intent intent = new Intent(getActivity(), EpubViewerActivity.class);
                intent.putExtra("filePath", selectedFileUri.toString());
                startActivity(intent); // Start EpubViewerActivity with the selected file
            }
        }
    }
}
