package monsterstack.io.partner.main.map;

import java.util.Currency;
import java.util.Locale;

import monsterstack.io.api.resources.Partner;
import monsterstack.io.partner.common.Mapper;
import monsterstack.io.partner.domain.Group;

public class GroupPartnerMapper implements Mapper<Group, Partner> {
    @Override
    public Group map(Partner in) {
        Group group = new Group(in.getName(),
                in.getNumberOfDrawSlots(),
                Currency.getInstance(Locale.US),
                in.getGoal(),
                in.getBaseContribution());

        group.setBankerId(in.getBankerId());
        group.setOrganizerId(in.getOrganizerId());
        return group;
    }
}
