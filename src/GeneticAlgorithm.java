import javafx.scene.control.Button;

import java.util.*;

public class GeneticAlgorithm implements MoveController{
    private final OutputFrameController controller;

    public  GeneticAlgorithm(OutputFrameController controller){ this.controller=controller;}


    @Override
    public int[] move(){
        Button[][] board= controller.getButtons();
        int n=board.length;
        int m=board[0].length;
//        List<int[]> emptySquares=getEmptySquares(board);
        String[][] boardString=new String[n][m];
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                boardString[i][j]=board[i][j].getText();
            }
        }
        List<Chromosome> chromosomes=new ArrayList<Chromosome>();
        List<Chromosome> ret=generate(boardString,chromosomes,false,"O","X",2);
        List<Chromosome> rett=fitnessFunction(ret);
//        Chromosome chromosome1 = select(rett);
//        Chromosome chromosome2 = select(rett);
//        List<Chromosome> crossOvers=crossover(chromosome1,chromosome2,0);

//        for (Chromosome r :
//                rett) {
//            System.out.println(r.toString());
//        }
        List<Chromosome> newChromosome=newCrossover(boardString,rett,0,"O","X");
        for (int i=0;i<10;i++){
            newChromosome=newCrossover(boardString,newChromosome,0,"O","X");
        }
        return newChromosome.stream().max(Comparator.comparing(v -> v.getFitness())).get().getGen().get(0);
//        return rett.get(0).getGen().get(0);
    }

    public int boardCount(String[][] board, String symbol){
        int count=0;
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board.length;j++){
                if (board[i][j].equals(symbol)){
                    count++;
                }
            }
        }
        return count;
    }

    public List<Chromosome> generate(String[][] board, List<Chromosome> chromosomes,boolean isOpponentTurn,String botSymbol, String opponentSymbol, int count){
        List<int[]> emptySquares=getEmptySquares(board);
//        System.out.println(count);
        if (count<1){
            return chromosomes;
//            return null;
        }
        if (emptySquares.isEmpty()){
            return chromosomes;
//            return null;
        } else {
            String[][] newBoard=board;
            System.out.println(count);
//            for (int i=0;i<newBoard.length;i++){
//                for (int j=0;j<newBoard[0].length;j++){
//                    System.out.print(newBoard[i][j]+" ");
//                }
//                System.out.print("\n");
//            }
            if (isOpponentTurn){
                int[] bestMove=getBestOpponentMove(newBoard,botSymbol);
                newBoard[bestMove[0]][bestMove[1]]=opponentSymbol;
                return generate(newBoard,chromosomes,!isOpponentTurn,botSymbol,opponentSymbol,count-1);
            } else {
                List<Chromosome> newChromosomes=new ArrayList<Chromosome>();

                if (chromosomes.isEmpty()){
                    newChromosomes=generateChromosome(emptySquares);
                } else {
                    for (Chromosome chromosome :
                            chromosomes) {
                        for (int i=0;i<board.length;i++){
                            for (int j=0;j<board[i].length;j++){
//                                System.out.println(i+" "+j);
                                if (board[i][j].equals("")){
                                    Chromosome ne=new Chromosome();
                                    ne.copy(chromosome);
//                                    System.out.println(ne.toString());
                                    ne.addGen(new int[]{i,j});
//                                    System.out.println(ne.toString());
                                    if (!isInside(newChromosomes,ne)){
                                        newChromosomes.add(ne);
                                    }
                                }
                            }
                        }
                    }
                }

//                for (Chromosome c :
//                        newChromosomes) {
//                    System.out.println(c);
//                }

                List<List<Chromosome>> ret=new ArrayList<List<Chromosome>>();
                List<Chromosome> rett=new ArrayList<Chromosome>();
                for (Chromosome chromosome:
                     newChromosomes) {
                    newBoard=new String[8][8];
                    for (int i=0;i<8;i++){
                        for (int j=0;j<8;j++){
                            newBoard[i][j]=board[i][j];
                        }
                    }
//                    System.out.println("aaaaa");
                    int[] botMove=chromosome.getGen().get(chromosome.getGen().size()-1);
                    newBoard[botMove[0]][botMove[1]]=botSymbol;
                    if (botMove[0]==0){
                        if (botMove[1]==0){
                            if (newBoard[botMove[0]][botMove[1]+1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]+1]=botSymbol;
                            }
                            if (newBoard[botMove[0]+1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]+1][botMove[1]]=botSymbol;
                            }
                        } else if (botMove[1]>0 && botMove[1]<7){
                            if (newBoard[botMove[0]][botMove[1]+1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]+1]=botSymbol;
                            }
                            if (newBoard[botMove[0]+1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]+1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]-1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]-1]=botSymbol;
                            }
                        } else if (botMove[1]==7){
                            if (newBoard[botMove[0]+1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]+1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]-1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]-1]=botSymbol;
                            }
                        }
                    } else if (botMove[0]>0 &&botMove[0]<7){
                        if (botMove[1]==0){
                            if (newBoard[botMove[0]-1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]-1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]+1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]+1]=botSymbol;
                            }
                            if (newBoard[botMove[0]+1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]+1][botMove[1]]=botSymbol;
                            }
                        } else if (botMove[1]>0 && botMove[1]<7){
                            if (newBoard[botMove[0]-1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]-1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]+1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]+1]=botSymbol;
                            }
                            if (newBoard[botMove[0]+1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]+1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]-1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]-1]=botSymbol;
                            }
                        } else if (botMove[1]==7){
                            if (newBoard[botMove[0]-1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]-1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]+1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]+1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]-1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]-1]=botSymbol;
                            }
                        }
                    } else if (botMove[0]==7){
                        if (botMove[1]==0){
                            if (newBoard[botMove[0]-1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]-1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]+1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]+1]=botSymbol;
                            }
                        } else if (botMove[1]>0 && botMove[1]<7){
                            if (newBoard[botMove[0]-1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]-1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]+1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]+1]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]-1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]-1]=botSymbol;
                            }
                        } else if (botMove[1]==7){
                            if (newBoard[botMove[0]-1][botMove[1]].equals(opponentSymbol)){
                                newBoard[botMove[0]-1][botMove[1]]=botSymbol;
                            }
                            if (newBoard[botMove[0]][botMove[1]-1].equals(opponentSymbol)){
                                newBoard[botMove[0]][botMove[1]-1]=botSymbol;
                            }
                        }
                    }

                    chromosome.setFitness(fitness(newBoard,botSymbol,opponentSymbol));
                    List<Chromosome> temp = generate(newBoard,newChromosomes,isOpponentTurn,botSymbol,opponentSymbol,count-1);
                    for (Chromosome c :
                            temp) {
                        if (!isInside(rett, c)) {
                            rett.add(c);
                        }
                    }
                }
                System.out.println(Integer.toString(rett.size()));
                return rett;
            }
        }
    }

    public boolean isInside(List<Chromosome> chromosomeList, Chromosome chromosome){
        for (Chromosome c :
                chromosomeList) {
            if (c==chromosome) {
                return true;
            }
        }
        return false;
    }

    public int[] getBestOpponentMove(String[][] board, String opponentSymbol){
        int[] currentBest=getEmptySquares(board).get(0);
        int currentBestVal=0;
        int currentVal=0;
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++){
                currentVal=0;
                if (board[i][j].equals("")){
                    if (i>0 && i<7){
                        if (j>0 && j<7){
                            if (board[i-1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j+1].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i+1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j-1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                        else if (j == 0) {
                            if (board[i-1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j+1].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i+1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                        else if (j == 7) {
                            if (board[i-1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i+1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j-1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                    }
                    else if (i==0){
                        if (j>0 && j<7){
                            if (board[i][j+1].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i+1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j-1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                        else if (j == 0) {
                            if (board[i][j+1].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i+1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                        else if (j == 7) {
                            if (board[i+1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j-1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                    }
                    else if (i==7){
                        if (j>0 && j<7){
                            if (board[i-1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j+1].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j-1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                        else if (j == 0) {
                            if (board[i-1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j+1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                        else if (j == 7) {
                            if (board[i-1][j].equals(opponentSymbol)){
                                currentVal++;
                            }
                            if (board[i][j-1].equals(opponentSymbol)){
                                currentVal++;
                            }
                        }
                    }
                }
                if (currentVal>currentBestVal){
                    currentBest=new int[]{i,j};
                    currentBestVal=currentVal;
                }
            }
        }
        return currentBest;
    }

    public List<Chromosome> generateChromosome(List<int[]> emptyBoard){
        List<Chromosome> ret=new ArrayList<Chromosome>();
        for (int[] coordinate :
                emptyBoard) {
            ret.add(new Chromosome().addGen(coordinate));
        }
        return ret;
    }


    public List<Chromosome> updateChromosome(List<int[]> emptyBoard, List<Chromosome> chromosomes){
        List<Chromosome> ret= new ArrayList<Chromosome>();
        for (Chromosome chromosome:
             chromosomes) {
            for (int[] coordinate:
            emptyBoard){
                ret.add(chromosome.addGen(coordinate));
            }
        }
        return ret;
    }


    public float fitness(String[][] board,String botSymbol, String opponentSymbol){
        int bot=boardCount(board,botSymbol);
        int opponent=boardCount(board,opponentSymbol);
        return (float)bot/(float) opponent;
    }

    public List<Chromosome> fitnessFunction(List<Chromosome> chromosomes)
    {
        float sumVal= (float) chromosomes.stream().mapToDouble(o->o.getFitness()).sum();
        List<Chromosome> ret = new ArrayList<Chromosome>();
        ret.addAll(chromosomes);
        for (Chromosome c :
                ret) {
//            System.out.println("before");
//            System.out.println(c.toString());
            c.setFitness(c.getFitness()/sumVal);
//            System.out.println("after");
//            System.out.println(c.toString());
        }
        Collections.sort(ret, Comparator.comparing(Chromosome::getFitness));
        return chromosomes;
    }

    public String[][] copyBoard(String[][] board){
        int n=board.length;
        int m=board[0].length;
        String[][] ret=new String[n][m];
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                ret[i][j]=board[i][j];
            }
        }
        return ret;
    }
    public List<Chromosome> crossover(Chromosome chromosome1, Chromosome chromosome2, int crossoverPoint){
        Chromosome newChromosome1 = new Chromosome();
        Chromosome newChromosome2 = new Chromosome();
        for (int i=0; i<crossoverPoint;i++){
            newChromosome1.addGen(chromosome1.getGen().get(i));
            newChromosome2.addGen(chromosome2.getGen().get(i));
        }
        for (int j=crossoverPoint; j<chromosome1.getGen().size();j++){
            newChromosome1.addGen(chromosome2.getGen().get(j));
            newChromosome2.addGen(chromosome1.getGen().get(j));
        }
        List<Chromosome> ret=new ArrayList<Chromosome>();
        ret.add(newChromosome1);
        ret.add(newChromosome2);
        return ret;
    }

    public List<Chromosome> newCrossover(String[][] board,List<Chromosome> chromosomes,int crossoverPoint,String botSymbol, String opponentSymbol){
        List<Chromosome> newChromosomes=new ArrayList<Chromosome>();
        for (int i=0;i<10;i++){
            Chromosome chromosome1 = select(chromosomes);
            Chromosome chromosome2 = select(chromosomes);
            newChromosomes.addAll(crossover(chromosome1,chromosome2,crossoverPoint));
        }
        return newFitness(board,newChromosomes,botSymbol,opponentSymbol);
    }

    public Chromosome select(List<Chromosome> chromosomes){
        Random random=new Random();
        float rfloat=random.nextFloat();
        float sum=0;
        for (Chromosome c :
                chromosomes) {
            sum += c.getFitness();
            if (sum>rfloat){
                return c;
            }
        }
        return chromosomes.get(chromosomes.size()-1);
    }
    public Chromosome mutation(Chromosome chromosome, int mutationIdx){
        Chromosome ret=new Chromosome();
        Random random=new Random();
        int newX=random.nextInt(8);
        int newY=random.nextInt(8);
        ret.copy(chromosome);
        ret.updateGen(mutationIdx,new int[]{newX,newY});
        return ret;
    }

    public List<Chromosome> newFitness(String[][] board, List<Chromosome> chromosomes,String botSymbol, String opponentSymbol){
        String[][] newBoard= copyBoard(board);
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.addAll(chromosomes);
        for (Chromosome c :
                newChromosomes) {
            newBoard=copyBoard(board);
            for (int[] coordinate:
            c.getGen()){
                newBoard=updateBoard(newBoard,coordinate,botSymbol,opponentSymbol);
                newBoard=updateBoard(newBoard,getBestOpponentMove(newBoard,botSymbol),opponentSymbol,botSymbol);
            }
            c.setFitness(fitness(board,botSymbol,opponentSymbol));
        }
        return newChromosomes;
    }

    public String[][] updateBoard(String[][] board, int[] coordinate, String botSymbol,String opponentSymbol){
        String[][] newBoard=copyBoard(board);
        newBoard[coordinate[0]][coordinate[1]]=botSymbol;
        if (coordinate[0]==0){
            if (coordinate[1]==0){
                if (newBoard[coordinate[0]][coordinate[1]+1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]+1]=botSymbol;
                }
                if (newBoard[coordinate[0]+1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]+1][coordinate[1]]=botSymbol;
                }
            } else if (coordinate[1]>0 && coordinate[1]<7){
                if (newBoard[coordinate[0]][coordinate[1]+1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]+1]=botSymbol;
                }
                if (newBoard[coordinate[0]+1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]+1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]-1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]-1]=botSymbol;
                }
            } else if (coordinate[1]==7){
                if (newBoard[coordinate[0]+1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]+1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]-1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]-1]=botSymbol;
                }
            }
        } else if (coordinate[0]>0 &&coordinate[0]<7){
            if (coordinate[1]==0){
                if (newBoard[coordinate[0]-1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]-1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]+1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]+1]=botSymbol;
                }
                if (newBoard[coordinate[0]+1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]+1][coordinate[1]]=botSymbol;
                }
            } else if (coordinate[1]>0 && coordinate[1]<7){
                if (newBoard[coordinate[0]-1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]-1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]+1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]+1]=botSymbol;
                }
                if (newBoard[coordinate[0]+1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]+1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]-1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]-1]=botSymbol;
                }
            } else if (coordinate[1]==7){
                if (newBoard[coordinate[0]-1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]-1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]+1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]+1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]-1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]-1]=botSymbol;
                }
            }
        } else if (coordinate[0]==7){
            if (coordinate[1]==0){
                if (newBoard[coordinate[0]-1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]-1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]+1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]+1]=botSymbol;
                }
            } else if (coordinate[1]>0 && coordinate[1]<7){
                if (newBoard[coordinate[0]-1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]-1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]+1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]+1]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]-1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]-1]=botSymbol;
                }
            } else if (coordinate[1]==7){
                if (newBoard[coordinate[0]-1][coordinate[1]].equals(opponentSymbol)){
                    newBoard[coordinate[0]-1][coordinate[1]]=botSymbol;
                }
                if (newBoard[coordinate[0]][coordinate[1]-1].equals(opponentSymbol)){
                    newBoard[coordinate[0]][coordinate[1]-1]=botSymbol;
                }
            }
        }
        return newBoard;
    }
    public List<int[]> getEmptySquares(String[][] board){
        List<int[]> ret=new ArrayList<int[]>();
        for (int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++){
                if (board[i][j].equals("")){
                    ret.add(new int[]{i,j});
                }
            }
        }
        return  ret;
    }
}

class Chromosome{
    private ArrayList<int[]> gen;
    private float fitness=0;

    public Chromosome(/*int[] coordinate*/){
        this.gen=new ArrayList<int[]>();
//            this.gen.add(coordinate);
    }

    public List<int[]> getGen(){
        return this.gen;
    }

    public float getFitness() {
        return fitness;
    }

    public Chromosome addGen(int[] coordinate){
        this.gen.add(coordinate);
        return this;
    }

    public void popGen(){
        this.gen.remove(this.gen.size()-1);
    }

    public void updateGen(int idx, int[] newGen){this.gen.set(idx,newGen);}

    public void setFitness(float fitness){ this.fitness=fitness;}

    @Override
    public String toString(){
        String ret= "";
        for (int[] g :
                this.gen) {
            ret=ret.concat("{").concat(Integer.toString(g[0])).concat(" ").concat(Integer.toString(g[1])).concat("}").concat(" ");
        }
        ret=ret.concat(Float.toString(this.fitness));
        return ret;
    }

    public boolean isEquals(Chromosome chromosome) {
        for (int[] g :
                this.gen) {
            for (int[] g2 :
                    chromosome.getGen()) {
                if (g[0] != g2[0] || g[1] != g2[1] || this.fitness != chromosome.fitness) {
                    return false;
                }
            }
        }
        return true;
    }

    public void copy(Chromosome chromosome){
        this.gen.addAll(chromosome.getGen());
        this.fitness=chromosome.fitness;
    }
}