package monsterstack.io.partner.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import monsterstack.io.partner.R;

public abstract class PreferenceMenuActivity extends AppCompatActivity implements ClosableMenu {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpTransitions();

        setContentView(getContentView());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getActionTitle());
        setSupportActionBar(myToolbar);

        getFragmentManager().beginTransaction().add(R.id.content_frame, new MenuPreferenceFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_action, menu);

        MenuItem menuItem = menu.findItem(R.id.modal_close_button);
        menuItem.setOnMenuItemClickListener(getCloseListener());
        return true;
    }

    public int getContentFrame() {
        return R.id.content_frame;
    }

    public abstract int getActionTitle();
    private void setUpTransitions() {

    }

    @Override
    public MenuItem.OnMenuItemClickListener getCloseListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        };
    }

}
