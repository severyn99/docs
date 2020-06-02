package com.yankevych.tickets.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.yankevych.tickets.web.rest.TestUtil;

public class ReservationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);
        reservation2.setId(2L);
        assertThat(reservation1).isNotEqualTo(reservation2);
        reservation1.setId(null);
        assertThat(reservation1).isNotEqualTo(reservation2);
    }
}
