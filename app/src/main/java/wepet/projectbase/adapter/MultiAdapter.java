package wepet.projectbase.adapter;

/**
 * Created by ccei on 2016-08-19.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wepet.projectbase.R;
import wepet.projectbase.activity.Chat_Data;

/**
 * Created by dongja94 on 2016-01-19.
 */
public class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Chat_Data> items;

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;

    public MultiAdapter(ArrayList<Chat_Data> data){
        items = data;
    }
    //왼쪽인지오른쪽인지 결정
    @Override
    public int getItemViewType(int position) {

        Chat_Data data = items.get(position);
        switch (data.type) {
            case 0 :
                return VIEW_TYPE_LEFT;
            case 1 :
                return VIEW_TYPE_RIGHT;
        }
        return super.getItemViewType(position);

    }

    //재활용뷰를 위한 모든 서브뷰

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        public TextView chatSender, senderDate;
        public CircleImageView chatImage;
        public LeftViewHolder(View itemView) {
            super(itemView);
            chatSender = (TextView)itemView.findViewById(R.id.chatSender);
            chatImage = (CircleImageView) itemView.findViewById(R.id.chatImage);
            senderDate = (TextView)itemView.findViewById(R.id.senderDate);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        public TextView chatDestinataire, destinatair_Date;
        public RightViewHolder(View itemView) {
            super(itemView);
            chatDestinataire = (TextView)itemView.findViewById(R.id.chatDestinataire);
            destinatair_Date = (TextView)itemView.findViewById(R.id.destinatair_Date);
        }
    }

    //왼쪽인지 오른쪽인지 결정한 것을 가지고 leftview, rightview를 만듦.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case VIEW_TYPE_LEFT :
                view = inflater.inflate(R.layout.dialog_left_layout, parent, false);
                return new LeftViewHolder(view);

            case VIEW_TYPE_RIGHT :
                view = inflater.inflate(R.layout.dialog_right_layout, parent, false);
                return new RightViewHolder(view);
        }
        return null;
    }

    //leftview와 rightview의 내용물을 만들어줌, 만들어진 viewholder에 데이터를 넣어줌
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat_Data tempData;

        switch (getItemViewType(position)) {
            case VIEW_TYPE_LEFT :
                tempData = items.get(position);
                LeftViewHolder leftHolder = (LeftViewHolder)holder;
                leftHolder.chatSender.setText(tempData.message);
                leftHolder.senderDate.setText(tempData.currentDate);
                leftHolder.chatImage.setImageResource(tempData.circleImage);
                break;

            case VIEW_TYPE_RIGHT :
                tempData = items.get(position);
                RightViewHolder rightHolder = (RightViewHolder)holder;
                rightHolder.chatDestinataire.setText(tempData.message);
                rightHolder.destinatair_Date.setText(tempData.currentDate);
                break;
        }
    }
    @Override
    public int getItemCount() {
        return  items.size();
    }
}