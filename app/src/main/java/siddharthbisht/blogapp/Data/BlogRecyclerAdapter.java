package siddharthbisht.blogapp.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import siddharthbisht.blogapp.Model.Blog;
import siddharthbisht.blogapp.R;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);
        return new ViewHolder(view,context);
    }
    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, int position) {
        Blog blog=blogList.get(position);
        String imageUrl;
        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());
        java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
        String formattedString=dateFormat.format(new Date(Long.valueOf(blog.getTimeStamp())).getTime());
        holder.timeStamp.setText(formattedString);
        imageUrl=blog.getImage();

        Picasso.with(context)
                .load(imageUrl)
                .into(holder.image);
    }
    @Override
    public int getItemCount() {
        return blogList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView image;
        public TextView timeStamp;
        public String userId;

        public ViewHolder(View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.tvPostTitleList);
            desc=itemView.findViewById(R.id.tvPostTextList);
            timeStamp=itemView.findViewById(R.id.tvDate);
            image=itemView.findViewById(R.id.ivPostImageList);
            userId=null;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: go to next activity
                }
            });
        }
    }
}
