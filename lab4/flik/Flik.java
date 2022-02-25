package flik;

/** An Integer tester created by Flik Enterprises.
 * @author Josh Hug
 * */
public class Flik {
    /** @param a Value 1
     *  @param b Value 2
     *  @return Whether a and b are the same */

    /** When assigning an "int" to an "Integer" box, it will automatically  call Integer.valueOf (autoboxing).
     *  Integer class can only return int when it's in cache range (-128 ~ 127).
     *  Otherwise, if exceed the limits, Integer class will return new Integer class object.
     *
     *  2 ways to fix:
     *  1. Change input class from "Integer" to "int"
     *  2. Use equals method (in Integer class) to compare not the address but the content.
     */
    public static boolean isSameNumber(Integer a, Integer b) {
        return a.equals(b);
    }
}
