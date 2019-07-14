package com.lco.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {
    EditText edtemail,edtpassword,edtname,edtuser;
    String email,password,username,name,about;
    Button bsignup;
    StorageReference postrefrance;
    Uri uri;
    CircleImageView profile_image;
     private FirebaseAuth firebaseAuth;
  private DatabaseReference mDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bsignup = findViewById(R.id.Signupbutton);
        edtemail = findViewById(R.id.editemail);
        edtpassword = findViewById(R.id.editpass);
        edtname = findViewById(R.id.editname);
        edtuser = findViewById(R.id.edituser);
        profile_image = findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(this);

        firebaseAuth= FirebaseAuth.getInstance();
      //mDatabase = FirebaseDatabase.getInstance().getReference();
        postrefrance= FirebaseStorage.getInstance().getReference("insta");
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }
    void pickImage()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(Intent.createChooser(intent,"select image"),1002);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1002){
            try {
                uri=data.getData();
                Bitmap bm= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                profile_image.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }
        //the end  onActivityResult
    }
    public void Register()
    {
        email = edtemail.getText().toString();
        password = edtpassword.getText().toString();
        name=edtname.getText().toString();
        username=edtuser.getText().toString();
        if (email.isEmpty()) {

        }

        else  if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();

        }
        else {


            progressDialog.setMessage("Siging User....");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                if (uri!=null)
                                {
                                    WriteDetails(uri);
                                }

                            } else {


                                Toast.makeText(signup.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        }
                    });

        }

    }
    public void WriteDetails(Uri uri)
    {


        final FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String id=currentuser.getUid();
        final ProgressDialog progressDialog=new ProgressDialog(signup.this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        UploadTask uploadTask;
        StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/jpeg").build();
        uploadTask=postrefrance.child("insta_"+id).putFile(uri,metadata);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url=taskSnapshot.getMetadata().getReference().toString();
                final User user = new User(id, name,email,username,url,about);
                mDatabase.child("Users").child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "User Added ", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i = new Intent(signup.this,Home.class);
                        startActivity(i);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    }


