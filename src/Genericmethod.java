public class Genericmethod {
    public static void main(String[] args) {
        Integer[] iray = {1,2,3,4};
        Character[] cray = {'b','u','c','y'};
        printMe(iray);
        printMe(cray);
    }
//Generic method
    public static <T> void printMe(T[] input) {
        for (T x: input) {
            System.out.printf("%s", x);
            System.out.println();
        }
    }
}
