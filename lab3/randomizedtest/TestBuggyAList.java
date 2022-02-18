package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove() {
        // Create correct list & bug list for test
        AListNoResizing<Integer> correctList = new AListNoResizing<>();
        BuggyAList<Integer> bugList = new BuggyAList<>();

        /** Add 4, 5, 6 to correct list sequentially
         *  Add 4, 5, 6 to bug list sequentially
         */
        for (int i = 4; i < 7; i++) {
            correctList.addLast(i);
            bugList.addLast(i);

            // Test addLast method
            assertEquals(correctList.getLast(), bugList.getLast());
            assertEquals(correctList.size(), bugList.size());
        }

        for (int i = 0; i < 3; i++) {
            // Test removeLast method
            assertEquals(correctList.removeLast(), bugList.removeLast());
            assertEquals(correctList.size(), bugList.size());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correctList = new AListNoResizing<>();
        BuggyAList<Integer> bugList = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {

                // AddLast
                int randVal = StdRandom.uniform(0, 100);
                correctList.addLast(randVal);
                bugList.addLast(randVal);

                // Test addLast method
                assertEquals(correctList.getLast(), bugList.getLast());

            } else if (operationNumber == 1) {

                // Size
                int size = correctList.size();

                // Ensure list sizes are the same after addition or removal
                assertEquals(correctList.size(), bugList.size());

            } else {

                // Check if there is at least 1 item in both lists
                if (correctList.size() > 0 || bugList.size() > 0) {

                    // RemoveLast
                    int correctRemove = correctList.removeLast();
                    int bugRemove = bugList.removeLast();

                    // Test removeLast method
                    assertEquals(correctRemove, bugRemove);
                }
            }
        }
    }
}
