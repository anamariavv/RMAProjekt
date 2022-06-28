package com.example.rmaprojekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);

        String text = currentComment.getText();
        String likeCount = currentComment.getLikeCount();
        String dislikeCount = currentComment.getDislikeCount();
        String author = currentComment.getAuthor();
        String published = currentComment.getPublished();

        holder.text.setText(text);
        holder.likes.setText(likeCount);
        holder.dislikes.setText(dislikeCount);
        holder.author.setText(author);
        holder.published.setText(published);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView likes;
        public TextView dislikes;
        public TextView author;
        public TextView published;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.comment_text);
            likes = itemView.findViewById(R.id.like_count);
            dislikes = itemView.findViewById(R.id.dislike_count);
            author = itemView.findViewById(R.id.author);
            published = itemView.findViewById(R.id.published);
        }
    }
}
