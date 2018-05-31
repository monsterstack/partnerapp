package monsterstack.io.partner.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Member;

public class MemberActivity extends BasicActivity {
    private Member member;

    @BindView(R.id.memberAvatar)
    AvatarView avatarView;

    @BindView(R.id.memberName)
    TextView memberNameView;

    @BindView(R.id.member_subtext)
    TextView memberSubText;

    @BindView(R.id.member_slot_label)
    TextView memberSlotText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);


        this.member = (Member)getIntent().getSerializableExtra("member");
        memberNameView.setText(member.getFullName());
        avatarView.setUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent ));

    }

    @Override
    public int getContentView() {
        return R.layout.activity_member;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.member_title;
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }
}
