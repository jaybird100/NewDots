package Neural;

import game.GameBoard;
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.nd4j.jita.conf.CudaEnvironment;

import java.io.IOException;
import java.util.ArrayList;

public class Neural {
    static boolean measure;
    static int wins=0;
    static int draws=0;
    static int loses=0;
    static int gamesPlayed=0;
    static double epsilon=0.05;
    static int maxStep=15000;
    static int batchsize=262144;

    static int boardWidth=4;
    static int boardHeight=3;
    public static void main(String args[]) throws IOException, InterruptedException {
        GameBoard r = new GameBoard(boardHeight,boardWidth);
        CudaEnvironment.getInstance().getConfiguration().allowMultiGPU(true);
        measure=true;
        ArrayList<String> toPrint = new ArrayList();
        for(int w=0;w<500;w++) {
            DQNPolicy<GState> t = dots();
            t.save("QLearning.bin");
            System.out.println("SAVED: Turn "+(w+1));
            toPrint.add(wins+":"+draws+":"+loses);
            System.out.println("FINAL SCORE: "+wins+":"+draws+":"+loses);
            wins=0;
            draws=0;
            loses=0;
        }
        System.out.println("DONE");
        for(String a:toPrint){
            System.out.println(a);
        }
    }

    private static DQNPolicy<GState> dots() throws IOException {

        QLearningConfiguration DOTS_QL = QLearningConfiguration.builder()
                .seed(1L)                //Random seed (for reproducability)
                .maxEpochStep(100000000)        // Max step By epoch
                .maxStep(maxStep)           // Max step
                .expRepMaxSize(1000)    // Max size of experience replay
                .batchSize(batchsize)            // size of batches
                .targetDqnUpdateFreq(2500) // target update (hard)
                .updateStart(0)          // num step noop warmup
                .rewardFactor(0.1)       // reward scaling
                .gamma(0.99)              // gamma
                .errorClamp(1.0)          // /td-error clipping
                .minEpsilon(epsilon)         // min epsilon
                .epsilonNbStep(0)      // num step for eps greedy anneal
                .doubleDQN(false)          // double DQN
                .build();

        /*
        DQNDenseNetworkConfiguration DOTS_NET =
                DQNDenseNetworkConfiguration.builder()
                        .l2(0)
                        .updater(new Adam(0.01))
                        .numHiddenNodes(350)
                        .numLayers(8)
                        .build();
         */


        DQNPolicy p = DQNPolicy.load("QLearning.bin");
        IDQN DOTS_NET = p.getNeuralNet();











        // The neural network used by the agent. Note that there is no need to specify the number of inputs/outputs.
        // These will be read from the gym environment at the start of training.

        MDP<GState,Integer,DiscreteSpace> env = new Env();
        QLearningDiscreteDense<GState> dql = new QLearningDiscreteDense<GState>(env, DOTS_NET, DOTS_QL);
        System.out.println(dql.toString());
        dql.train();
        return dql.getPolicy();
    }
}
