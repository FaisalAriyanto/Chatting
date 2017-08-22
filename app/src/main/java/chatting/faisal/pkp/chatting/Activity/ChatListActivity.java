package chatting.faisal.pkp.chatting.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chatting.faisal.pkp.chatting.Function.HttpRequest;
import chatting.faisal.pkp.chatting.Function.SessionManagerUtil;
import chatting.faisal.pkp.chatting.Model.ChatListModel;
import chatting.faisal.pkp.chatting.Adapter.ListChatAdapter;
import chatting.faisal.pkp.chatting.R;

public class ChatListActivity extends AppCompatActivity {
    private ListView listview;
    private ListChatAdapter adapter;
    private ArrayList<ChatListModel> chatBase = new ArrayList<ChatListModel>();
    private String photoUrl = "https://cdn1.iconfinder.com/data/icons/unique-round-blue/93/user-512.png";
    public static android.view.ActionMode mActionMode;
    private SessionManagerUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        util = new SessionManagerUtil(getApplicationContext());
        listview = (ListView) findViewById(R.id.list_view_chat);
        adapter = new ListChatAdapter(getApplicationContext(), chatBase);
        listview.setAdapter(adapter);

        HttpRequest http = new HttpRequest(getApplicationContext());
        http.chatList(new HttpRequest.SuccessCallback() {
            @Override
            public void onHttpPostSuccess(String result) {
                String myId = util.sessionUserGet("userId");
                try {
                    JSONArray users = new JSONArray(result);
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = new JSONObject(String.valueOf(users.get(i)));
                        ChatListModel chat = new ChatListModel(
                                user.getString("name"),
                                "Test Message " + i,
                                0,
                                photoUrl);
                        chat.id = user.getString("id");
                        if (!chat.id.equals(myId))
                            adapter.add(chat);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("", "");
            }
        }, new HttpRequest.ErrorCallback() {
            @Override
            public void onHttpPostError(VolleyError error) {
                Log.d("", "");
            }
        });
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int nr = 0;

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    adapter.setNewSelection(position, checked);
                } else {
                    nr--;
                    adapter.removeSelection(position);
                }
                mode.setTitle(nr + " selected");
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mActionMode = mode;
                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        nr = 0;
                        adapter.clearSelection();
                        SparseBooleanArray sparseBooleanArray = listview.getCheckedItemPositions();

                        for (int i = sparseBooleanArray.size() - 1; i >= 0; i--)
                            chatBase.remove(sparseBooleanArray.keyAt(i));
                        adapter.notifyDataSetChanged();
                        mode.finish();
                        break;
                    case R.id.select_all:
                        for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                            if (!adapter.isPositionChecked(i)) {
                                listview.setItemChecked(i, true);
                            }
                        }
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                mActionMode = null;
                adapter.clearSelection();
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listview.setItemChecked(position, !adapter.isPositionChecked(position));
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ChatListModel chat = chatBase.get(position);
                Toast.makeText(getApplicationContext(), chat.name, Toast.LENGTH_LONG).show();
                // mListener.onChatSelected(chat);
                Intent i = new Intent(getApplicationContext(), ChatScreenActivity.class);
                Bundle b = new Bundle();
                b.putString("receiverId", chat.id); //Your id
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

}
