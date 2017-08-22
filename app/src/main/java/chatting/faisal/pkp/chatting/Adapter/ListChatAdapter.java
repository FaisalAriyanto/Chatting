package chatting.faisal.pkp.chatting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import chatting.faisal.pkp.chatting.Function.CircleTransform;
import chatting.faisal.pkp.chatting.Model.ChatListModel;
import chatting.faisal.pkp.chatting.R;

/**
 * Created by faisal.ariyanto on 10/27/2016.
 */
public class ListChatAdapter extends ArrayAdapter<ChatListModel>{
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    public ListChatAdapter(Context ctx, ArrayList<ChatListModel> chat){
        super(ctx, 0, chat);
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        ChatListModel chat = getItem(position);
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_chat_list, viewgroup, false);
        }
        TextView name = (TextView) view.findViewById(R.id.tvChatName);
        TextView message = (TextView) view.findViewById(R.id.tvChatMessage);
//        TextView status = (TextView) view.findViewById(R.id.tvChatStatus);
        ImageView photo = (ImageView) view.findViewById(R.id.chat_photo_url);

        name.setText(chat.name);
        message.setText(chat.message);
//        status.setText("D");
        Glide.with(getContext()).load(chat.getPhotoUrl())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);
        return view;
    }

}
