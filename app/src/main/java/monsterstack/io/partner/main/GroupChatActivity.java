package monsterstack.io.partner.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.main.control.GroupChatControl;
import monsterstack.io.partner.main.presenter.GroupChatPresenter;

public class GroupChatActivity extends BasicActivity implements GroupChatControl {
    public static final String EXTRA_GROUP = "group";

    private GroupChatPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = (GroupChatPresenter)getPresenterFactory().getGroupChatPresenter(this, this)
                .bind(this).present(Optional.empty());

    }

    @Override
    public int getContentView() {
        return R.layout.group_chat;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.group_chat;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
