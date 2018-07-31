package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Contact;

public class SelectedContactArrayAdapter extends RecyclerView.Adapter<SelectedContactArrayAdapter.ViewHolder> {

    private View view;
    private Contact[] selectedContacts;
    private LayoutInflater inflater;

    private Function<Integer, Void> onContactAdded;
    private Function<Integer, Void> onContactRemoved;

    public SelectedContactArrayAdapter(Context context, Contact[] contacts) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.selectedContacts = contacts;
    }

    @NonNull
    @Override
    public SelectedContactArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = inflater.inflate(R.layout.selected_contact_list_item, parent, false);
        SelectedContactArrayAdapter.ViewHolder viewHolder = new SelectedContactArrayAdapter.ViewHolder(this.view);
        ButterKnife.bind(viewHolder, this.view);

        return viewHolder;
    }

    public void setOnContactAdded(Function<Integer, Void> onContactAdded) {
        this.onContactAdded = onContactAdded;
    }

    public void setOnContactRemoved(Function<Integer, Void> onContactRemoved) {
        this.onContactRemoved = onContactRemoved;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedContactArrayAdapter.ViewHolder holder, int position) {
        Contact contact = selectedContacts[position];
        if (contact != null) {
            holder.avatarView.setUser(new User(contact.getFullName(), contact.getAvatar(), R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return this.selectedContacts.length;
    }

    /**
     * Add Contact to adapter
     * @param contact   Contact
     */
    public void addContact(Contact contact) {
        Contact[] newContacts = new Contact[this.selectedContacts.length+1];

        System.arraycopy(selectedContacts, 0, newContacts, 0, this.selectedContacts.length);

        this.selectedContacts = newContacts;

        this.selectedContacts[newContacts.length-1] = contact;

        if (null != this.view) {
            notifyItemInserted(getItemCount()-1);

            if (null != onContactAdded) {
                onContactAdded.apply(selectedContacts.length);
            }
        }
    }

    /**
     * Remove Contact from adapter
     * @param contact Contact
     */
    public void removeContact(Contact contact) {
        Integer index = -1;
        for (int i = 0; i < selectedContacts.length; i++) {
            if (selectedContacts[i].getPhoneNumber().equalsIgnoreCase(contact.getPhoneNumber())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            this.selectedContacts[index] = null;

            Contact[] newContacts = new Contact[this.selectedContacts.length - 1];
            List<Contact> list = new ArrayList<>();
            for (Contact selectedContact : selectedContacts) {
                if (selectedContact != null) {
                    list.add(selectedContact);
                }
            }

            newContacts = list.toArray(newContacts);

            this.selectedContacts = newContacts;

            if (null != this.view) {
                notifyItemRemoved(index);
            }

            if (null != onContactRemoved) {
                onContactAdded.apply(newContacts.length);
            }
        }
    }

    // needs to isolate resources away from adapter -- see GroupAdapterPresenter
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.selectedContactListItem_avatar)
        AvatarView avatarView;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
        }
    }
}
