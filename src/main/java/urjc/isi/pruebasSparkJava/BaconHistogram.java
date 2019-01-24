package urjc.isi.pruebasSparkJava;

/******************************************************************************
 *  Compilation:  javac BaconHistogram.java
 *  Execution:    java BaconHistogram input.txt
 *  Dependencies: Graph.java In.java PathFinder.java
 *  
 *  Reads in a data file containing movie records (a movie followed by a list 
 *  of actors appearing in that movie), and runs breadth first search to
 *  find the shortest distance from the source (Kevin Bacon) to each other
 *  actor and movie. After computing the Kevin Bacon numbers, the programs
 *  prints a histogram of the number of actors with each Kevin Bacon number.
 * 
 *  % java Bacon cast.txt
 *    0        1
 *    1     2083
 *    2   187072
 *    3   515582
 *    4   113741
 *    5     8269
 *    6      772
 *    7       93
 *    8        7
 *  Inf    28942
 *
 *  Remark: the full cast.txt file may take several minutes.
 *
 ******************************************************************************/

public class BaconHistogram {
    public static void main(String[] args) {

        // read in data and initialize graph
        String filename = args[0];
        Graph G = new Graph(filename, "/");
        StdOut.println("Done reading movies and building graph");
        StdOut.println("vertices = " + G.V());
        StdOut.println("edges = " + G.E());

        // run breadth first search
        String s = "Bacon, Kevin";
        PathFinder finder = new PathFinder(G, s);
        StdOut.println("Done BFS");

        // compute histogram of Kevin Bacon numbers - 100 for infinity
        int MAX_BACON = 100;
        int[] hist = new int[MAX_BACON + 1];
        for (String actor : G.vertices()) {
            if (finder.distanceTo(actor) % 2 != 0) continue;  // it's a movie vertex

            int bacon = Math.min(MAX_BACON, finder.distanceTo(actor) / 2);
            hist[bacon]++;
            if (bacon >= 7 && bacon < MAX_BACON)
                StdOut.printf("%d %s\n", bacon, actor);
        }

        // print out histogram
        for (int i = 0; i < MAX_BACON; i++) {
            if (hist[i] == 0) break;
            StdOut.printf("%3d %8d\n", i, hist[i]);
        }
        StdOut.printf("Inf %8d\n", hist[MAX_BACON]);
    }
}
