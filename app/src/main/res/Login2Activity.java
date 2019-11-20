package com.example.listview1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login2Activity extends AppCompatActivity {
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private EditText mEditTextID;
    private EditText mEditTextPassword;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mEditTextID = (EditText)findViewById(R.id.editText_login_id);
        mEditTextPassword = (EditText)findViewById(R.id.editText_login_password);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        Button buttonLogin = (Button)findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login2Activity.Login task = new Login2Activity.Login();
                String id = mEditTextID.getText().toString();
                String password = mEditTextPassword.getText().toString();

                task.execute("http://" + IP_ADDRESS + "/login.php", id, password);

                mEditTextID.setText("");
                mEditTextPassword.setText("");
            }
        });

        Button buttonRegister = (Button)findViewById(R.id.button_Register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    class Login extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Login2Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            if (result.length() > 0){
                mTextViewResult.setText(result);

                Intent intent = new Intent(getBaseContext(), InsertActivity.class);
                intent.putExtra("oid", result);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&password=" + password;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {

                Log.d(TAG, "Login: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }
}
