package monsterstack.io.partner.domain;

import java.io.Serializable;
import java.util.Currency;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Group implements Serializable {
    private String id;
    private String name;
    private Integer numberOfSlots;

    private Currency currency;

    private Double optionalDeposit;
    private Boolean active;

    private Double drawAmount;
    private Double baseContribution;


    private String bankerId;
    private String organizerId;

    public Group() {
        this.id = UUID.randomUUID().toString();
    }

    public Group(String name, Integer numberOfSlots,
                 Currency currency, Double drawAmount,
                 Double baseContribution) {
        this();
        this.name = name;
        this.numberOfSlots = numberOfSlots;
        this.currency = currency;
        this.drawAmount = drawAmount;
        this.baseContribution = baseContribution;
    }

}
