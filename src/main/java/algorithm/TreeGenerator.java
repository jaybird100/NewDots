package algorithm;

import game.ELine;
import game.Graph;

import java.util.*;
import java.util.stream.Collectors;

// generate the game tree
public class TreeGenerator {
    private static final int FOUR_SCORE = 20;
    private static final int Three_SCORE = 5;
    private static final int TWO_SCORE = 10;
    private static final int ONE_SCORE = 10;

    public static TreeNode generateTree(int[][] matrix, List<ELine> currentLines) {
        // root node is the node the robot will place. The  edgeIndex is the result this algorithm gets.
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode rootNode = new TreeNode();
        rootNode.setMatrix(matrix);
        rootNode.seteLines(currentLines);
        rootNode.setPlayer(false);
        queue.add(rootNode);
        int depth = 2;
        while (depth >= 0 && !queue.isEmpty()) {
            TreeNode currentNode = queue.remove();
            while (!calculatedIsDone(currentNode)) {
                TreeNode newNode = generateNode(currentNode);
                queue.add(newNode);
            }
            depth--;
        }
        return rootNode;
    }

    // generate new node
    private static TreeNode generateNode(TreeNode currentNode) {
        TreeNode newNode = new TreeNode();
        // get nextStep
        int index = getNextEdgeIndex(currentNode.geteLines());
        if (index == -1) {
            // this means the next step calculating is done
            return null;
        }
        newNode.seteLines(copyElines(currentNode.geteLines()));
        newNode.geteLines().get(index).setActivated(true);
        newNode.setPlayer(playerTurn(currentNode));
        newNode.setMatrix(copyMatrix(currentNode.getMatrix()));
        // get the chosen line
        ELine eLine = newNode.geteLines().get(index);
        // update this step in matrix
        newNode.getMatrix()[eLine.vertices.get(0).getID()][eLine.vertices.get(1).getID()] = 2;
        newNode.getMatrix()[eLine.vertices.get(1).getID()][eLine.vertices.get(0).getID()] = 2;
        // update the board
        newNode.setEdgeIndex(index);
        newNode.setScore(getScore(newNode));
        newNode.setEnd(GetIsEnd(newNode));
        currentNode.getChildNodes().add(newNode);
        return newNode;
    }

    public static int[][] copyMatrix(int[][] origin) {
        int[][] newMatrix = new int[origin.length][origin[0].length];
        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[0].length; j++) {
                newMatrix[i][j] = origin[i][j];
            }
        }
        return newMatrix;
    }

    public static List<ELine> copyElines(List<ELine> origin) {
        ArrayList<ELine> temp = new ArrayList<>();
        for(int e=0;e<origin.size();e++){
            ELine a = origin.get(e);
            ELine t = new ELine(a.isActivated(),a.getEdgeListIndex(),a.getVertices(),a.getHorizontal(),a.isCalculated());
            temp.add(t);
        }
        return temp;
    }

    private static int getScore(TreeNode newNode) {
        ELine eline = newNode.geteLines().get(newNode.getEdgeIndex());
        int box1 = 0;
        int box2 = 0;
        if (!eline.getHorizontal()) {
            if (eline.vertices.get(0).getRightVertex() != null) {
                box1 = newNode.getMatrix()[eline.vertices.get(0).getID()][eline.vertices.get(0).getID() + 1] + newNode.getMatrix()[eline.vertices.get(0).getID() + 1][eline.vertices.get(1).getID() + 1] + newNode.getMatrix()[eline.vertices.get(1).getID()][eline.vertices.get(1).getID() + 1];
            }
            if (eline.vertices.get(0).getLeftVertex() != null) {
                box2 = newNode.getMatrix()[eline.vertices.get(0).getID()][eline.vertices.get(0).getID() - 1] + newNode.getMatrix()[eline.vertices.get(0).getID() - 1][eline.vertices.get(1).getID() - 1] + newNode.getMatrix()[eline.vertices.get(1).getID()][eline.vertices.get(1).getID() - 1];
            }

        } else {
            // does the same but for horizontal edges
            if (eline.vertices.get(0).getDownVertex() != null) {
                box1 = newNode.getMatrix()[eline.vertices.get(0).getID()][eline.vertices.get(0).getID() + Graph.getWidth()] + newNode.getMatrix()[eline.vertices.get(0).getID() + Graph.getWidth()][eline.vertices.get(1).getID() + Graph.getWidth()] + newNode.getMatrix()[eline.vertices.get(1).getID()][eline.vertices.get(1).getID() + Graph.getWidth()];
            }
            if (eline.vertices.get(0).getUpVertex() != null) {
                box2 = newNode.getMatrix()[eline.vertices.get(0).getID()][eline.vertices.get(0).getID() - Graph.getWidth()] + Graph.getMatrix()[eline.vertices.get(0).getID() - Graph.getWidth()][eline.vertices.get(1).getID() - Graph.getWidth()] + newNode.getMatrix()[eline.vertices.get(1).getID()][eline.vertices.get(1).getID() - Graph.getWidth()];
            }
        }
        int score = 0;
        if (box1 > 0) {
            score += calSingleBox(box1, newNode, score);
        } else if (box2 > 0) {
            score += calSingleBox(box2, newNode, score);
        }
        return score;
    }

    private static int calSingleBox(int box1, TreeNode newNode, int score) {
        switch (box1) {
            case 6:
                if (newNode.isPlayer()) {
                    score += FOUR_SCORE;
                    updateStates(newNode, true);
                } else {
                    score += FOUR_SCORE;
                    updateStates(newNode, false);
                }
                break;
            case 5:
                score += Three_SCORE;
                break;
            case 4:
                score += TWO_SCORE;
                break;
            default:
                score += ONE_SCORE;
        }
        return score;
    }

    private static void updateStates(TreeNode newNode, boolean playerBox) {
        newNode.setGetScore(!playerBox);
    }

    private static int getNextEdgeIndex(List<ELine> candidateLines) {
        for (int i = 0, eLinesSize = candidateLines.size(); i < eLinesSize; i++) {
            ELine eLine = candidateLines.get(i);
            if (eLine.isCalculated()) {
                continue;
            }
            candidateLines.get(i).setCalculated(true);
            return i;
        }
        return -1;
    }

    private static boolean playerTurn(TreeNode currentNode) {
        // if last step  one gets the box,then next step is still the one's
        if (currentNode.isGetScore()) {
            return currentNode.isPlayer();
        } else {
            return !currentNode.isPlayer();
        }
    }

    private static boolean GetIsEnd(TreeNode newNode) {
        List<ELine> list = newNode.geteLines().stream().filter(e -> !e.isActivated()).collect(Collectors.toList());
        return list.isEmpty();
    }

    private static boolean calculatedIsDone(TreeNode currentNode) {
        List<ELine> result = currentNode.geteLines().stream().filter(e -> !e.isCalculated()).collect(Collectors.toList());
        return result.isEmpty();
    }
}
