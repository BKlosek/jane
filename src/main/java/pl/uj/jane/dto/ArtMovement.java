package pl.uj.jane.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

@JsonDeserialize(using = ArtMovement.ArtMovementDeserializer.class)
public class ArtMovement {
    public String id;
    public String name;

    @Override
    public String toString() {
        return "ArtMovement {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public ArtMovement() {
        this.id = "Unknown";
        this.name = "Unknown";
    }

    public ArtMovement(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class ArtMovementDeserializer extends StdDeserializer<ArtMovement> {

        public ArtMovementDeserializer() {
            this(null);
        }

        public ArtMovementDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ArtMovement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);

            //Conversion http://www.wikidata.org/entity/Q2599 -> Q2599
            String[] artMovementIdUrlSplitted = node.get("code_").get("value").toString().replace("\"", "").split("/");
            String id = artMovementIdUrlSplitted[artMovementIdUrlSplitted.length - 1];

            String name = node.get("code_Label").get("value").asText();

            return new ArtMovement(id, name);
        }
    }
}
