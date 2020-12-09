package game;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

public class DotDrawer extends LayerUI<JComponent> {
    // Layers the dots over the JPanel/JLabels
    public DotDrawer(){ }
    @Override
    public void paint(Graphics g, JComponent c){
        super.paint(g,c);
        Graphics2D g2 = (Graphics2D) g.create();
        for(int h=0;h<Graph.getVertexList().size();h++) {
            g2.fillOval(Graph.getVertexList().get(h).getWidth()-15,Graph.getVertexList().get(h).getHeight()-15,20,20);
        }
    }
}
