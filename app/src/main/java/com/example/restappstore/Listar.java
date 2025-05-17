package com.example.restappstore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    private final String URL = "https://restapisoftware-production.up.railway.app/api/softwares";

    ListView listView;

    ArrayList<software> listaDatos;
    SoftwareAdapter adapter;
    EditText etBuscarId;
    Button btnBuscar;

    RequestQueue requestQueue;

    private void getData() {
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listaDatos.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                int id = obj.getInt("id");
                                String nombre = obj.getString("nombre");
                                String version = obj.getString("versionsoft");
                                int espacio = obj.getInt("espaciomb");
                                double precio = obj.getDouble("precio");

                                software soft = new software(id, nombre, version, espacio, precio);
                                listaDatos.add(soft);
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }
    private void buscarPorId(int idBuscado) {
        String urlBusqueda = URL + "/" + idBuscado;

        JsonArrayRequest vaciarLista = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // No necesitamos usar esta parte
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlBusqueda,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listaDatos.clear(); // Limpiar lista para mostrar solo uno
                        try {
                            int id = response.getInt("id");
                            String nombre = response.getString("nombre");
                            String version = response.getString("versionsoft");
                            int espacio = response.getInt("espaciomb");
                            double precio = response.getDouble("precio");

                            software soft = new software(id, nombre, version, espacio, precio);
                            listaDatos.add(soft);

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listaDatos.clear();
                        adapter.notifyDataSetChanged();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        listView = findViewById(R.id.listView);
        listaDatos = new ArrayList<>();
        adapter = new SoftwareAdapter(this, listaDatos);
        listView.setAdapter(adapter);
        etBuscarId = findViewById(R.id.etBuscarId);
        btnBuscar = findViewById(R.id.btnBuscar);


        getData();
        btnBuscar.setOnClickListener(v -> {
            String idTexto = etBuscarId.getText().toString().trim();
            if (!idTexto.isEmpty()) {
                try {
                    int idBuscado = Integer.parseInt(idTexto);
                    buscarPorId(idBuscado);
                } catch (NumberFormatException e) {
                    etBuscarId.setError("ID inválido");
                }
            } else {
                getData(); // Si está vacío, vuelve a listar todos
            }
        });

    }
}
