package minMax;

import game.ELine;
import game.Graph;
import game.Vertex;

import java.util.ArrayList;


public class Node{
    public void addChild(Node child) {
        children.add(child);
    }

    ArrayList<Node> children = new ArrayList<Node>();
    int[][] matrix;
    int botScore;
    int oScore;
    ArrayList<ELine> availLines;
    boolean botTurn;
    boolean terminal;
    boolean bonusTurn;
    public ELine move;



    public String toString(){

        String toReturn = "";
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
        toReturn+=" Children: "+children.size();
        return toReturn;
    }

    double evaluation(){
        double a = botScore-oScore;
        int numberOfLongChains = numberOfLongChains();
        double b;
        if(numberOfLongChains==0){
            b=0;
        }else{
            if((Graph.getHeight()*Graph.getWidth()%2!=0&&Graph.isMiniMaxP1())||(Graph.getHeight()*Graph.getWidth()%2==0&&!Graph.isMiniMaxP1())){
                if(numberOfLongChains%2!=0){
                    b=1;
                }else{
                    b=-1;
                }
            }else{
                if(numberOfLongChains%2==0){
                    b=1;
                }else{
                    b=-1;
                }
            }
        }
        double c;
        if(doubleCross()){
            c=1;
        }else{
            c=0;
        }
        // multipliers
        double a1 = MinMax.a;
        double b1 = MinMax.b;
        double c1 = MinMax.c;
        return a1*a+b1*b;


    }
    public boolean doubleCross(){
        if(checkBox(move,matrix).size()>1){
            return true;
        }else{
            return false;
        }
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
