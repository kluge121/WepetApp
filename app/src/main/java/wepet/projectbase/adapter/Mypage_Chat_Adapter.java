package wepet.projectbase.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wepet.projectbase.R;
import wepet.projectbase.activity.DialogActivity;
import wepet.projectbase.data.Data_Mypage_Chat;

/**
 * Created by coco on 2016-08-03.
 */
public class Mypage_Chat_Adapter extends RecyclerView.Adapter<Mypage_Chat_Adapter.ViewHolder> {


    ArrayList<Data_Mypage_Chat> items;

    public Mypage_Chat_Adapter(ArrayList<Data_Mypage_Chat> items) {
        this.items = items;
    }

    public Mypage_Chat_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_recycler_chat, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setView(items.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DialogActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView mCircleImage;
        public TextView mNameInfo;
        public TextView mTextInfo;
        public TextView mTextRead;
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCircleImage = (CircleImageView) view.findViewById(R.id.circle_image);
            mNameInfo = (TextView) view.findViewById(R.id.nameInfo);
            mTextInfo = (TextView) view.findViewById(R.id.textInfo);
            mTextRead = (TextView) view.findViewById(R.id.textRead);
        }

        public void setView(Data_Mypage_Chat data) {
            this.mCircleImage.setImageResource(data.circleImageWD);
            this.mNameInfo.setText(data.textNameWD);
            this.mTextInfo.setText(data.textInfoWD);
            this.mTextRead.setText(data.textReadWD);
        }
    }
}