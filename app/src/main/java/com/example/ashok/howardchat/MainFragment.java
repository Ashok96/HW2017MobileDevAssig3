package com.example.ashok.howardchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akafle on 8/7/17.
 */

public class MainFragment extends Fragment {
    private ListView mlistview;
    private EditText meditText;
    private Button mbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Get references to text views to display database data.
        mlistview = (ListView) v.findViewById(R.id.mlistview);
        meditText = (EditText) v.findViewById(R.id.editText1);
        mbutton = (Button) v.findViewById(R.id.mbutton);


        MessageSource.get(getContext()).getMessage(new MessageSource.MessageListener() {
            @Override
            public void onMessageReceived(List<Message> MessageList) {
                MessageAdapter mMessageAdapter = new MessageAdapter(getActivity());
                mMessageAdapter.setItems(MessageList);
                mlistview.setAdapter(mMessageAdapter);
                mlistview.setSelection(mMessageAdapter.getCount()-1);
            }
        });

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                if(fuser ==null){
                    Toast.makeText(getContext(),"Message not sent, please try again",Toast.LENGTH_SHORT).show();
                    return;
                }
                Message toBeSent = new Message(fuser.getDisplayName(),fuser.getUid(),meditText.getText().toString());
                meditText.setText("");
                MessageSource.get(getContext()).sendMessage(toBeSent);
            }
        });

        return v;
    }

    private class MessageAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private List<Message> mDataSource;

        public MessageAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<Message> articleList) {
            mDataSource.clear();
            mDataSource.addAll(articleList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get model item for this row, and generate view for this row.
            final Message message = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.message_list, parent, false);
            TextView musername = (TextView) rowView.findViewById(R.id.userid);
            TextView mcontent=(TextView) rowView.findViewById(R.id.content);
            musername.setText(message.getmUserName() );
            mcontent.setText(message.getmMessage());


            return rowView;
        }



    }


}
