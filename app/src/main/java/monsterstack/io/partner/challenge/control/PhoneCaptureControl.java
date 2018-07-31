package monsterstack.io.partner.challenge.control;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import monsterstack.io.partner.common.Control;

public interface PhoneCaptureControl extends Control {
    void onCapture();
    default boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
        boolean isValid = false;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber usNumberProto = phoneUtil.parse(phoneNumber, countryCode);            //with default country
            isValid = phoneUtil.isValidNumber(usNumberProto);                  //returns true
        } catch (NumberParseException e) {
            ;
        }

        return isValid;
    }
}
