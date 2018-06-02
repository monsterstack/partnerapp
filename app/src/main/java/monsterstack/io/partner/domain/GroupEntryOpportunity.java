package monsterstack.io.partner.domain;

import lombok.Data;

@Data
public class GroupEntryOpportunity {
    private Group group;
    private Integer slotNumber;

    public GroupEntryOpportunity(Group group, Integer slotNumber) {
        this.group = group;
        this.slotNumber = slotNumber;
    }
}
