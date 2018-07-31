package monsterstack.io.partner.main.presenter.impl;

import android.content.Context;
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
import monsterstack.io.partner.domain.Transaction;
import monsterstack.io.partner.main.control.MemberControl;
import monsterstack.io.partner.main.presenter.MemberPresenter;

public class MemberPresenterImpl implements MemberPresenter {
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
                DividerItemDecoration dividerItemDecoration = createDividerDecoration(control.getContext());
                memberTransactions.addItemDecoration(dividerItemDecoration);
                memberTransactions.setAdapter(createTransactionArrayAdapter(control.getContext(), transactions));
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

    protected DividerItemDecoration createDividerDecoration(Context context) {
        return new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL);
    }

    protected TransactionArrayAdapter createTransactionArrayAdapter(Context context, Transaction[] transactions) {
        return new TransactionArrayAdapter(context, transactions);
    }
}
