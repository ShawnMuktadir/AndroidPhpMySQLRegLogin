package com.example.admin.bloodaplication.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.admin.bloodaplication.Handler.RequestHandler;
import com.example.admin.bloodaplication.LoginActivity;
import com.example.admin.bloodaplication.ProfileActivity;
import com.example.admin.bloodaplication.R;
import com.example.admin.bloodaplication.SharedPrefManager.SharedPreferenceManager;
import com.example.admin.bloodaplication.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private EditText et_email,et_username,et_password,et_mobile,et_address;
    private Spinner spinner_bloodgroup, spinner_category;
    private Button btn_signup;
    private TextView tv_reglogin;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (SharedPreferenceManager.getInstance(this).isLoggedIn()){

            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            return;
        }

        makeObj();

        progressDialog = new ProgressDialog(this);

        btn_signup.setOnClickListener(this);
        tv_reglogin.setOnClickListener(this);

        //for blood group spinner
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.blood_group,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_bloodgroup.setAdapter(arrayAdapter);
        spinner_bloodgroup.setOnItemSelectedListener(this);

        //for blood category spinner
        ArrayAdapter<CharSequence> categoryadapter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_category.setAdapter(categoryadapter);
        spinner_category.setOnItemSelectedListener(this);

    }

    private void makeObj() {
        et_email = (EditText)findViewById(R.id.et_email);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_address = (EditText)findViewById(R.id.et_address);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        spinner_bloodgroup = (Spinner)findViewById(R.id.spinner_bloodgroup);
        tv_reglogin = (TextView)findViewById(R.id.tv_reglogin);
        spinner_category = (Spinner)findViewById(R.id.spinner_category);
    }


    private void registerUser() {
        final String email = et_email.getText().toString().trim();
        final String username = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        final int phonenumber = Integer.parseInt(et_mobile.getText().toString().trim());
        final String bloodgroup = spinner_bloodgroup.getSelectedItem().toString();
        final String address = et_address.getText().toString().trim();
        final String bloodcategory = spinner_category.getSelectedItem().toString();

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("username",username);
                params.put("password",password);
                params.put("phonenumber", String.valueOf(phonenumber));
                params.put("bloodgroup",bloodgroup);
                params.put("address",address);
                params.put("category",bloodcategory);

                return params;
            }
        };

        //using singleton pattern
        if (isEmailValid(email)){
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
        else if (!isEmailValid(email)){
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), "Please insert valid email", Toast.LENGTH_SHORT).show();
            et_email.setText("");
        }


        et_email.setText("");
        et_username.setText("");
        et_password.setText("");
        et_mobile.setText("");
        spinner_bloodgroup.setSelection(0);
        spinner_bloodgroup.setEnabled(true);
        et_address.setText("");
        spinner_category.setSelection(0);
        spinner_category.setEnabled(true);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signup){
            registerUser();
        }
        else if (v.getId() == R.id.tv_reglogin){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }

    public boolean isEmailValid(String email)
    {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String bloodgroupText = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(), bloodgroupText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
