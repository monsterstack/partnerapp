package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SelectedContactArrayAdapter extends RecyclerView.Adapter<SelectedContactArrayAdapter.ViewHolder> {
    private static final String TAG = "SelectedContactArrayAdapter";

    private View view;
    private List<Contact> selectedContacts;
    private LayoutInflater inflater;

    private Function<Integer, Void> onContactAdded;
    private Function<Integer, Void> onContactRemoved;

    public SelectedContactArrayAdapter(Context context, Contact[] contacts) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.selectedContacts = new ArrayList<>(Arrays.asList(contacts));
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
        Contact contact = selectedContacts.get(position);
        if (contact != null) {
            holder.avatarView.setUser(new User(contact.getFullName(), contact.getAvatar(), R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return this.selectedContacts.size();
    }

    public void appendContact(Contact contact) {
        this.selectedContacts.add(contact);

        if (null != this.view) {
            notifyItemInserted(getItemCount());

            if (null != onContactAdded) {
                onContactAdded.apply(selectedContacts.size());
            }
        }
    }

    public void removeContact(Contact contact) {
        Integer index = -1;
        for (int i = 0; i < selectedContacts.size(); i++) {
            if (selectedContacts.get(i).getFullName().equalsIgnoreCase(contact.getFullName())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            this.selectedContacts.remove(contact);

            final Integer itemIndex = index;
            if (null != this.view) {
                notifyItemRemoved(itemIndex);
            }

            if (null != onContactRemoved) {
                onContactAdded.apply(selectedContacts.size());
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
