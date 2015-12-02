package knoma.newsgroup.gui;

import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Instances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import javax.enterprise.context.ApplicationScoped;
import java.awt.*;

import static weka.core.Utils.doubleToString;

/**
 * Created by gabriel on 12/2/15.
 */
@ApplicationScoped
public class GraphEvaluationDisplay {
    public void show(Evaluation evaluation) {
        // generate curve
        ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0;
        Instances result = tc.getCurve(evaluation.predictions(), classIndex);

        // plot curve
        ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
        vmc.setROCString("(Area under ROC = " + doubleToString(tc.getROCArea(result), 4) + ")");
        vmc.setName(result.relationName());

        PlotData2D tempd = new PlotData2D(result);
        tempd.setPlotName(result.relationName());
        tempd.addInstanceNumberAttribute();

        // specify which points are connected
        boolean[] cp = new boolean[result.numInstances()];
        for (int n = 1; n < cp.length; n++)
            cp[n] = true;
        try {
            tempd.setConnectPoints(cp);

            // add plot
            vmc.addPlot(tempd);

            // display curve
            String plotName = vmc.getName();
            final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
            jf.setSize(500,400);
            jf.getContentPane().setLayout(new BorderLayout());
            jf.getContentPane().add(vmc, BorderLayout.CENTER);
            jf.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    jf.dispose();
                }
            });
            jf.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException("Cannot should evaluation");
        }

    }
}
