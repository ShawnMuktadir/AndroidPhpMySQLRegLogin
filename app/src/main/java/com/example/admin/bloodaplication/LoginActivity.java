package com.example.admin.bloodaplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.admin.bloodaplication.SharedPrefManager.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText et_loginusername,et_loginpassword;
    private Button btn_login;
    private Spinner spinner_logincategory;
    private TextView tv_signup;
    public TextView txt_bloodgroup;

    private ProgressDialog progressDialog;

    private static final String jsonURL = "http://192.168.0.101/Blood/includes/getjson.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPreferenceManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            return;
        }

        makeObj();

        progressDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);

        //for blood category spinner
        ArrayAdapter<CharSequence> categoryadapter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_logincategory.setAdapter(categoryadapter);
        spinner_logincategory.setOnItemSelectedListener(this);


    }

    private void makeObj() {
        et_loginusername = (EditText)findViewById(R.id.et_loginusername);
        et_loginpassword = (EditText)findViewById(R.id.et_loginpassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        spinner_logincategory = (Spinner) findViewById(R.id.spinner_logincategory);
        txt_bloodgroup = (TextView)findViewById(R.id.txt_bloodgroup);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login){
            userLogin();
            getJSON(jsonURL);
        }
        else if (v.getId() == R.id.tv_signup){
            startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
        }
    }

    private String getJSON(final String jsonUrl) {
        class GetJSON extends AsyncTask <String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    StringBuilder stringBuilder = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        stringBuilder.append(json);
                    }
                    return stringBuilder.toString().trim();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return jsonUrl;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                txt_bloodgroup.setText(s);

                Intent intent = new Intent( getApplicationContext(), ProfileActivity.class);
                intent.putExtra( "paramName", txt_bloodgroup.getText().toString() );
                startActivity( intent );
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute(jsonUrl);

        return jsonUrl;
    }


    private void userLogin(){
        final String username = et_loginusername.getText().toString().trim();
        final String password = et_loginpassword.getText().toString().trim();
        final String bloodcategory = spinner_logincategory.getSelectedItem().toString();


        progressDialog.setMessage("Checking User...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (!object.getBoolean("error")){
                                SharedPreferenceManager.getInstance(getApplicationContext()).userLogin(
                                                                                            object.getInt("id"),
                                                                                            object.getString("username"),
                                                                                            object.getString("email"),
                                                                                            object.getString("category"));


//                                Toast.makeText(getApplicationContext(), "User Login Successful!!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                finish();

                            }else {

                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",username);
                map.put("password",password);
                map.put("category",bloodcategory);

                return map;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        et_loginusername.setText("");
        et_loginpassword.setText("");
        spinner_logincategory.setSelection(0);
        spinner_logincategory.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String bloodcategoryText = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(), bloodcategoryText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
