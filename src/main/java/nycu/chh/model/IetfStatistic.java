package nycu.chh.model;

import java.time.OffsetDateTime;

public class IetfStatistic {
    OffsetDateTime discontinuityTime;
    public IetfStatistic() {}
    public IetfStatistic(String discontinuityTime) {
        this.discontinuityTime = OffsetDateTime.parse(discontinuityTime);
    }
    public IetfStatistic(OffsetDateTime discontinuityTime) {
        this.discontinuityTime = discontinuityTime;
    }
    public OffsetDateTime getDiscontinuityTime() {
        return discontinuityTime;
    }
    public void setDiscontinuityTime(String discontinuityTime) {
        this.discontinuityTime = OffsetDateTime.parse(discontinuityTime);
    }
}