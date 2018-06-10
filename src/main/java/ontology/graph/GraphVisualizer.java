package ontology.graph;

import edu.uci.ics.jung.visualization.renderers.Renderer;

import java.awt.*;

public interface GraphVisualizer {
    void addVertexFillPaint();
    void addEdgeStroke();
    void addLabels();
    void setPosition(Renderer.VertexLabel.Position position);
    Component getVisualizationComponent();
}
