package flik;

import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {

    @Test
    public void testIntegerLibrary() {

        /** Test Integer class behavior when assigning 0~500
         *  Detects the problem when i = 128, the address of a & b are different.
         */
        for (int i = 0; i < 501; i++) {
            Integer a = i;
            Integer b = i;
            System.out.println(a + " " + b + " " +System.identityHashCode(a) + " " + System.identityHashCode(b));
            assertEquals(System.identityHashCode(a), System.identityHashCode(b));
        }
    }

}
