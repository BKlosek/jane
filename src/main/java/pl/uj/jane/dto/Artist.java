package pl.uj.jane.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * The class representing the artist that is associated with a given art genre.
 */

@JsonDeserialize(using = Artist.ArtistDeserializer.class)
public class Artist {

    /**
     * An id of a given author.
     */
    public String id;

    /**
     * The name of a given author.
     */
    public String name;

    /**
     * An overridden string representation of the Actor class for more convenient representation.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * A default constructor.
     */
    public Artist() {
        this.id = "Unknown";
        this.name = "Unknown";
    }

    /**
     * A constructor with given "id" and "name" fields taken from WikiData.
     *
     * @param id
     * @param name
     */
    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * A class dedicated to deserialization of an artist from json format extracted from WikiData.
     */
    public static class ArtistDeserializer extends StdDeserializer<Artist> {

        /**
         * A default constructor.
         */
        public ArtistDeserializer() {
            this(null);
        }

        /**
         * @param vc
         */
        public ArtistDeserializer(Class<?> vc) {
            super(vc);
        }

        /**
         * The method for conversion from JSON format taken from WikiData and Artist object.
         *
         * @param jp   An object of JsonParser from "jackson" library.
         * @param ctxt An object of DeserializationContext from "jackson" library.
         * @return Returns the deserialized version of Artist class.
         * @throws IOException
         */
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

