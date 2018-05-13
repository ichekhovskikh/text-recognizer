package ontology.graph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.Collection;
import java.util.NoSuchElementException;

public class OntologyGraph {
    private Graph<OntologyVertex, OntologyEdge> graph;

    public OntologyGraph(){
        this(new SparseGraph<>());
    }

    public OntologyGraph(Graph<OntologyVertex, OntologyEdge> graph){
        this.graph = graph;
    }

    public void addVertex(OntologyVertex vertex){
        if (findVertex(vertex) == null) {
            graph.addVertex(vertex);
        }
    }

    public void addEdge(OntologyVertex subject, OntologyEdge property, OntologyVertex object) {
        OntologyVertex subjectVertex = findVertex(subject);
        if (subjectVertex == null) {
            subjectVertex = subject;
            graph.addVertex(subjectVertex);
        }
        OntologyVertex objectVertex = findVertex(object);
        if (objectVertex == null) {
            objectVertex = object;
            graph.addVertex(objectVertex);
        }
        graph.addEdge(GraphUtils.localizeEdge(property), subjectVertex, objectVertex, EdgeType.DIRECTED);
    }

    public Graph<OntologyVertex, OntologyEdge> getGraph() {
        return graph;
    }

    private OntologyVertex findVertex(OntologyVertex vertex) {
        OntologyVertex searchedVertex;
        try {
            searchedVertex = graph.getVertices()
                    .stream()
                    .findFirst()
                    .filter(elem -> elem.equals(vertex))
                    .get();
        } catch (NoSuchElementException e){
            searchedVertex = null;
        }
        return searchedVertex;
    }
}
