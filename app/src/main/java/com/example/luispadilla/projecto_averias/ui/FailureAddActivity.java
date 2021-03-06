package com.example.luispadilla.projecto_averias.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.luispadilla.projecto_averias.R;
import com.example.luispadilla.projecto_averias.bd.Failure;
import com.example.luispadilla.projecto_averias.bd.ImageResponse;
import com.example.luispadilla.projecto_averias.bd.services.ApiServices;
import com.example.luispadilla.projecto_averias.bd.services.FailureService;
import com.example.luispadilla.projecto_averias.bd.services.ImgurService;
import com.example.luispadilla.projecto_averias.utils.Constants;
import com.example.luispadilla.projecto_averias.utils.Utilities;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureAddActivity extends AppCompatActivity  {

    FailureService failureServices = ApiServices.getFailureServices();
    ImgurService imgurService = ApiServices.getImgurServices();
    @BindView(R.id.failure_name) EditText failureName;
    @BindView(R.id.failure_description) EditText failureDescription;
    @BindView(R.id.failure_type) EditText failureType;
    @BindView(R.id.failure_image) ImageView failurePhoto;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure_add);
        ButterKnife.bind(this);
        failurePhoto.setImageDrawable(getResources().getDrawable(Constants.failurePlaceHolder));
    }
    @OnClick(R.id.failure_image)
    public void photoAction(){
        this.takePicture();
    }

    @OnClick(R.id.btn_add_failure)
    public void addBtnAction() {
        Failure.Builder failureBuilder = new Failure.Builder();
        failureBuilder.id(Utilities.getUUID());
        failureBuilder.date(Utilities.getCurrentDate(null));
        failureBuilder.name(failureName.getText().toString());
        failureBuilder.description(failureDescription.getText().toString());
        failureBuilder.type(failureType.getText().toString());
        this.addFailure(failureBuilder.build());
    }

    public void addFailure(Failure newFailure) {
        Callback<Failure> responseCallback = new Callback<Failure>() {
            @Override
            public void onResponse(Call<Failure> call, Response<Failure> response) {
                if(response.body() == null){
                    FailureAddActivity.this.responseError();
                } else {
                    Utilities.SwitchActivity(FailureAddActivity.this, FaultActivity.class, null);
                }


            }
            @Override
            public void onFailure(Call<Failure> call, Throwable t) {
                FailureAddActivity.this.responseError();
            }
        };
        failureServices.createFailure(newFailure).enqueue(responseCallback);
    }

    public void takePictureAction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = Utilities.createImageFile(this);
            if (photoFile != null) {
                mUri = FileProvider.getUriForFile(this, "com.example.luispadilla.projecto_averias", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(takePictureIntent, Constants.REQUEST_TAKE_PHOTO);
            }
        }
    }


    public void takePicture () {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            takePictureAction();
        } else {
            askForPermission();
        }
    }

    public void askForPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERM_CODE);
    }
    public void responseError(){
        Utilities.showToast(FailureAddActivity.this, "Error adding your failure");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                failurePhoto.setImageBitmap(imageBitmap);
            }catch(Exception e){
                Utilities.showToast(this,"Error taking your picture");
            }
        }
    }

    public void uploadToImgur(String name, String description){
        final Call<ImageResponse> call = null;
//                imgurService.postImage(name, description, "", "",
//                        MultipartBody.Part.createFormData(
//                                "image",
//                                chosenFile.getName(),
//                                RequestBody.create(MediaType.parse("image/*"), chosenFile)
//                        ));

        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response == null) {
                    FailureAddActivity.this.onImgurFail();
                    return;
                }
                if (response.isSuccessful()) {
                    Utilities.showToast(FailureAddActivity.this, "Upload successful !");
                    // Log.d("URL Picture", "http://imgur.com/" + response.body().data.id);
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                FailureAddActivity.this.onImgurFail();
                t.printStackTrace();
            }
        });
    }

    public void onImgurFail(){
        Utilities.showToast(FailureAddActivity.this, "An unknown error has occured.");
    }
}
