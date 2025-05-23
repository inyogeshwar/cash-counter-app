package com.yogeshwar.cashcounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private EditText[] inputs;
    private TextView totalAmountText;
    private HistoryAdapter adapter;
    private DrawerLayout drawerLayout;
    private TextToSpeech tts;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Setup input fields for denominations
        inputs = new EditText[]{
                findViewById(R.id.input_2000),
                findViewById(R.id.input_500),
                findViewById(R.id.input_200),
                findViewById(R.id.input_100),
                findViewById(R.id.input_50),
                findViewById(R.id.input_20),
                findViewById(R.id.input_10),
                findViewById(R.id.input_5),
                findViewById(R.id.input_2),
                findViewById(R.id.input_1)
        };

        totalAmountText = findViewById(R.id.totalAmountText);

        // Update total when any input changes
        for (EditText input : inputs) {
            input.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateTotal();
                }
            });
        }
        updateTotal();

        // History Adapter
        RecyclerView historyRecyclerView = findViewById(R.id.historyRecyclerView);
        adapter = new HistoryAdapter();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(adapter);

        viewModel.getAllHistory().observe(this, histories -> adapter.submitList(histories));

        // Save Button
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveCount());

        // TTS
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
            }
        });
        Button ttsButton = findViewById(R.id.ttsButton);
        ttsButton.setOnClickListener(v -> speakTotal());

        // Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                findViewById(R.id.toolbar), R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this::onDrawerItemSelected);

        // Banner Ad
        bannerAdView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
    }

    private void updateTotal() {
        int[] denoms = {2000,500,200,100,50,20,10,5,2,1};
        long total = 0;
        for (int i = 0; i < denoms.length; i++) {
            String val = inputs[i].getText().toString();
            int count = 0;
            try { count = Integer.parseInt(val); } catch (Exception ignore) {}
            total += denoms[i] * count;
        }
        totalAmountText.setText("₹" + total);
    }

    private void saveCount() {
        int[] denoms = {2000,500,200,100,50,20,10,5,2,1};
        int[] counts = new int[denoms.length];
        for (int i = 0; i < denoms.length; i++) {
            String val = inputs[i].getText().toString();
            try { counts[i] = Integer.parseInt(val); } catch (Exception ignore) {}
        }
        long total = 0;
        for (int i = 0; i < denoms.length; i++) total += denoms[i]*counts[i];
        CashHistory history = new CashHistory(System.currentTimeMillis(), total,
                counts[0],counts[1],counts[2],counts[3],counts[4],counts[5],counts[6],counts[7],counts[8],counts[9]);
        viewModel.insert(history);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    private void speakTotal() {
        String text = totalAmountText.getText().toString();
        tts.speak("Total amount is " + text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private boolean onDrawerItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_history:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_settings:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_dark_mode:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Dark mode coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_about:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Cash Counter app\nBy Yogeshwar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_rate:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Thanks for rating!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (tts != null) tts.shutdown();
        super.onDestroy();
    }
}