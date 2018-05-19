package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Member;

public class MemberArrayAdapter extends ArrayAdapter<Member> {
    private Context context;
    private Member[] values;

    public MemberArrayAdapter(@NonNull Context context, Member[] members) {
        super(context, -1, members);
        this.values = members;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupArrayAdapter.ViewHolder viewHolder = null;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null != convertView)
            viewHolder = (GroupArrayAdapter.ViewHolder)convertView.getTag();
        else {
            convertView = inflater.inflate(R.layout.member_list_item, parent, false);
            viewHolder = new GroupArrayAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.firstLine.setText(values[position].getFirstName() + " " + values[position].getLastName());
        viewHolder.secondLine.setText("Draw Slot - " + values[position].getSlotNumber());
        // change the icon for Windows and iPhone
        //Glide.with(context).load(values[position].getAvatar())
        //        .placeholder(R.drawable.ic_launcher_background)
        //        .dontAnimate()
        //        .into(viewHolder.imageView);

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
