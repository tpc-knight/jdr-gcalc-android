package com.jdr.groupsizecalculator;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jdr.groupsizecalculator.permissions.PermissionsManager;
import com.jdr.groupsizecalculator.permissions.PermissionsManagerImpl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_IMAGE = 2;

    private String mCurrentPhotoPath;
    private String imagePathUriString;

    private PermissionsManager permissionsManager;

    public MainActivity() {
        super();
        permissionsManager = new PermissionsManagerImpl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchCamera(View view) {
        if(permissionsManager.hasStoragePermissions(this)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // TODO -- handle this error in the android way
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.jdr.fileprovider",
                            photoFile);
                    imagePathUriString = photoURI.toString();

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                    galleryAddPic();
                }

            }
        }
    }

    public void launchGallery(View view) {
        Intent selectImageFromGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectImageFromGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(selectImageFromGalleryIntent, "Select Image"), SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO -- refactor this
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Intent plotterIntent = new Intent(this, PlotterActivity.class);
                plotterIntent.putExtra(PlotterActivity.IMAGE_URI, imagePathUriString);
                startActivity(plotterIntent);
            }
            if(requestCode == SELECT_IMAGE) {
                Intent plotterIntent = new Intent(this, PlotterActivity.class);
                plotterIntent.putExtra(PlotterActivity.IMAGE_URI, data.getDataString());
                startActivity(plotterIntent);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = getString(R.string.app_name) + "_" + timeStamp + "_";

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
        if(!storageDir.exists()) {
            boolean dirsCreated = storageDir.mkdirs();
            if(!dirsCreated) {
                //TODO handle this error case
            }
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
