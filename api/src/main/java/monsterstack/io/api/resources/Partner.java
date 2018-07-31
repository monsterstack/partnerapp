package monsterstack.io.api.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Partner {
    private String id;
    @JsonProperty("organizer_id")
    private String organizerId;
    @JsonProperty("banker_id")
    private String bankerId;

    private String name;

    @JsonProperty("number_of_draw_slots")
    private Integer numberOfDrawSlots;

    @JsonProperty("draw_frequency")
    private Frequency drawFrequency;
    @JsonProperty("contribution_frequency")
    private Frequency contributionFrequency;

    @JsonProperty("published_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date publishedAt;

    private Double goal;

    @JsonProperty("base_contribution")
    private Double baseContribution;

    @JsonProperty("currency")
    private Currency currency;

    private Wallet wallet;
}
