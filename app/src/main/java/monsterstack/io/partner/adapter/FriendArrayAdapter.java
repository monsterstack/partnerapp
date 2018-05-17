package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Friend;

public class FriendArrayAdapter extends ArrayAdapter<Friend> {
    private Friend[] values;
    private Context context;

    public FriendArrayAdapter(@NonNull Context context, @NonNull Friend[] friends) {
        super(context, -1, friends);
        this.values = friends;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendArrayAdapter.ViewHolder viewHolder = null;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null != convertView)
            viewHolder = (FriendArrayAdapter.ViewHolder)convertView.getTag();
        else {
            convertView = inflater.inflate(R.layout.friend_list_item, parent, false);
            viewHolder = new FriendArrayAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.firstLine.setText(values[position].getFirstName() + " " + values[position].getLastName());
        viewHolder.secondLine.setText(values[position].getEmailAddress());
        // change the icon for Windows and iPhone
        Glide.with(context).load(values[position].getAvatar()).placeholder(R.drawable.ic_launcher_background)
                .dontAnimate().into(viewHolder.imageView);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.firstLine)
        TextView firstLine;
        @BindView(R.id.secondLine) TextView secondLine;

        @BindView(R.id.icon)
        CircleImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
