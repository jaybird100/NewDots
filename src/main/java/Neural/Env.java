package Neural;

import game.*;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


public class Env implements MDP<GState, Integer, DiscreteSpace> {
    DiscreteSpace actionSpace = new DiscreteSpace(Graph.getEdgeList().size());

    // takes amount of possible edges      ^
    ObservationSpace<GState> observationSpace = new ArrayObservationSpace(new int[] {Graph.matrix.length*Graph.matrix[0].length});
    private GState GState = new GState(Graph.getMatrix(),0,0,1,Graph.getAvailableLines(),true,Graph.getCounterBoxes(),Graph.getEdgeList());
    boolean illegal=false;
    public Env(){ }
    @Override
    public ObservationSpace<GState> getObservationSpace() {
        return observationSpace;
    }
    @Override
    public DiscreteSpace getActionSpace() {
        return actionSpace;
    }
    @Override
    public GState reset() {
       // System.out.println("RESET");
        try {
            GameBoard r = new GameBoard(Neural.boardHeight, Neural.boardWidth);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     //   System.out.println("RESET");
        GState =new GState(Graph.getMatrix(),0,0,1,Graph.getAvailableLines(),true,Graph.getCounterBoxes(),Graph.getEdgeList());
        return GState;
    }
    @Override
    public void close() { }
    @Override
    public StepReply<GState> step(Integer action) {
      //  System.out.println(state.toString());
      //  System.out.println("Action: "+action);
     //   System.out.println(Arrays.deepToString(Graph.getMatrix()));
        boolean scoredPoint=false;
        int reward;
        placeEdge(action);
        if(GState.numMoves==1&& GState.botTurn){
            scoredPoint=true;
        }
        GState.availLines=Graph.availCheck(GState.availLines);
        //System.out.println("BOT SCORE: "+Graph.player1Score);
        // change the getPlayer1 to whichever player the neural is
     //   System.out.println("step: "+state.step);
        if(!illegal) {
            if (isDone()&&(GState.botScore+ GState.otherPlayerScore)== GState.countBoxes.size()) {

                if(Neural.measure) {
                    Neural.gamesPlayed++;
                }
                if(Neural.gamesPlayed%100==0&& Neural.measure){
                    System.out.println(Neural.wins+":"+ Neural.draws+":"+ Neural.loses);
                }
                System.out.println("SCORE: "+ GState.botScore+":"+ GState.otherPlayerScore);
                if (GState.botScore > GState.otherPlayerScore) {
                    System.out.println("WIN REWARD");
                    reward = 500;
                    if(Neural.measure){
                        Neural.wins++;
                    }
                } else {
                    if (GState.botScore == GState.otherPlayerScore) {
                        reward = 0;
                        if(Neural.measure) {
                            Neural.draws++;
                        }
                    } else {
                        reward = -10;
                        if(Neural.measure) {
                            Neural.loses++;
                        }
                    }
                }
            }else {
                if(scoredPoint){
                    reward= 5 ;
                }else {
                    reward = 1;
                    GState.botTurn = false;
                    GState.numMoves=1;
                    int counter=0;
                    while(GState.numMoves>0){
                        if (!isDone()) {
                            counter++;
                            placeRandomEdge();
                            GState.availLines=Graph.availCheck(GState.availLines);
                            if(counter>1){
                                reward=-4;
                            }
                        } else {
                            GState.numMoves = 0;
                            if((GState.botScore+ GState.otherPlayerScore)== GState.countBoxes.size()) {
                                if(Neural.measure) {
                                    Neural.gamesPlayed++;
                                }
                                if(Neural.gamesPlayed%100==0&& Neural.measure){
                                    System.out.println(Neural.wins+":"+ Neural.draws+":"+ Neural.loses);
                                }
                                System.out.println("score: " + GState.botScore + ":" + GState.otherPlayerScore);
                                //   System.out.println("score = 4 : "+((state.botScore+state.otherPlayerScore)==4));
                                if (GState.botScore > GState.otherPlayerScore) {
                                    System.out.println("WIN REWARD");
                                    reward = 500;
                                    if(Neural.measure){
                                        Neural.wins++;
                                    }
                                } else {
                                    if (GState.botScore == GState.otherPlayerScore) {
                                        reward = 0;
                                        if(Neural.measure) {
                                            Neural.draws++;
                                        }
                                    } else {
                                        reward = -10;
                                        if(Neural.measure) {
                                            Neural.loses++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    GState.botTurn = true;
                    GState.numMoves=1;
                }
            }
        }else{
            reward=-20;
            illegal=false;
        }
        GState t = new GState(GState.matrix, GState.botScore, GState.otherPlayerScore, GState.numMoves, GState.availLines, GState.botTurn, GState.countBoxes, GState.edgeList);
        GState =t;
        return new StepReply<>(t, reward, isDone(), null);
    }

    @Override
    public boolean isDone() {
        for(ScoreBox box: GState.countBoxes){
            if(!box.getActivated()){
                return false;
            }
        }
        return true;
    }

    @Override
    public MDP<GState, Integer, DiscreteSpace> newInstance() {
        Env test = new Env();
        return test;
    }
    public void placeEdge(int index){
        ELine line = GState.edgeList.get(index).getEline();
        if(!line.isActivated()) {
           // System.out.println("NChosen: "+line.vertices.get(0).id+"--"+line.vertices.get(1).id);
            line.setActivated(true);
            // make it black
            line.setBackground(Color.BLACK);
            line.repaint();
            // set the adjacency matrix to 2, 2==is a line, 1==is a possible line
            GState.matrix[line.vertices.get(0).getID()][line.vertices.get(1).getID()] = 2;
            GState.matrix[line.vertices.get(1).getID()][line.vertices.get(0).getID()] = 2;
            // gets an arrayList of each box the ELine creates. The box is an arrayList of 4 vertices.
            ArrayList<ArrayList<Vertex>> boxes = checkBox(line);
            if (boxes != null) {
                for (ArrayList<Vertex> box : boxes) {
                    // looks through the counterBoxes arrayList and sets the matching one visible.
                    checkMatching(box);
                    // updates the score board
                    if (GState.botTurn) {
                        GState.botScore++;
                    } else {
                        GState.otherPlayerScore++;
                    }
                }
                // if every counterBox has been activated, the game is over
            } else {
                GState.numMoves=0;
                // switches turn. If randomBot is active switches to their turn.
            }
        }else{
          //  System.out.println("ILLEGAL");
            illegal=true;
        }
    }
    public ArrayList<ArrayList<Vertex>> checkBox(ELine line){
        ArrayList<ArrayList<Vertex>> listOfBoxes = new ArrayList<>();
        if(line.getHorizontal()){
            if(line.vertices.get(0).getUpVertex()!=null){
                if(GState.matrix[line.vertices.get(0).getID()][line.vertices.get(0).getUpVertex().getID()]==2&& GState.matrix[line.vertices.get(1).getID()][line.vertices.get(1).getUpVertex().getID()]==2&& GState.matrix[line.vertices.get(0).getUpVertex().getID()][line.vertices.get(1).getUpVertex().getID()]==2){
                    ArrayList<Vertex> box = new ArrayList<>();
                    box.add(line.vertices.get(0));
                    box.add(line.vertices.get(1));
                    box.add(line.vertices.get(0).getUpVertex());
                    box.add(line.vertices.get(1).getUpVertex());
                    listOfBoxes.add(box);
                }
            }
            if(line.vertices.get(0).getDownVertex()!=null){
                if(GState.matrix[line.vertices.get(0).getID()][line.vertices.get(0).getDownVertex().getID()]==2&& GState.matrix[line.vertices.get(1).getID()][line.vertices.get(1).getDownVertex().getID()]==2&& GState.matrix[line.vertices.get(0).getDownVertex().getID()][line.vertices.get(1).getDownVertex().getID()]==2){
                    ArrayList<Vertex> box2 = new ArrayList<>();
                    box2.add(line.vertices.get(0));
                    box2.add(line.vertices.get(1));
                    box2.add(line.vertices.get(0).getDownVertex());
                    box2.add(line.vertices.get(1).getDownVertex());
                    listOfBoxes.add(box2);
                }
            }
        }else{
            if(line.vertices.get(0).getRightVertex()!=null){
                if(GState.matrix[line.vertices.get(0).getID()][line.vertices.get(0).getRightVertex().getID()]==2&& GState.matrix[line.vertices.get(1).getID()][line.vertices.get(1).getRightVertex().getID()]==2&& GState.matrix[line.vertices.get(0).getRightVertex().getID()][line.vertices.get(1).getRightVertex().getID()]==2){
                    ArrayList<Vertex> box3 = new ArrayList<>();
                    box3.add(line.vertices.get(0));
                    box3.add(line.vertices.get(1));
                    box3.add(line.vertices.get(0).getRightVertex());
                    box3.add(line.vertices.get(1).getRightVertex());
                    listOfBoxes.add(box3);
                }
            }
            if(line.vertices.get(0).getLeftVertex()!=null){
                if(GState.matrix[line.vertices.get(0).getID()][line.vertices.get(0).getLeftVertex().getID()]==2&& GState.matrix[line.vertices.get(1).getID()][line.vertices.get(1).getLeftVertex().getID()]==2&& GState.matrix[line.vertices.get(0).getLeftVertex().getID()][line.vertices.get(1).getLeftVertex().getID()]==2){
                    ArrayList<Vertex> box4 = new ArrayList<>();
                    box4.add(line.vertices.get(0));
                    box4.add(line.vertices.get(1));
                    box4.add(line.vertices.get(0).getLeftVertex());
                    box4.add(line.vertices.get(1).getLeftVertex());
                    listOfBoxes.add(box4);
                }
            }
        }
        // if it creates no boxes, return null.
        if(listOfBoxes.isEmpty()){
            return null;
        }
        return listOfBoxes;
    }

    public void checkMatching(ArrayList<Vertex> box){
        int avgX=0;
        int avgY=0;
        for(Vertex v:box){
            avgX+=v.getWidth();
            avgY+=v.getHeight();
        }
        avgX=avgX/4;
        avgY=avgY/4;
        for(ScoreBox sc: GState.countBoxes){
            if(sc.getAvgX()==avgX&&sc.getAvgY()==avgY){
                sc.setText();
            }
        }
    }

    public void placeRandomEdge() {
        // chosen is the index in availableLines of the edge it will choose to place
        int chosen;
        // checks to see if it can create a box
        int c=checkForBox();
        if (c != -1) {
                // if it can, it sets that to the index
            chosen = c;
        } else {
            // if not, selects a random edge that doesn't set up a box for the other player.
            // if that's not possible it just selects a random edge
            chosen = checkFor3s();
            if (!checkPick(chosen)) {
                //  System.out.println(Graph.getAvailableLines().get(chosen).toString());
                chosen = checkFor3s();
                //    System.out.println(Graph.getAvailableLines().get(chosen).toString());
            }
        }
        ELine line = GState.availLines.get(chosen);
      //  System.out.println("RChosen: "+line.vertices.get(0).id+"--"+line.vertices.get(1).id);
        line.setActivated(true);
        // make it black
        line.setBackground(Color.BLACK);
        line.repaint();
        // set the adjacency matrix to 2, 2==is a line, 1==is a possible line
        GState.matrix[line.vertices.get(0).getID()][line.vertices.get(1).getID()] = 2;
        GState.matrix[line.vertices.get(1).getID()][line.vertices.get(0).getID()] = 2;
        // gets an arrayList of each box the ELine creates. The box is an arrayList of 4 vertices.
        ArrayList<ArrayList<Vertex>> boxes = checkBox(line);
        if (boxes != null) {
            for (ArrayList<Vertex> box : boxes) {
                // looks through the counterBoxes arrayList and sets the matching one visible.
                checkMatching(box);
                // updates the score board
                if (GState.botTurn) {
                    GState.botScore++;
                } else {
                    GState.otherPlayerScore++;
                }
            }
        } else {
            GState.numMoves=0;
        }
    }
    // checks to see if it can create a box
    // returns the edge that creates the box's index in availableLines
    public int checkForBox(){
        // for each box in counterBoxes
        for(ScoreBox box: GState.countBoxes){
            int a = GState.matrix[box.getVertices().get(0).getID()][box.getVertices().get(1).getID()];
            int b = GState.matrix[box.getVertices().get(0).getID()][box.getVertices().get(2).getID()];
            int c = GState.matrix[box.getVertices().get(1).getID()][box.getVertices().get(3).getID()];
            int d = GState.matrix[box.getVertices().get(2).getID()][box.getVertices().get(3).getID()];
            // if each int adds up to 7, there must be 3 lines in a box. A line = 1 when available and = 2 when placed.
            // as 3 completed lines is 3*2=6, +1 for the remaining line == 7
            if(a+b+c+d==7){
                // checks to see which line is the available one, e.g == 1
                if(a==1){
                    return findMatch(box.getVertices().get(0).getID(),box.getVertices().get(1).getID());
                }
                if(b==1){
                    return findMatch(box.getVertices().get(0).getID(),box.getVertices().get(2).getID());
                }
                if(c==1){
                    return findMatch(box.getVertices().get(1).getID(),box.getVertices().get(3).getID());
                }
                if(d==1){
                    return findMatch(box.getVertices().get(2).getID(),box.getVertices().get(3).getID());
                }
            }
        }
        return -1;
    }
    // finds the index in available lines which matches the input vertex id's
    // e.g you input 5 and 4, it returns the index of the edge 4--5.
    public int findMatch(int a, int b){
        for(int p = GState.availLines.size()-1; p>=0; p--){
            if(GState.availLines.get(p).vertices.get(0).getID()==a&& GState.availLines.get(p).vertices.get(1).getID()==b){
                return p;
            }
        }
        for(int p = GState.availLines.size()-1; p>=0; p--){
            if(GState.availLines.get(p).vertices.get(0).getID()==b&& GState.availLines.get(p).vertices.get(1).getID()==a){
                return p;
            }
        }
        /*
        for(ELine l: Graph.getAvailableLines()){
            System.out.println(l.vertices.get(0).getID()+" -- "+l.vertices.get(1).getID());
        }

         */
        return -1;
    }
    // removes every edge which sets up a box for the other player
    public int checkFor3s(){
        ArrayList<Integer> av = new ArrayList<>();
        // goes through each availableLine
        for(int q = 0; q< GState.availLines.size(); q++){
            ELine edge = GState.availLines.get(q);
            boolean noBox=true;
            // if the edge is vertical, it can only have a box to the right and left of it.
            if(!edge.getHorizontal()){
                int leftBox=0;
                int rightBox=0;
                if(edge.vertices.get(0).getRightVertex()!=null){
                    rightBox = GState.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()+1]+ GState.matrix[edge.vertices.get(0).getID()+1][edge.vertices.get(1).getID()+1]+ GState.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()+1];
                }
                if(edge.vertices.get(0).getLeftVertex()!=null){
                    leftBox= GState.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()-1]+ GState.matrix[edge.vertices.get(0).getID()-1][edge.vertices.get(1).getID()-1]+ GState.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()-1];
                }
                // it adds up the int value of each edge in each box in the adjacency matrix
                // if it == 5, then placing another edge there will set up a box for the other player
                // it checks the 3 edges around the chosen edge, not the chosen edge itself
                // so if the 3 edge's sum == 5, then they must be 2+2+1 = 5
                // so there's 2 lines in the box, so putting another line there sets up the other player
                if(leftBox==5||rightBox==5){
                    noBox=false;
                }
            }else{
                // does the same but for horizontal edges
                int downBox=0;
                int upBox=0;
                if(edge.vertices.get(0).getDownVertex()!=null){
                    downBox= GState.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()+Graph.getWidth()]+ GState.matrix[edge.vertices.get(0).getID()+Graph.getWidth()][edge.vertices.get(1).getID()+Graph.getWidth()]+ GState.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()+Graph.getWidth()];
                }
                if(edge.vertices.get(0).getUpVertex()!=null){
                    upBox= GState.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()-Graph.getWidth()]+ GState.matrix[edge.vertices.get(0).getID()-Graph.getWidth()][edge.vertices.get(1).getID()-Graph.getWidth()]+ GState.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()-Graph.getWidth()];
                }
                if(upBox==5||downBox==5){
                    noBox=false;
                }
            }
            if(noBox){
                // if the line doesn't create a box it adds the index from availableLines to a new arrayList, av
                av.add(q);
            }
        }
        if(av.size()!=0){
            // if there are edges in av, it returns a random entry in av
            // all entries in av are indexes from availableLine
            // System.out.println("NO BOX: "+av.size());
            int ret = av.get((int)(Math.random()*av.size()));
            return ret;
        }else{
            // if not it just returns a random index from availableLine
            return (int)(Math.random()* GState.availLines.size());
        }
    }
    public boolean checkPick(int c){
        if(GState.availLines.get(c).isActivated()){
            GState.availLines.remove(c);
            return false;
        }
        return true;
    }
}
