package com.yankevych.tickets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.yankevych.tickets.web.rest.TestUtil;

public class FlightTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flight.class);
        Flight flight1 = new Flight();
        flight1.setId(1L);
        Flight flight2 = new Flight();
        flight2.setId(flight1.getId());
        assertThat(flight1).isEqualTo(flight2);
        flight2.setId(2L);
        assertThat(flight1).isNotEqualTo(flight2);
        flight1.setId(null);
        assertThat(flight1).isNotEqualTo(flight2);
    }
}
