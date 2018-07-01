package monsterstack.io.partner.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import butterknife.ButterKnife;
import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.InviteMembersControl;
import monsterstack.io.partner.main.presenter.InviteMembersPresenter;
import monsterstack.io.partner.utils.NavigationUtils;

public class InviteMembersActivity extends BasicActivity implements InviteMembersControl {
    private static final Integer CREATE_GROUP_RESULT_CODE = 101;

    private InviteMembersPresenter presenter;

    private List<Contact> selectedContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).component().inject(this);

        presenter = getPresenterFactory().getInviteMembersPresenter(this, this);
        presenter.present(Optional.empty());

        this.selectedContacts = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.next_action, menu);
        MenuItem menuItem = menu.findItem(R.id.next_button);
        menuItem.setOnMenuItemClickListener(getNextListener());
        return true;
    }

    @Override
    public void injectDependencies(BasicActivity basicActivity) {
        super.injectDependencies(basicActivity);
        ((Application) getApplication()).component().inject(this);
    }

    @Override
    public int getContentView() {
        return R.layout.members_invite;
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    public int getActionTitle() {
        return R.string.invite_members;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MenuItem.OnMenuItemClickListener getNextListener() {
        return item -> {
            Intent createGroupIntent = new Intent(getContext(), GroupCreationActivity.class);
            createGroupIntent.putExtra(GroupCreationActivity.EXTRA_INVITES, (Serializable)selectedContacts );
            ((Activity)getContext()).startActivityForResult(createGroupIntent,
                    CREATE_GROUP_RESULT_CODE, NavigationUtils.enterStageRightBundle(getContext()));
            return false;
        };
    }

    /**
     * Search
     * @param listener OnResponseListener
     *
     * @see InviteMembersControl#search(OnResponseListener)
     */
    @Override
    public void search(OnResponseListener<Contact[], HttpError> listener) {
        List<Contact> friends = getFriends();
        List<Contact> phoneContacts = getPhoneContacts();

        List<Contact> contacts = new ArrayList<>(friends);
        contacts.addAll(phoneContacts);

        new Handler().postDelayed(() ->
                listener.onResponse(contacts.toArray(new Contact[contacts.size()]), null),
                1000);
    }

    /**
     * Search
     * @param query     CharSequence
     * @param listener  OnResponseListener
     *
     * @see InviteMembersControl#search(CharSequence, OnResponseListener)
     */
    @Override
    public void search(CharSequence query, OnResponseListener<Contact[], HttpError> listener) {
        final String queryString = query.toString();

        new Handler().postDelayed(() ->
            listener.onResponse(getFriends().stream().filter(
                    friend -> friend.getFullName().startsWith(queryString)).toArray(Contact[]::new),
                    null),
            1000);
    }

    /**
     * Add Selected Contact
     * @param contact   Contact
     *
     * @see InviteMembersControl#addSelectedContact(Contact)
     */
    @Override
    public void addSelectedContact(Contact contact) {
        presenter.addSelectedContact(contact);

        this.selectedContacts.add(contact);
    }

    /**
     * Remove Selected Contact
     * @param contact   Contact
     *
     * @see InviteMembersControl#removeSelectedContact(Contact)
     */
    @Override
    public void removeSelectedContact(Contact contact) {
        if (!selectedContacts.isEmpty()) {
            presenter.removeSelectedContact(contact);
            this.selectedContacts.remove(contact);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_GROUP_RESULT_CODE) {
            setResult(CREATE_GROUP_RESULT_CODE, data);
            finish();
        }
    }

    // Private -------------------
    private List<Contact> getFriends() {
        Contact[] friends = new Contact[] {
                new Contact("Lois", "Armstrong",
                        "lois.armstrong@gmail.com", "+1 954 678 9090"),
                new Contact("Samuel", "Anderson",
                        "samuel.anderson@gmail.com", "+1 954 678 9090"),
                new Contact("Paris", "Arnold",
                        "paris.arnold@gmail.com", "+1 954 678 9090"),
                new Contact("Janet", "Lansing",
                        "janet.lansing@gmail.com", "+1 954 678 9090"),
                new Contact("Lois", "Armstrong",
                        "lois.armstrong@gmail.com", "+1 954 678 9090"),
                new Contact("Samuel", "Anderson",
                        "samuel.anderson@gmail.com", "+1 954 678 9090"),
                new Contact("Paris", "Arnold",
                        "paris.arnold@gmail.com", "+1 954 678 9090"),
                new Contact("Janet", "Lansing",
                        "janet.lansing@gmail.com", "+1 954 678 9090"),
                new Contact("Lois", "Armstrong",
                        "lois.armstrong@gmail.com", "+1 954 678 9090"),
                new Contact("Samuel", "Anderson",
                        "samuel.anderson@gmail.com", "+1 954 678 9090"),
                new Contact("Paris", "Arnold",
                        "paris.arnold@gmail.com", "+1 954 678 9090"),
                new Contact("Janet", "Lansing",
                        "janet.lansing@gmail.com", "+1 954 678 9090"),
                new Contact("Jonathan", "Armstrong",
                        "jon.armstrong@gmail.com", "+1 954 678 9090")
        };

        return new ArrayList<>(Arrays.asList(friends));
    }

    private List<Contact> getPhoneContacts() {
        List<Contact> phoneContacts = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            if (null != phones) {
                while (phones.moveToNext()) {
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String emailAddress = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    phoneContacts.add(new Contact(name, emailAddress, phoneNumber));
                }
                phones.close();
            }
        }

        return phoneContacts;
    }
}
