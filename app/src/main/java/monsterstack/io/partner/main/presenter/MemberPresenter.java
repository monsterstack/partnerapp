package monsterstack.io.partner.main.presenter;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.TransactionArrayAdapter;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.main.control.MemberControl;

public class MemberPresenter implements Presenter<MemberControl> {
    @BindView(R.id.memberAvatar)
    AvatarView avatarView;

    @BindView(R.id.memberName)
    TextView memberNameView;

    @BindView(R.id.member_subtext)
    TextView memberSubText;

    @BindView(R.id.member_slot_label)
    TextView memberSlotText;

    @BindView(R.id.memberTransactions)
    RecyclerView memberTransactions;

    private MemberControl control;

    @Override
    public Presenter<MemberControl> present(Optional<Map> metadata) {
        if (metadata.isPresent() && metadata.get().containsKey("member")) {
            Member member = (Member) metadata.get().get("member");
            setMemberFullName(member.getFullName());
            setMemberAvatarUser(new User(member.getFullName(), member.getAvatar(), R.color.colorAccent));

            control.findTransactionsForMemberGroup((transactions, httpError) -> {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(control.getContext());
                memberTransactions.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(control.getContext(),
                        DividerItemDecoration.VERTICAL);
                memberTransactions.addItemDecoration(dividerItemDecoration);
                memberTransactions.setAdapter(new TransactionArrayAdapter(control.getContext(), transactions));
            });
        }

        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public Presenter<MemberControl> bind(MemberControl control) {
        this.control = control;
        return this;
    }

    @Override
    public MemberControl getControl() {
        return control;
    }

    public void setMemberFullName(String memberFullName) {
        memberNameView.setText(memberFullName);
    }

    public void setMemberAvatarUser(User avatarUser) {
        avatarView.setUser(avatarUser);
    }
}
