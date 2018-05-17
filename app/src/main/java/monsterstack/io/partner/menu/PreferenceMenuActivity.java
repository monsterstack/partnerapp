package monsterstack.io.partner.menu;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import monsterstack.io.partner.MainActivity;
import monsterstack.io.partner.R;

public abstract class PreferenceMenuActivity extends AppCompatActivity implements ClosableMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpTransitions();

        setContentView(getContentView());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        TextView toolbarTitle = myToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getActionTitle());
        setSupportActionBar(myToolbar);

        // Register Close action
        ImageButton closeButton = findViewById(R.id.modal_close_button);
        closeButton.setOnClickListener(getCloseListener());

        getFragmentManager().beginTransaction().add(R.id.content_frame, new MenuPreferenceFragment()).commit();


    }

    public int getContentFrame() {
        return R.id.content_frame;
    }

    public abstract int getActionTitle();
    private void setUpTransitions() {

    }

    @Override
    public View.OnClickListener getCloseListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), 0, R.anim.slide_down).toBundle();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("openDrawer", Boolean.TRUE);
                startActivity(intent, bundle);
            }
        };
    }

}
