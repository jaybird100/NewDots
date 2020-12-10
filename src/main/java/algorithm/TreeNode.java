package algorithm;

import game.ELine;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private List<TreeNode> childNodes = new ArrayList<>();
    private int score;
    private int edgeIndex = -1;
    private boolean isPlayer;
    private List<ELine> eLines;
    private boolean getScore;
    private int[][] matrix;
    private boolean isEnd;
    private int alpher = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;

    public int getAlpher() {
        return alpher;
    }

    public void setAlpher(int alpher) {
        this.alpher = alpher;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public boolean isGetScore() {
        return getScore;
    }

    public void setGetScore(boolean getScore) {
        this.getScore = getScore;
    }

    public List<ELine> geteLines() {
        return eLines;
    }

    public void seteLines(List<ELine> eLines) {
        this.eLines = eLines;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public List<TreeNode> getChildNodes() {
        return childNodes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEdgeIndex() {
        return edgeIndex;
    }

    public void setEdgeIndex(int edgeIndex) {
        this.edgeIndex = edgeIndex;
    }

}
