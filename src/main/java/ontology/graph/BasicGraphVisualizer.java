package ontology.graph;

import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.annotation.Nullable;
import java.awt.*;

public class BasicGraphVisualizer implements GraphVisualizer {
    private BasicVisualizationServer<OntologyVertex, OntologyEdge> visualizationServer;

    public BasicGraphVisualizer(OntologyGraph graph, Dimension size) {
        Layout<OntologyVertex, OntologyEdge> layout = new CircleLayout(graph.getGraph());
        layout.setSize(size);
        visualizationServer = new BasicVisualizationServer<>(layout);
        visualizationServer.setPreferredSize(new Dimension(size.width + 50, size.height + 50));

    }

    public void addVertexFillPaint() {
        Function<OntologyVertex, Paint> vertexPaint =
                new Function<OntologyVertex, Paint>() {
                    @Nullable
                    @Override
                    public Paint apply(@Nullable OntologyVertex vertex) {
                        return GraphUtils.getEntityColor(vertex.getTag());
                    }
                };
        visualizationServer.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
    }

    public void addEdgeStroke() {
        float dash[] = {10.0f};
        Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Function<OntologyEdge, Stroke> edgeStrokeTransformer =
                new Function<OntologyEdge, Stroke>() {
                    @Nullable
                    @Override
                    public Stroke apply(@Nullable OntologyEdge edge) {
                        return edgeStroke;
                    }
                };
        visualizationServer.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
    }

    public void addLabels() {
        visualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
    }

    public void setPosition(Renderer.VertexLabel.Position position) {
        visualizationServer.getRenderer().getVertexLabelRenderer().setPosition(position);
    }

    public Component getVisualizationComponent() {
        return visualizationServer;
    }
}
