package monsterstack.io.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.EmptyUser;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Member;

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder> {
    private Member[] members;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    MemberRecyclerViewAdapter(@NonNull Context context, @NonNull Member[] members) {
        super();
        Objects.requireNonNull(context);
        Objects.requireNonNull(members);

        this.inflater = LayoutInflater.from(context);
        this.members = members;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mini_member_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member member = members[position];
        if (member == null) {
            // Empty slot
            holder.avatarView.setUser(new EmptyUser(R.color.colorPrimaryDark));
        } else {
            String fullName = member.getFullName();
            holder.avatarView.setUser(new User(fullName, member.getAvatar(), R.color.colorAccent));


        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return members.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AvatarView avatarView;
        TextView fullNameView;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            avatarView = itemView.findViewById(R.id.memberAvatarView);
            fullNameView = itemView.findViewById(R.id.memberFullName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(@NonNull ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
