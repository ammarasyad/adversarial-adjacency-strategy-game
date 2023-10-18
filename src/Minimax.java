import javafx.scene.control.Button;

import java.util.Arrays;

public class Minimax implements MoveController {

    static int MAX_VAL = 64;
    static int MIN_VAL = -64;

    private final OutputFrameController controller;
    private final Boolean player;


    public Minimax(OutputFrameController controller, Boolean isPlayerX){
        this.controller = controller;
        this.player = isPlayerX;
    }

    @Override
    public int[] move() {
        Board board = new Board(controller,player);
        Object[] minimax = minimaxValue(new Object[]{board,new int[]{0,0}},true,MIN_VAL,MAX_VAL);
        Object[] value = (Object[]) minimax[0];
        return (int[]) value[1];
    }


    // find optimal value of all boards using minimax algorithm and a-b pruning
    // TODO: find the corresponding best next step based on the optimal value calculated
    public Object[] minimaxValue (Object[] nodeValues, boolean isMaximizing, int alpha, int beta) {
        Board board = (Board) nodeValues[0];
        Object[] bestBranch = new Object[]{nodeValues, 0};

        if (board.isBoardFull()) {
            return new Object[]{nodeValues, board.getFunctionValue()};
        }

        int bestVal;
        if (isMaximizing) {
            Object[][] branch = board.getPossibleChilds(this.player);
            bestVal = MIN_VAL;

            for (Object[] objects : branch) {
                Object[] currentVal = minimaxValue(objects, false, alpha, beta);
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
                Object[] currentVal = minimaxValue(objects, true, alpha, beta);
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
        Boolean isPlayerX;

        public Board(String[][] states, Boolean isPlayerX) {
            this.states = states; // be careful of parameter input that doesn't comply with 8x8 rule
            this.isPlayerX = isPlayerX;
        }

        public Board(OutputFrameController controller, Boolean isPlayerX){
            Button[][] temp = controller.getButtons();
            for (int i = 0; i<controller.getButtons().length ; i++){
                for (int j = 0; j<controller.getButtons().length ; j++){
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
                    } else if (states[i][j].equals("O")){
                        countO++;
                    }
                }
            }

            return (isPlayerX) ? (countX - countO) : (countO - countX);
        }

        public Boolean isBoardFull(){
            for (int i = 0;i<8;i++){
                for (int j = 0; j<8;j++){
                    if (isEmpty(this.states[i][j])) return false;
                 }
            }
            return true;
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
                    if(isEmpty(states[i][j])) {
                        children[index] = this.makeNewChild(isPlayer, i, j);
                        child[index] = new Object[]{swapSurround(children[index], i, j),new int[]{i,j}};
                        index++;
                    }
                }
            }

            return child;
        }


        public Board swapSurround(Board board, int row, int column){
            if ((row> 0 && row<7)&&(column>0 && column <7)){
                board.states[row-1][column] = (board.isPlayerX && !isEmpty(board.states[row-1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row-1][column]))?"Y":"" ;
                board.states[row][column-1] = (board.isPlayerX && !isEmpty(board.states[row][column-1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column-1]))?"Y":"" ;
                board.states[row][column+1] = (board.isPlayerX && !isEmpty(board.states[row][column+1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column+1]))?"Y":"" ;
                board.states[row+1][column] = (board.isPlayerX && !isEmpty(board.states[row+1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row+1][column]))?"Y":"" ;
            }
            if ((row> 0 && row<7)&&(column>0)){
                board.states[row-1][column] = (board.isPlayerX && !isEmpty(board.states[row-1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row-1][column]))?"Y":"" ;
                board.states[row][column-1] = (board.isPlayerX && !isEmpty(board.states[row][column-1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column-1]))?"Y":"" ;
                board.states[row+1][column] = (board.isPlayerX && !isEmpty(board.states[row+1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row+1][column]))?"Y":"" ;
            }
            if ((row> 0 && row<7)&&(column<7)){
                board.states[row-1][column] = (board.isPlayerX && !isEmpty(board.states[row-1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row-1][column]))?"Y":"" ;
                board.states[row][column+1] = (board.isPlayerX && !isEmpty(board.states[row][column+1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column+1]))?"Y":"" ;
                board.states[row+1][column] = (board.isPlayerX && !isEmpty(board.states[row+1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row+1][column]))?"Y":"" ;
            }
            if ((row> 0)&&(column>0 && column <7)){
                board.states[row-1][column] = (board.isPlayerX && !isEmpty(board.states[row-1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row-1][column]))?"Y":"" ;
                board.states[row][column-1] = (board.isPlayerX && !isEmpty(board.states[row][column-1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column-1]))?"Y":"" ;
                board.states[row][column+1] = (board.isPlayerX && !isEmpty(board.states[row][column+1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column+1]))?"Y":"" ;
            }
            if ((row<7)&&(column>0 && column <7)){
                board.states[row][column-1] = (board.isPlayerX && !isEmpty(board.states[row][column-1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column-1]))?"Y":"" ;
                board.states[row][column+1] = (board.isPlayerX && !isEmpty(board.states[row][column+1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column+1]))?"Y":"" ;
                board.states[row+1][column] = (board.isPlayerX && !isEmpty(board.states[row+1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row+1][column]))?"Y":"" ;
            }
            if ((row> 0)&&(column>0)){
                board.states[row][column-1] = (board.isPlayerX && !isEmpty(board.states[row][column-1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column-1]))?"Y":"" ;
                board.states[row-1][column] = (board.isPlayerX && !isEmpty(board.states[row-1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row-1][column]))?"Y":"" ;
            }
            if ((row > 0)&&(column <7)){
                board.states[row-1][column] = (board.isPlayerX && !isEmpty(board.states[row-1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row-1][column]))?"Y":"" ;
                board.states[row][column+1] = (board.isPlayerX && !isEmpty(board.states[row][column+1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column+1]))?"Y":"" ;
            }
            if ((row<7)&&(column <7)){
                board.states[row][column+1] = (board.isPlayerX && !isEmpty(board.states[row][column+1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column+1]))?"Y":"" ;
                board.states[row+1][column] = (board.isPlayerX && !isEmpty(board.states[row+1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row+1][column]))?"Y":"" ;
            }
            if ((row<7)&&(column>0)){
                board.states[row][column-1] = (board.isPlayerX && !isEmpty(board.states[row][column-1]))?"X":(!board.isPlayerX && !isEmpty(board.states[row][column-1]))?"Y":"" ;
                board.states[row+1][column] = (board.isPlayerX && !isEmpty(board.states[row+1][column]))?"X":(!board.isPlayerX && !isEmpty(board.states[row+1][column]))?"Y":"" ;
            }
            return board;
        }

        private static boolean isEmpty(String gridValue) {
            return gridValue.equals("");
        }
    }
}
