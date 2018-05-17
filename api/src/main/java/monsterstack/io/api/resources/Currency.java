package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Currency {
    USD;

    @JsonCreator
    public String getCode() {
        return this.name().toLowerCase();
    }
}
