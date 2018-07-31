package monsterstack.io.partner.main.map;

import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import monsterstack.io.api.resources.Currency;
import monsterstack.io.api.resources.Frequency;
import monsterstack.io.api.resources.Partner;
import monsterstack.io.partner.domain.Group;

import static junit.framework.Assert.assertEquals;

public class GroupPartnerMapperTest {
    @Test
    public void givenPartner_whenMappingToGroup_expectFieldMatches() {
        Partner partner = partner();

        Group group = new GroupPartnerMapper().map(partner);

        assertEquals(partner.getBankerId(), group.getBankerId());
        assertEquals(partner.getOrganizerId(), group.getOrganizerId());
        assertEquals(java.util.Currency.getInstance(partner.getCurrency().name().toUpperCase()), group.getCurrency());
    }


    private Partner partner() {
        Partner partner = new Partner();
        partner.setBankerId(UUID.randomUUID().toString());
        partner.setPublishedAt(new Date());
        partner.setNumberOfDrawSlots(10);
        partner.setGoal(500.00);
        partner.setOrganizerId(UUID.randomUUID().toString());
        partner.setCurrency(Currency.usd);
        partner.setDrawFrequency(Frequency.monthly);
        partner.setContributionFrequency(Frequency.weekly);

        return partner;
    }
}
