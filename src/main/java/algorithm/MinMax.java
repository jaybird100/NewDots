package algorithm;

/*
function minimax(node, depth)
    if node is a terminal node or depth = 0
        return the heuristic value of node
    if the adversary is to play at node
        let α := +∞
        foreach child of node
            α := min(α, minimax(child, depth-1))
    else {we are to play at node}
        let α := -∞
        foreach child of node
            α := max(α, minimax(child, depth-1))
    return α
*/
public class MinMax {
    public static TreeNode minMax(TreeNode node, int depth) {
        int bestVal;
        if (node.isEnd() || depth == 0 || node.getChildNodes().isEmpty()) {
            return node;
        }
        if (node.isPlayer()) {
            bestVal = Integer.MAX_VALUE;
            int index = -1;
            for (TreeNode child : node.getChildNodes()) {
                if (node.getAlpher() >= node.getBeta()) {
                    break;
                }
                TreeNode temp = minMax(child, depth - 1);
                if (bestVal >= temp.getScore()) {
                    bestVal = temp.getScore();
                    node.setBeta(bestVal);
                    index = temp.getEdgeIndex();
                }
            }
            node.setScore(bestVal);
            if (node.getEdgeIndex() == -1) {
                node.setEdgeIndex(index);
            }
        } else {
            int index = -1;
            bestVal = Integer.MIN_VALUE;
            for (TreeNode child : node.getChildNodes()) {
                if (node.getAlpher() >= node.getBeta()) {
                    break;
                }
                TreeNode temp = minMax(child, depth - 1);
                if (bestVal <= temp.getScore()) {
                    bestVal = temp.getScore();
                    node.setAlpher(bestVal);
                    index = temp.getEdgeIndex();
                }
            }
            node.setScore(bestVal);
            if (node.getEdgeIndex() == -1) {
                node.setEdgeIndex(index);
            }
        }
        return node;
    }
}
