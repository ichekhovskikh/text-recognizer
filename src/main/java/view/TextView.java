package view;

import edu.uci.ics.jung.visualization.renderers.Renderer;
import nlp.NlpSentence;
import nlp.NlpText;
import nlp.NlpUtils;
import nlp.NlpController;
import nlp.analyzers.NlpParseException;
import nlp.words.NamedWord;
import nlp.words.RelationWord;
import ontology.OntologyController;
import ontology.graph.GraphVisualizer;
import ontology.graph.OntologyGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class TextView {
    private OntologyController ontologyController;
    private NlpController nlpController;

    private WordsView wordsView;

    private JTextArea textArea;
    private JButton btnStart, btnAdd, btnShow;
    private JLabel label;
    private JFrame frame;

    public TextView(OntologyController ontologyController, NlpController nlpController) {
        this(ontologyController, nlpController, new Font("Custom", Font.PLAIN, 28));
    }

    public TextView(OntologyController ontologyController, NlpController nlpController, Font font) {
        this.ontologyController = ontologyController;
        this.nlpController = nlpController;
        wordsView = new WordsView();
        initComponents(font);
    }

    public void setFont(Font font) {
        label.setFont(font);
        textArea.setFont(font);
        btnStart.setFont(font);
        btnAdd.setFont(font);
        btnShow.setFont(font);
    }

    private void initComponents(Font font) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel labelPanel = new JPanel();
        label = new JLabel("Введите текст:");
        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalStrut(10));

        textArea = new JTextArea(20, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JPanel grid = new JPanel(new GridLayout(1, 3, 5, 0));
        btnStart = new JButton("Обработать");
        btnAdd = new JButton("Добавить в онтологию");
        btnShow = new JButton("Построить онтологию");
        btnStart.addActionListener(new StartAction());
        btnAdd.addActionListener(new AddAction());
        btnShow.addActionListener(new ShowAction());
        btnAdd.setEnabled(false);
        grid.add(btnStart);
        grid.add(btnAdd);
        grid.add(btnShow);
        flow.add(grid);

        panel.add(labelPanel);
        panel.add(new JScrollPane(textArea));
        panel.add(Box.createVerticalStrut(10));
        panel.add(flow);

        setFont(font);

        frame = new JFrame("Распознование текста");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private class StartAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            NlpText text = new NlpText(textArea.getText());
            wordsView.clearTable();
            for (NlpSentence sentence : text.getAllSentences()) {
                try {
                    nlpController.setSentence(sentence);
                } catch (IOException | NlpParseException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось обработать [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
                List<RelationWord> properties = nlpController.getRelationWords();
                for (RelationWord property : properties) {
                    List<NamedWord> subjects = nlpController.getParentRelationship(property);
                    List<NamedWord> objects = nlpController.getChildRelationship(property);

                    for (NamedWord subject : subjects) {
                        for (NamedWord object : objects) {
                            String subjectName = NlpUtils.wordMatching(subject.getIndexes(), nlpController.getSyntaxWords(), nlpController.getMorphWords());
                            subjectName += " [" + NlpUtils.getLocalizeName(subject.getNamedTag()) + "]";

                            String objectName = NlpUtils.wordMatching(object.getIndexes(), nlpController.getSyntaxWords(), nlpController.getMorphWords());
                            objectName += " [" + NlpUtils.getLocalizeName(object.getNamedTag()) + "]";

                            wordsView.addTableRow(subjectName.toUpperCase(),
                                    NlpUtils.getLocalizeType(property.getType()).toUpperCase(),
                                    objectName.toUpperCase());
                        }
                    }
                }
            }
            wordsView.setVisible(true);
            btnAdd.setEnabled(true);
        }
    }

    private class AddAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            NlpText text = new NlpText(textArea.getText());
            for (NlpSentence sentence : text.getAllSentences()) {
                try {
                    nlpController.setSentence(sentence);
                } catch (IOException | NlpParseException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось построить онтологию [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
                List<RelationWord> properties = nlpController.getRelationWords();
                for (RelationWord property : properties) {
                    List<NamedWord> subjects = nlpController.getParentRelationship(property);
                    List<NamedWord> objects = nlpController.getChildRelationship(property);

                    for (NamedWord subject : subjects) {
                        for (NamedWord object : objects) {
                            String subjectName = NlpUtils.wordMatching(subject.getIndexes(), nlpController.getSyntaxWords(), nlpController.getMorphWords());
                            String subjectClassName = NlpUtils.getClassName(subject.getNamedTag());

                            String objectName = NlpUtils.wordMatching(object.getIndexes(), nlpController.getSyntaxWords(), nlpController.getMorphWords());
                            String objectClassName = NlpUtils.getClassName(object.getNamedTag());

                            ontologyController.addIndividual(subjectName, subjectClassName);
                            ontologyController.addIndividual(objectName, objectClassName);
                            ontologyController.addIndividualProperty(subjectName, objectName, property.getType());
                        }
                    }
                }
            }
            ontologyController.commit();
        }
    }

    private class ShowAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            OntologyGraph graph = ontologyController.getGraph();
            GraphVisualizer visualizer = new GraphVisualizer(graph, new Dimension(1000, 1000));
            visualizer.addLabels();
            visualizer.setPosition(Renderer.VertexLabel.Position.CNTR);
            //visualizer.addVertexFillPaint();
            GraphView graphView = new GraphView(visualizer);
        }
    }
}
