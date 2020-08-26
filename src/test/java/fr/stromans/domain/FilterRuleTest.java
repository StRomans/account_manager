package fr.stromans.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.stromans.web.rest.TestUtil;

public class FilterRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilterRule.class);
        FilterRule filterRule1 = new FilterRule();
        filterRule1.setId(1L);
        FilterRule filterRule2 = new FilterRule();
        filterRule2.setId(filterRule1.getId());
        assertThat(filterRule1).isEqualTo(filterRule2);
        filterRule2.setId(2L);
        assertThat(filterRule1).isNotEqualTo(filterRule2);
        filterRule1.setId(null);
        assertThat(filterRule1).isNotEqualTo(filterRule2);
    }
}
