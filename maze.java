import java.util.*;

public class maze {

    private static final int right = 0;
    private static final int down = 1;
    private static final int left = 2;
    private static final int up = 3;
    private static Random randomGenerator;  // for random numbers

    public static int Size;
    public static int[] Up;
    public static int[] height;
    public static ArrayList<Edge> unusedEdges;
    public static ArrayList<Integer> S;

    public static class Point {  // a Point is a position in the maze

        public int x, y;

        // Constructor
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void copy(Point p) {
            this.x = p.x;
            this.y = p.y;
        }
    }

    public static class Edge {
        // an Edge links two neighboring Points:
        // For the grid graph, an edge can be represented by a point and a direction.
        Point point;
        int direction;    // one of right, down, left, up
        boolean used;     // for maze creation
        boolean deleted;  // for maze creation

        // Constructor
        public Edge(Point p, int d) {
            this.point = p;
            this.direction = d;
            this.used = false;
            this.deleted = false;
        }
    }

    // A board is an SizexSize array whose values are Points
    public static Point[][] board;

    // A graph is simply a set of edges: graph[i][d] is the edge
    // where i is the index for a Point and d is the direction
    public static Edge[][] graph;
    public static int N;   // number of points in the graph

    public static void displayInitBoard() {
        System.out.println("\nInitial Configuration:");

        for (int i = 0; i < Size; ++i) {
            System.out.print("    -");
            for (int j = 0; j < Size; ++j) System.out.print("----");
            System.out.println();
            if (i == 0) System.out.print("Start");
            else System.out.print("    |");
            for (int j = 0; j < Size; ++j) {
                if (i == Size-1 && j == Size-1)
                    System.out.print("    End");
                else System.out.print("   |");
            }
            System.out.println();
        }
        System.out.print("    -");
        for (int j = 0; j < Size; ++j) System.out.print("----");
        System.out.println();
    }

    public static void myBoard(ArrayList<Integer> input){
        System.out.print("    -");
        for (int j = 0; j < Size; ++j) System.out.print("----");
        System.out.println();

        for (int i = 0; i < Size; ++i) {
            if (i == 0) System.out.print("Start");
            else { System.out.print("    |"); }
            for (int l = 0; l < Size; ++l) {


                int pindex = i*Size+l;   // Point(i, j)'s index is i*Size + j
                Edge rightEdge = graph[pindex][right];
                if(i == Size-1 && l == Size-1) System.out.printf(" %c End", input.contains(i*Size+l)? 'X' : ' ');
                else {
                    if (!rightEdge.deleted) {
                        System.out.printf(" %c |", input.contains(i*Size+l)? 'X' : ' ');
                    } else {
                        System.out.printf(" %c  ", input.contains(i*Size+l)? 'X' : ' ');
                    }
                }

            }
            System.out.println();
            System.out.print("    -");
            for(int l = 0; l < Size; ++l) {
                int pindex = i*Size+l;
                Edge downEdge = graph[pindex][down];
                if(!downEdge.deleted){ System.out.print("----"); }
                else { System.out.print("    "); }
            }
            System.out.println();
        }
    }

    public static void union(int i, int j){
        int ri = height[i];
        int rj = height[j];
        if (ri < rj) {
            Up[i] = j;
        } if (ri > rj) {
            Up[j] = i;
        } else { // ri == rj
            height[j]++; Up[j] = i;
        }
    }

    public static int find(int i) {
        int r = i;
        while (Up[r] != -1) //find root
            r = Up[r];
        if (i != r) { //compress path//
            int k = Up[i];
            while (k != r) {
                Up[i] = r;
                i = k;
                k = Up[k];
            }
        }
        return r;
    }

    public static void generateMaze(){
        while(unusedEdges.size()>0){
            int r = randomGenerator.nextInt(unusedEdges.size());
            Edge myEdge = unusedEdges.get(r);
            Point myPoint = myEdge.point;
            int row1 = myPoint.x;
            int col1 = myPoint.y;
            if(myEdge.direction==right){
                int row2 = row1;
                int col2 = col1 + 1;

                int p = find(row1*Size+col1);
                int q = find(row2*Size+col2);

                if(p!=q){
                    union(p,q);
                    myEdge.deleted = true;

                }
            }
            if(myEdge.direction==down){
                int row2 = row1 + 1;
                int col2 = col1;

                int p = find(row1*Size+col1);
                int q = find(row2*Size+col2);

                if(p!=q){
                    union(p,q);
                    myEdge.deleted = true;

                }
            }
            unusedEdges.remove(myEdge);
            //unusedEdges.remove(myEdge);
            //System.out.println(unusedEdges.size());
            //myBoard();
        }
    }

    public static boolean traverse(ArrayList<Integer> solve, int x, int y) {
        // x is column
        // y is row
        int i = x * Size + y;
        if (i == N - 1) {
            solve.add(i);
            return true;
        }
        for (int e = 0; e < 4; e++) {
            Edge myEdge = graph[i][e];
            if (myEdge.deleted && !myEdge.used) {
                myEdge.used = true;
                int row = x;
                int col = y;
                if(e==right){
                    col++;
                }
                else if(e==down){
                    row++;
                }
                else if(e==left){
                    col--;
                }
                else { //myEdge.direction == up
                    row--;
                }
                if(traverse(solve,row,col)){
                    solve.add(x*Size+y);
                    return true;
                }
            }
        }
        return false;
    }

    public static void size10Maze(){
        Size = 10;
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.used = true;

        // Create board and graph.
        board = new Point[Size][Size];
        N = Size*Size;  // number of points
        graph = new Edge[N][4];

        Up = new int[N];
        for(int i = 0; i < N; i++){
            Up[i] = -1;
        }

        height = new int[N];
        for(int i = 0; i < N; i++){
            height[i] = 0;
        }

        randomGenerator = new Random();

        unusedEdges = new ArrayList<>();
        S = new ArrayList<>();

        for (int i = 0; i < Size; ++i)
            for (int j = 0; j < Size; ++j) {
                Point p = new Point(i, j);
                int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j

                board[i][j] = p;

                graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
                graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;
                graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;
                graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;

                if(j < Size-1){
                    unusedEdges.add(graph[pindex][right]);
                }
                if(i < Size-1){
                    unusedEdges.add(graph[pindex][down]);
                }
            }
        System.out.println("10 x 10 Maze:");
        //displayInitBoard();
        generateMaze();
        traverse(S,0,0);
        myBoard(S);
    }

    public static void size20Maze(){
        Size = 20;
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.used = true;

        // Create board and graph.
        board = new Point[Size][Size];
        N = Size*Size;  // number of points
        graph = new Edge[N][4];

        Up = new int[N];
        for(int i = 0; i < N; i++){
            Up[i] = -1;
        }

        height = new int[N];
        for(int i = 0; i < N; i++){
            height[i] = 0;
        }

        randomGenerator = new Random();

        unusedEdges = new ArrayList<>();
        S = new ArrayList<>();

        for (int i = 0; i < Size; ++i)
            for (int j = 0; j < Size; ++j) {
                Point p = new Point(i, j);
                int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j

                board[i][j] = p;

                graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
                graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;
                graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;
                graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;

                if(j < Size-1){
                    unusedEdges.add(graph[pindex][right]);
                }
                if(i < Size-1){
                    unusedEdges.add(graph[pindex][down]);
                }
            }
        System.out.println("20 x 20 Maze:");
        //displayInitBoard();
        generateMaze();
        traverse(S,0,0);
        myBoard(S);
    }

    public static void size30Maze(){
        Size = 30;
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.used = true;

        // Create board and graph.
        board = new Point[Size][Size];
        N = Size*Size;  // number of points
        graph = new Edge[N][4];

        Up = new int[N];
        for(int i = 0; i < N; i++){
            Up[i] = -1;
        }

        height = new int[N];
        for(int i = 0; i < N; i++){
            height[i] = 0;
        }

        randomGenerator = new Random();

        unusedEdges = new ArrayList<>();
        S = new ArrayList<>();

        for (int i = 0; i < Size; ++i)
            for (int j = 0; j < Size; ++j) {
                Point p = new Point(i, j);
                int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j

                board[i][j] = p;

                graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
                graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;
                graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;
                graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;

                if(j < Size-1){
                    unusedEdges.add(graph[pindex][right]);
                }
                if(i < Size-1){
                    unusedEdges.add(graph[pindex][down]);
                }
            }

        //displayInitBoard();
        System.out.println("30 x 30 Maze:");
        generateMaze();
        traverse(S,0,0);
        myBoard(S);
    }

    public static void main(String[] args) {
        size10Maze();
        size20Maze();
        size30Maze();

        System.out.println("See above for 10x10, 20x20, and 30x30 mazes.");

        // Read in the Size of a maze
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("What's the size of your maze? ");
            Size = scan.nextInt();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        scan.close();


        // Create one dummy edge for all boundary edges.
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.used = true;

        // Create board and graph.
        board = new Point[Size][Size];
        N = Size*Size;  // number of points
        graph = new Edge[N][4];

        Up = new int[N];
        for(int i = 0; i < N; i++){
            Up[i] = -1;
        }

        height = new int[N];
        for(int i = 0; i < N; i++){
            height[i] = 0;
        }

        randomGenerator = new Random();

        unusedEdges = new ArrayList<>();
        S = new ArrayList<>();

        for (int i = 0; i < Size; ++i)
            for (int j = 0; j < Size; ++j) {
                Point p = new Point(i, j);
                int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j

                board[i][j] = p;

                graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
                graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;
                graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;
                graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;

                if(j < Size-1){
                    unusedEdges.add(graph[pindex][right]);
                }
                if(i < Size-1){
                    unusedEdges.add(graph[pindex][down]);
                }
            }

        //displayInitBoard();
        generateMaze();
        traverse(S,0,0);
        myBoard(S);

        // Hint: To randomly pick an edge in the maze, you may
        // randomly pick a point first, then randomly pick
        // a direction to get the edge associated with the point.
        //int i = randomGenerator.nextInt(N);
        //System.out.println("\nA random number between 0 and " + (N-1) + ": " + i);

    }
}
