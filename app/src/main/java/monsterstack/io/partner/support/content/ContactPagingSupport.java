package monsterstack.io.partner.support.content;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import monsterstack.io.partner.domain.Contact;

public class ContactPagingSupport extends ContentPagingSupport<Contact> {
    public static final String[] CONTACTS_PROJECTION = new String[] {
            ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.Data.MIMETYPE,
    };

    @Override
    public Contact map(Cursor cursor) {
        Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String emailAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

        Contact contact = new Contact(name, emailAddress, phoneNumber);

        Uri contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);

        Uri person = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

        if (null != person.getPath())
            contact.setAvatar(person.getPath());

        return contact;
    }
}
