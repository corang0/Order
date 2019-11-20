package com.example.listview1;

import android.app.ProgressDialog;
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

public class PushActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";
    private static String sid = "";

    private EditText mEditTextName;
    private EditText mEditTextType;
    private EditText mEditTextPrice;
    private TextView mTextViewResult;
    private TextView selected_item_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        mEditTextName = (EditText)findViewById(R.id.editText_main_name);
        mEditTextType = (EditText)findViewById(R.id.editText_main_type);
        mEditTextPrice = (EditText)findViewById(R.id.editText_main_price);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        selected_item_textview = (TextView)findViewById(R.id.selected_item_textview);

        Bundle extras = getIntent().getExtras();
        sid = extras.getString("sid");

        selected_item_textview.setText(extras.getString("sname"));

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PushActivity.InsertData task = new PushActivity.InsertData();
                String name = mEditTextName.getText().toString();
                String type = mEditTextType.getText().toString();
                String price = mEditTextPrice.getText().toString();

                task.execute("http://" + IP_ADDRESS + "/insert_product.php", sid, name, type, price);

                mEditTextName.setText("");
                mEditTextType.setText("");
                mEditTextPrice.setText("");
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PushActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String name = (String)params[2];
            String type = (String)params[3];
            String price = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&name=" + name + "&type=" + type + "&price=" + price;

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

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }
}
