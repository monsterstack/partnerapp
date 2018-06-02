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
import monsterstack.io.partner.domain.Member;

public class MiniMemberDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_MEMBER = "member";

    @BindView(R.id.memberAvatar)
    AvatarView avatarView;

    @BindView(R.id.memberName)
    TextView memberName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mini_member_details);

        ButterKnife.bind(this);

        Member member = (Member) getIntent().getSerializableExtra(EXTRA_MEMBER);

        avatarView.setUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent));
        memberName.setText(member.getFullName());
    }
}
