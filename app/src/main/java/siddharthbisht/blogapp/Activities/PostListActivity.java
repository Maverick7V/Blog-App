package siddharthbisht.blogapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import siddharthbisht.blogapp.Data.BlogRecyclerAdapter;
import siddharthbisht.blogapp.Model.Blog;
import siddharthbisht.blogapp.R;

public class PostListActivity extends AppCompatActivity {
    private FirebaseDatabase mdatabase;
    private DatabaseReference mdatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mdatabase=FirebaseDatabase.getInstance();
        mdatabaseReference=mdatabase.getReference().child("MBlog");
        mdatabaseReference.keepSynced(true);
        blogList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add:
                //launch add post activity
                if(mUser!=null && mAuth!=null){
                    startActivity(new Intent(PostListActivity.this,AddPostActivity.class));
                    finish();
                }
                break;
            case R.id.menu_signout:
                //sign out
                if(mUser!=null && mAuth!=null){
                    mAuth.signOut();
                    startActivity(new Intent(PostListActivity.this,MainActivity.class));
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mdatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    System.out.println("Title: " + blog.getTitle());
                    System.out.println("Description: " + blog.getDesc());
                    blogList.add(blog);
                    Collections.reverse(blogList);
                    blogRecyclerAdapter = new BlogRecyclerAdapter(PostListActivity.this, blogList);
                    recyclerView.setAdapter(blogRecyclerAdapter);
                    blogRecyclerAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
