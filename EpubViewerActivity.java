package com.example.navigationdrawer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EpubViewerActivity extends AppCompatActivity {
    private WebView webView;
    private List<String> chapters; // List to hold chapter contents
    private int currentChapterIndex = 0; // Current chapter index

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(true);
        setContentView(R.layout.activity_epub_viewer);
        webView = findViewById(R.id.webView);

        // Get the file path from the intent
        String filePath = getIntent().getStringExtra("filePath");
        Log.e("EpubViewerActivity", "filePath: " + filePath);
        Button previousButton = findViewById(R.id.button_previous);
        Button nextButton = findViewById(R.id.button_next);

        previousButton.setOnClickListener(v -> previousChapter());
        nextButton.setOnClickListener(v -> nextChapter());

        if (filePath != null) {
            loadEpub(Uri.parse(filePath));
        } else {
            Log.e("EpubViewerActivity", "No file path provided");
            Toast.makeText(this, "No file path provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadEpub(Uri epubUri) {
        try {
            Log.e("EpubViewerActivity", "URI: " + epubUri);
            InputStream epubInputStream = getContentResolver().openInputStream(epubUri);

            if (epubInputStream != null) {
                Book book = new EpubReader().readEpub(epubInputStream);
                Log.e("EpubViewerActivity", "Book Title: " + book.getTitle());

                chapters = new ArrayList<>();

                // Extract chapters from the EPUB content
                for (Resource resource : book.getContents()) {
                    if (resource.getMediaType().getName().equals("application/xhtml+xml")) {
                        String content = new String(resource.getData());
                        content = embedImages(content, book);
                        chapters.add(content); // Add chapter to the list
                    }
                }

                // Load the first chapter into the WebView
                if (!chapters.isEmpty()) {
                    loadChapter(currentChapterIndex);
                } else {
                    Log.e("EpubViewerActivity", "No chapters found");
                    Toast.makeText(this, "No chapters found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("EpubViewerActivity", "Failed to open InputStream for Uri");
                Toast.makeText(this, "Failed to open the input stream", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("EpubViewerActivity", "Error loading EPUB", e);
            Toast.makeText(this, "Error loading EPUB: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadChapter(int index) {
        if (index < 0 || index >= chapters.size()) {
            Log.e("EpubViewerActivity", "Chapter index out of bounds");
            return;
        }
        String styledHtml = getStyledHtml(chapters.get(index));
        webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null);
    }

    public void nextChapter() {
        if (currentChapterIndex < chapters.size() - 1) {
            currentChapterIndex++;
            loadChapter(currentChapterIndex);
        } else {
            Toast.makeText(this, "No more chapters", Toast.LENGTH_SHORT).show();
        }
    }

    public void previousChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--;
            loadChapter(currentChapterIndex);
        } else {
            Toast.makeText(this, "You are at the first chapter", Toast.LENGTH_SHORT).show();
        }
    }

    private String embedImages(String content, Book book) {
        // Same implementation as before
        return content; // Return modified content
    }

    private String getStyledHtml(String htmlContent) {
        return "<html><head><style>body { font-family: 'serif'; font-size: 16px; }</style></head><body>" + htmlContent + "</body></html>";
    }
}
