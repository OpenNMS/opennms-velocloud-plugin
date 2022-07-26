package org.opennms.velocloud;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.v1.model.immutables.ImmutableAlarm;
import org.opennms.velocloud.model.Alert;

public class AlarmForwarderTest {

    @Test
    public void canConvertAlarmToAlert() {
//        Alarm alarm = ImmutableAlarm.newBuilder()
//                .setId(1)
//                .setReductionKey("hey:oh")
//                .setSeverity(Severity.CRITICAL)
//                .build();
//
//        Alert alert = AlarmForwarder.toAlert(alarm);
//
//        assertThat(alert.getStatus(), equalTo(Alert.Status.CRITICAL));
    }
}
