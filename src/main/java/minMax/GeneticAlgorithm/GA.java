package minMax.GeneticAlgorithm;

import game.GameBoard;
import minMax.MinMax;

import java.io.IOException;
import java.util.ArrayList;

public class GA {
    static int iterations = 10;

    public static void main(String[] args) throws IOException {
        GA temp = new GA(1,0,0,0,0);
        temp.createBots();
    }
    public static int  number=10;

    public static MinMax tt;
    public static int h = 4;
    public static int w = 4;
    public static int depth = 5;

    public static boolean finished=false;
    public static int wins;
    public static int draws;
    public static int loses;

    public static int simulations = 30;
    static double aRange=0.5;
    static double bRange=0.5;
    static double cRange=1;
    static double dRange= 0.5;
    static double eRange = 2;

    static double a;
    static double b;
    static double c;
    static double d;
    static double e;

    static ArrayList<double[]> topValues;

    public static double tempA;
    public static double tempB;
    public static double tempC;
    public static double tempD;
    public static double tempE;
    public static ArrayList<double[]> values;
    public GA(double a, double b,double c,double d,double e){
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
        this.e=e;
        double[] val = new double[5];
        val[0]=a;
        val[1]=b;
        val[2]=c;
        val[3]=d;
        val[4]=e;
        topValues=new ArrayList<>();
        topValues.add(val);
        values= new ArrayList<>();
    }
    public static int counter=0;
    public static void createBots() throws IOException {
        number--;
        if(number==0){
            for(double[] a:values){
                System.out.println(a[0]+" "+a[1]+" "+a[2]+" "+a[3]+" "+a[4]+" | "+a[5]);
            }
        }else{
            if(counter<topValues.size()){
                tempA=topValues.get(counter)[0];
                tempB=topValues.get(counter)[1];
                tempC=topValues.get(counter)[2];
                tempD=topValues.get(counter)[3];
                tempE=topValues.get(counter)[4];
                number++;
            }else {
                int id = (int) (Math.random() * topValues.size());
                if ((topValues.get(id)[0] == 0 &&Math.random()<0.85)|| Math.random() < 0.1) {
                    tempA = 0;
                } else {
                    if (Math.random() < 0.8) {
                        if (a < 0) {
                            tempA = -1 * Math.abs(topValues.get(id)[0] + (Math.random() * aRange - (aRange / 2)));
                        } else {
                            if(a==0){
                                tempA=topValues.get(id)[0] + (Math.random() * aRange - (aRange / 2));
                            }else {
                                tempA = Math.abs(topValues.get(id)[0] + (Math.random() * aRange - (aRange / 2)));
                            }
                        }
                    } else {
                        tempA = topValues.get(id)[0] + (Math.random() * aRange - (aRange / 2));
                    }
                }
                if ((topValues.get(id)[1] == 0&&Math.random()<0.85) || Math.random() < 0.15) {
                    tempB = 0;
                } else {
                    if (Math.random() < 0.8) {
                        if (b < 0) {
                            tempB = -1 * Math.abs(topValues.get(id)[1] + (Math.random() * bRange - (bRange / 2)));
                        } else {
                            if(b==0){
                                tempB=topValues.get(id)[1] + (Math.random() * bRange - (bRange / 2));
                            }else {
                                tempB = Math.abs(topValues.get(id)[1] + (Math.random() * bRange - (bRange / 2)));
                            }
                        }
                    } else {
                        tempB = topValues.get(id)[1] + (Math.random() * bRange - (bRange / 2));
                    }
                }
                if ((topValues.get(id)[2] == 0 &&Math.random()<0.85)|| Math.random() < 0.2) {
                    tempC = 0;
                } else {
                    if (Math.random() < 0.8) {
                        if (b < 0) {
                            tempC = -1 * Math.abs(topValues.get(id)[2] + (Math.random() * cRange - (bRange / 2)));
                        } else {
                            if(c==0){
                                tempC=topValues.get(id)[2] + (Math.random() * cRange - (cRange / 2));
                            }else {
                                tempC = Math.abs(topValues.get(id)[2] + (Math.random() * cRange - (cRange / 2)));
                            }                        }
                    } else {
                        tempC = topValues.get(id)[2] + (Math.random() * cRange - (cRange / 2));
                    }
                }
                if ((topValues.get(id)[3] == 0&&Math.random()<0.85) || Math.random() < 0.15) {
                    tempD = 0;
                } else {
                    if (Math.random() < 0.8) {
                        if (d < 0) {
                            tempD = -1 * Math.abs(topValues.get(id)[3] + (Math.random() * dRange - (dRange / 2)));
                        } else {
                            if(d==0){
                                tempD=topValues.get(id)[3] + (Math.random() * dRange - (dRange / 2));
                            }else {
                                tempD = Math.abs(topValues.get(id)[3] + (Math.random() * dRange - (dRange / 2)));
                            }                        }
                    } else {
                        tempD = topValues.get(id)[3] + (Math.random() * dRange - (dRange / 2));
                    }
                }
                if ((topValues.get(id)[4] == 0&&Math.random()<0.85) || Math.random() < 0.2) {
                    tempE = 0;
                } else {
                    if (Math.random() < 0.8) {
                        if (e < 0) {
                            tempE = -1 * Math.abs(topValues.get(id)[4] + (Math.random() * eRange - (eRange / 2)));
                        } else {
                            if(e==0){
                                tempE=topValues.get(id)[4] + (Math.random() * eRange - (eRange / 2));
                            }else {
                                tempE = Math.abs(topValues.get(id)[4] + (Math.random() * eRange - (eRange / 2)));
                            }                        }
                    } else {
                        tempE = topValues.get(id)[4] + (Math.random() * eRange - (eRange / 2));
                    }
                }
            }
            counter++;
            MinMax t = new MinMax();
            t.setA(tempA);
            t.setB(tempB);
            t.setC(tempC);
            t.setD(tempD);
            t.setE(tempE);
            tt=t;
            System.out.println("num: "+number);
            GameBoard a = new GameBoard(h, w, t, simulations, depth, true);
        }
    }
    public static void sortBots() throws IOException {
        counter=0;
        iterations--;
        if(iterations!=0) {
            if (number == 0) {
                double max = -1;
                double[] value = new double[5];
                for (double[] a : values) {
                    if (a[5] > max) {
                        max = a[5];
                        value[0] = a[0];
                        value[1] = a[1];
                        value[2] = a[2];
                        value[3] = a[3];
                        value[4]= a[4];
                    }
                }
                for(int i =0;i<values.size();i++){
                    double a=values.get(i)[0];
                    double b=values.get(i)[1];
                    double c=values.get(i)[2];
                    double d=values.get(i)[3];
                    double e = values.get(i)[4];
                    int iZeroCount=0;
                    if(values.get(i)[1]==0){
                        iZeroCount++;
                    }
                    if(values.get(i)[2]==0){
                        iZeroCount++;
                    }
                    if(values.get(i)[3]==0){
                        iZeroCount++;
                    }
                    if(values.get(i)[4]==0){
                        iZeroCount++;
                    }
                    double score = values.get(i)[5];
                    int counter=1;
                    ArrayList<Integer> indexes = new ArrayList<>();
                    indexes.add(i);
                    for(int y=0;y<values.size();y++){
                        int zerocount=0;
                        if(values.get(y)[1]==0){
                            zerocount++;
                        }
                        if(values.get(y)[2]==0){
                            zerocount++;
                        }
                        if(values.get(y)[3]==0){
                            zerocount++;
                        }
                        if(values.get(y)[4]==0){
                            zerocount++;
                        }
                        if(y!=i&&((values.get(y)[0]==a&&values.get(y)[1]==b&&values.get(y)[2]==c&&values.get(y)[3]==d&&values.get(y)[4]==e)||(zerocount==iZeroCount&&zerocount==4&&(values.get(y)[0]!=0)))){
                            indexes.add(y);
                            counter++;
                            score+=values.get(y)[5];
                        }
                    }
                    for(Integer q:indexes){
                        values.get(q)[5]=score/counter;
                    }
                }
                a=value[0];
                b=value[1];
                c=value[2];
                d=value[3];
                e=value[4];
                topValues=new ArrayList<>();
                for(double[] a : values){
                    if(a[3]>(max-2.5)){
                        double[] val = new double[5];
                        val[0]=a[0];
                        val[1]=a[1];
                        val[2]=a[2];
                        val[3]=a[3];
                        val[4]=a[4];
                        topValues.add(val);
                    }
                }
                if(topValues.size()==0){
                    System.out.println("WHY");
                    System.out.println("max: "+max);
                    double[] val = new double[5];
                    val[0]=0;
                    val[1]=0;
                    val[2]=0;
                    val[3]=0;
                    val[4]=1;
                    topValues.add(val);
                }
                number=10;
                createBots();
            }
        }else{
            System.out.println("A: "+a+" B: "+b+" C: "+c+" D: "+d+" E: "+e);
        }
    }
}
