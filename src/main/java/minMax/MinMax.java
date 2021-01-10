package minMax;

import game.ELine;
import game.Graph;
import game.ScoreBox;

import java.util.ArrayList;

import static game.Graph.getAvailableLines;

public class MinMax {
    public ArrayList<Node> tree = new ArrayList<Node>();

    /*
    public void generateTree(Node root, int depth){
        if(depth==0){
        }else{
            for(ELine a:root.availLines){
                Node temp = root.performMove(a);
                generateTree(temp,depth-1);
                root.addChild(temp);
            }
            tree.add(root);
        }
    }
    public Node alphaBeta(Node node, int depth, double a, double b, boolean bot){
        counter++;
        int t=counter;
        if(print) {
            System.out.println(t + "| " + node.toString());
        }
        if(depth==0|| node.terminal){
            return node;
        }
        Node toReturn=null;
        if(bot){
            a = -1000000;
            String maxS = "MAX "+t+": ";
            Node temp=null;
            for(Node tem:node.children){
                double toCompare = alphaBeta(tem,depth-1,a,b,tem.botTurn).evaluation();
                maxS+= tem.move.toString()+": "+toCompare+", ";
                if(a<toCompare){
                    a=toCompare;
                    temp = tem;
                }
                toReturn=temp;
                a = Math.max(a,toCompare);
                if(a>=b){
                    System.out.println(a+">="+b+" BREAK");
                    break;
                }
            }
            maxS+= "== "+a;
            if(print) {
                System.out.println(maxS);
            }
        }else{
            b = 1000000;
            String minS = "Min "+t+": ";
            Node temp=null;
            for(Node tem:node.children){
                double toCompare =  alphaBeta(tem,depth-1,a,b,tem.botTurn).evaluation();
                if(b>toCompare){
                    b=toCompare;
                    temp = tem;
                }
                minS+= tem.move.toString()+": "+toCompare+", ";
                b = Math.min(b,toCompare);
                if(b<=a){
                    System.out.println(b+"<="+a+" BREAK");
                    break;
                }
            }
            minS+= "== "+b;
            if(print) {
                System.out.println(minS);
            }
            toReturn=temp;
        }
        if(print) {
            System.out.println(t + "| val: b:" + b+" a:"+a);
        }

        return toReturn;
    }

     */

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }
    public void setC(double c) {
        this.c = c;
    }
    // mutlipliers
    public static double a=1;
    public static double b=0.5;
    public static double c=5;


    boolean print=false;
    public static int counter=0;


    public Node alphaBeta(Node node, int depth, double a, double b, boolean bot){
        counter++;
        int t=counter;
        if(print) {
            System.out.println(t + "| " + node.toString());
        }
        if(depth==0|| node.terminal){
            return node;
        }
        Node toReturn=null;
        ArrayList<ELine> availList = sortElines(node);
        if(bot){
            a = -1000000;
            String maxS = "MAX "+t+": ";
            Node temp=null;
            for(ELine v: availList){
                Node tem = node.performMove(v);
                double toCompare = alphaBeta(tem,depth-1,a,b,tem.botTurn).evaluation();
                maxS+= v.toString()+": "+toCompare+", ";
                if(a<toCompare){
                    a=toCompare;
                    temp = tem;
                }
                toReturn=temp;
                a = Math.max(a,toCompare);
                if(a>=b){
                    if(print) {
                        System.out.println(a + ">=" + b + " BREAK");
                    }
                    break;
                }
            }
            maxS+= "== "+a;
            if(print) {
                System.out.println(maxS);
            }
        }else{
            b = 1000000;
            String minS = "Min "+t+": ";
            Node temp=null;
            for(ELine v: availList){
                Node tem = node.performMove(v);
                double toCompare =  alphaBeta(tem,depth-1,a,b,tem.botTurn).evaluation();
                if(b>toCompare){
                    b=toCompare;
                    temp = tem;
                }
                minS+= v.toString()+": "+toCompare+", ";
                b = Math.min(b,toCompare);
                if(b<=a){
                    if(print) {
                        System.out.println(b + "<=" + a + " BREAK");
                    }
                    break;
                }
            }
            minS+= "== "+b;
            if(print) {
                System.out.println(minS);
            }
            toReturn=temp;
        }
        if(print) {
            System.out.println(t + "| val: b:" + b+" a:"+a);
        }

        return toReturn;
    }


    public ArrayList<ELine> sortElines(Node state){
        ArrayList<Integer> completesABox = getCompleteBoxes(state);
        ArrayList<ELine> av = Node.avCopy(state.availLines);
        for(int i=0;i<completesABox.size();i++){
            int r = completesABox.get(i);
            ELine temp = av.get(r);
            av.remove(r);
            av.add(0,temp);
        }
        ArrayList<Integer> setsUpBox = checkFor3s(state,av);
        for(int i =0;i<setsUpBox.size();i++){
            int r = setsUpBox.get(i);
            ELine temp = av.get(r);
            av.remove(r);
            av.add(temp);
        }
        return av;
    }

    public static ArrayList<Integer> checkFor3s(Node state,ArrayList<ELine> availLines){
        ArrayList<Integer> av = new ArrayList<>();
        // goes through each availableLine
        for(int q=0;q<availLines.size();q++){
            ELine edge = availLines.get(q);
            boolean noBox=true;
            if(!edge.getHorizontal()){
                int leftBox=0;
                int rightBox=0;
                if(edge.vertices.get(0).getRightVertex()!=null){
                    rightBox = state.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()+1]+state.matrix[edge.vertices.get(0).getID()+1][edge.vertices.get(1).getID()+1]+state.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()+1];
                }
                if(edge.vertices.get(0).getLeftVertex()!=null){
                    leftBox=state.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()-1]+state.matrix[edge.vertices.get(0).getID()-1][edge.vertices.get(1).getID()-1]+state.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()-1];
                }
                if(leftBox==5||rightBox==5){
                    noBox=false;
                }
            }else{
                // does the same but for horizontal edges
                int downBox=0;
                int upBox=0;
                if(edge.vertices.get(0).getDownVertex()!=null){
                    downBox=state.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()+ Graph.getWidth()]+state.matrix[edge.vertices.get(0).getID()+Graph.getWidth()][edge.vertices.get(1).getID()+Graph.getWidth()]+state.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()+Graph.getWidth()];
                }
                if(edge.vertices.get(0).getUpVertex()!=null){
                    upBox=state.matrix[edge.vertices.get(0).getID()][edge.vertices.get(0).getID()-Graph.getWidth()]+state.matrix[edge.vertices.get(0).getID()-Graph.getWidth()][edge.vertices.get(1).getID()-Graph.getWidth()]+state.matrix[edge.vertices.get(1).getID()][edge.vertices.get(1).getID()-Graph.getWidth()];
                }
                if(upBox==5||downBox==5){
                    noBox=false;
                }
            }
            if(!noBox){
                // if the line doesn't create a box it adds the index from availableLines to a new arrayList, av
                av.add(q);
            }
        }
        return av;
    }

    public ArrayList<Integer> getCompleteBoxes(Node state){
        ArrayList<Integer> completeBoxes = new ArrayList<>();
        for(ScoreBox box: Graph.getCounterBoxes()) {
            int a = state.matrix[box.getVertices().get(0).getID()][box.getVertices().get(1).getID()];
            int b = state.matrix[box.getVertices().get(0).getID()][box.getVertices().get(2).getID()];
            int c = state.matrix[box.getVertices().get(1).getID()][box.getVertices().get(3).getID()];
            int d = state.matrix[box.getVertices().get(2).getID()][box.getVertices().get(3).getID()];
            // if each int adds up to 7, there must be 3 lines in a box. A line = 1 when available and = 2 when placed.
            // as 3 completed lines is 3*2=6, +1 for the remaining line == 7
            if (a + b + c + d == 7) {
                if(a==1){
                    if(a==1){
                        completeBoxes.add(findMatch(box.getVertices().get(0).getID(),box.getVertices().get(1).getID(),state));
                    }
                    if(b==1){
                        completeBoxes.add(findMatch(box.getVertices().get(0).getID(),box.getVertices().get(2).getID(),state));
                    }
                    if(c==1){
                        completeBoxes.add(findMatch(box.getVertices().get(1).getID(),box.getVertices().get(3).getID(),state));
                    }
                    if(d==1){
                        completeBoxes.add(findMatch(box.getVertices().get(2).getID(),box.getVertices().get(3).getID(),state));
                    }
                }
            }
        }
        return completeBoxes;
    }
    public int findMatch(int a, int b,Node state){
        for(int p=state.availLines.size()-1;p>=0;p--){
            if(state.availLines.get(p).vertices.get(0).getID()==a&&state.availLines.get(p).vertices.get(1).getID()==b){
                return p;
            }
            if(state.availLines.get(p).vertices.get(0).getID()==b&&state.availLines.get(p).vertices.get(1).getID()==a){
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


    public Node minMaxFunction(Node node, int depth, boolean bot){
        counter++;
        int t=counter;
        if(print) {
            System.out.println(t + "| " + node.toString());
        }
        if(depth==0|| node.terminal){
            return node;
        }
        double val;
        Node toReturn=null;
        if(bot){
            val = -1 * Double.MAX_VALUE;
            String maxS = "MAX "+t+": ";
            Node temp=null;
            for(ELine a: node.availLines){
                Node tem = node.performMove(a);
                double toCompare = minMaxFunction(tem,depth-1,tem.botTurn).evaluation();
                maxS+= toCompare+", ";
                if(val<toCompare){
                    val=toCompare;
                    temp = tem;
                }
                val = Math.max(val,toCompare);
            }
            maxS+= "== "+val;
            if(print) {
                System.out.println(maxS);
            }
            toReturn=temp;
        }else{
            val = Double.MAX_VALUE;
            String minS = "Min "+t+": ";
            Node temp=null;
            for(ELine a: node.availLines){
                Node tem = node.performMove(a);
                double toCompare =  minMaxFunction(tem,depth-1,tem.botTurn).evaluation();
                if(val>toCompare){
                    val=toCompare;
                    temp = tem;
                }
                minS+= toCompare+", ";
                val = Math.min(val,toCompare);
            }
            minS+= "== "+val;
            if(print) {
                System.out.println(minS);
            }
            toReturn=temp;
        }
        if(print) {
            System.out.println(t + "| val: " + val);
        }
        return toReturn;
    }


}
