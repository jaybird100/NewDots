package minMax;

import game.ELine;
import game.Graph;
import game.Vertex;

import java.util.ArrayList;


public class Node{
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    int depth;
    Node parent;
    int[][] matrix;
    int botScore;
    int oScore;
    ArrayList<ELine> availLines;
    boolean botTurn;
    boolean terminal;
    boolean bonusTurn;
    public ELine move;

    Node dummyTurn;



    public String toString(){

        String toReturn = "";
        if(dummyTurn!=null){
            toReturn+="DUMMY: ";
        }
        if(move!=null){
            toReturn+="move: "+move.toString()+'\n';
        }
        toReturn+="av: ";
        for(ELine a:availLines){
            toReturn+=a.toString()+" ";
        }
        toReturn+= " |"+" sc: "+botScore+":"+oScore+" next turn: "+botTurn;
        if(bonusTurn){
            toReturn+= " BONUS TURN";
        }
        toReturn+=" Score: "+evaluation();

        return toReturn;
    }
    double evaluation(){
        double a = botScore-oScore;

        int numberOfChains = numberOfChains();
        double b;
        if(numberOfChains==0){
            b=0;
        }else{
            if((Graph.getHeight()*Graph.getWidth()%2!=0&&Graph.isMiniMaxP1())||(Graph.getHeight()*Graph.getWidth()%2==0&&!Graph.isMiniMaxP1())){
                if(numberOfChains%2!=0){
                    b=1;
                }else{
                    b=-1;
                }
            }else{
                if(numberOfChains%2==0){
                    b=1;
                }else{
                    b=-1;
                }
            }
        }
        int numberOfLongChains = numberOfLongChains()-numberOfLongLoops();
        double d;
        if(numberOfLongChains==0){
            d=0;
        }else{
            if((Graph.getHeight()*Graph.getWidth()%2!=0&&Graph.isMiniMaxP1())||(Graph.getHeight()*Graph.getWidth()%2==0&&!Graph.isMiniMaxP1())){
                if(numberOfLongChains%2!=0){
                    d=1;
                }else{
                    d=-1;
                }
            }else{
                if(numberOfLongChains%2==0){
                    d=1;
                }else{
                    d=-1;
                }
            }
        }

        double c;
        if(doubleCross()&&isLongerChainAvailable()){
            c=1;
        }else{
            c=0;
        }
        double e = 0;
        if(availLines.size()==0){
            if(botScore>oScore){
                e=1;
            }else{
                if(botScore<oScore){
                    e=-1;
                }else{
                    if(botScore==oScore){
                        e=-0.1;
                    }
                }
            }
        }
        // multipliers
        double a1 = MinMax.a;
        double b1 = MinMax.b;
        double c1 = MinMax.c;
        double d1 = MinMax.d;
        double e1= MinMax.e;
        double v = a1 * a + b1 * b + c1 * c+ d1*d+ e1*e;
        return v;
    }
    public boolean isLongerChainAvailable(){
        // double cross: if there's a longer chain available;
        matrix=parent.matrix;
        visited= new ArrayList<>();
        for(int i=0;i<Graph.getHeight()-1;i++){
            ArrayList<Boolean> row = new ArrayList<>();
            for(int w=0;w<Graph.getWidth()-1;w++){
                row.add(false);
            }
            visited.add(row);
        }
        if(!move.isHorizontal()){
            visited.get(move.vertices.get(0).id/Graph.getWidth()).set(move.vertices.get(0).id%Graph.getWidth(),true);
            visited.get((move.vertices.get(0).id-1)/Graph.getWidth()).set((move.vertices.get(0).id-1)%Graph.getWidth(),true);
        }else{
            visited.get(move.vertices.get(0).id/Graph.getWidth()).set(move.vertices.get(0).id%Graph.getWidth(),true);
            visited.get((move.vertices.get(0).id-Graph.getWidth())/Graph.getWidth()).set((move.vertices.get(0).id-Graph.getWidth())%Graph.getWidth(),true);
        }
        for(int i=0;i<Graph.getHeight()-1;i++) {
            for (int w = 0; w < Graph.getWidth() - 1; w++) {
                if (!visited.get(i).get(w)) {
                    int top = matrix[(i * Graph.getWidth()) + w][(i * Graph.getWidth()) + w + 1];
                    int left = matrix[(i * Graph.getWidth()) + w][((i + 1) * Graph.getWidth()) + w];
                    int right = matrix[(i * Graph.getWidth()) + w + 1][((i + 1) * Graph.getWidth()) + w + 1];
                    int bottom = matrix[((i + 1) * Graph.getWidth()) + w][((i + 1) * Graph.getWidth()) + w + 1];
                    //sideways
                    if (top == 2 && bottom == 2 && left == 1 && right == 1) {
                        if (countNumBoxesInChain(i, w, 1, boxType.sideways, visited) >= 3) {
                            return true;
                        }
                    }
                    //bottomleft
                    if (left == 2 && bottom == 2 && right == 1 && top == 1) {
                        if (countNumBoxesInChain(i, w, 1, boxType.bottomleft, visited) >= 3) {
                            return true;
                        }
                    }
                    //topleft
                    if (top == 2 && left == 2 && bottom == 1 && right == 1) {
                        if (countNumBoxesInChain(i, w, 1, boxType.topleft, visited) >= 3) {
                            return true;
                        }
                    }
                    //bottomright
                    if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                        if (countNumBoxesInChain(i, w, 1, boxType.bottomright, visited) >= 3) {
                            return true;
                        }
                    }
                    //topright
                    if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                        if (countNumBoxesInChain(i, w, 1, boxType.topright, visited) >= 3) {
                            return true;
                        }
                    }
                    //longways
                    if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                        if (countNumBoxesInChain(i, w, 1, boxType.longways, visited) >= 3) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean doubleCross(){
        ArrayList<ArrayList<Vertex>> temp = checkBox(move,matrix);
        if(temp!=null&&temp.size()>1){
            return true;
        }else{
            return false;
        }
    }
    public boolean doesMoveOpenShortestChain(ELine move){
        int[][] matrix = this.parent.matrix;
        visited= new ArrayList<>();
        int moveMaxChain;
        if(move.isHorizontal()){
            int upchain=-1;
            int downchain=-1;
            // above
            if(move.getVertices().get(0).id-Graph.getWidth()>=0){
                int top = matrix[move.getVertices().get(0).id-Graph.getWidth()][move.getVertices().get(1).id-Graph.getWidth()];
                int left = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id-Graph.getWidth()];
                int right = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id-Graph.getWidth()];
                int bottom = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];

                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    upchain=countNumBoxesInChain((move.getVertices().get(0).id-Graph.getWidth())/Graph.getWidth(),(move.getVertices().get(0).id-Graph.getWidth())%Graph.getWidth(),1,boxType.topleft,visited);
                }
                //topright
                if(top==2&&left==1&&bottom==1&&right==2){
                    upchain=countNumBoxesInChain((move.getVertices().get(0).id-Graph.getWidth())/Graph.getWidth(),(move.getVertices().get(0).id-Graph.getWidth())%Graph.getWidth(),1,boxType.topright,visited);

                }
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    upchain=countNumBoxesInChain((move.getVertices().get(0).id-Graph.getWidth())/Graph.getWidth(),(move.getVertices().get(0).id-Graph.getWidth())%Graph.getWidth(),1,boxType.longways,visited);
                }
            }
            //below
            if(move.getVertices().get(0).id+Graph.getWidth()<Graph.getVertexList().size()){
                int bottom = matrix[move.getVertices().get(0).id+Graph.getWidth()][move.getVertices().get(1).id+Graph.getWidth()];
                int left = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id+Graph.getWidth()];
                int right = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id+Graph.getWidth()];
                int top = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];

                //bottomleft
                if(top==1&&left==2&&bottom==2&&right==1){
                    downchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomleft,visited);
                }
                //bottomright
                if(top==1&&left==1&&bottom==2&&right==2){
                    downchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomright,visited);

                }
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    downchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.longways,visited);
                }
            }
            moveMaxChain=Math.max(upchain,downchain);
        }else{
            int rightchain=-1;
            int leftchain=-1;
            // right
            if(move.getVertices().get(0).id%Graph.getWidth()!=Graph.getWidth()-1){
                int bottom = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id+1];
                int left = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];
                int right = matrix[move.getVertices().get(0).id+1][move.getVertices().get(1).id+1];
                int top = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id+1];
                //topright
                if(top==1&&left==2&&bottom==2&&right==1){
                    rightchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.topright,visited);
                }
                //bottomright
                if(top==1&&left==1&&bottom==2&&right==2){
                    rightchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomright,visited);

                }
                //sideways
                if (top == 2 && bottom == 1 && left == 2 && right == 2) {
                    rightchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.sideways,visited);
                }
            }
            if(move.getVertices().get(0).id%Graph.getWidth()!=0){
                int bottom = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id-1];
                int right = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];
                int left = matrix[move.getVertices().get(0).id+1][move.getVertices().get(1).id-1];
                int top = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id-1];
                //topleft
                if(top==1&&left==2&&bottom==2&&right==1){
                    leftchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.topright,visited);
                }
                //bottomleft
                if(top==1&&left==1&&bottom==2&&right==2){
                    leftchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomright,visited);

                }
                //sideways
                if (top == 2 && bottom == 1 && left == 2 && right == 2) {
                    leftchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.sideways,visited);
                }
            }
            moveMaxChain=Math.max(leftchain,rightchain);
        }
        if(moveMaxChain==-1){
            return false;
        }
        for(int i=0;i<Graph.getHeight()-1;i++){
            for(int w=0;w<Graph.getWidth()-1;w++){
                if(!visited.get(i).get(w)){
                    int top = matrix[(i*Graph.getWidth())+w][(i*Graph.getWidth())+w+1];
                    int left = matrix[(i*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w];
                    int right = matrix[(i*Graph.getWidth())+w+1][((i+1)*Graph.getWidth())+w+1];
                    int bottom = matrix[((i+1)*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w+1];
                    //sideways
                    if(top==2&&bottom==2&&left==1&&right==1){
                        int temp =countNumBoxesInChain(i,w,1,boxType.sideways,visited);
                        if(temp<moveMaxChain){
                            return false;
                        }
                    }
                    //bottomleft
                    if(left==2&&bottom==2&&right==1&&top==1){
                        int temp =countNumBoxesInChain(i,w,1,boxType.bottomleft,visited);
                        if(temp<moveMaxChain){
                            return false;
                        }
                    }
                    //topleft
                    if(top==2&&left==2&&bottom==1&&right==1){
                        int temp =countNumBoxesInChain(i,w,1,boxType.topleft,visited);
                        if(temp<moveMaxChain){
                            return false;
                        }
                    }
                    //bottomright
                    if(left==1&&bottom==2&&right==2&&top==1){
                        int temp =countNumBoxesInChain(i,w,1,boxType.bottomright,visited);
                        if(temp<moveMaxChain){
                            return false;
                        }
                    }
                    //topright
                    if(top==2&&left==1&&bottom==1&&right==2){
                        int temp =countNumBoxesInChain(i,w,1,boxType.topright,visited);
                        if(temp<moveMaxChain){
                            return false;
                        }
                    }
                    //longways
                    if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                        int temp = countNumBoxesInChain(i,w,1,boxType.longways,visited);
                        if(temp<moveMaxChain){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean doesMoveOpen3Chain(){
        int[][] matrix = this.parent.matrix;
        visited= new ArrayList<>();
        int moveMaxChain;
        if(move.isHorizontal()){
            int upchain=-1;
            int downchain=-1;
            // above
            if(move.getVertices().get(0).id-Graph.getWidth()>=0){
                int top = matrix[move.getVertices().get(0).id-Graph.getWidth()][move.getVertices().get(1).id-Graph.getWidth()];
                int left = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id-Graph.getWidth()];
                int right = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id-Graph.getWidth()];
                int bottom = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];

                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    upchain=countNumBoxesInChain((move.getVertices().get(0).id-Graph.getWidth())/Graph.getWidth(),(move.getVertices().get(0).id-Graph.getWidth())%Graph.getWidth(),1,boxType.topleft,visited);
                }
                //topright
                if(top==2&&left==1&&bottom==1&&right==2){
                    upchain=countNumBoxesInChain((move.getVertices().get(0).id-Graph.getWidth())/Graph.getWidth(),(move.getVertices().get(0).id-Graph.getWidth())%Graph.getWidth(),1,boxType.topright,visited);

                }
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    upchain=countNumBoxesInChain((move.getVertices().get(0).id-Graph.getWidth())/Graph.getWidth(),(move.getVertices().get(0).id-Graph.getWidth())%Graph.getWidth(),1,boxType.longways,visited);
                }
            }
            //below
            if(move.getVertices().get(0).id+Graph.getWidth()<Graph.getVertexList().size()){
                int bottom = matrix[move.getVertices().get(0).id+Graph.getWidth()][move.getVertices().get(1).id+Graph.getWidth()];
                int left = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id+Graph.getWidth()];
                int right = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id+Graph.getWidth()];
                int top = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];

                //bottomleft
                if(top==1&&left==2&&bottom==2&&right==1){
                    downchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomleft,visited);
                }
                //bottomright
                if(top==1&&left==1&&bottom==2&&right==2){
                    downchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomright,visited);

                }
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    downchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.longways,visited);
                }
            }
            moveMaxChain=Math.max(upchain,downchain);
        }else{
            int rightchain=-1;
            int leftchain=-1;
            // right
            if(move.getVertices().get(0).id%Graph.getWidth()!=Graph.getWidth()-1){
                int bottom = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id+1];
                int left = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];
                int right = matrix[move.getVertices().get(0).id+1][move.getVertices().get(1).id+1];
                int top = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id+1];
                //topright
                if(top==1&&left==2&&bottom==2&&right==1){
                    rightchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.topright,visited);
                }
                //bottomright
                if(top==1&&left==1&&bottom==2&&right==2){
                    rightchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomright,visited);

                }
                //sideways
                if (top == 2 && bottom == 1 && left == 2 && right == 2) {
                    rightchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.sideways,visited);
                }
            }
            if(move.getVertices().get(0).id%Graph.getWidth()!=0){
                int bottom = matrix[move.getVertices().get(1).id][move.getVertices().get(1).id-1];
                int right = matrix[move.getVertices().get(0).id][move.getVertices().get(1).id];
                int left = matrix[move.getVertices().get(0).id+1][move.getVertices().get(1).id-1];
                int top = matrix[move.getVertices().get(0).id][move.getVertices().get(0).id-1];
                //topleft
                if(top==1&&left==2&&bottom==2&&right==1){
                    leftchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.topright,visited);
                }
                //bottomleft
                if(top==1&&left==1&&bottom==2&&right==2){
                    leftchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.bottomright,visited);

                }
                //sideways
                if (top == 2 && bottom == 1 && left == 2 && right == 2) {
                    leftchain=countNumBoxesInChain((move.getVertices().get(0).id)/Graph.getWidth(),(move.getVertices().get(0).id)%Graph.getWidth(),1,boxType.sideways,visited);
                }
            }
            moveMaxChain=Math.max(leftchain,rightchain);
        }
        if(moveMaxChain==3){
            return true;
        }
        return false;
    }

    public int c(){
        return sizeG()-4*numberOfLongChains()-8*numberOfLoops()+tb();

    }
    public int sizeG(){
        return ((Graph.getWidth()-1)*(Graph.getHeight()-1))-(botScore + oScore);
    }
    public int tb(){
        if(numberOfLoops()>0){
            if(numberOfLongChains()>0){
                return 8;
            }
            return 6;
        }
        return 4;
    }
    static ArrayList<ArrayList<Integer>> loopVisited;
    public int numberOfLongLoops(){
        loopVisited= new ArrayList<>();
        for(int i=0;i<Graph.getHeight()-1;i++){
            ArrayList<Integer> row = new ArrayList<>();
            for(int w=0;w<Graph.getWidth()-1;w++){
                row.add(0);
            }
            loopVisited.add(row);
        }
        int count=0;
        for(int i=0;i<Graph.getHeight()-1;i++){
            for(int w=0;w<Graph.getWidth()-1;w++){
                if(loopVisited.get(i).get(w)==0){
                    int top = matrix[(i*Graph.getWidth())+w][(i*Graph.getWidth())+w+1];
                    int left = matrix[(i*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w];
                    int right = matrix[(i*Graph.getWidth())+w+1][((i+1)*Graph.getWidth())+w+1];
                    int bottom = matrix[((i+1)*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w+1];
                    //sideways
                    if(top==2&&bottom==2&&left==1&&right==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.sideways,loopVisited,true)>=3){
                            count++;
                        }
                    }
                    //bottomleft
                    if(left==2&&bottom==2&&right==1&&top==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.bottomleft,loopVisited,true)>=3){
                            count++;
                        }
                    }
                    //topleft
                    if(top==2&&left==2&&bottom==1&&right==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.topleft,loopVisited,true)>=3){
                            count++;
                        }
                    }
                    //bottomright
                    if(left==1&&bottom==2&&right==2&&top==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.bottomright,loopVisited,true)>=3){
                            count++;
                        }
                    }
                    //topright
                    if(top==2&&left==1&&bottom==1&&right==2){
                        if(countNumBoxesInLoop(i,w,1,boxType.topright,loopVisited,true)>=3){
                            count++;
                        }
                    }
                    //longways
                    if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                        if(countNumBoxesInLoop(i,w,1,boxType.longways,loopVisited,true)>=3){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }
    public int numberOfLoops(){
        loopVisited= new ArrayList<>();
        for(int i=0;i<Graph.getHeight()-1;i++){
            ArrayList<Integer> row = new ArrayList<>();
            for(int w=0;w<Graph.getWidth()-1;w++){
                row.add(0);
            }
            loopVisited.add(row);
        }
        int count=0;
        for(int i=0;i<Graph.getHeight()-1;i++){
            for(int w=0;w<Graph.getWidth()-1;w++){
                if(loopVisited.get(i).get(w)==0){
                    int top = matrix[(i*Graph.getWidth())+w][(i*Graph.getWidth())+w+1];
                    int left = matrix[(i*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w];
                    int right = matrix[(i*Graph.getWidth())+w+1][((i+1)*Graph.getWidth())+w+1];
                    int bottom = matrix[((i+1)*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w+1];
                    //sideways
                    if(top==2&&bottom==2&&left==1&&right==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.sideways,loopVisited,true)>=1){
                            count++;
                        }
                    }
                    //bottomleft
                    if(left==2&&bottom==2&&right==1&&top==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.bottomleft,loopVisited,true)>=1){
                            count++;
                        }
                    }
                    //topleft
                    if(top==2&&left==2&&bottom==1&&right==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.topleft,loopVisited,true)>=1){
                            count++;
                        }
                    }
                    //bottomright
                    if(left==1&&bottom==2&&right==2&&top==1){
                        if(countNumBoxesInLoop(i,w,1,boxType.bottomright,loopVisited,true)>=1){
                            count++;
                        }
                    }
                    //topright
                    if(top==2&&left==1&&bottom==1&&right==2){
                        if(countNumBoxesInLoop(i,w,1,boxType.topright,loopVisited,true)>=1){
                            count++;
                        }
                    }
                    //longways
                    if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                        if(countNumBoxesInLoop(i,w,1,boxType.longways,loopVisited,true)>=1){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public int countNumBoxesInLoop(int h,int w,int num, boxType type,ArrayList<ArrayList<Integer>> visited,boolean first){
        if(first) {
            visited.get(h).set(w, 2);
        }else{
            visited.get(h).set(w, 1);
        }
        if(w>0&&num!=2){
            if(visited.get(h).get(w-1)==2){
                return num;
            }
        }
        if(h>0&&num!=2){
            if(visited.get(h-1).get(w)==2){
                return num;
            }
        }
        if(w!=Graph.getWidth()-2&&num!=2){
            if(visited.get(h).get(w+1)==2){
                return num;
            }
        }
        if(h!=Graph.getHeight()-2&&num!=2){
            if(visited.get(h+1).get(w)==2){
                return num;
            }
        }
        if(type==boxType.sideways){
            //left
            if(w>0&&(visited.get(h).get(w-1)==0)){
                int top = matrix[(h*Graph.getWidth())+w-1][(h*Graph.getWidth())+w];
                int left = matrix[(h*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w-1];
                int right = matrix[(h*Graph.getWidth())+w][((h+1)*Graph.getWidth())+w];
                int bottom = matrix[((h+1)*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.sideways,visited,false);
                }
                //bottomleft
                if(left==2&&bottom==2&&right==1&&top==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.bottomleft,visited,false);
                }
                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.topleft,visited,false);
                }
            }
            //right
            if(w!=Graph.getWidth()-2&&visited.get(h).get(w+1)==0){
                int top = matrix[(h*Graph.getWidth())+w+2][(h*Graph.getWidth())+w+1];
                int left = matrix[(h*Graph.getWidth())+w+1][((h+1)*Graph.getWidth())+w+1];
                int right = matrix[(h*Graph.getWidth())+w+2][((h+1)*Graph.getWidth())+w+2];
                int bottom = matrix[((h+1)*Graph.getWidth())+w+1][((h+1)*Graph.getWidth())+w+2];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w+1,num,boxType.sideways,visited,false);
                }
                //bottomright
                if(left==1&&bottom==2&&right==2&&top==1){
                    num++;
                    num=countNumBoxesInLoop(h,w+1,num,boxType.bottomright,visited,false);
                }
                //topright
                if(top==2&&left==1&&bottom==1&&right==2){
                    num++;
                    num=countNumBoxesInLoop(h,w+1,num,boxType.topright,visited,false);
                }
            }
        }
        if(type==boxType.topleft) {
            //right
            if (w != Graph.getWidth()-2 && visited.get(h).get(w + 1)==0) {
                int top = matrix[(h * Graph.getWidth()) + w + 2][(h * Graph.getWidth()) + w + 1];
                int left = matrix[(h * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 1];
                int right = matrix[(h * Graph.getWidth()) + w + 2][((h + 1) * Graph.getWidth()) + w + 2];
                int bottom = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 2];
                //sideways
                if (top == 2 && bottom == 2 && left == 1 && right == 1) {
                    num++;
                    num = countNumBoxesInLoop(h, w + 1, num, boxType.sideways, visited,false);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInLoop(h, w + 1, num, boxType.bottomright, visited,false);
                }
                //topright
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h, w + 1, num, boxType.topright, visited,false);
                }
            }
            //down
            if (h != Graph.getHeight()-2 && visited.get(h + 1).get(w)==0) {
                int top = matrix[((h + 1) * Graph.getWidth()) + w][((h + 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h + 1) * Graph.getWidth()) + w][((h + 2) * Graph.getWidth()) + w];
                int right = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h + 2) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.longways, visited,false);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.bottomright, visited,false);
                }
                //bottomleft
                if (top == 1 && left == 2 && bottom == 2 && right == 1) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.bottomleft, visited,false);
                }
            }
        }
        if(type==boxType.topright) {
            //left
            if(w-1>=0&&visited.get(h).get(w-1)==0){
                int top = matrix[(h*Graph.getWidth())+w-1][(h*Graph.getWidth())+w];
                int left = matrix[(h*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w-1];
                int right = matrix[(h*Graph.getWidth())+w][((h+1)*Graph.getWidth())+w];
                int bottom = matrix[((h+1)*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.sideways,visited,false);
                }
                //bottomleft
                if(left==2&&bottom==2&&right==1&&top==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.bottomleft,visited,false);
                }
                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.topleft,visited,false);
                }
            }
            //down
            if (h != Graph.getHeight()-2 && visited.get(h + 1).get(w)==0) {
                int top = matrix[((h + 1) * Graph.getWidth()) + w][((h + 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h + 1) * Graph.getWidth()) + w][((h + 2) * Graph.getWidth()) + w];
                int right = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h + 2) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.longways, visited,false);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.bottomright, visited,false);
                }
                //bottomleft
                if (top == 1 && left == 2 && bottom == 2 && right == 1) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.bottomleft, visited,false);
                }
            }
        }
        if(type==boxType.bottomright) {
            //left
            if(w-1>=0&&visited.get(h).get(w-1)==0){
                int top = matrix[(h*Graph.getWidth())+w-1][(h*Graph.getWidth())+w];
                int left = matrix[(h*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w-1];
                int right = matrix[(h*Graph.getWidth())+w][((h+1)*Graph.getWidth())+w];
                int bottom = matrix[((h+1)*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.sideways,visited,false);
                }
                //bottomleft
                if(left==2&&bottom==2&&right==1&&top==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.bottomleft,visited,false);
                }
                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    num++;
                    num=countNumBoxesInLoop(h,w-1,num,boxType.topleft,visited,false);
                }
            }
            //up
            if (h - 1 >= 0 && visited.get(h - 1).get(w)==0) {
                int top = matrix[((h - 1) * Graph.getWidth()) + w][((h - 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h - 1) * Graph.getWidth()) + w][((h) * Graph.getWidth()) + w];
                int right = matrix[((h - 1) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.longways, visited,false);
                }
                //topright
                if (left == 2 && bottom == 1 && right == 1 && top == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.topright, visited,false);
                }
                //topleft
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.topleft, visited,false);
                }
            }
        }
        if(type==boxType.bottomleft){
            //right
            if (w != Graph.getWidth()-2 && visited.get(h).get(w + 1)==0) {
                int top = matrix[(h * Graph.getWidth()) + w + 2][(h * Graph.getWidth()) + w + 1];
                int left = matrix[(h * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 1];
                int right = matrix[(h * Graph.getWidth()) + w + 2][((h + 1) * Graph.getWidth()) + w + 2];
                int bottom = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 2];
                //sideways
                if (top == 2 && bottom == 2 && left == 1 && right == 1) {
                    num++;
                    num = countNumBoxesInLoop(h, w + 1, num, boxType.sideways, visited,false);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInLoop(h, w + 1, num, boxType.bottomright, visited,false);
                }
                //topright
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h, w + 1, num, boxType.topright, visited,false);
                }
            }
            //up
            if (h - 1 >= 0 && visited.get(h - 1).get(w)==0) {
                int top = matrix[((h - 1) * Graph.getWidth()) + w][((h - 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h - 1) * Graph.getWidth()) + w][((h) * Graph.getWidth()) + w];
                int right = matrix[((h - 1) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.longways, visited,false);
                }
                //topright
                if (left == 2 && bottom == 1 && right == 1 && top == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.topright, visited,false);
                }
                //topleft
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.topleft, visited,false);
                }
            }
        }
        if(type==boxType.longways){
            //up
            if (h - 1 >= 0 && visited.get(h - 1).get(w)==0) {
                int top = matrix[((h - 1) * Graph.getWidth()) + w][((h - 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h - 1) * Graph.getWidth()) + w][((h) * Graph.getWidth()) + w];
                int right = matrix[((h - 1) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.longways, visited,false);
                }
                //topright
                if (left == 2 && bottom == 1 && right == 1 && top == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.topright, visited,false);
                }
                //topleft
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h - 1, w, num, boxType.topleft, visited,false);
                }
            }
            //down
            if (h != Graph.getHeight()-2 && visited.get(h + 1).get(w)==0) {
                int top = matrix[((h + 1) * Graph.getWidth()) + w][((h + 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h + 1) * Graph.getWidth()) + w][((h + 2) * Graph.getWidth()) + w];
                int right = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h + 2) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.longways, visited,false);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.bottomright, visited,false);
                }
                //bottomleft
                if (top == 1 && left == 2 && bottom == 2 && right == 1) {
                    num++;
                    num = countNumBoxesInLoop(h + 1, w, num, boxType.bottomleft, visited,false);
                }
            }
        }
        return -1;
    }



    public ArrayList<Integer> checkFor3s(){
        ArrayList<Integer> av = new ArrayList<>();
        // goes through each availableLine
        for(int q=0;q<availLines.size();q++){
            ELine edge = availLines.get(q);
            boolean noBox=true;
            if(!edge.getHorizontal()){
                int leftBox=0;
                int rightBox=0;
                if(edge.vertices.get(0).getRightVertex()!=null){
                    rightBox = matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()+1]+matrix[edge.vertices.get(0).getID()+1][edge.vertices.get(1).getID()+1]+matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()+1];
                }
                if(edge.vertices.get(0).getLeftVertex()!=null){
                    leftBox= matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()-1]+matrix[edge.vertices.get(0).getID()-1][edge.vertices.get(1).getID()-1]+matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()-1];
                }
                if(leftBox==5||rightBox==5){
                    noBox=false;
                }
            }else{
                // does the same but for horizontal edges
                int downBox=0;
                int upBox=0;
                if(edge.vertices.get(0).getDownVertex()!=null){
                    downBox= matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()+ Graph.getWidth()]+matrix[edge.vertices.get(0).getID()+Graph.getWidth()][edge.vertices.get(1).getID()+Graph.getWidth()]+matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()+Graph.getWidth()];
                }
                if(edge.vertices.get(0).getUpVertex()!=null){
                    upBox= matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()-Graph.getWidth()]+matrix[edge.vertices.get(0).getID()-Graph.getWidth()][edge.vertices.get(1).getID()-Graph.getWidth()]+matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()-Graph.getWidth()];
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
        //returns list of edges that don't set up a box
        return av;
    }

    static ArrayList<ArrayList<Boolean>> visited;
    public int numberOfChains(){
        visited= new ArrayList<>();
        for(int i=0;i<Graph.getHeight()-1;i++){
            ArrayList<Boolean> row = new ArrayList<>();
            for(int w=0;w<Graph.getWidth()-1;w++){
                row.add(false);
            }
            visited.add(row);
        }
        int count=0;
        for(int i=0;i<Graph.getHeight()-1;i++){
            for(int w=0;w<Graph.getWidth()-1;w++){
                if(!visited.get(i).get(w)){
                    int top = matrix[(i*Graph.getWidth())+w][(i*Graph.getWidth())+w+1];
                    int left = matrix[(i*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w];
                    int right = matrix[(i*Graph.getWidth())+w+1][((i+1)*Graph.getWidth())+w+1];
                    int bottom = matrix[((i+1)*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w+1];
                    //sideways
                    if(top==2&&bottom==2&&left==1&&right==1){
                        if(countNumBoxesInChain(i,w,1,boxType.sideways,visited)>=1){
                            count++;
                        }
                    }
                    //bottomleft
                    if(left==2&&bottom==2&&right==1&&top==1){
                        if(countNumBoxesInChain(i,w,1,boxType.bottomleft,visited)>=1){
                            count++;
                        }
                    }
                    //topleft
                    if(top==2&&left==2&&bottom==1&&right==1){
                        if(countNumBoxesInChain(i,w,1,boxType.topleft,visited)>=1){
                            count++;
                        }
                    }
                    //bottomright
                    if(left==1&&bottom==2&&right==2&&top==1){
                        if(countNumBoxesInChain(i,w,1,boxType.bottomright,visited)>=1){
                            count++;
                        }
                    }
                    //topright
                    if(top==2&&left==1&&bottom==1&&right==2){
                        if(countNumBoxesInChain(i,w,1,boxType.topright,visited)>=1){
                            count++;
                        }
                    }
                    //longways
                    if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                        if(countNumBoxesInChain(i,w,1,boxType.longways,visited)>=1){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }
    public int numberOfLongChains(){
        visited= new ArrayList<>();
        for(int i=0;i<Graph.getHeight()-1;i++){
            ArrayList<Boolean> row = new ArrayList<>();
            for(int w=0;w<Graph.getWidth()-1;w++){
                row.add(false);
            }
            visited.add(row);
        }
        int count=0;
        for(int i=0;i<Graph.getHeight()-1;i++){
            for(int w=0;w<Graph.getWidth()-1;w++){
                if(!visited.get(i).get(w)){
                    int top = matrix[(i*Graph.getWidth())+w][(i*Graph.getWidth())+w+1];
                    int left = matrix[(i*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w];
                    int right = matrix[(i*Graph.getWidth())+w+1][((i+1)*Graph.getWidth())+w+1];
                    int bottom = matrix[((i+1)*Graph.getWidth())+w][((i+1)*Graph.getWidth())+w+1];
                    //sideways
                    if(top==2&&bottom==2&&left==1&&right==1){
                        if(countNumBoxesInChain(i,w,1,boxType.sideways,visited)>=3){
                            count++;
                        }
                    }
                    //bottomleft
                    if(left==2&&bottom==2&&right==1&&top==1){
                        if(countNumBoxesInChain(i,w,1,boxType.bottomleft,visited)>=3){
                            count++;
                        }
                    }
                    //topleft
                    if(top==2&&left==2&&bottom==1&&right==1){
                        if(countNumBoxesInChain(i,w,1,boxType.topleft,visited)>=3){
                            count++;
                        }
                    }
                    //bottomright
                    if(left==1&&bottom==2&&right==2&&top==1){
                        if(countNumBoxesInChain(i,w,1,boxType.bottomright,visited)>=3){
                            count++;
                        }
                    }
                    //topright
                    if(top==2&&left==1&&bottom==1&&right==2){
                        if(countNumBoxesInChain(i,w,1,boxType.topright,visited)>=3){
                            count++;
                        }
                    }
                    //longways
                    if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                        if(countNumBoxesInChain(i,w,1,boxType.longways,visited)>=3){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }


    public int countNumBoxesInChain(int h,int w,int num, boxType type,ArrayList<ArrayList<Boolean>> visited){
        visited.get(h).set(w,true);
        if(type==boxType.sideways){
            //left
            if(w>0&&!visited.get(h).get(w-1)){
                int top = matrix[(h*Graph.getWidth())+w-1][(h*Graph.getWidth())+w];
                int left = matrix[(h*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w-1];
                int right = matrix[(h*Graph.getWidth())+w][((h+1)*Graph.getWidth())+w];
                int bottom = matrix[((h+1)*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.sideways,visited);
                }
                //bottomleft
                if(left==2&&bottom==2&&right==1&&top==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.bottomleft,visited);
                }
                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.topleft,visited);
                }
            }
            //right
            if(w!=Graph.getWidth()-2&&!visited.get(h).get(w+1)){
                int top = matrix[(h*Graph.getWidth())+w+2][(h*Graph.getWidth())+w+1];
                int left = matrix[(h*Graph.getWidth())+w+1][((h+1)*Graph.getWidth())+w+1];
                int right = matrix[(h*Graph.getWidth())+w+2][((h+1)*Graph.getWidth())+w+2];
                int bottom = matrix[((h+1)*Graph.getWidth())+w+1][((h+1)*Graph.getWidth())+w+2];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w+1,num,boxType.sideways,visited);
                }
                //bottomright
                if(left==1&&bottom==2&&right==2&&top==1){
                    num++;
                    num=countNumBoxesInChain(h,w+1,num,boxType.bottomright,visited);
                }
                //topright
                if(top==2&&left==1&&bottom==1&&right==2){
                    num++;
                    num=countNumBoxesInChain(h,w+1,num,boxType.topright,visited);
                }
            }
        }
        if(type==boxType.topleft) {
            //right
            if (w != Graph.getWidth()-2 && !visited.get(h).get(w + 1)) {
                int top = matrix[(h * Graph.getWidth()) + w + 2][(h * Graph.getWidth()) + w + 1];
                int left = matrix[(h * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 1];
                int right = matrix[(h * Graph.getWidth()) + w + 2][((h + 1) * Graph.getWidth()) + w + 2];
                int bottom = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 2];
                //sideways
                if (top == 2 && bottom == 2 && left == 1 && right == 1) {
                    num++;
                    num = countNumBoxesInChain(h, w + 1, num, boxType.sideways, visited);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInChain(h, w + 1, num, boxType.bottomright, visited);
                }
                //topright
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h, w + 1, num, boxType.topright, visited);
                }
            }
            //down
            if (h != Graph.getHeight()-2 && !visited.get(h + 1).get(w)) {
                int top = matrix[((h + 1) * Graph.getWidth()) + w][((h + 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h + 1) * Graph.getWidth()) + w][((h + 2) * Graph.getWidth()) + w];
                int right = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h + 2) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.longways, visited);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.bottomright, visited);
                }
                //bottomleft
                if (top == 1 && left == 2 && bottom == 2 && right == 1) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.bottomleft, visited);
                }
             }
        }
        if(type==boxType.topright) {
            //left
            if(w-1>=0&&!visited.get(h).get(w-1)){
                int top = matrix[(h*Graph.getWidth())+w-1][(h*Graph.getWidth())+w];
                int left = matrix[(h*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w-1];
                int right = matrix[(h*Graph.getWidth())+w][((h+1)*Graph.getWidth())+w];
                int bottom = matrix[((h+1)*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.sideways,visited);
                }
                //bottomleft
                if(left==2&&bottom==2&&right==1&&top==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.bottomleft,visited);
                }
                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.topleft,visited);
                }
            }
            //down
            if (h != Graph.getHeight()-2 && !visited.get(h + 1).get(w)) {
                int top = matrix[((h + 1) * Graph.getWidth()) + w][((h + 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h + 1) * Graph.getWidth()) + w][((h + 2) * Graph.getWidth()) + w];
                int right = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h + 2) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.longways, visited);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.bottomright, visited);
                }
                //bottomleft
                if (top == 1 && left == 2 && bottom == 2 && right == 1) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.bottomleft, visited);
                }
            }
        }
        if(type==boxType.bottomright) {
            //left
            if(w-1>=0&&!visited.get(h).get(w-1)){
                int top = matrix[(h*Graph.getWidth())+w-1][(h*Graph.getWidth())+w];
                int left = matrix[(h*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w-1];
                int right = matrix[(h*Graph.getWidth())+w][((h+1)*Graph.getWidth())+w];
                int bottom = matrix[((h+1)*Graph.getWidth())+w-1][((h+1)*Graph.getWidth())+w];
                //sideways
                if(top==2&&bottom==2&&left==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.sideways,visited);
                }
                //bottomleft
                if(left==2&&bottom==2&&right==1&&top==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.bottomleft,visited);
                }
                //topleft
                if(top==2&&left==2&&bottom==1&&right==1){
                    num++;
                    num=countNumBoxesInChain(h,w-1,num,boxType.topleft,visited);
                }
            }
            //up
            if (h - 1 >= 0 && !visited.get(h - 1).get(w)) {
                int top = matrix[((h - 1) * Graph.getWidth()) + w][((h - 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h - 1) * Graph.getWidth()) + w][((h) * Graph.getWidth()) + w];
                int right = matrix[((h - 1) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.longways, visited);
                }
                //topright
                if (left == 2 && bottom == 1 && right == 1 && top == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.topright, visited);
                }
                //topleft
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.topleft, visited);
                }
            }
        }
        if(type==boxType.bottomleft){
            //right
            if (w != Graph.getWidth()-2 && !visited.get(h).get(w + 1)) {
                int top = matrix[(h * Graph.getWidth()) + w + 2][(h * Graph.getWidth()) + w + 1];
                int left = matrix[(h * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 1];
                int right = matrix[(h * Graph.getWidth()) + w + 2][((h + 1) * Graph.getWidth()) + w + 2];
                int bottom = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 1) * Graph.getWidth()) + w + 2];
                //sideways
                if (top == 2 && bottom == 2 && left == 1 && right == 1) {
                    num++;
                    num = countNumBoxesInChain(h, w + 1, num, boxType.sideways, visited);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInChain(h, w + 1, num, boxType.bottomright, visited);
                }
                //topright
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h, w + 1, num, boxType.topright, visited);
                }
            }
            //up
            if (h - 1 >= 0 && !visited.get(h - 1).get(w)) {
                int top = matrix[((h - 1) * Graph.getWidth()) + w][((h - 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h - 1) * Graph.getWidth()) + w][((h) * Graph.getWidth()) + w];
                int right = matrix[((h - 1) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.longways, visited);
                }
                //topright
                if (left == 2 && bottom == 1 && right == 1 && top == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.topright, visited);
                }
                //topleft
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.topleft, visited);
                }
            }
        }
        if(type==boxType.longways){
            //up
            if (h - 1 >= 0 && !visited.get(h - 1).get(w)) {
                int top = matrix[((h - 1) * Graph.getWidth()) + w][((h - 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h - 1) * Graph.getWidth()) + w][((h) * Graph.getWidth()) + w];
                int right = matrix[((h - 1) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h) * Graph.getWidth()) + w + 1][((h) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.longways, visited);
                }
                //topright
                if (left == 2 && bottom == 1 && right == 1 && top == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.topright, visited);
                }
                //topleft
                if (top == 2 && left == 1 && bottom == 1 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h - 1, w, num, boxType.topleft, visited);
                }
            }
            //down
            if (h != Graph.getHeight()-2 && !visited.get(h + 1).get(w)) {
                int top = matrix[((h + 1) * Graph.getWidth()) + w][((h + 1) * Graph.getWidth()) + w + 1];
                int left = matrix[((h + 1) * Graph.getWidth()) + w][((h + 2) * Graph.getWidth()) + w];
                int right = matrix[((h + 1) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w + 1];
                int bottom = matrix[((h + 2) * Graph.getWidth()) + w + 1][((h + 2) * Graph.getWidth()) + w];
                //longways
                if (top == 1 && bottom == 1 && left == 2 && right == 2) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.longways, visited);
                }
                //bottomright
                if (left == 1 && bottom == 2 && right == 2 && top == 1) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.bottomright, visited);
                }
                //bottomleft
                if (top == 1 && left == 2 && bottom == 2 && right == 1) {
                    num++;
                    num = countNumBoxesInChain(h + 1, w, num, boxType.bottomleft, visited);
                }
            }
        }
        return num;
    }


    public Node(int[][] m, int bs,int os, ArrayList<ELine> av, boolean p1T,boolean bonusTurn,boolean t,ELine move){
        matrix=m;
        botScore=bs;
        oScore=os;
        availLines=av;
        botTurn=p1T;
        terminal=t;
        this.bonusTurn=bonusTurn;
        this.move=move;
        dummyTurn=null;
    }
    public Node(Node dummy){
        this.dummyTurn=dummy;
        botTurn=!dummyTurn.botTurn;
        matrix=dummyTurn.matrix;
        botScore=dummyTurn.botScore;
        oScore=dummyTurn.oScore;
        availLines= dummyTurn.availLines;
        terminal=false;
        bonusTurn=false;
        move=dummyTurn.move;
    }

    public static int[][] matrixCopy(int[][] m) {
        int[][] newMatrix = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                newMatrix[i][j] = m[i][j];
            }
        }
        return newMatrix;
    }
    public static ArrayList<ELine> avCopy(ArrayList<ELine> av){
        ArrayList<ELine> temp = new ArrayList<>();
        for(ELine b:av){
            temp.add(b);
        }
        return temp;
    }

    Node performMove(ELine line) {
        int score = botScore;
        int oScore = this.oScore;
        boolean turn;
        ArrayList<ELine> cp = avCopy(availLines);
        ArrayList<ELine> al = availCheck(cp,line);
        int[][] newMatrix = matrixCopy(matrix);
        newMatrix[line.vertices.get(0).getID()][line.vertices.get(1).getID()] = 2;
        newMatrix[line.vertices.get(1).getID()][line.vertices.get(0).getID()] = 2;
        ArrayList<ArrayList<Vertex>> boxes = checkBox(line, newMatrix);
        boolean bonTurn = false;
        if (boxes != null) {
            bonTurn=true;
            for (ArrayList<Vertex> box : boxes) {
                if (botTurn) {
                    score++;
                } else {
                    oScore++;
                }
            }
        }
        if(!bonTurn) {
            turn = !this.botTurn;
        }else{
            turn = this.botTurn;
        }
        if(al.size()==0){
            return new Node(newMatrix,score,oScore,al,turn,bonTurn,true,line);
        }
        return new Node(newMatrix,score,oScore,al,turn,bonTurn,false,line);
    }

    public ArrayList<ArrayList<Vertex>> checkBox(ELine line, int[][] GState) {
        ArrayList<ArrayList<Vertex>> listOfBoxes = new ArrayList<>();
        if (line.getHorizontal()) {
            if (line.vertices.get(0).getUpVertex() != null) {
                if (GState[line.vertices.get(0).getID()][line.vertices.get(0).getUpVertex().getID()] == 2 && GState[line.vertices.get(1).getID()][line.vertices.get(1).getUpVertex().getID()] == 2 && GState[line.vertices.get(0).getUpVertex().getID()][line.vertices.get(1).getUpVertex().getID()] == 2) {
                    ArrayList<Vertex> box = new ArrayList<>();
                    box.add(line.vertices.get(0));
                    box.add(line.vertices.get(1));
                    box.add(line.vertices.get(0).getUpVertex());
                    box.add(line.vertices.get(1).getUpVertex());
                    listOfBoxes.add(box);
                }
            }
            if (line.vertices.get(0).getDownVertex() != null) {
                if (GState[line.vertices.get(0).getID()][line.vertices.get(0).getDownVertex().getID()] == 2 && GState[line.vertices.get(1).getID()][line.vertices.get(1).getDownVertex().getID()] == 2 && GState[line.vertices.get(0).getDownVertex().getID()][line.vertices.get(1).getDownVertex().getID()] == 2) {
                    ArrayList<Vertex> box2 = new ArrayList<>();
                    box2.add(line.vertices.get(0));
                    box2.add(line.vertices.get(1));
                    box2.add(line.vertices.get(0).getDownVertex());
                    box2.add(line.vertices.get(1).getDownVertex());
                    listOfBoxes.add(box2);
                }
            }
        } else {
            if (line.vertices.get(0).getRightVertex() != null) {
                if (GState[line.vertices.get(0).getID()][line.vertices.get(0).getRightVertex().getID()] == 2 && GState[line.vertices.get(1).getID()][line.vertices.get(1).getRightVertex().getID()] == 2 && GState[line.vertices.get(0).getRightVertex().getID()][line.vertices.get(1).getRightVertex().getID()] == 2) {
                    ArrayList<Vertex> box3 = new ArrayList<>();
                    box3.add(line.vertices.get(0));
                    box3.add(line.vertices.get(1));
                    box3.add(line.vertices.get(0).getRightVertex());
                    box3.add(line.vertices.get(1).getRightVertex());
                    listOfBoxes.add(box3);
                }
            }
            if (line.vertices.get(0).getLeftVertex() != null) {
                if (GState[line.vertices.get(0).getID()][line.vertices.get(0).getLeftVertex().getID()] == 2 && GState[line.vertices.get(1).getID()][line.vertices.get(1).getLeftVertex().getID()] == 2 && GState[line.vertices.get(0).getLeftVertex().getID()][line.vertices.get(1).getLeftVertex().getID()] == 2) {
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
        if (listOfBoxes.isEmpty()) {
            return null;
        }
        return listOfBoxes;
    }
    /*
    public ArrayList<ScoreBox> checkMatching(ArrayList<Vertex> box, ArrayList<ScoreBox> GState){
        int avgX=0;
        int avgY=0;
        for(Vertex v:box){
            avgX+=v.getWidth();
            avgY+=v.getHeight();
        }
        avgX=avgX/4;
        avgY=avgY/4;
        for(ScoreBox sc: GState){
            if(sc.getAvgX()==avgX&&sc.getAvgY()==avgY){
                sc.setText();
            }
        }
        return GState;
    }

     */
    public static ArrayList<ELine> availCheck(ArrayList<ELine> av,ELine line){
        //  System.out.println("AV CHECK:");
        for(int q=av.size()-1;q>=0;q--){
            if(av.get(q).equals(line)){
                //  System.out.println("REMOVE: "+av.get(q).vertices.get(0).id+" -- "+av.get(q).vertices.get(1).id);
                av.remove(q);
            }
        }
        return av;
    }

}
