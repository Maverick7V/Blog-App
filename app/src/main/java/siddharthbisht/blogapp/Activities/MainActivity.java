package siddharthbisht.blogapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import siddharthbisht.blogapp.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private EditText etemail;
    private EditText etpassword;
    private Button btlogin;
    private Button btsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etemail=findViewById(R.id.etEmail);
        etpassword=findViewById(R.id.etPassword);
        btlogin=findViewById(R.id.btlogin);
        btsignup=findViewById(R.id.btsignup);
        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser=firebaseAuth.getCurrentUser();
                if(mUser!=null){
                    Toast.makeText(MainActivity.this,"Signed in",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,PostListActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Not Signed in",Toast.LENGTH_LONG).show();
                }

            }
        };
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etemail.getText().toString())&& !TextUtils.isEmpty((etpassword.getText().toString()))){
                    String email=etemail.getText().toString();
                    String pwd= etemail.getText().toString();
                    login(email,pwd);
                }
            }
        });
        btsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
            }
        });
    }

    private void login(String email, String pwd) {

        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(!task.isSuccessful()){
               FirebaseAuthException e = (FirebaseAuthException )task.getException();
               Toast.makeText(MainActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
               Log.d("Error",e.getMessage().toString());
           }
           else{
               Toast.makeText(MainActivity.this,"Sign in Successful",Toast.LENGTH_SHORT).show();
               startActivity(new Intent(MainActivity.this,PostListActivity.class));
               finish();
           }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuth!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId()==R.id.menu_signout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
