package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Contact;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {
    private Contact[] values;
    private Context context;

    public ContactArrayAdapter(@NonNull Context context, @NonNull Contact[] contacts) {
        super(context, -1, contacts);

        this.values = contacts;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactArrayAdapter.ViewHolder viewHolder;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(null != convertView)
            viewHolder = (ContactArrayAdapter.ViewHolder)convertView.getTag();
        else {
            convertView = inflater.inflate(R.layout.contact_list_item, parent, false);
            viewHolder = new ContactArrayAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.firstLine.setText(values[position].getFirstName() + " " + values[position].getLastName());
        if(!values[position].getAlreadyPartnering()) {
            viewHolder.secondLine.setText(values[position].getPhoneNumber());
            viewHolder.actionButton.setText(R.string.add_contact);
        } else {
            viewHolder.secondLine.setText(R.string.contact_already_partnering);
            viewHolder.actionButton.setText(R.string.invite_contact);
        }

        Glide.with(context).load(values[position].getAvatar())
                .placeholder(R.drawable.ic_launcher_background)
                .dontAnimate().into(viewHolder.imageView);

        // change the icon for Windows and iPhone
        //viewHolder.imageView.setImageResource(R.drawable.ic_launcher_background);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.firstLine)
        TextView firstLine;
        @BindView(R.id.secondLine) TextView secondLine;

        @BindView(R.id.icon)
        CircleImageView imageView;

        @BindView(R.id.contact_action_button)
        Button actionButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
