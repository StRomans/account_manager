package fr.stromans.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.stromans.web.rest.TestUtil;

public class ClassificationRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassificationRule.class);
        ClassificationRule classificationRule1 = new ClassificationRule();
        classificationRule1.setId(1L);
        ClassificationRule classificationRule2 = new ClassificationRule();
        classificationRule2.setId(classificationRule1.getId());
        assertThat(classificationRule1).isEqualTo(classificationRule2);
        classificationRule2.setId(2L);
        assertThat(classificationRule1).isNotEqualTo(classificationRule2);
        classificationRule1.setId(null);
        assertThat(classificationRule1).isNotEqualTo(classificationRule2);
    }
}
