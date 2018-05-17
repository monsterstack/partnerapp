package monsterstack.io.partner.menu;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import monsterstack.io.partner.R;

public abstract class MenuActivity extends AppCompatActivity implements ClosableMenu {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        TextView toolbarTitle = myToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getActionTitle());
        setSupportActionBar(myToolbar);


        // Register Close action
        ImageButton closeButton = findViewById(R.id.modal_close_button);
        closeButton.setOnClickListener(getCloseListener());
    }

    @Override
    public View.OnClickListener getCloseListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    public abstract AppCompatActivity getActivity();

    public abstract int getActionTitle();
}
