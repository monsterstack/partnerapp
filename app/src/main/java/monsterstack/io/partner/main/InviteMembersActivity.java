package monsterstack.io.partner.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.Application;
import monsterstack.io.partner.R;
import monsterstack.io.partner.common.BasicActivity;
import monsterstack.io.partner.domain.Contact;
import monsterstack.io.partner.main.control.InviteMembersControl;
import monsterstack.io.partner.main.presenter.impl.InviteMembersPresenterImpl;
import monsterstack.io.partner.support.content.ContactPagingSupport;
import monsterstack.io.partner.utils.NavigationUtils;

import static monsterstack.io.partner.support.content.ContactPagingSupport.CONTACTS_PROJECTION;

public class InviteMembersActivity extends BasicActivity implements InviteMembersControl {

    private static final Integer CREATE_GROUP_RESULT_CODE = 101;
    private static final Integer MIN_MEMBER_SIZE = 4;

    private InviteMembersPresenterImpl presenter;

    private List<Contact> selectedContacts;

    private transient int page = 0;

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void resetPage() {
        page = 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).component().inject(this);

        presenter = (InviteMembersPresenterImpl)getPresenterFactory().getInviteMembersPresenter(
                this, this).present(Optional.empty());

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
            if (selectedContacts.size() < MIN_MEMBER_SIZE) {
                presenter.showError(getResources().getString(R.string.insufficient_members_warning));
            } else {
                Intent createGroupIntent = new Intent(getContext(), GroupCreationActivity.class);
                createGroupIntent.putExtra(GroupCreationActivity.EXTRA_INVITES, (Serializable) selectedContacts);
                ((Activity) getContext()).startActivityForResult(createGroupIntent,
                        CREATE_GROUP_RESULT_CODE, NavigationUtils.enterStageRightBundle(getContext()));
            }
            return false;
        };
    }

    /**
     * Search
     * @param listener OnResponseListener
     *
     * @see InviteMembersControl#search(int, OnResponseListener)
     */
    @Override
    public void search(int page, OnResponseListener<Contact[], HttpError> listener) {
        List<Contact> phoneContacts = getPhoneContacts(page, Optional.empty());

        new Handler().postDelayed(() ->
                listener.onResponse(phoneContacts
                        .toArray(new Contact[phoneContacts.size()]), null),
                1000);
    }

    /**
     * Search
     * @param page      int
     * @param query     CharSequence
     * @param listener  OnResponseListener
     *
     * @see InviteMembersControl#search(int page, CharSequence, OnResponseListener)
     */
    @Override
    public void search(int page, CharSequence query, OnResponseListener<Contact[], HttpError> listener) {
        final String queryString = query.toString();

        List<Contact> phoneContacts = getPhoneContacts(page, Optional.of(queryString));

        List<Contact> contacts = new ArrayList<>();
        contacts.addAll(phoneContacts);

        new Handler().postDelayed(() ->
                        listener.onResponse(contacts.toArray(new Contact[contacts.size()]), null),
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

    public List<Contact> getPhoneContacts(int page, Optional<String> query) {
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = "_id limit 12 offset " + (12 * page);

        if(query.isPresent()) {
            selection = "display_name LIKE ?";
            selectionArgs = new String[] { "%"+query.get()+"%" };
        }

        return new ContactPagingSupport().builder()
                .contentResolver(getContentResolver())
                .contentUri(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                .projection(CONTACTS_PROJECTION)
                .selection(selection)
                .selectionArgs(selectionArgs)
                .sortOrder(sortOrder).build().query();
    }
}
