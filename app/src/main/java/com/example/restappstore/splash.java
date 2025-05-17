package com.example.restappstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    private static final int DURACION_SPLASH = 2000; // 2 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Espera 2 segundos y pasa al Login
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, MainActivity.class); // Cambia a tu actividad de login real
                startActivity(intent);
                finish(); // Cierra splash para que no regrese con el botón "atrás"
            }
        }, DURACION_SPLASH);
    }
}
