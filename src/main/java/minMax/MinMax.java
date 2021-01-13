package minMax;

import game.ELine;
import game.Graph;
import game.ScoreBox;

import java.util.ArrayList;

import static game.Graph.actualMinMaxDepth;


public class MinMax {
    public ArrayList<Node> tree = new ArrayList<Node>();

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }
    public void setC(double c) {
        this.c = c;
    }
    public void setD(double d) {
        this.d = d;
    }
    public void setE(double e){
        this.e= e;
    }
    // mutlipliers
    public static double a=1;
    public static double b=0;
    public static double c=-2.5;
    public static double d=0.5;
    public static double e=1;


    boolean print=false;
    public static int counter=0;

    public Node alphaBeta(Node node, int depth, double a, double b, boolean bot){
        counter++;
        int t=counter;
        if(print) {
            System.out.println("Depth: "+(Graph.actualMinMaxDepth-depth)+" | "+t + "| " + node.toString());
        }
        if(depth==0|| node.terminal){
            return node;
        }
        Node toReturn;
        ArrayList<ELine> availList = new ArrayList<>();
        if(node.dummyTurn==null) {
            availList = sortElines(node);
        }
        if(bot){
            a = -1000000;
            String maxS = "Depth: "+(Graph.actualMinMaxDepth-depth)+" MAX "+t+": ";
            Node temp=null;
            for (ELine v : availList) {
                Node tem = node.performMove(v);
                tem.setParent(node);
                tem.setDepth((Graph.actualMinMaxDepth-depth)+1);
                Node toCompare = alphaBeta(tem, depth - 1, a, b, tem.botTurn);
                double eval = toCompare.evaluation();
                maxS += v.toString() + ": " +eval + ", ";
                if (a < eval) {
                    a = eval;
                    temp = toCompare;
                }
                a = Math.max(a, eval);
                if (a >= b) {
                    if (print) {
                        System.out.println(a + ">=" + b + " BREAK");
                    }
                    break;
                }
            }
            if((Graph.actualMinMaxDepth-depth)==0){
                while(temp.depth!=1){
                    temp=temp.parent;
                }
            }
            toReturn=temp;
            maxS += "== " + a;
            if (print) {
                System.out.println(maxS);
            }
        }else{
            b = 1000000;
            String minS = "Depth: "+(Graph.actualMinMaxDepth-depth)+" Min "+t+": ";
            Node temp=null;
            for (ELine v : availList) {
                Node tem = node.performMove(v);
                tem.setParent(node);
                tem.setDepth((actualMinMaxDepth-depth)+1);
                Node toCompare = alphaBeta(tem, depth - 1, a, b, tem.botTurn);
                double eval = toCompare.evaluation();
                if (b > eval) {
                    b = eval;
                        temp = toCompare;
                    }
                minS += v.toString() + ": " + eval + ", ";
                b = Math.min(b, eval);
                if (b <= a) {
                    if (print) {
                        System.out.println(b + "<=" + a + " BREAK");
                    }
                    break;
                }
            }
            if((actualMinMaxDepth-depth)==0){
                while(temp.depth!=1){
                    temp=temp.parent;
                }
            }
            toReturn=temp;
            minS += "== " + b;
            if (print) {
                System.out.println(minS);
            }
        }
        if(print) {
            System.out.println("Depth: "+(actualMinMaxDepth-depth)+" | "+t + "| val: b:" + b+" a:"+a);
        }
        if(toReturn==null||toReturn.move==null){
            System.out.println(node.toString());
            System.out.println("a: "+a+" b: "+b+" c: "+c+" d: "+d+" e: "+e);
        }
        return toReturn;
    }
    /*
    public Node alphaBeta(Node node, int depth, double a, double b, boolean bot){
        counter++;
        int t=counter;
        if(print) {
            System.out.println("Depth: "+(Graph.actualMinMaxDepth-depth)+" | "+t + "| " + node.toString());
        }
        if(depth==0|| node.terminal){
            return node;
        }
        Node toReturn;
        ArrayList<ELine> availList = new ArrayList<>();
        if(node.dummyTurn==null) {
            availList = sortElines(node);
        }
        if(bot){
            a = -1000000;
            String maxS = "Depth: "+(Graph.actualMinMaxDepth-depth)+" MAX "+t+": ";
            Node temp=null;
            if(node.dummyTurn!=null){
                Node tem = node.dummyTurn;
                tem.setParent(node);
                tem.setDepth((Graph.actualMinMaxDepth-depth));
                Node toCompare = alphaBeta(tem, depth-1, a, b, false);
                double eval = toCompare.evaluation();
               // System.out.println(toCompare);
                maxS += tem.move.toString() + ": " + eval + ", ";
                if (a < eval) {
                    a = eval;
                    temp = toCompare;
                }
                if((Graph.actualMinMaxDepth-depth)==0){
                    System.out.println("TTTTTTTTTTTTTTTTT");
                    System.out.println("1 temp depth: "+temp.depth);
                    while(temp.depth!=1){
                        temp=temp.parent;
                        System.out.println("temp depth: "+temp.depth);
                        if(temp.depth==1){
                            System.out.println("TEMP: "+temp.toString());
                        }
                    }
                }
                toReturn = temp;
                a = Math.max(a, eval);
            }else {
                for (ELine v : availList) {
                    Node tem = node.performMove(v);
                    tem.setParent(node);
                    tem.setDepth((Graph.actualMinMaxDepth-depth));
                    if (tem.bonusTurn) {
                        Node dummy = new Node(tem);
                        dummy.setParent(node);
                        Node toCompare = alphaBeta(dummy, depth-1, a, b, false);
                        double eval = toCompare.evaluation();
                        maxS += v.toString() + ": " + eval + ", ";
                        if (a < eval) {
                            a = eval;
                            temp = toCompare;
                        }
                        a = Math.max(a, eval);
                    } else {
                        Node toCompare = alphaBeta(tem, depth - 1, a, b, false);
                        double eval = toCompare.evaluation();
                        maxS += v.toString() + ": " +eval + ", ";
                        System.out.println("To compare: "+toCompare.toString());
                        System.out.println("eval: "+eval);
                        System.out.println("a < eval: "+a +" < "+eval+" : "+ (a<eval));
                        if (a < eval) {
                            a = eval;
                            temp = toCompare;
                        }
                        a = Math.max(a, eval);
                    }
                    if (a >= b) {
                        if (print) {
                            System.out.println(a + ">=" + b + " BREAK");
                        }
                        break;
                    }
                }
                if((Graph.actualMinMaxDepth-depth)==0){
                    System.out.println("TTTTTTTTTTTTTTTTT");
                    System.out.println("PRE TEMP: "+temp.toString());
                    System.out.println("2 temp depth: "+temp.depth);
                    System.out.println(temp==null);
                    while(temp.depth!=1){
                        temp=temp.parent;
                        System.out.println("temp depth: "+temp.depth);
                        if(temp.depth==1){
                            System.out.println("TEMP: "+temp.toString());
                        }
                    }
                }
                toReturn=temp;
            }
            maxS += "== " + a;
            if (print) {
                System.out.println(maxS);
            }
        }else{
            b = 1000000;
            String minS = "Depth: "+(Graph.actualMinMaxDepth-depth)+" Min "+t+": ";
            Node temp=null;
            if(node.dummyTurn!=null){
                Node tem = node.dummyTurn;
                tem.setParent(node);
                tem.setDepth((Graph.actualMinMaxDepth-depth));
                Node toCompare =  alphaBeta(tem,depth-1,a,b,true);
                double eval = toCompare.evaluation();
             //   System.out.println("2:"+toCompare);
                if(b>eval){
                    b=eval;
                    temp = toCompare;
                }
                if((Graph.actualMinMaxDepth-depth)==0){
                    System.out.println("TTTTTTTTTTTTTTTTT");
                    System.out.println("3 temp depth: "+temp.depth);
                    while(temp.depth!=1){
                        temp=temp.parent;
                        System.out.println("temp depth: "+temp.depth);
                        if(temp.depth==1){
                            System.out.println("TEMP: "+temp.toString());
                        }
                    }
                }
                toReturn=temp;
                minS+= tem.move.toString()+": "+eval+", ";
                b = Math.min(b,eval);
            } else {
                for (ELine v : availList) {
                    Node tem = node.performMove(v);
                    tem.setParent(node);
                    tem.setDepth((actualMinMaxDepth-depth));
                    if(tem.bonusTurn){
                        Node dummy = new Node(tem);
                        dummy.setParent(node);
                        Node toCompare = alphaBeta(dummy, depth-1, a, b, true);
                        double eval = toCompare.evaluation();
                        if (b > eval) {
                            b = eval;
                            temp = toCompare;
                        }
                        minS += v.toString() + ": " + toCompare + ", ";
                        b = Math.min(b, eval);
                    }else {
                        Node toCompare = alphaBeta(tem, depth - 1, a, b, true);
                        double eval = toCompare.evaluation();
                        if (b > eval) {
                            b = eval;
                            temp = toCompare;
                        }
                        minS += v.toString() + ": " + eval + ", ";
                        b = Math.min(b, eval);
                    }
                    if (b <= a) {
                        if (print) {
                            System.out.println(b + "<=" + a + " BREAK");
                        }
                        break;
                    }
                }
                if((actualMinMaxDepth-depth)==0){
                    System.out.println("TTTTTTTTTTTTTTTTT");
                    System.out.println("4 temp depth: "+temp.depth);
                    while(temp.depth!=1){
                        temp=temp.parent;
                        System.out.println("temp depth: "+temp.depth);
                        if(temp.depth==1){
                            System.out.println("TEMP: "+temp.toString());
                        }
                    }
                }
                toReturn=temp;
            }
            minS += "== " + b;
            if (print) {
                System.out.println(minS);
            }
        }
        if(print) {
            System.out.println("Depth: "+(actualMinMaxDepth-depth)+" | "+t + "| val: b:" + b+" a:"+a);
        }
        if(toReturn==null||toReturn.move==null){
            System.out.println(node.toString());
        }
        System.out.println("TO RETURN: "+toReturn.toString());
        return toReturn;
    }

     */


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
