package view;

import ontology.graph.GraphVisualizer;

import javax.swing.*;

public class GraphView {
    private GraphVisualizer visualizer;

    public GraphView(GraphVisualizer visualizer) {
        this.visualizer = visualizer;
        initComponents();
    }

    private void initComponents() {
        JFrame frame = new JFrame("Онтология");
        frame.getContentPane().add(visualizer.getVisualizationComponent());
        frame.pack();
        frame.setVisible(true);
    }
}
