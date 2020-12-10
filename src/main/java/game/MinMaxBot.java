package game;

import algorithm.MinMax;
import algorithm.TreeGenerator;
import algorithm.TreeNode;

import java.awt.*;
import java.util.ArrayList;

public class MinMaxBot {

    public static void placeEdge() throws InterruptedException {
        // init the game tree
        TreeNode root = TreeGenerator.generateTree(TreeGenerator.copyMatrix(Graph.getMatrix()), TreeGenerator.copyElines(Graph.getAvailableLines()));
        int chosen = MinMax.minMax(root, 2).getEdgeIndex();
        GameThread.clickEdge(chosen);
    }

}
