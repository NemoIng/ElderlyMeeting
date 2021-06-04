package com.example.elderlymeeting.ui.registration;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elderlymeeting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class RegisterPicture extends AppCompatActivity{

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button selectImageBtn, confirmBtn;

    private ImageView imageProfile;

    public Uri filePath;
    private StorageTask uploadTask;

    StorageReference storageReference;
    private FirebaseAuth mAuth;

    //let the user pick a profile picture
    // Credit to: https://www.simplifiedcoding.net/firebase-storage-tutorial-android/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pff);
        storageReference = FirebaseStorage.getInstance().getReference("Images");

        imageProfile = (ImageView) findViewById(R.id.imageProfile);

        selectImageBtn = (Button) findViewById(R.id.selectImage);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelector();
            }
        });
        confirmBtn = (Button) findViewById(R.id.confirmButton);
        //upload picture to database
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(RegisterPicture.this, "Upload in progress",
                            Toast.LENGTH_LONG).show();
                }
                uploadPicture();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            filePath = data.getData();
            imageProfile.setImageURI(filePath);
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),
                        filePath);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                imageProfile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void imageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select your profile picture"),
                PICK_IMAGE_REQUEST);
    }

    //upload picture to database
    private void uploadPicture () {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String id = firebaseUser.getUid();

        StorageReference storageReference = this.storageReference.child(id);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(id)
                .child("profilePicture");

        //put picture in firebase storage
        uploadTask = storageReference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {@Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    String url = Objects.requireNonNull(task.getResult()).toString();
                                    //put image url in database
                                    databaseReference.setValue(url);
                                    Toast.makeText(RegisterPicture.this, "Uploaded",
                                            Toast.LENGTH_SHORT).show();
                                    //Go to hobby screen
                                    startActivity(new Intent(RegisterPicture.this,
                                            RegisterHobbies.class));
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPicture.this, "Failed "+e
                                    .getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
}
