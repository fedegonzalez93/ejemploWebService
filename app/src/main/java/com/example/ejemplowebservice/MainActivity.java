package com.example.ejemplowebservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.net.InetAddress;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edtCodigo,edtProducto,edtPrecio,edtFabricante,et_buscar;
    Button btnCargar,btnBuscar,btnModificar,btnEliminar;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCodigo=findViewById(R.id.et_codigo);
        edtProducto=findViewById(R.id.et_producto);
        edtPrecio=findViewById(R.id.et_precio);
        edtFabricante=findViewById(R.id.et_fabricante);
        et_buscar=findViewById(R.id.et_buscar);
        btnCargar=findViewById(R.id.btn_insertar);
        btnBuscar=findViewById(R.id.btnBuscarP);
        btnModificar=findViewById(R.id.btnModificar);
        btnEliminar=findViewById(R.id.btnEliminar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto("https://vested-console.000webhostapp.com/ejemploWS/buscarProducto.php?codigo="+et_buscar.getText() +"");
            }
        });


        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("https://vested-console.000webhostapp.com/ejemploWS/insertarProducto.php");
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("https://vested-console.000webhostapp.com/ejemploWS/editarProducto.php");
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarproducto("https://vested-console.000webhostapp.com/ejemploWS/eliminarProducto.php");
            }
        });

    }

    private void ejecutarServicio (String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Operacion Exitosa", Toast.LENGTH_SHORT).show();
                limpiarFomulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map <String,String> parametros= new HashMap<String,String>();
                parametros.put("codigo",edtCodigo.getText().toString());
                parametros.put("producto",edtProducto.getText().toString());
                parametros.put("precio",edtPrecio.getText().toString());
                parametros.put("fabricante",edtFabricante.getText().toString());
                return parametros;
            }

        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void buscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtCodigo.setText(et_buscar.getText().toString());
                        edtProducto.setText(jsonObject.getString("producto"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            },new Response.ErrorListener(){
                @Override
                        public void onErrorResponse(VolleyError error){

                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();

            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void eliminarproducto (String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"EL PRODUCTO FUE ELIMINADO", Toast.LENGTH_SHORT).show();
                limpiarFomulario();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map <String,String> parametros= new HashMap<String,String>();
                parametros.put("codigo",edtCodigo.getText().toString());

                return parametros;
            }

        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    private void limpiarFomulario(){
        edtPrecio.setText("");
        edtFabricante.setText("");
        edtProducto.setText("");
        edtCodigo.setText("");
    }

    }

