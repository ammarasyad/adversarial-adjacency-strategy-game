import javafx.scene.control.Button;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Minimax implements MoveController {

    static int MAX_VAL = 64;
    static int MIN_VAL = -64;

    private final OutputFrameController controller;
    private final boolean player;


    public Minimax(OutputFrameController controller, boolean isPlayerX) {
        this.controller = controller;
        this.player = isPlayerX;
    }

    @Override
    public int[] move() {
        Board board = new Board(controller, player);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Object[]> temp = executorService.submit(() -> minimaxValue(new Object[]{board, new int[]{0, 0}}, true, MIN_VAL, MAX_VAL, controller.roundsLeft));
        int[] val = new int[2];
        try {
            Object[] minimax = temp.get();
            Object[] value = (Object[]) minimax[0];
            return (int[]) value[1];
        } catch (InterruptedException | ExecutionException e) {
            return val;
        }
    }


    // find optimal value of all boards using minimax algorithm and a-b pruning
    // TODO: find the corresponding best next step based on the optimal value calculated
    public Object[] minimaxValue(Object[] nodeValues, boolean isMaximizing, int alpha, int beta, int round) {
        Board board = (Board) nodeValues[0];
        Object[] bestBranch = new Object[]{nodeValues, 0};

        if (board.isBoardFull()) {
            return new Object[]{nodeValues, board.getFunctionValue()};
        }
        if (round == 0) {
            return new Object[]{nodeValues, board.getFunctionValue()};
        }

        int bestVal;
        if (isMaximizing) {
            Object[][] branch = board.getPossibleChilds(this.player);
            bestVal = MIN_VAL;

            for (Object[] objects : branch) {
                Object[] currentVal = minimaxValue(objects, false, alpha, beta, round - 1);
                int temp = (int) currentVal[1];

                bestVal = Math.max(bestVal, temp);
                alpha = Math.max(alpha, bestVal);
                if (alpha == temp) bestBranch = currentVal;

                if (beta <= alpha) {
                    break;
                }
            }

        } else {
            Object[][] branch = board.getPossibleChilds(!this.player);
            bestVal = MAX_VAL;

            for (Object[] objects : branch) {
                Object[] currentVal = minimaxValue(objects, true, alpha, beta, round - 1);
                int temp = (int) currentVal[1];

                bestVal = Math.min(bestVal, temp);
                beta = Math.min(beta, bestVal);
                if (beta == temp) bestBranch = currentVal;

                if (beta <= alpha) {
                    break;
                }
            }

        }
        return bestBranch;
    }

    private static class Board {
        String[][] states = new String[8][8]; // can be optimized by turning it into a String of 64 char length
        boolean isPlayerX;

        public Board(String[][] states, boolean isPlayerX) {
            this.states = states; // be careful of parameter input that doesn't comply with 8x8 rule
            this.isPlayerX = isPlayerX;
        }

        public Board(OutputFrameController controller, boolean isPlayerX) {
            Button[][] temp = controller.getButtons();
            for (int i = 0; i < controller.getButtons().length; i++) {
                for (int j = 0; j < controller.getButtons().length; j++) {
                    this.states[i][j] = temp[i][j].getText();
                }
            }
            this.isPlayerX = isPlayerX;
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
                    if (states[i][j].equals("X")) {
                        countX++;
                    } else if (states[i][j].equals("O")) {
                        countO++;
                    }
                }
            }

            return (isPlayerX) ? (countX - countO) : (countO - countX);
        }

        public boolean isBoardFull() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isEmpty(this.states[i][j])) return false;
                }
            }
            return true;
        }

        public int getEmptyGridCount() {
            int count = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isEmpty(states[i][j])) {
                        count++;
                    }
                }
            }

            return count;
        }

        public Board makeNewChild(boolean isPlayerX, int iVal, int jVal) {
            String[][] result = new String[8][8];

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i == iVal && j == jVal) {
                        result[i][j] = isPlayerX ? "X" : "O"; // TODO: maybe flipped
                        continue;
                    }

                    result[i][j] = states[i][j];
                }
            }

            return new Board(result, this.isPlayerX);
        }

        public Object[][] getPossibleChilds(boolean isPlayer) {
            Object[][] child = new Object[this.getEmptyGridCount()][];
            Board[] children = new Board[this.getEmptyGridCount()];
            int index = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (isEmpty(states[i][j])) {
                        children[index] = this.makeNewChild(isPlayer, i, j);
                        child[index] = new Object[]{swapSurround(children[index], i, j), new int[]{i, j}};
                        index++;
                    }
                }
            }

            return child;
        }


        public Board swapSurround(Board board, int row, int column) {
            try {
                board.states[row - 1][column] = (board.isPlayerX && !isEmpty(board.states[row - 1][column])) ? "X" : (!board.isPlayerX && !isEmpty(board.states[row - 1][column])) ? "Y" : "";
                board.states[row][column - 1] = (board.isPlayerX && !isEmpty(board.states[row][column - 1])) ? "X" : (!board.isPlayerX && !isEmpty(board.states[row][column - 1])) ? "Y" : "";
                board.states[row][column + 1] = (board.isPlayerX && !isEmpty(board.states[row][column + 1])) ? "X" : (!board.isPlayerX && !isEmpty(board.states[row][column + 1])) ? "Y" : "";
                board.states[row + 1][column] = (board.isPlayerX && !isEmpty(board.states[row + 1][column])) ? "X" : (!board.isPlayerX && !isEmpty(board.states[row + 1][column])) ? "Y" : "";
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return board;
        }

        private static boolean isEmpty(String gridValue) {
            return gridValue.isEmpty();
        }
    }
}
