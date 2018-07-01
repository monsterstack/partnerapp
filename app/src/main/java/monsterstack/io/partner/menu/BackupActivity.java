package monsterstack.io.partner.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

import monsterstack.io.partner.R;
import monsterstack.io.partner.menu.control.BackupControl;
import monsterstack.io.partner.menu.presenter.BackupPresenter;

public class BackupActivity extends MenuActivity implements BackupControl {
    private BackupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = getPresenterFactory().getBackupPresenter(this, this);

        presenter.present(Optional.empty());
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getContentView() {
        return R.layout.backup_funds;
    }

    public int getContentFrame() { return getContentView(); }

    public int getActionTitle() { return R.string.backup; }


    @Override
    public Context getContext() {
        return this;
    }
}
