package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE

        // Create ALists for adding & resizing, N for largest size, powerCount for marking different sizes
        AList<Integer> arrayList = new AList<>();
        int N = 128000;
        int powerCount = 0;

        // Create ALists for tracing size of element, runtimes, operation counts
        AList<Integer> sizes = new AList<>();
        AList<Double> runtimes = new AList<>();
        AList<Integer> opCounts = new AList<>();

        // Start stopwatch for acquiring the running time
        Stopwatch sw = new Stopwatch();

        /** Iterate over 128000 times to acquire the running time.
         *  When N = 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000
         */
        for (int i = 0; i < N; i++) {
            arrayList.addLast(0);
            if (i == Math.pow(2, powerCount) * 1000 - 1) {

                // Fetch runtime (current time - stopwatch start time)
                double timeInSeconds = sw.elapsedTime();

                // Add tracing sizes
                sizes.addLast(i + 1);

                // Add tracing runtimes
                runtimes.addLast(timeInSeconds);

                // Add tracing operation counts
                opCounts.addLast(i + 1);

                powerCount ++;
            }
        }
        printTimingTable(sizes, runtimes, opCounts);
    }
}
