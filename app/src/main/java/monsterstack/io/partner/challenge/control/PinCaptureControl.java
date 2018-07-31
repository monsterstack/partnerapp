package monsterstack.io.partner.challenge.control;

import monsterstack.io.partner.common.Control;

public interface PinCaptureControl extends Control {
    void onNext();
    boolean isValidPin(String pin);
}
