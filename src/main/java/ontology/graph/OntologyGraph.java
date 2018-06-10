package ontology.graph;

import edu.uci.ics.jung.graph.Graph;

public interface OntologyGraph {
    void addVertex(OntologyVertex vertex);
    void addEdge(OntologyVertex subject, OntologyEdge property, OntologyVertex object);
    Graph<OntologyVertex, OntologyEdge> getGraph();
}
