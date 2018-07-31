package monsterstack.io.partner.challenge.control;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PhoneCaptureControlTest {

    private String[] supportedPhoneNumbers = {
            "+19544830245"
    };

    @Test
    public void givenSupportedPhoneNumbers_whenValidating_expectAllValid() {
        for (String phoneNumber : supportedPhoneNumbers) {
            assertTrue(new PhoneCaptureControlImpl().isValidPhoneNumber(phoneNumber));
        }
    }

    public class PhoneCaptureControlImpl implements PhoneCaptureControl {

        @Override
        public void onCapture() {

        }

        @Override
        public Context getContext() {
            return null;
        }
    }
}
