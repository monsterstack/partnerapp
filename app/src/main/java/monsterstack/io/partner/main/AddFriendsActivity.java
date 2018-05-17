package monsterstack.io.partner.main;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import monsterstack.io.partner.R;
import monsterstack.io.partner.adapter.ContactArrayAdapter;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Contact;

import static android.view.View.GONE;

public class AddFriendsActivity extends BasicActivity {
    @BindView(R.id.add_friends_main_content)
    SwipeRefreshLayout mainContent;
    @BindView(R.id.add_friend_search_edit)
    EditText searchEdit;

    @BindView(R.id.keyboard)
    KeyboardView keyboard;

    @BindView(R.id.cancelButton)
    Button cancelSearch;

    @BindView(R.id.add_friend_list)
    ListView listView;

    private Contact[] contactArray = {
            new Contact("Sally", "Fields", "sally.fields@gmail.com", "+1 987 6543"),
            new Contact("Lois", "Renolds", "lois.renolds@yahoo.com", "+1 987 6543").alreadyPartnering(),
            new Contact("Sally", "Fields", "sally.fields@gmail.com", "+1 987 6543"),
            new Contact("Lois", "Renolds", "lois.renolds@yahoo.com", "+1 987 6543").alreadyPartnering(),
            new Contact("Sally", "Fields", "sally.fields@gmail.com", "+1 987 6543"),
            new Contact("Lois", "Renolds", "lois.renolds@yahoo.com", "+1 987 6543").alreadyPartnering(),
            new Contact("Sally", "Fields", "sally.fields@gmail.com", "+1 987 6543"),
            new Contact("Lois", "Renolds", "lois.renolds@yahoo.com", "+1 987 6543").alreadyPartnering(),
            new Contact("Sally", "Fields", "sally.fields@gmail.com", "+1 987 6543"),
            new Contact("Lois", "Renolds", "lois.renolds@yahoo.com", "+1 987 6543").alreadyPartnering()
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        listView.setAdapter(new ContactArrayAdapter(getApplicationContext(), contactArray ));

        cancelSearch.setVisibility(GONE);
        mainContent.setProgressViewOffset(true, 100, 200);
        mainContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AddFriendsActivity.this.onRefresh();
            }
        });

        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    cancelSearch.setVisibility(View.VISIBLE);
                } else {
                    cancelSearch.setVisibility(GONE);
                }
            }
        });

        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSearch.setVisibility(View.VISIBLE);
            }
        });

        keyboard.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    onSearchCancel();
                }
                return false;
            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    cancelSearch.setVisibility(GONE);
                } else {
                    cancelSearch.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

    }

    @Override
    public int getContentView() {
        return R.layout.add_friends;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.add_friends;
    }


    @OnClick(R.id.cancelButton)
    public void onSearchCancel() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        searchEdit.setText(null);
        searchEdit.clearFocus();
        cancelSearch.setVisibility(GONE);
    }

    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainContent.setRefreshing(false);
            }
        }, 1000);
    }
}
