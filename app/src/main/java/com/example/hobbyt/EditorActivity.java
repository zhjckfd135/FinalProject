package com.example.hobbyt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.modules.Record;
import com.example.hobbyt.utils.UrlMap;

import java.io.UnsupportedEncodingException;

public class EditorActivity extends AppCompatActivity {

    Record record;
    EditText editText;
    EditText titleEditText;
    Button backButton;
    Button saveButton;

    SharedPreferences loginSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        InitFields(savedInstanceState);
        SetListeners();
    }

    private void InitFields(Bundle bundle) {
        editText = findViewById(R.id.editTextEditor);
        backButton = findViewById(R.id.editorBackButton);
        saveButton = findViewById(R.id.editorSaveButton);
        titleEditText = findViewById(R.id.editTextTitle);

        loginSharedPreferences = getSharedPreferences(LoginFragment.LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        record = (Record) getIntent().getSerializableExtra("record");
        if(record != null){
            editText.setText(record.getData());
            titleEditText.setText(record.getTitle());
        }
    }

    private void SetListeners() {
        backButton.setOnClickListener(v -> {
            onCreateBackDialog(this.getIntent().getExtras()).show();
        });

        saveButton.setOnClickListener(v -> {
            saveRecord();
        });
    }

    private void GoToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveRecord(){
        RequestQueue queue = Volley.newRequestQueue(this);

        saveButton.setText("Saving...");
        saveButton.setClickable(false);
        backButton.setClickable(false);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, getUrl().generateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("success")){
                    Toast.makeText(getBaseContext(), "Saved Successful", Toast.LENGTH_LONG).show();
                    GoToMain();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowError(error);
            }
        });

        queue.add(stringRequest);
    }

    private UrlMap getUrl(){
        UrlMap urlMap = null;
        if(record == null){
            urlMap = new UrlMap(UrlBase.ADD_RECORD);
            urlMap.put("user_id", loginSharedPreferences.getString(LoginFragment.USER_ID_PREFERENCES, ""));
            urlMap.put("title", titleEditText.getText().toString());
            urlMap.put("data", editText.getText().toString());

        }else {
            urlMap = new UrlMap(UrlBase.UPDATE_RECORD);
            urlMap.put("record_id", Integer.toString(record.getId()));
            urlMap.put("title", editText.getText().toString());
            urlMap.put("data", titleEditText.getText().toString());
        }



        return urlMap;
    }

    private void ShowError(VolleyError error) {
        saveButton.setText("Save");
        saveButton.setClickable(true);
        backButton.setClickable(true);
        String response;
        try {
            response = new String(error.networkResponse.data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            response = "UnsupportedEncodingException";
            Log.e("SignIn", e.toString());
        }
        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
        Log.w("SignUp" , error.toString());
    }

    private Dialog onCreateBackDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit?")
                .setMessage("Everything that is written will not be saved")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GoToMain();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}