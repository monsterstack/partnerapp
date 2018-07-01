package monsterstack.io.partner.main.control;

import android.view.MenuItem;

import monsterstack.io.api.listeners.OnResponseListener;
import monsterstack.io.api.resources.HttpError;
import monsterstack.io.partner.common.Control;
import monsterstack.io.partner.domain.Contact;

public interface InviteMembersControl extends Control {
    void search(OnResponseListener<Contact[], HttpError> listener);
    void search(CharSequence query, OnResponseListener<Contact[], HttpError> listener);
//
//    List<Contact> getPhoneContacts();
//    List<Contact> getFriends();

    MenuItem.OnMenuItemClickListener getNextListener();

    void addSelectedContact(Contact contact);
    void removeSelectedContact(Contact contact);
}
