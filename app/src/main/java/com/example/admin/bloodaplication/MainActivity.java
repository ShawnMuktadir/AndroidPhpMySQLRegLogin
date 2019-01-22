package com.example.admin.bloodaplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.bloodaplication.Registration.RegistrationActivity;
import com.example.admin.bloodaplication.constants.Constants;
import com.example.admin.bloodaplication.Handler.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_seeker,btn_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeObj();

        btn_donor.setOnClickListener(this);
        btn_seeker.setOnClickListener(this);
    }

    private void makeObj() {
        btn_seeker = (Button)findViewById(R.id.btn_seeker);
        btn_donor = (Button)findViewById(R.id.btn_donor);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_seeker){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }else if (v.getId() == R.id.btn_donor){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }
}
