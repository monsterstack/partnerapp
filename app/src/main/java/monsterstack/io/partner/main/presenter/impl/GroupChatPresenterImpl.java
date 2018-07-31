package monsterstack.io.partner.main.presenter.impl;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Menu;

import com.github.bassaer.chatmessageview.view.MessageView;

import java.util.Map;
import java.util.Optional;

import butterknife.BindView;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.presenter.Presenter;
import monsterstack.io.partner.main.control.GroupChatControl;
import monsterstack.io.partner.main.presenter.GroupChatPresenter;

public class GroupChatPresenterImpl implements GroupChatPresenter {

    @BindView(R.id.group_message_view)
    MessageView chatView;
    private GroupChatControl control;

    @Override
    public Presenter<GroupChatControl> present(Optional<Map> metadata) {
        //Set UI parameters if you need
        chatView.setRightBubbleColor(getChatRightBubbleColor());
        chatView.setLeftBubbleColor(Color.WHITE);
        chatView.setBackgroundColor(getChatBackgroundColor());
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);
        return this;
    }

    @Override
    public Presenter<GroupChatControl> bind(GroupChatControl control) {
        this.control = control;
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public GroupChatControl getControl() {
        return control;
    }

    protected int getChatBackgroundColor() {
        return ContextCompat.getColor(getControl().getContext(), R.color.blueGray500);
    }

    protected int getChatRightBubbleColor() {
        return ContextCompat.getColor(getControl().getContext(), R.color.green500);
    }
}
