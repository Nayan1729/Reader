package com.example.navigationdrawer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.InputStream;

public class EpubViewerActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        WebView.setWebContentsDebuggingEnabled(true);
        setContentView(R.layout.activity_epub_viewer);
        webView = findViewById(R.id.webView); // Assuming you have a WebView in your layout

        // Get the file path from the intent
        String filePath = getIntent().getStringExtra("filePath");
        Log.e("EpubViewerActivity", "filePath: " + filePath);

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

                String htmlContent = new String(book.getContents().get(0).getData(), "UTF-8");
                Log.e("Hello","Html"+htmlContent);
                String styledHtml = getStyledHtml(htmlContent);
                webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null);
            } else {
                Log.e("EpubViewerActivity", "Failed to open InputStream for Uri");
                Toast.makeText(this, "Failed to open the input stream", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("EpubViewerActivity", "Error loading EPUB", e);
            Toast.makeText(this, "Error loading EPUB: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getStyledHtml(String htmlContent) {
        return "<html><head><style>body { font-family: 'serif'; font-size: 16px; }</style></head><body>" + htmlContent + "</body></html>";
    }
}
