package siddharthbisht.blogapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

import siddharthbisht.blogapp.Model.Blog;
import siddharthbisht.blogapp.R;

public class AddPostActivity extends AppCompatActivity {
    private static final int GALLETY_CODE = 1;
    private ImageView image;
    private EditText tvTitle;
    private EditText tvDescription;
    private Button btSubmit;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostDatabase;
    private FirebaseDatabase mDatabase;
    private ProgressDialog mprogress;
    private Uri mImageUri;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
        mPostDatabase=mDatabase.getInstance().getReference().child("MBlog");
        image=findViewById(R.id.imageButton);
        tvTitle=findViewById(R.id.etpostTitle);
        tvDescription=findViewById(R.id.etdescription);
        btSubmit=findViewById(R.id.submitPost);
        mprogress=new ProgressDialog(this);
        mStorage= FirebaseStorage.getInstance().getReference();
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setup();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLETY_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLETY_CODE && resultCode== RESULT_OK){
            mImageUri=data.getData();
            image.setImageURI(mImageUri);
        }
    }

    private void setup() {
        mprogress.setMessage("Posting to Blog...");
        mprogress.show();
        final String titleVal=tvTitle.getText().toString().trim();
        final String descVal=tvDescription.getText().toString().trim();
        if(!TextUtils.isEmpty(titleVal)&& !TextUtils.isEmpty(descVal)&& mImageUri!=null){
            //Start uploading to the database.......


            StorageReference filepath=mStorage.child("MBlog_images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mPostDatabase.push();

                    //Create a hashmap that we will push in the database

                   Map<String,String > dataToSave=new HashMap<>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadUrl.toString());
                    dataToSave.put("timeStamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userId",mUser.getUid());
                  /* Blog blog=new Blog();
                   blog.setTitle(titleVal);
                   blog.setDesc(descVal);
                   blog.setImage(downloadUrl.toString());
                   blog.setTimeStamp(String.valueOf(java.lang.System.currentTimeMillis()));
                   blog.setUserId(mUser.getUid());*/
                    newPost.setValue(dataToSave);
                    mprogress.dismiss();
                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
                    finish();
                }
            });

        }
    }
}
