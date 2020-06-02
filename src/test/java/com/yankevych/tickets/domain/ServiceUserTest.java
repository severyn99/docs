package com.yankevych.tickets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.yankevych.tickets.web.rest.TestUtil;

public class ServiceUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceUser.class);
        ServiceUser serviceUser1 = new ServiceUser();
        serviceUser1.setId(1L);
        ServiceUser serviceUser2 = new ServiceUser();
        serviceUser2.setId(serviceUser1.getId());
        assertThat(serviceUser1).isEqualTo(serviceUser2);
        serviceUser2.setId(2L);
        assertThat(serviceUser1).isNotEqualTo(serviceUser2);
        serviceUser1.setId(null);
        assertThat(serviceUser1).isNotEqualTo(serviceUser2);
    }
}
