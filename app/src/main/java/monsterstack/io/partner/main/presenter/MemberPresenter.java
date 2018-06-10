package monsterstack.io.partner.main.presenter;

import android.widget.TextView;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.main.control.MemberControl;

public class MemberPresenter extends PresenterAdapter {
    @BindView(R.id.memberAvatar)
    AvatarView avatarView;

    @BindView(R.id.memberName)
    TextView memberNameView;

    @BindView(R.id.member_subtext)
    TextView memberSubText;

    @BindView(R.id.member_slot_label)
    TextView memberSlotText;

    private MemberControl control;

    public MemberPresenter(MemberControl control) {
        this.control = control;
    }

    @Override
    public void present(Optional<Map> metadata) {
        if (metadata.isPresent() && metadata.get().containsKey("member")) {
            Member member = (Member) metadata.get().get("member");
            setMemberFullName(member.getFullName());
            setMemberAvatarUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent));
        }
    }


    public void setMemberFullName(String memberFullName) {
        memberNameView.setText(memberFullName);
    }

    public void setMemberAvatarUser(User avatarUser) {
        avatarView.setUser(avatarUser);
    }
}
