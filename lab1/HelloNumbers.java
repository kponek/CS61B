public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        int total = 0;
        while (x < 10) {
        	total += x;
            System.out.print(total + " ");
            x = x + 1;
        }
        System.out.println();
    }
}