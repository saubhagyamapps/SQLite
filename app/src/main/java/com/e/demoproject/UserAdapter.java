package com.e.demoproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private List<UserModel> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtGender, txtPost, txtHobby;
        public CircleImageView profile_image;

        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtGender = view.findViewById(R.id.txtGender);
            txtPost = view.findViewById(R.id.txtPost);
            txtHobby = view.findViewById(R.id.txtHobby);
            profile_image = view.findViewById(R.id.profile_image);
        }
    }


    public UserAdapter(Context context, List<UserModel> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserModel userModel = notesList.get(position);
        holder.txtName.setText(userModel.getName());
        holder.txtGender.setText(userModel.getGender());
        holder.txtHobby.setText(userModel.getHobby());
        holder.txtPost.setText(userModel.getPost());
        holder.profile_image.setImageBitmap(decodeBase64(userModel.getImages()));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}