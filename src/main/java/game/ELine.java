package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static game.Graph.availableLines;
import static game.GameThread.clickEdge;


public class ELine extends JLabel implements Comparable, Serializable {
    // The graphical display of the edges


    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    // Whether the line has been clicked or not
    private boolean activated = false;
    // the bottom left x and y coordinates of the line
    private int edgeListIndex;
    // the vertices
    public ArrayList<Vertex> vertices;
    // whether it's horizontal
    private boolean horizontal;
    // Whether the line has been calculated
    private boolean calculated = false;

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public ELine(boolean act,int edL,ArrayList<Vertex> v,boolean h, boolean c){
        activated=act;
        edgeListIndex=edL;
        vertices=v;
        horizontal=h;
        calculated=c;
    }


    public ELine(int w,int h,int x,int y,ArrayList<Vertex> v){
        vertices=v;
        if(vertices.get(1).getID()-vertices.get(0).getID()==1){
            horizontal=true;
        }else{
            horizontal=false;
        }
        // the line starts off invisible, e.g White
        setBackground(Color.WHITE);
        setBounds(x,y,w,h);
        setOpaque(true);
        // the mouseListener

        addMouseListener(new MouseAdapter() {
                // when the player hovers over a line it displays it in their colour
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!activated&&(Graph.isBothPlayers()||(Graph.isPlayerPlays()&&Graph.isPlayerisP1()==Graph.player1Turn))) {
                        if (Graph.getPlayer1Turn()) {
                            setBackground(Color.RED);
                        } else {
                            setBackground(Color.BLUE);
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!activated&&(Graph.isBothPlayers()||(Graph.isPlayerPlays()&&Graph.isPlayerisP1()==Graph.player1Turn))) {
                        setBackground(Color.WHITE);
                    }
                }

                // when clicked
                @Override
                public void mousePressed(MouseEvent e) {
                    //  if the line has not been activated before
                    if (!activated&&(Graph.isBothPlayers()||(Graph.isPlayerPlays()&&Graph.isPlayerisP1()==Graph.player1Turn))) {
                        int index = -1;
                        for (int p = availableLines.size() - 1; p >= 0; p--) {
                            if (availableLines.get(p).vertices.get(0).getID() == vertices.get(0).getID() && availableLines.get(p).vertices.get(1).getID() == vertices.get(1).getID()) {
                                index = p;
                            }
                        }
                        if (index == -1) {
                            for (ELine x : availableLines) {
                                System.out.print(x.toString() + " | ");
                            }
                            System.out.println();
                        }
                        try {
                            clickEdge(index);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                }
            });
    }


    public void setActivated(boolean b) {
        activated=b;
    }

    public boolean getHorizontal() {
        return horizontal;
    }
    public int getEdgeListIndex() {
        return edgeListIndex;
    }

    public void setEdgeListIndex(int edgeListIndex) {
        this.edgeListIndex = edgeListIndex;
    }
    public boolean isActivated() {
        return activated;
    }
    public String toString(){
        return vertices.get(0).getID()+" -- "+vertices.get(1).getID();
    }

    @Override
    public int compareTo(Object o) {
        if(vertices.get(0).id>((ELine) o).vertices.get(0).id){
            return 1;
        }
        if(vertices.get(0).id<((ELine) o).vertices.get(0).id){
            return -1;
        }
        if(vertices.get(0).id==((ELine) o).vertices.get(0).id){
            if(vertices.get(1).id>((ELine) o).vertices.get(1).id){
                return 1;
            }
            if(vertices.get(1).id<((ELine) o).vertices.get(1).id){
                return -1;
            }
        }
        return 0;
    }

    // checks whether placing this edge creates a box, through the adjacency matrix
    // adds each box the line does create to an arrayList of 4 vertices, then returns an arrayList of those arrayLists.
    public ArrayList<ArrayList<Vertex>> checkBox(int[][] matrix) {
        ArrayList<ArrayList<Vertex>> listOfBoxes = new ArrayList<>();
        if (horizontal) {
            if (vertices.get(0).getUpVertex() != null) {
                if (matrix[vertices.get(0).getID()][vertices.get(0).getUpVertex().getID()] == 2 && matrix[vertices.get(1).getID()][vertices.get(1).getUpVertex().getID()] == 2 && matrix[vertices.get(0).getUpVertex().getID()][vertices.get(1).getUpVertex().getID()] == 2) {
                    ArrayList<Vertex> box = new ArrayList<>();
                    box.add(vertices.get(0));
                    box.add(vertices.get(1));
                    box.add(vertices.get(0).getUpVertex());
                    box.add(vertices.get(1).getUpVertex());
                    listOfBoxes.add(box);
                }
            }
            if (vertices.get(0).getDownVertex() != null) {
                if (matrix[vertices.get(0).getID()][vertices.get(0).getDownVertex().getID()] == 2 &&matrix[vertices.get(1).getID()][vertices.get(1).getDownVertex().getID()] == 2 &&matrix[vertices.get(0).getDownVertex().getID()][vertices.get(1).getDownVertex().getID()] == 2) {
                    ArrayList<Vertex> box2 = new ArrayList<>();
                    box2.add(vertices.get(0));
                    box2.add(vertices.get(1));
                    box2.add(vertices.get(0).getDownVertex());
                    box2.add(vertices.get(1).getDownVertex());
                    listOfBoxes.add(box2);
                }
            }
        } else {
            if (vertices.get(0).getRightVertex() != null) {
                if (matrix[vertices.get(0).getID()][vertices.get(0).getRightVertex().getID()] == 2 && matrix[vertices.get(1).getID()][vertices.get(1).getRightVertex().getID()] == 2 && matrix[vertices.get(0).getRightVertex().getID()][vertices.get(1).getRightVertex().getID()] == 2) {
                    ArrayList<Vertex> box3 = new ArrayList<>();
                    box3.add(vertices.get(0));
                    box3.add(vertices.get(1));
                    box3.add(vertices.get(0).getRightVertex());
                    box3.add(vertices.get(1).getRightVertex());
                    listOfBoxes.add(box3);
                }
            }
            if (vertices.get(0).getLeftVertex() != null) {
                if (matrix[vertices.get(0).getID()][vertices.get(0).getLeftVertex().getID()] == 2 && matrix[vertices.get(1).getID()][vertices.get(1).getLeftVertex().getID()] == 2 &&matrix[vertices.get(0).getLeftVertex().getID()][vertices.get(1).getLeftVertex().getID()] == 2) {
                    ArrayList<Vertex> box4 = new ArrayList<>();
                    box4.add(vertices.get(0));
                    box4.add(vertices.get(1));
                    box4.add(vertices.get(0).getLeftVertex());
                    box4.add(vertices.get(1).getLeftVertex());
                    listOfBoxes.add(box4);
                }
            }
        }
        // if it creates no boxes, return null.
        if (listOfBoxes.isEmpty()) {
            return null;
        }
        return listOfBoxes;
    }

    // if every ScoreBox is active, the game is over
    public boolean checkFinished(List<ScoreBox> counterBoxes) {
        for (ScoreBox box : counterBoxes) {
            if (!box.getActivated()) {
                return false;
            }
        }
        return true;
    }

    // gets an arrayList of 4 vertices and finds the matching ScoreBox in counterBoxes through their average x and y coordinates, then displays it.
    // for when a box is completed
    // it uses average x and y coordinates because then no matter the order of the arrayList, if they have the same average x and y then they are the same box.
    public void checkMatching(ArrayList<Vertex> box) {
        int avgX = 0;
        int avgY = 0;
        for (Vertex v : box) {
            avgX += v.getWidth();
            avgY += v.getHeight();
        }
        avgX = avgX / 4;
        avgY = avgY / 4;
        for (ScoreBox sc : Graph.getCounterBoxes()) {
            if (sc.getAvgX() == avgX && sc.getAvgY() == avgY) {
                sc.setText();
            }
        }
    }

}

