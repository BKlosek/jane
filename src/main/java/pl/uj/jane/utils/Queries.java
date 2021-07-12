package pl.uj.jane.utils;

import static java.lang.String.format;

public class Queries {

    public static String fillQueryWithParameters(String query, Object... parameters) {
        return format(query, parameters);
    }

    //WikiData queries
    public static final String ARTISTS_LIST = "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "SELECT DISTINCT ?artistLabel ?artist WHERE { ?artist wdt:P106 wd:Q1028181. SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" } } limit 20";

    public static final String ART_MOVEMENTS_LIST = "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "SELECT DISTINCT ?code_Label ?code_ { ?painting wdt:P31 wd:Q3305213. ?painting ?p ?mov. ?mov wdt:P135 ?code_. ?code_ wdt:P31 wd:Q968159. " +
            "SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" } }";

//    Q28483
    public static final String ARTIST_INTENT = "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "SELECT ?coord (count(*) as ?count) " +
            "WHERE { ?painting wdt:P31 wd:Q3305213 . ?painting wdt:P276 ?location . ?painting wdt:P170 wd:" + "%s" + ". ?location wdt:P625 ?coord " +
            "SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }} " +
            "GROUP BY ?coord ORDER BY DESC(?count)";

//    Q170292
    public static final String ART_MOVEMENT_INTENT = "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "SELECT ?coord (count(*) as ?count) " +
            "WHERE { ?painting wdt:P31 wd:Q3305213 . ?painting wdt:P276 ?location . ?painting wdt:P135 wd:" + "%s" + ". ?location wdt:P625 ?coord " +
            "SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }} " +
            "GROUP BY ?coord ORDER BY DESC(?count)";

//    Point(25 30)
    public static final String UNKNOWN_ART_INTENT = "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "SELECT (count(*) as ?count) " +
            "WHERE {?painting wdt:P31 wd:Q3305213. ?painting wdt:P276 ?location. ?location wdt:P625 ?coord. " +
            "SERVICE wikibase:around {?location wdt:P625 ?localization. bd:serviceParam wikibase:center \"Point(%s %s)\"^^geo:wktLiteral. bd:serviceParam wikibase:radius \"500\". bd:serviceParam wikibase:distance ?distance.} " +
            "SERVICE wikibase:label {bd:serviceParam wikibase:language \"en\" } }";
}
