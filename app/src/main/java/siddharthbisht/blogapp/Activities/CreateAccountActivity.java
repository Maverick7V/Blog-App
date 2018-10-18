package siddharthbisht.blogapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import siddharthbisht.blogapp.R;

public class CreateAccountActivity extends AppCompatActivity {
    private static final int GALLERY_CODE =1 ;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private Button  createAccount;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private ImageButton profilePic;
    private  Uri resultUri;
    private StorageReference mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MUsers");
        mAuth=FirebaseAuth.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");

        mProgressDialog=new ProgressDialog(this);

        firstName=findViewById(R.id.etFirstNameCA);
        lastName=findViewById(R.id.etLastNameCA);
        email=findViewById(R.id.etEmailCA);
        password=findViewById(R.id.etPasswordCA);
        createAccount=findViewById(R.id.btCreateAccount);
        profilePic= findViewById(R.id.ivDp);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch the image
                Intent GalleryIntent=new Intent();
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                GalleryIntent.setType("image/*");
                startActivityForResult(GalleryIntent,GALLERY_CODE);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();
            CropImage.activity(mImageUri).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
            System.out.println("Starting crop procedure");
            System.out.println("request code: "+ CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            System.out.println("result code: "+ resultCode);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                Log.d("crop activity: ","cropping");
                System.out.println("Into the main if statement");
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                System.out.println("result image obtained");
                if (resultCode == RESULT_OK) {
                    System.out.println("result code is ok");
                     resultUri = result.getUri();
                    System.out.println("Uri is obtained: "+ resultUri.toString());
                    profilePic.setImageURI(resultUri);
                    System.out.println("Profile picture updated successfully");

               } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    System.out.println("error: "+ error.getMessage());
                }

            }
        }


    private void createNewAccount(){
        final String fName=firstName.getText().toString();
        final String lName=lastName.getText().toString();
        String Email=email.getText().toString();
        String Password=password.getText().toString();

        if(!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)&& !TextUtils.isEmpty(Email)&& !TextUtils.isEmpty(Password)){
            mProgressDialog.setMessage("Creating Account ..........");
            mProgressDialog.show();

            //Creating user
            Log.d("creating user"," now");
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult!=null){

                        StorageReference imagePath = mFirebaseStorage.child("MBlog_Profile_Pics")
                                .child(resultUri.getLastPathSegment());

                        imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String userId=mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb=mDatabaseReference.child(userId);
                                currentUserDb.child("firstName").setValue(fName);
                                currentUserDb.child("lastName").setValue(lName);
                                currentUserDb.child("image").setValue(resultUri.toString());
                                mProgressDialog.dismiss();

                                //send user to PostList Activity
                                Intent intent=new Intent(CreateAccountActivity.this,PostListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);


                            }
                        });



                    }
                }
            });
        }
    }
}
