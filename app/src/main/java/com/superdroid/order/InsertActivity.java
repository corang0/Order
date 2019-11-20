package com.superdroid.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class InsertActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";
    private static String oid = "";
    static final int getimagesetting=1001;
    private static final int REQUEST_CAMERA = 1;
    String temp = "";

    private EditText mEditTextName;
    private EditText mEditTextType;
    private EditText mEditTextAddress;
    private TextView mTextViewResult;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        mEditTextName = (EditText)findViewById(R.id.editText_main_name);
        mEditTextType = (EditText)findViewById(R.id.editText_main_type);
        mEditTextAddress = (EditText)findViewById(R.id.editText_main_address);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        image = (ImageView)findViewById(R.id.image);

        Bundle extras = getIntent().getExtras();
        oid = extras.getString("oid");

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InsertData task = new InsertData();
                String name = mEditTextName.getText().toString();
                String type = mEditTextType.getText().toString();
                String address = mEditTextAddress.getText().toString();

                task.execute("http://" + IP_ADDRESS + "/insert_store.php", name, type, address, oid);

                mEditTextName.setText("");
                mEditTextType.setText("");
                mEditTextAddress.setText("");
            }
        });

        Button buttonGet = (Button)findViewById(R.id.get);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SetImageActivity.class);
                startActivityForResult(intent, getimagesetting);
            }
        });

        permission_init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==getimagesetting){	//if image change
            if(resultCode==RESULT_OK){
                Bitmap selPhoto = null;
                selPhoto=(Bitmap) data.getParcelableExtra("bitmap");
                image.setImageBitmap(selPhoto);//썸네일
                BitMapToString(selPhoto);
            }
        }
    }

    void permission_init(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {	//권한 거절
            // Request missing location permission.
            // Check Permissions Now

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                // 이전에 권한거절
                // Toast.makeText(this,getString(R.string.limit),Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        } else {	//권한승인
            //Log.e("onConnected","else");
            // mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    public void BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);	//bitmap compress
        byte [] arr=baos.toByteArray();
        String image= Base64.encodeToString(arr, Base64.DEFAULT);
        try{
            temp=URLEncoder.encode(image,"utf-8");
        }catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(InsertActivity.this,
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

            String name = (String)params[1];
            String type = (String)params[2];
            String address = (String)params[3];
            String owner = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&type=" + type + "&address=" + address + "&owner=" + owner + "&image=" + temp;

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
