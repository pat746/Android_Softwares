package com.example.restappstore;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class SoftwareAdapter extends ArrayAdapter<software> {

    public SoftwareAdapter(Context context, List<software> softwares) {
        super(context, 0, softwares);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final software software = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_software, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvVersion = convertView.findViewById(R.id.tvVersion);
        TextView tvEspacio = convertView.findViewById(R.id.tvEspacio);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecio);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminar);
        Button btnEditar = convertView.findViewById(R.id.btnEditar);

        tvNombre.setText("Nombre: " + software.nombre);
        tvVersion.setText("Versión: " + software.versionsoft);
        tvEspacio.setText("Espacio: " + software.espaciomb + " MB");
        tvPrecio.setText("Precio: $" + String.format("%.2f", software.precio));

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(software.id, position);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(software, position);
            }
        });

        return convertView;
    }

    private void showEditDialog(final software software, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_edit_software, null);

        final EditText etNombre = dialogView.findViewById(R.id.etNombre);
        final EditText etVersion = dialogView.findViewById(R.id.etVersion);
        final EditText etEspacio = dialogView.findViewById(R.id.etEspacio);
        final EditText etPrecio = dialogView.findViewById(R.id.etPrecio);

        etNombre.setText(software.nombre);
        etVersion.setText(software.versionsoft);
        etEspacio.setText(String.valueOf(software.espaciomb));
        etPrecio.setText(String.valueOf(software.precio));

        new AlertDialog.Builder(getContext())
                .setTitle("Editar Software")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoNombre = etNombre.getText().toString().trim();
                    String nuevaVersion = etVersion.getText().toString().trim();
                    int nuevoEspacio = Integer.parseInt(etEspacio.getText().toString().trim());
                    double nuevoPrecio = Double.parseDouble(etPrecio.getText().toString().trim());

                    // Mostrar confirmación antes de guardar
                    new AlertDialog.Builder(getContext())
                            .setTitle("Confirmar Guardado")
                            .setMessage("¿Deseas guardar los cambios realizados?")
                            .setPositiveButton("Sí", (confirmDialog, whichConfirm) -> {
                                software.nombre = nuevoNombre;
                                software.versionsoft = nuevaVersion;
                                software.espaciomb = nuevoEspacio;
                                software.precio = nuevoPrecio;

                                actualizarSoftware(software, position);
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void actualizarSoftware(software software, final int position) {
        String url = "https://restapisoftware-production.up.railway.app/api/softwares/" + software.id;

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nombre", software.nombre);
            jsonBody.put("versionsoft", software.versionsoft);
            jsonBody.put("espaciomb", software.espaciomb);
            jsonBody.put("precio", software.precio);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    jsonBody,
                    response -> {
                        remove(getItem(position));
                        insert(software, position);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "Software actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(getContext(), "Error al actualizar el software.", Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(getContext()).add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error en el formato de datos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(final int softwareId, final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este software?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarSoftware(softwareId, position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminarSoftware(int softwareId, final int position) {
        String url = "https://restapisoftware-production.up.railway.app/api/softwares/" + softwareId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        remove(getItem(position));
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "Aplicación eliminada exitosamente.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al eliminar. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(jsonRequest);
    }
}
