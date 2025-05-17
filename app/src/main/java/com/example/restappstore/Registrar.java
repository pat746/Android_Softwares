package com.example.restappstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    private final String URL = "https://restapisoftware-production.up.railway.app/api/softwares";
    RequestQueue requestQueue;

    EditText etNombre, etVersion, etEspacio, etPrecio;
    Button btnRegistrarSoftware,btnCancelar;

    private void loadUI() {
        etNombre = findViewById(R.id.etNombre);
        etVersion = findViewById(R.id.etVersion);
        etEspacio = findViewById(R.id.etEspacio);
        etPrecio = findViewById(R.id.etPrecio);
        btnRegistrarSoftware = findViewById(R.id.btnRegistrarSoftware);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    /**
     * Muestra un diálogo de confirmación antes de enviar los datos
     */
    private void confirmarRegistro() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Está seguro que desea registrar esta aplicación?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveData();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Envía los datos al backend utilizando POST
     */
    private void saveData() {
        String nombre = etNombre.getText().toString().trim();
        String version = etVersion.getText().toString().trim();
        String espacioStr = etEspacio.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (nombre.isEmpty() || version.isEmpty() || espacioStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int espacio;
        double precio;

        try {
            espacio = Integer.parseInt(espacioStr);
            precio = Double.parseDouble(precioStr);
            if (espacio < 0 || precio < 0) {
                Toast.makeText(this, "Espacio y precio deben ser positivos", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Espacio o precio tienen formato incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject datos = new JSONObject();
        try {
            datos.put("nombre", nombre);
            datos.put("versionsoft", version);
            datos.put("espaciomb", espacio);
            datos.put("precio", precio);
        } catch (Exception e) {
            Log.e("JSON Error", "Error al construir el objeto JSON: " + e.getMessage());
            return;
        }

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Respuesta del servidor", response.toString());

                        Toast.makeText(Registrar.this, "¡Aplicación registrada exitosamente!", Toast.LENGTH_SHORT).show();

                        // Limpiar campos
                        etNombre.setText("");
                        etVersion.setText("");
                        etEspacio.setText("");
                        etPrecio.setText("");

                        // Redirigir a la pantalla de lista
                        Intent intent = new Intent(Registrar.this, Listar.class);
                        startActivity(intent);
                        finish(); // Cierra esta actividad
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error al registrar", error.toString());
                        Toast.makeText(Registrar.this, "Error al registrar. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        loadUI();

        btnRegistrarSoftware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarRegistro(); // Mostrar alerta antes de guardar
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrar.this, MainActivity.class); // Asegúrate de que 'Main' sea el nombre correcto
                startActivity(intent);
                finish(); // Opcional: cierra esta actividad para que no quede en el historial
            }
        });

    }
}
