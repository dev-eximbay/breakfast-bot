package org.example.breakfast.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Created by Robin on 25. 4. 5.
 * Description :
 */

public class TimeProvider {
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");
    private final Clock clock;

    public TimeProvider(Clock clock) {
        this.clock = clock;
    }


    public LocalDate nowDate() {
        return LocalDate.now(this.clock);
    }

    public LocalTime nowTime() {
        return LocalTime.now(this.clock);
    }

    public static TimeProvider withZone(ZoneId zoneId) {
        return new TimeProvider(Clock.system(zoneId));
    }

    /*KST 기준*/
    public static TimeProvider ofKorea() {
        return withZone(KOREA_ZONE_ID);
    }
}

