package view;

import edu.uci.ics.jung.visualization.renderers.Renderer;
import nlp.NlpSentence;
import nlp.NlpText;
import nlp.NlpUtils;
import nlp.NlpController;
import nlp.analyzers.NlpParseException;
import nlp.analyzers.RelationAnalyzer;
import nlp.analyzers.SyntaxAnalyzer;
import nlp.analyzers.TreeTaggerMorphAnalyzer;
import nlp.tree.NlpTreeObject;
import nlp.tree.NlpTreeRelation;
import nlp.words.NamedWord;
import nlp.words.RelationWord;
import ontology.OntologyController;
import ontology.OntologyModelFactory;
import ontology.OntologyUtils;
import ontology.graph.GraphVisualizer;
import ontology.graph.OntologyGraph;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
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
        frame = new JFrame("Распознование текста");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        btnShow = new JButton("Показать онтологию");
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

        initMenu(font);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private void initMenu(Font font) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setFont(font);

        JMenuItem helpMenu = new JMenuItem("О программе");
        helpMenu.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Автоматизированная система построения онтологии взаимного расположения\n" +
                        "геометрических объектов на основе их текстового описания\n" +
                        "Группа: 6401-090301D\n" +
                        "Студент: И.В. Чеховских\n" +
                        "Руководитель ВКР: Е.В. Симонова",
                "О программе", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.setFont(font);

        JMenuItem newItem = new JMenuItem("Новый файл с онтологией");
        newItem.addActionListener(new NewOntologyAction());
        newItem.setFont(font);
        fileMenu.add(newItem);

        JMenuItem openItem = new JMenuItem("Выбрать файл с онтологией");
        openItem.addActionListener(new OpenOntologyAction());
        openItem.setFont(font);
        fileMenu.add(openItem);

        JMenu openDictionaryMenu = new JMenu("Выбрать словарь");
        openDictionaryMenu.setFont(font);
        fileMenu.add(openDictionaryMenu);

        JMenuItem openMorphItem = new JMenuItem("Морфологический");
        openMorphItem.addActionListener(new OpenMorphAction());
        openMorphItem.setFont(font);
        openDictionaryMenu.add(openMorphItem);

        JMenuItem openSyntaxItem = new JMenuItem("Синтаксический");
        openSyntaxItem.addActionListener(new OpenSyntaxAction());
        openSyntaxItem.setFont(font);
        openDictionaryMenu.add(openSyntaxItem);

        JMenuItem openRelationItem = new JMenuItem("Отношений");
        openRelationItem.addActionListener(new OpenRelationAction());
        openRelationItem.setFont(font);
        openDictionaryMenu.add(openRelationItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        exitItem.setFont(font);

        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);
    }

    private class StartAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            NlpText text = new NlpText(textArea.getText());
            wordsView.clearTable();
            nlpController.getNlpTreeDependency().clear();
            try {
                for (NlpSentence sentence : text.getAllSentences()) {
                    nlpController.setSentence(sentence);
                    nlpController.process();
                }
            } catch (IOException | NlpParseException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Не удалось обработать [" + ex.getMessage() + "]!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
            List<NlpTreeRelation> properties = nlpController.getNlpTreeDependency().getRelations();
            for (NlpTreeRelation property : properties) {
                List<NlpTreeObject> subjects = property.getHeads();
                List<NlpTreeObject> objects = property.getDependents();
                for (NlpTreeObject subject : subjects) {
                    for (NlpTreeObject object : objects) {
                        String subjectName = subject.getText() + " [" + NlpUtils.getLocalizeName(subject.getTag()) + "]";
                        String objectName = object.getText() + " [" + NlpUtils.getLocalizeName(object.getTag()) + "]";
                        wordsView.addTableRow(subjectName.toUpperCase(),
                                NlpUtils.getLocalizeType(property.getTag()).toUpperCase(),
                                objectName.toUpperCase());
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
            List<NlpTreeRelation> properties = nlpController.getNlpTreeDependency().getRelations();
            for (NlpTreeRelation property : properties) {
                List<NlpTreeObject> subjects = property.getHeads();
                List<NlpTreeObject> objects = property.getDependents();
                for (NlpTreeObject subject : subjects) {
                    for (NlpTreeObject object : objects) {
                        ontologyController.addIndividual(subject.getText(), subject.getTag());
                        ontologyController.addIndividual(object.getText(), object.getTag());
                        ontologyController.addIndividualProperty(subject.getText(), object.getText(), property.getTag());
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
            visualizer.addVertexFillPaint();
            GraphView graphView = new GraphView(visualizer);
        }
    }

    private class NewOntologyAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            final String FILE_EXTENSION = "rdf";
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "rdf", FILE_EXTENSION);
            chooser.setFileFilter(filter);
            if (chooser.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                path = path.endsWith("." + FILE_EXTENSION) ? path : path + "." + FILE_EXTENSION;
                try {
                    FileWriter writer = new FileWriter(path, false);
                    writer.write(OntologyUtils.emptyOntologyText());
                    writer.close();
                    ontologyController = new OntologyController(path);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось открыть онтологию [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class OpenOntologyAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "rdf", "rdf");
            chooser.setFileFilter(filter);
            if (chooser.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    ontologyController = new OntologyController(file.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось открыть онтологию [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class OpenMorphAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "par", "par");
            chooser.setFileFilter(filter);
            if (chooser.showDialog(null, "Выбрать словарь") == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    TreeTaggerMorphAnalyzer morphAnalyzer = new TreeTaggerMorphAnalyzer(file.getAbsolutePath());
                    nlpController.setMorphAnalyzer(morphAnalyzer);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось выбрать морфологический словарь [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class OpenSyntaxAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "mco", "mco");
            chooser.setFileFilter(filter);
            if (chooser.showDialog(null, "Выбрать словарь") == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(
                            nlpController.getMorphAnalyzer(),
                            file.getAbsolutePath());
                    nlpController.setSyntaxAnalyzer(syntaxAnalyzer);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось выбрать синтаксический словарь [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class OpenRelationAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "rel", "rel");
            chooser.setFileFilter(filter);
            if (chooser.showDialog(null, "Выбрать словарь") == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    RelationAnalyzer relationAnalyzer = new RelationAnalyzer(
                            file.getAbsolutePath());
                    nlpController.setRelationAnalyzer(relationAnalyzer);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Не удалось выбрать словарь отношений [" + ex.getMessage() + "]!",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}
