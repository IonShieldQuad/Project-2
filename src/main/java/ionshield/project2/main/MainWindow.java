package ionshield.project2.main;

import ionshield.project2.graphics.GraphDisplay;
import ionshield.project2.math.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    private JTextField c0Field;
    private JButton calculateButton;
    private GraphDisplay graphCout;
    private JTextField p1Field;
    private JTextField m0Field;
    private JTextField t0Field;
    private JTextField deltaField;
    private JTextField deltaCField;
    private JTextField deltaMField;
    private JTextField deltaTfield;
    private JTextField timeField;
    private JTextField deltaTimeField;
    private GraphDisplay graphCin;
    private GraphDisplay graphMin;
    private GraphDisplay graphTp;
    
    public static final String TITLE = "Project-2";
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        calculateButton.addActionListener(e -> calculate());
    }
    
    
    
    private void calculate() {
        try {
            log.setText("");
            
            double c0 = Double.parseDouble(c0Field.getText());
            double m0 = Double.parseDouble(m0Field.getText());
            double t0 = Double.parseDouble(t0Field.getText());
    
            double deltaC = Double.parseDouble(deltaCField.getText());
            double deltaM = Double.parseDouble(deltaMField.getText());
            double deltaT = Double.parseDouble(deltaTfield.getText());
    
            double time = Double.parseDouble(timeField.getText());
            double deltaTime = Double.parseDouble(deltaTimeField.getText());
            
            int steps = Math.min((int)Math.round(time / deltaTime), 1000000);
            
            Evaporator evaporator = new Evaporator();
            Interpolator[] result = new Interpolator[4];
            
            List<PointDouble> points0 = new ArrayList<>();
            List<PointDouble> points1 = new ArrayList<>();
            List<PointDouble> points2 = new ArrayList<>();
            List<PointDouble> points3 = new ArrayList<>();
    
            evaporator.init(c0, m0, t0);
            
            points0.add(new PointDouble(0, evaporator.getcOut()));
            points1.add(new PointDouble(0, c0));
            points2.add(new PointDouble(0, m0));
            points3.add(new PointDouble(0, t0));
            
            for (int i = 0; i <= steps; i++) {
                evaporator.tick(deltaTime, c0 + deltaC, m0 + deltaM, t0 + deltaT);
                
                double currTime = evaporator.getTime();
    
                PointDouble point = new PointDouble(currTime, evaporator.getcOut());
                
                points0.add(point);
                points1.add(new PointDouble(currTime, c0 + deltaC));
                points2.add(new PointDouble(currTime, m0 + deltaM));
                points3.add(new PointDouble(currTime, t0 + deltaT));
                
                log.append("\n" + point.toString(6));
            }
                    
            result[0] = new LinearInterpolator(points0);
            result[1] = new LinearInterpolator(points1);
            result[2] = new LinearInterpolator(points2);
            result[3] = new LinearInterpolator(points3);
            
            updateGraphs(result);
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    private void updateGraphs(Interpolator[] result) {
        try {
            if (result == null || result.length < 4 || result[0] == null || result[1] == null || result[2] == null || result[3] == null) {
                graphCout.setInterpolators(new ArrayList<>());
                graphCout.repaint();
                graphCin.setInterpolators(new ArrayList<>());
                graphCin.repaint();
                graphMin.setInterpolators(new ArrayList<>());
                graphMin.repaint();
                graphTp.setInterpolators(new ArrayList<>());
                graphTp.repaint();
                return;
            }
            graphCout.setMinX(result[0].lower());
            graphCout.setMaxX(result[0].upper());
            graphCout.setMinY(result[0].lowerVal());
            graphCout.setMaxY(result[0].upperVal());
    
            graphCin.setMinX(result[1].lower());
            graphCin.setMaxX(result[1].upper());
            graphCin.setMinY(result[1].lowerVal());
            graphCin.setMaxY(result[1].upperVal());
    
            graphMin.setMinX(result[2].lower());
            graphMin.setMaxX(result[2].upper());
            graphMin.setMinY(result[2].lowerVal());
            graphMin.setMaxY(result[2].upperVal());
    
            graphTp.setMinX(result[3].lower());
            graphTp.setMaxX(result[3].upper());
            graphTp.setMinY(result[3].lowerVal());
            graphTp.setMaxY(result[3].upperVal());
            
            graphCout.setInterpolators(Collections.singletonList(result[0]));
            graphCin.setInterpolators(Collections.singletonList(result[1]));
            graphMin.setInterpolators(Collections.singletonList(result[2]));
            graphTp.setInterpolators(Collections.singletonList(result[3]));
            
            //graph.setInterpolatorsHighligthed(Collections.singletonList(results.get(results.size() - 1)));
    
            graphCout.repaint();
            graphCin.repaint();
            graphMin.repaint();
            graphTp.repaint();
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
