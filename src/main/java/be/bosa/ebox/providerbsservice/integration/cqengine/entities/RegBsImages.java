package be.bosa.ebox.providerbsservice.integration.cqengine.entities;

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
@JsonDeserialize(builder = RegBsImages.RegBsImagesBuilder.class)
public class RegBsImages {
    @JsonProperty("items")
    private List<RegBsImage> items;
    @JsonProperty("binaries")
    private List<RegBsBinary> binaries;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RegBsImagesBuilder {
    }
}

