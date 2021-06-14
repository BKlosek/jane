package pl.uj.jane.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;


@JsonDeserialize(using = Artist.ArtistDeserializer.class)
public class Artist {

    public String id;
    public String name;

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Artist() {
        this.id = "Unknown";
        this.name = "Unknown";
    }

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static class ArtistDeserializer extends StdDeserializer<Artist> {

        public ArtistDeserializer() {
            this(null);
        }

        public ArtistDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Artist deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);

            //Conversion http://www.wikidata.org/entity/Q2599 -> Q2599
            String[] artistIdUrlSplitted = node.get("artist").get("value").toString().replace("\"", "").split("/");
            String id = artistIdUrlSplitted[artistIdUrlSplitted.length - 1];
            String name = node.get("artistLabel").get("value").asText();

            return new Artist(id, name);
        }
    }
}

