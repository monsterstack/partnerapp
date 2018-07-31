package monsterstack.io.partner.main.control;

import android.view.MenuItem;

import java.util.List;
import java.util.Optional;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Contact;

public interface InviteMembersControl extends Control {
    void search(int page, OnResponseListener<Contact[], HttpError> listener);
    void search(int page, CharSequence query, OnResponseListener<Contact[], HttpError> listener);
    List<Contact> getPhoneContacts(int page, Optional<String> query);

    int getPage();
    void resetPage();

    MenuItem.OnMenuItemClickListener getNextListener();

    void addSelectedContact(Contact contact);
    void removeSelectedContact(Contact contact);
}
