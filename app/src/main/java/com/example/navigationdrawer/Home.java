package com.example.navigationdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends Fragment {
    private static final int FILE_SELECT_CODE = 1;
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
                // Handle the selected file (e.g., display file info or process it)
                Toast.makeText(getContext(), "Selected file: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
