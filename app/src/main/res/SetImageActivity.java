package com.example.listview1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SetImageActivity extends AppCompatActivity implements View.OnClickListener {
    static final int camera=2001;
    static final int gallery=2002;
    Button ClosepopBtn,camerapopBtn,gallerypopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);
        init();
    }

    private void init() {
        camerapopBtn=(Button)findViewById(R.id.camerapopBtn);
        gallerypopBtn=(Button)findViewById(R.id.gallerypopBtn);
        ClosepopBtn=(Button)findViewById(R.id.ClosepopBtn);

        gallerypopBtn.setOnClickListener(this);
        camerapopBtn.setOnClickListener(this);
        ClosepopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch(v.getId()){
            case R.id.camerapopBtn:
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, camera);
                break;
            case R.id.gallerypopBtn:
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, gallery);
                break;
            case R.id.ClosepopBtn:
                setResult(RESULT_CANCELED, intent);
                Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }

    }
    @SuppressLint("NewApi")
    private Bitmap resize(Bitmap bm){
        Configuration config=getResources().getConfiguration();
		if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);

        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=new Intent();
        Bitmap bm;
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case camera:
                    bm=(Bitmap) data.getExtras().get("data");
                    bm=resize(bm);
                    intent.putExtra("bitmap",bm);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case gallery:
                    try {
                        bm = MediaStore.Images.Media.getBitmap( getContentResolver(), data.getData());
                        bm=resize(bm);
                        intent.putExtra("bitmap",bm);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }catch(OutOfMemoryError e){
                        Toast.makeText(getApplicationContext(), "이미지 용량이 너무 큽니다.", Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    break;
            }
        }
        else{
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
