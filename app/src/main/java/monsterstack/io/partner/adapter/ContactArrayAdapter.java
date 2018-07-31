package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Contact;

public class ContactArrayAdapter extends RecyclerView.Adapter<ContactArrayAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private View view;

    private List<Contact> contacts;

    private Function<Contact, Void> onContactSelectedHandler;
    private Function<Contact, Void> onContactUnSelectedHandler;

    private SparseBooleanArray itemStateArray= new SparseBooleanArray();

    public ContactArrayAdapter(Context context, Contact[] contacts) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.contacts = new ArrayList<>(Arrays.asList(contacts));
    }

    /**
     * Listen for Selected Contact
     * @param handler   Function<Contact, Void>
     */
    public void setOnContactSelectedHandler(Function<Contact, Void> handler) {
        this.onContactSelectedHandler = handler;
    }

    /**
     * Listen for Unselected Contact
     * @param handler   Function<Contact, Void>
     */
    public void setOnContactUnSelectedHandler(Function<Contact, Void> handler) {
        this.onContactUnSelectedHandler = handler;
    }

    @NonNull
    @Override
    public ContactArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = inflater.inflate(R.layout.contact_list_item, parent, false);
        ContactArrayAdapter.ViewHolder viewHolder = new ContactArrayAdapter.ViewHolder(this.view);
        ButterKnife.bind(viewHolder, this.view);

        return viewHolder;
    }

    public void appendToContacts(final Contact[] contacts) {
        Integer preAppendSize = ContactArrayAdapter.this.contacts.size();
        ContactArrayAdapter.this.contacts.addAll(Arrays.asList(contacts));

        notifyItemRangeChanged(preAppendSize, contacts.length);
    }

    public void replaceContacts(final Contact[] contacts) {
        ContactArrayAdapter.this.contacts.clear();
        ContactArrayAdapter.this.contacts.addAll(Arrays.asList(contacts));

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactArrayAdapter.ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        if (contact != null) {
            if (!itemStateArray.get(position, false)) {
                holder.bind(contact, false, position);
            } else {
                holder.bind(contact, true, position);
            }

        }
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    // @FixMe: needs to isolate resources away from adapter -- see GroupAdapterPresenter
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contactName)
        TextView contactName;
        @BindView(R.id.contactEmail)
        TextView contactEmail;
        @BindView(R.id.contactPhoneNumber)
        TextView contactPhoneNumber;
        @BindView(R.id.contactAvatar)
        AvatarView avatar;
        @BindView(R.id.contactSelectCheckBox)
        AppCompatCheckBox checkBox;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
        }

        void bind(Contact contact, boolean checked, int position) {
            contactName.setText(contact.getFullName());
            contactEmail.setText(contact.getEmailAddress());
            contactPhoneNumber.setText(contact.getPhoneNumber());

            avatar.setUser(new User(contact.getFullName(),
                    contact.getAvatar(), R.color.green));

            checkBox.setChecked(checked);

            checkBox.setOnClickListener(v -> {
                if (ContactArrayAdapter.this.itemStateArray.indexOfKey(position) == -1) {
                    itemStateArray.append(position, true);
                    checkBox.setChecked(true);
                    onContactSelectedHandler.apply(ContactArrayAdapter.this.contacts.get(position));
                } else if (!ContactArrayAdapter.this.itemStateArray.get(position)) {
                    checkBox.setChecked(true);
                    itemStateArray.put(position, true);
                    onContactSelectedHandler.apply(ContactArrayAdapter.this.contacts.get(position));
                } else {
                    checkBox.setChecked(false);
                    itemStateArray.put(position, false);
                    onContactUnSelectedHandler.apply(ContactArrayAdapter.this.contacts.get(position));
                }
            });
        }
    }
}
