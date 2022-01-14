package be.bosa.ebox.providerbsservice.integration.cqengine.entities;

import be.bosa.ebox.providerbsservice.api.model.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString(onlyExplicitlyIncluded = true)
@JsonDeserialize(builder = RegBsImage.RegBsImageBuilder.class)
public class RegBsImage {
    @JsonProperty("id")
    private String id;
    @JsonProperty("messageRegistryIds")
    private List<String> messageRegistryIds;
    @JsonProperty("format")
    private Image.FormatEnum format;
    @JsonProperty("mediaType")
    private Image.MediaTypeEnum mediaType;
    @JsonProperty("language")
    private String language;
    @JsonProperty("binaryId")
    private String binaryId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RegBsImageBuilder {
    }
}

