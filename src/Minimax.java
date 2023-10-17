import javafx.scene.control.Button;

public class Minimax implements MoveController {

    static int MAX_VAL = 64;
    static int MIN_VAL = -64;

    private final OutputFrameController controller;


    public Minimax(OutputFrameController controller) {
        this.controller = controller;
    }

    @Override
    public int[] move() {
        //          draft ide           //
        
        // expand nodes

        // get optimal value

        // get corresponding move

        // issue the computed move
        return new int[2];
    }


    // ini ngide buat dp expanding sampe ke bawah, lalu digabungin jdi satu array
    public Board[] calculateBoards(Board currentBoard, boolean isPlayer, int depth) {
        if (depth == 3) { // ubah dengan max depth - 1
            return currentBoard.getPossibleChilds(isPlayer);
        }

        Board[] resultArray = new Board[currentBoard.getEmptyGridCount() * (currentBoard.getEmptyGridCount() - 1)];
        int index = 0;

        Board[] tempChildren = currentBoard.getPossibleChilds(isPlayer);
        for (int i = 0; i < currentBoard.getEmptyGridCount(); i++) {
            for (Board newBoard : calculateBoards(tempChildren[i], !isPlayer, depth + 1)) {
                resultArray[index] = newBoard;
                index++;
            }
        }

        return resultArray;
    }


    // find optimal value of all boards using minimax algorithm and a-b pruning
    // TODO: find the corresponding best next step based on the optimal value calculated
    public int minimaxValue (Board[] nodeValues, int nodeIndex, int depth, boolean isMaximizing, int alpha, int beta) {

        if (depth == 0) {
            return nodeValues[nodeIndex].getFunctionValue();
        }

        if (isMaximizing) {
            int bestVal = MIN_VAL;

            for (int i = 0; i < 2; i++) {
                int currentVal = minimaxValue(nodeValues, nodeIndex * 2 + i, depth+1, false, alpha, beta);

                bestVal = Math.max(bestVal, currentVal);
                alpha = Math.max(alpha, bestVal);

                if (beta <= alpha) {
                    break;
                }
            }

            return bestVal;
        } else {
            int bestVal = MAX_VAL;

            for (int i = 0; i < 2; i++) {
                int currentVal = minimaxValue(nodeValues, nodeIndex * 2 + i, depth+1, true, alpha, beta);

                bestVal = Math.min(bestVal, currentVal);
                beta = Math.min(beta, bestVal);

                if (beta <= alpha) {
                    break;
                }
            }

            return bestVal;
        }
    }

    private static class Board {
        String[][] states; // can be optimized by turning it into a String of 64 char length

        public Board(String[][] states) {
            this.states = states; // be careful of parameter input that doesn't comply with 8x8 rule
        }

        public Board(Button[][] states) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    this.states[i][j] = states[i][j].getText();
                }
            }
        }

        public Board() {
            this.states = new String[8][8];
        }

        public int getFunctionValue() {
            int countX = 0, countO = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isPlayer(states[i][j])) {
                        countX++;
                    } else if (isBot(states[i][j])){ // me, as a bot
                        countO++;
                    }
                }
            }

            return countO - countX;
        }

        public int getEmptyGridCount() {
            int count = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(isEmpty(states[i][j])) {
                        count++;
                    }
                }
            }

            return count;
        }

        public Board makeNewChild(boolean isPlayer, int iVal, int jVal) {
            String[][] result = new String[8][8];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i == iVal && j == jVal) {
                        result[i][j] = isPlayer ? "X" : "O"; // TODO: maybe flipped
                        continue;
                    }

                    result[i][j] = states[i][j];
                }
            }

            return new Board(result);
        }

        public Board[] getPossibleChilds(boolean isPlayer) {
            Board[] children = new Board[this.getEmptyGridCount()];
            int index = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(isEmpty(states[i][j])) {
                        children[index] = this.makeNewChild(isPlayer, i, j);

                        index++;
                    }
                }
            }

            return children;
        }

        private static boolean isPlayer(String gridValue) {
            return gridValue.equals("X");
        }

        private static boolean isBot(String gridValue) {
            return gridValue.equals("O");
        }

        private static boolean isEmpty(String gridValue) {
            return gridValue.equals("");
        }
    }
}
