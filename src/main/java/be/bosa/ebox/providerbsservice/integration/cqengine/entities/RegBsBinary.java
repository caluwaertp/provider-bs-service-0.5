package be.bosa.ebox.providerbsservice.integration.cqengine.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString(onlyExplicitlyIncluded = true)
@JsonDeserialize(builder = RegBsBinary.RegBsBinaryBuilder.class)
public class RegBsBinary {
    @JsonProperty("id")
    private String id;
    @JsonProperty("content")
    private byte[] content;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RegBsBinaryBuilder {
    }
}

