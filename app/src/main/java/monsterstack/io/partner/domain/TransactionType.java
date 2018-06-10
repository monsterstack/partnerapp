package monsterstack.io.partner.domain;

public enum TransactionType {
    DEBIT("Received"),
    CREDIT("Sent");

    String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
