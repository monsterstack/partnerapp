package monsterstack.io.partner.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import monsterstack.io.partner.R;

public abstract class MenuActivity extends AppCompatActivity implements ClosableMenu {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getActionTitle());
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_action, menu);

        MenuItem menuItem = menu.findItem(R.id.modal_close_button);
        menuItem.setOnMenuItemClickListener(getCloseListener());
        return true;
    }


    public MenuItem.OnMenuItemClickListener getCloseListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        };
    }

    public abstract AppCompatActivity getActivity();

    public abstract int getActionTitle();
}
