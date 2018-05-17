package monsterstack.io.partner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Group;

public class GroupArrayAdapter extends ArrayAdapter<Group> {
    private Context context;
    private Group[] values;

    public GroupArrayAdapter(Context context, Group[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null != convertView)
            viewHolder = (ViewHolder)convertView.getTag();
        else {
            convertView = inflater.inflate(R.layout.group_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.firstLine.setText(values[position].getName());
        // change the icon for Windows and iPhone
        viewHolder.imageView.setImageResource(R.drawable.ic_help_black_24dp);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.firstLine) TextView firstLine;
        @BindView(R.id.secondLine) TextView secondLine;

        @BindView(R.id.icon) ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
