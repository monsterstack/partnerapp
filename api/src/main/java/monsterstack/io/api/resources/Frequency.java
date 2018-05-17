package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Frequency {
    DAILY, WEEKLY, MONTHLY;

    @JsonCreator
    public String getCode() {
        return this.name().toLowerCase();
    }
}
