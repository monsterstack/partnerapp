package monsterstack.io.partner.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.api.UserSessionManager;
import monsterstack.io.api.resources.AuthenticatedUser;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.domain.Member;

public class MiniMemberDetailsFragment extends Fragment {
    public static final String ARG_MEMBER = "member";
    @BindView(R.id.memberAvatarView)
    AvatarView avatarView;

    @BindView(R.id.memberFullName)
    TextView memberName;

    @BindView(R.id.member_slot_label)
    Button memberCloseButton;

    private View view;

    public static MiniMemberDetailsFragment newInstance(Member member) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEMBER, member);
        MiniMemberDetailsFragment fragment = new MiniMemberDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.mini_member_details, container);
        ButterKnife.bind(this, view);

        return this.view;
    }

    public void bindMember(Member member) {

        Context context = view.getContext();

        UserSessionManager sessionManager = new UserSessionManager(context);
        AuthenticatedUser authenticatedUser = sessionManager.getUserDetails();

        String fullName = member.getFullName();
        if(authenticatedUser.getFullName().equalsIgnoreCase(member.getFullName())) {
            fullName = "You";
        }

        avatarView.setUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent));
        memberName.setText(fullName);

        memberCloseButton.setText("Draw Slot - " + member.getSlotNumber());
    }
}
