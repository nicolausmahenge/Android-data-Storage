package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.HashSet;
import java.util.Set;

import com.example.myapplication.R;  // Ensure this import statement matches your actual package name

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private EditText bioEditText;
    private TextView infoTextView;
    private Button saveButton;
    private Button viewBiosButton;
    private Toolbar toolbar;

    private static final String SHARED_PREFS_NAME = "ProfilePrefs";
    private static final String KEY_BIOS = "bios";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.profile_name);

        profileNameTextView = findViewById(R.id.profileNameTextView);
        bioEditText = findViewById(R.id.bioEditText);
        infoTextView = findViewById(R.id.infoTextView);
        saveButton = findViewById(R.id.saveButton);
        viewBiosButton = findViewById(R.id.viewBiosButton);

        // Get the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        if (!username.isEmpty()) {
            profileNameTextView.setText(username);
        }

        // Load and display the saved bios from SharedPreferences
        Set<String> bios = loadBios();
        updateInfoTextView(bios);

        // Set the save button listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBio();
            }
        });

        // Set the view bios button listener
        viewBiosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayBios();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_dark_theme) {
            applyDarkTheme();
            return true;
        } else if (id == R.id.menu_light_theme) {
            applyLightTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        Toast.makeText(this, R.string.dark_theme_applied, Toast.LENGTH_SHORT).show();
    }

    private void applyLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Toast.makeText(this, R.string.light_theme_applied, Toast.LENGTH_SHORT).show();
    }

    private void saveBio() {
        String bio = bioEditText.getText().toString();
        if (!bio.isEmpty()) {
            // Load existing bios
            Set<String> bios = loadBios();

            // Add the new bio
            bios.add(bio);

            // Save the bios to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(KEY_BIOS, bios);
            editor.apply();

            Toast.makeText(this, R.string.bio_saved, Toast.LENGTH_SHORT).show();
            // Update the profile information with all bios
            updateInfoTextView(bios);
        } else {
            Toast.makeText(this, R.string.enter_bio, Toast.LENGTH_SHORT).show();
        }
    }

    private Set<String> loadBios() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(KEY_BIOS, new HashSet<>());
    }

    private void updateInfoTextView(Set<String> bios) {
        StringBuilder biosText = new StringBuilder();
        for (String bio : bios) {
            biosText.append("- ").append(bio).append("\n");
        }
        infoTextView.setText(biosText.toString());
    }

    private void displayBios() {
        Set<String> bios = loadBios();
        StringBuilder biosText = new StringBuilder();
        for (String bio : bios) {
            biosText.append("- ").append(bio).append("\n");
        }
        Toast.makeText(this, biosText.toString(), Toast.LENGTH_LONG).show();
    }
}
