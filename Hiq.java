import java.util.*;

/**
 * Created by Vishal Kulkarni on 11/29/16.
 *    VVK27 | CS635 | vvk27@njit.edu
 */

/*
##### BOARD ####
# 24 23 22 21 20
# 19 18 17 16 15
# 14 13 12 11 10
#  9  8  7  6  5
#  4  3  2  1  0
 */

public class Hiq {

    // list of seen boards - this is used to prevent rechecking of paths
    private static final HashSet<Long> seenBoards = new HashSet<Long>();

    // list of solution boards in ascending order - filled in once the solution is found
    private static final ArrayList<Long> solution = new ArrayList<Long>();

    private static final ArrayList<long[]> finalMoves = new ArrayList<long[]>();

    public static void setGoalBoard(long goalBoard) {
        GOAL_BOARD = goalBoard;
    }

    public static void setInitialBoard(long initialBoard) {
        INITIAL_BOARD = initialBoard;
    }

    // goal board (one marble in center)
    private static long GOAL_BOARD = 1048576;


    // initial board (one marble free in center)
    private static long INITIAL_BOARD = 105983;


    // board that contains a ball in every available slot, i.e. GOAL_BOARD | INITIAL_BOARD
    private static final long VALID_BOARD_CELLS = 1154559;


    static void initialize(int initial_position) {
        int boardBits = 0;
        String boardOppBits = "";
        int counter = 0;

        // printing the initial position of board
        for (int i = 0; i < 5; i++) {
            System.out.print("  ");
            for (int k = 0; k < 4 - i; k++) {
                System.out.print("  ");
                boardOppBits += "0";
            }

            for (int j = 0; j < 5; j++) {
                if (j <= i) {
                    if (counter == initial_position) {
                        System.out.print("o   ");
                        boardOppBits += "1";
                    } else {
                        System.out.print("x   ");
                        boardOppBits += "0";
                    }

                    counter += 1;
                }

            }
            System.out.println();

        }


        System.out.println("----------------------");
        System.out.println("       Starting ");
        System.out.println("----------------------");
        //print(boardOppBits)
        boardBits = Integer.parseInt(boardOppBits, 2) ^ 1154559;
        //print(boardBits)

        //initial board (one marble free in given location by user )
        setInitialBoard(boardBits);
        //INITIAL_BOARD = 105983
        //print(INITIAL_BOARD)

        //goal board (one marble in given location by user)
        if ((initial_position) == 4)
            setGoalBoard(4);
        else if ((initial_position) == 8)
            setGoalBoard(4096);
        else if(initial_position == 7)
            setGoalBoard(1024);
        else
            setGoalBoard(Integer.parseInt(boardOppBits, 2));
        //GOAL_BOARD = 1048576
        //print(GOAL_BOARD)


    }


    // holds all 36 moves that are possible
    // the inner array is structures as following:
    // - first entry holds the peg that is added by the move
    // - second entry holds the two pegs that are removed by the move
    // - third entry holds all three involved pegs
    private static final long[][] moves = new long[36][];

    // -------

    // print the board
    private static void printBoard(long board) {
        // loop over all cells (the board is 5 x 5)
        for (int i = 24; i > -1; i--) {
            boolean validCell = ((1L << i) & VALID_BOARD_CELLS) != 0L;
            System.out.print(validCell ? (((1L << i) & board) != 0L ? "x   " : "o   ") : "  ");
            if (i % 5 == 0) System.out.println();
        }
        //System.out.println("--------------------");
    }

    // create the two possible moves for the three added pegs
    // (this function assumes that the pegs are in one continuous line)
    private static void createMoves(int bit1, int bit2, int bit3, ArrayList<long[]> moves) {
        moves.add(new long[]{(1L << bit1), (1L << bit2) | (1L << bit3), (1L << bit1) | (1L << bit2) | (1L << bit3)});
        moves.add(new long[]{(1L << bit3), (1L << bit2) | (1L << bit1), (1L << bit1) | (1L << bit2) | (1L << bit3)});

        //System.out.println("moves" + moves);
    }

    // do the calculation recursively by starting from
    // the "GOAL_BOARD" and doing moves in reverse
    private static boolean search(long board) {
        //int here =0;
        // for all possible moves
        for (long[] move : moves) {
            //System.out.println(here++);
            // check if the move is valid
            if ((move[1] & board) == 0L && (move[0] & board) != 0L) {
                // calculate the board after this move was applied
                long newBoard = board ^ move[2];
                // only continue processing if we have not seen this board before
                if (!seenBoards.contains(newBoard)) {
                    seenBoards.add(newBoard);
                    // check if the initial board is reached
                    if (newBoard == INITIAL_BOARD || search(newBoard)) {
                        solution.add(board);
                        finalMoves.add(move);
                        return true;
                    }
                }
            }
        }

        return false;
    }


    private static void convertMove(long[] moves) {
        String Newmove = Long.toBinaryString(moves[2]);
        int count = 0;
        ArrayList<Integer> tof = new ArrayList<>();


        int count1 = 5;
        if ((Newmove).length() >= 5) {
            for (int x = ((Newmove).length() - 1); x > -1; x -= 5) {
                for (int y = x; y > (x - count1); y--) {
                    if (y > -1 && (Newmove.charAt(y)) == '0')
                        count += 1;
                    if (y > -1 && (Newmove.charAt(y)) == '1') {
                        count += 1;
                        tof.add(15 - count);
                    }
                }
                count1--;
            }
        } else {

            for (int y = (Newmove).length() - 1; y > -1; y--) {
                if (y > -1 && (Newmove.charAt(y)) == '0')
                    count += 1;
                if (y > -1 && (Newmove.charAt(y)) == '1') {
                    count += 1;
                    tof.add(15 - count);
                }
            }

        }

        System.out.println("------------------------");
        System.out.print("[from,over,to]= ");
        for (int i = 0; i < 3; i++) {
            System.out.print(tof.get(i) + " ");

        }
        System.out.println();
        System.out.println("------------------------");
    }

    // the main method
    public static void main(String[] args) {
        // to measure the overall runtime of the program
        long time = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter initial position of your peg: ");
        int initialPos = scanner.nextInt();
        if (!(initialPos >= 0 && initialPos <= 14)) {
            System.out.println("invalid output please enter number between 0 to 14");
            System.exit(0);
        }
        initialize(initialPos);


        // add starting board (as this board is not added by the recursive function)
        solution.add(INITIAL_BOARD);

        // generate all possible moves
        //ArrayList<long[]> moves = new ArrayList<long[]>();
        // holds all starting positions in west-east direction
        //int[] startsX = new int[]{24, 23, 22, 19, 18, 14};


        // fill in the global moves variable that is used by the solver
        //moves.toArray(Hiq.moves);

        boolean run = true;
        while (run) {
            seenBoards.clear();
            //moves.clear();
            ArrayList<long[]> moves = new ArrayList<long[]>();
            int[] startsX = new int[]{4, 3, 2, 8, 7, 12};
            for (int x : startsX) {
                createMoves(x, x - 1, x - 2, moves);
            }
            // holds all starting positions in north-south direction
            //int[] startsY = new int[]{24, 23, 22, 19, 18, 14};
            int[] startsY = new int[]{20, 15, 10, 16, 11, 12};
            for (int y : startsY) {
                createMoves(y, y - 5, y - 10, moves);
            }
            // holds all starting positions in north-south direction
            //int[] startsZ = new int[]{4, 8, 12, 9, 13, 14};
            int[] startsZ = new int[]{4, 8, 12, 3, 7, 2};
            for (int z : startsZ) {
                createMoves(z, z + 4, z + 8, moves);
            }

            Collections.shuffle(moves);
            moves.toArray(Hiq.moves);
            // start recursively search for the initial board from the goal (reverse direction!)
            search(GOAL_BOARD);


            // the found solution
            int i = 0;
            for (long step : solution) {
                //System.out.println("solutions"+solution);
                printBoard(step);

                if (i < (finalMoves).size()) {
                    convertMove(finalMoves.get(i));
                    i += 1;
                }
            }
            System.out.println("------------------------");
            System.out.println("Completed in " + (System.currentTimeMillis() - time) + " ms.");
            System.out.println("------------------------");

            scanner = new Scanner(System.in);
            System.out.print("Find more solutions?(yes/no): ");
            String Answer = scanner.next();

            if (Answer.equals("no"))
                run = false;
        }


    }
}
