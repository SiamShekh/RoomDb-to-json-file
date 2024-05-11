package com.JannatiSobdo.nur_e_quran.Introduction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.JannatiSobdo.nur_e_quran.API.Room_DB.Bookmarks.Ayah.BookmarkAyah_Dao;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Bookmarks.Ayah.BookmarkAyah_entity;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Bookmarks.Ayah.BookmarksAyah_DB;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Hadeeth.Category.Hadeeth_Category_Entity;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Hadeeth.Category.getHadeethCategory;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Hadeeth.Hadeeth.Hadeeth_DAO;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Hadeeth.Hadeeth.Hadeeth_Database;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Hadeeth.Sub_Category.Hadeeth_SubCategory_Database;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Quran.SurahDatabase;
import com.JannatiSobdo.nur_e_quran.API.Room_DB.Quran.Surah_Dao;
import com.JannatiSobdo.nur_e_quran.App.MainActivity;
import com.JannatiSobdo.nur_e_quran.Hook.DataExporter;
import com.JannatiSobdo.nur_e_quran.R;
import com.JannatiSobdo.nur_e_quran.surah.view_surah;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class splash extends AppCompatActivity {

    CountDownTimer countDownTimer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                SharedPreferences SurahData = getSharedPreferences("Surah_Info", MODE_PRIVATE);
                String OpennigType = SurahData.getString("OpennigType", "open");
                // What is the type, if type is Quran then open view_surah.java and if type is open then open mainactivtiy.java
                // That type set from MyApplication.java
                if (OpennigType.equals("Quran")){
                    Intent i = new Intent(splash.this, view_surah.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }.start();

        BookmarksAyah_DB db = Room.databaseBuilder(this, BookmarksAyah_DB.class, "Ayah_Bookmark")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        BookmarkAyah_Dao dao = db.bookmarkAyahDao();

        List<BookmarkAyah_entity> Ayah = dao.getAyahBookmark();

        Gson gson = new Gson();
        String ayah = gson.toJson(Ayah);

        Log.d("TAGAYAH", "onCreate: "+ayah);

        File outputDir = new File(getExternalFilesDir(null), "exports");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputDir, "data.json");

        // Step 3: Write JSON to file
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(ayah);
            // Display a toast message to indicate successful file creation
            Toast.makeText(this,  "Data exported to: " + outputFile.getAbsolutePath(),Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}