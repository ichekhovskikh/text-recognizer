package ontology;

public class OntologyUtils {
    private OntologyUtils() {}

    public static String emptyOntologyText() {
        return "<rdf:RDF\n" +
                "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
                "    xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n" +
                "    xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n" +
                "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\">\n" +
                "</rdf:RDF>";
    }
}
