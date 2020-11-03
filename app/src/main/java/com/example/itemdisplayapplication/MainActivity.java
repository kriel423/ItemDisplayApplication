package com.example.itemdisplayapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemImpl;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonViewItems;
    private Button mButtonUpload;
    private TextView mTextViewFileName;
    private TextView mTextDescription;
    private TextView mTextQuantity;
    private TextView mTextViewShowUploads;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonUpload = findViewById(R.id.button_upload);
        mButtonViewItems = findViewById(R.id.view_items_button);
        mTextViewShowUploads = findViewById(R.id.view_items_button);
        mTextViewFileName = findViewById(R.id.edit_text_title);
        mTextDescription = findViewById(R.id.edit_text_description);
        mTextQuantity = findViewById(R.id.edit_text_quantity);
        mProgressBar = findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.image_upload);

        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress())
                {
                    Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    openFileChooser();
                }
            }
        });

        mButtonViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            mImageUri = data.getData();

            Picasso.get()
                    .load(mImageUri)
                    .into(mImageView);

            //********if the image was to be loaded natively***********

            //           mImageView.setImageURI(mImageUri);

            //********if the image was to be loaded natively***********
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadFile() {
        if(mImageUri != null)
        {
            StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
            + "."+ getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    }, 500);

                    Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(mTextViewFileName.getText().toString().trim(),
                            mTextDescription.getText().toString().trim(),
                            mTextQuantity.getText().toString().trim(),
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uploadId = mDatabaseReference.push().getKey();
                    mDatabaseReference.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() /  snapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            });
        }
        else
        {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}