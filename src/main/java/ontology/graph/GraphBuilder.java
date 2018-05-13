package ontology.graph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class GraphBuilder {
    private Graph<OntologyVertex, OntologyEdge> graph;


    public GraphBuilder(){
        graph = new SparseGraph<>();
    }
}
