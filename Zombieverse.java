import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This lab assignment mainly target at your mastery of matrix, array, and BFS(or DFS).
 * Your goal is to complete the given function below and to produce the expected output
 * as commented in the main method. The grid/matrix has size of "m x n" in which 1 <= m,n <= 10
 *
 * @author CS 245 Data Sturcture & Algorithms (University of San Francisco)
 * @version Fall 2021
 */
public class Zombieverse {
    /** List of 2D arrays of all test cases */
    public static List<int[][]> test;

    /**
     * Initialize a list contains of 8 tests
     */
    public Zombieverse() {
        test = List.of(
                new int[][]{{0, 2}},

                new int[][]{{0, 0},
                            {0, 2}},

                new int[][]{{2, 1, 1},
                            {1, 1, 0},
                            {0, 1, 1}},

                new int[][]{{2, 1, 1},
                            {0, 1, 1},
                            {1, 0, 1}},

                new int[][]{{1, 0, 0, 2, 1, 1, 0, 1, 2},
                            {0, 1, 1, 1, 1, 0, 1, 2, 1},
                            {2, 2, 1, 0, 1, 1, 2, 1, 1},
                            {1, 1, 1, 1, 0, 2, 1, 1, 1},
                            {0, 1, 1, 1, 1, 1, 1, 2, 0},
                            {0, 2, 0, 2, 0, 1, 2, 1, 1},
                            {1, 0, 2, 1, 1, 1, 1, 0, 1},
                            {1, 2, 0, 1, 0, 2, 1, 1, 1},
                            {2, 1, 1, 1, 0, 1, 1, 0, 1}},

                new int[][]{{1, 1, 0, 2, 1, 1, 0, 1, 2},
                            {0, 1, 1, 1, 1, 0, 1, 2, 1},
                            {2, 2, 1, 0, 1, 1, 2, 1, 1},
                            {1, 1, 1, 1, 0, 2, 1, 1, 1},
                            {0, 1, 1, 1, 1, 1, 1, 2, 0},
                            {0, 2, 0, 2, 0, 1, 2, 1, 1},
                            {1, 0, 2, 1, 1, 1, 1, 0, 1},
                            {1, 2, 0, 1, 0, 2, 1, 1, 1},
                            {2, 1, 1, 1, 0, 1, 1, 0, 1}},

                new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 2, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 1, 1}},

                new int[][]{{1, 1, 0, 1, 1, 1, 0, 1, 1},
                            {0, 1, 1, 1, 1, 0, 1, 1, 1},
                            {1, 1, 1, 0, 1, 1, 1, 1, 1},
                            {1, 1, 1, 1, 0, 1, 1, 1, 1},
                            {0, 1, 1, 1, 1, 1, 1, 1, 0},
                            {0, 1, 0, 1, 0, 1, 1, 1, 1},
                            {1, 1, 1, 1, 1, 1, 1, 0, 1},
                            {1, 0, 0, 1, 0, 1, 1, 1, 1},
                            {1, 1, 1, 1, 0, 1, 1, 0, 2}});
    }

    /**
     * Will track the number of days in the given grid that how long it will take
     * for the zombies to infect every human(if there's any)
     *
     * @param grid a "m x n" grid contains only values of 0(empty space),
     *             1(human), and 2(zombie)
     * @return -1 if any human survived, 0 if no human at day 1,
     *          else return the days it took for all human to be infected
     */
    public int infection(int[][] grid) {
        //Grad the dimensions of the grid
        int rows = grid.length;
        int cols = grid[0].length;

        //Return value of the function = Number of days the humans have survived
        int days = 0;

        //We keep track of the number of humans to see if there are any survivors after the infection
        int humanCount = 0;

        //Q containing the current day zombies
        //int arrys contain two values that hold the coordinate location of each zombie
        //int[0] = row, int[1] = col
        Queue<int[]> zombieQ = new LinkedList<>();

        //Scan the grid to find the amount of zombies and humans on day one
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                //If we find a zombie at it to the q
                if(grid[i][j] == 2){
                    zombieQ.add(new int[]{i, j});
                }
                //If we find a human add it to the count
                if(grid[i][j] == 1){
                    humanCount++;
                }
            }
        }

        //If zero humans are found there is nothing to infect
        if(humanCount == 0) return 0;

        //Keep infecting as long as there is at least one zombie in the q
        while (!zombieQ.isEmpty()){
            //Increment the day
            ++days;
            //Size of the q is used to keep track of the # of zombies in the current day. Bc we add
            // the new zombies to the same q as the current.
            int size = zombieQ.size();

            //For each zombie we infect its human neighbors.
            for(int i = 0; i < size; i++) {
                //Get current zombie data
                int[] zombie = zombieQ.poll();
                int x = zombie[1];
                int y = zombie[0];

                //Check for humans adjacent to the current zombie. If a human is found change it
                // to a zombie and add the new zombie to the new q. Decrement human count
                //Left
                if (x > 0 && grid[y][x - 1] == 1) {
                    grid[y][x - 1] = 2;
                    humanCount--;
                    zombieQ.add(new int[]{y, x - 1});
                }

                //Right
                if (x < grid[0].length - 1 && grid[y][x + 1] == 1) {
                    // if the cell is a human
                    // infect the human
                    grid[y][x + 1] = 2;
                    humanCount--;
                    zombieQ.add(new int[]{y, x + 1});
                }

                //Up
                if (y > 0 && grid[y - 1][x] == 1) {
                    grid[y - 1][x] = 2;
                    humanCount--;
                    zombieQ.add(new int[]{y - 1, x});
                }

                //Down
                if (y < grid.length - 1 && grid[y + 1][x] == 1) {
                    grid[y + 1][x] = 2;
                    humanCount--;
                    zombieQ.add(new int[]{y + 1, x});
                }

            }
        }

        //if no humans are left return days -1 else return -1;
        return humanCount == 0 ? days - 1 : -1;
    }


    public static void main(String[] args) {
        Zombieverse zombieverse = new Zombieverse();
        for (int[][] region : test) {
            System.out.printf("Result of days: %d\n", zombieverse.infection(region));
        }
        /*Result of days: 0
        Result of days: 0
        Result of days: 4
        Result of days: -1
        Result of days: -1
        Result of days: 4
        Result of days: 8
        Result of days: 16*/
    }
}