package monsterstack.io.partner.main.presenter;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.avatarview.AvatarView;
import monsterstack.io.avatarview.User;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.TransactionArrayAdapter;
import monsterstack.io.partner.common.presenter.PresenterAdapter;
import monsterstack.io.partner.domain.Member;
import monsterstack.io.partner.domain.Transaction;
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

    @BindView(R.id.memberTransactions)
    RecyclerView memberTransactions;

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

            control.findTransactionsForMemberGroup(new OnResponseListener<Transaction[], HttpError>() {
                @Override
                public void onResponse(Transaction[] transactions, HttpError httpError) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(control.getContext());
                    memberTransactions.setLayoutManager(layoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(memberTransactions.getContext(),
                            DividerItemDecoration.VERTICAL);
                    memberTransactions.addItemDecoration(dividerItemDecoration);
                    memberTransactions.setAdapter(new TransactionArrayAdapter(control.getContext(), transactions));
                }
            });
        }
    }


    public void setMemberFullName(String memberFullName) {
        memberNameView.setText(memberFullName);
    }

    public void setMemberAvatarUser(User avatarUser) {
        avatarView.setUser(avatarUser);
    }
}
