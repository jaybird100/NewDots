package minMax.GeneticAlgorithm;

import game.GameBoard;
import minMax.MinMax;

import java.io.IOException;
import java.util.ArrayList;

public class GA {
    static int iterations = 10;

    public static void main(String[] args) throws IOException {
        GA temp = new GA(1.5510972083543284,0.3808819670949158);
        temp.createBots();
    }
    public static int number=10;

    public static MinMax tt;
    public static int h = 4;
    public static int w = 4;
    public static int depth = 5;

    public static boolean finished=false;
    public static int wins;
    public static int draws;
    public static int loses;

    public static int simulations = 40;
    static double aRange=0.5;
    static double bRange=0.5;

    static double a;
    static double b;

    static ArrayList<double[]> topValues;

    public static double tempA;
    public static double tempB;
    public static ArrayList<double[]> values;
    public GA(double a, double b){
        this.a=a;
        this.b=b;
        double[] val = new double[2];
        val[0]=a;
        val[1]=b;
        topValues=new ArrayList<>();
        topValues.add(val);
        values= new ArrayList<>();
    }
    public static void createBots() throws IOException {
        number--;
        if(number==0){
            for(double[] a:values){
                System.out.println(a[0]+" "+a[1]+" | "+a[2]);
            }
        }else{
            int id = (int)(Math.random()*topValues.size());
            tempA = topValues.get(id)[0]+(Math.random()*aRange-(aRange/2));
            tempB = topValues.get(id)[1]+(Math.random()*bRange-(bRange/2));
            MinMax t = new MinMax();
            t.setA(tempA);
            t.setB(tempB);
            tt=t;
            System.out.println("num: "+number);
            GameBoard a = new GameBoard(h, w, t, simulations, depth, true);
        }
    }
    public static void sortBots() throws IOException {
        iterations--;
        if(iterations!=0) {
            if (number == 0) {
                double max = -1;
                double[] value = new double[2];
                for (double[] a : values) {
                    if (a[2] > max) {
                        max = a[2];
                        value[0] = a[0];
                        value[1] = a[1];
                    }
                }
                a=value[0];
                b=value[1];
                topValues=new ArrayList<>();
                for(double[] a : values){
                    if(a[2]>(max-3)){
                        double[] val = new double[2];
                        val[0]=a[0];
                        val[1]=a[1];
                        topValues.add(val);
                    }
                }
                number=10;
                createBots();
            }
        }else{
            System.out.println("A: "+a+" B: "+b);
        }
    }
}
