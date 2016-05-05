import java.util.Scanner;

/**
 * Created by brendan on 4/27/16.
 */
public class Arti {

    public static double price(int p, int a, int b, int c, int d, int k){
        double firstInternal = (a * k + b) % (2 * Math.PI);
        double secInternal = (c * k + d) % (2 * Math.PI);
        return p * (Math.sin(firstInternal) + Math.cos(secInternal) + 2);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String lineVars = sc.nextLine();
            String[] vars = lineVars.split(" ");
            int p = Integer.parseInt(vars[0]);
            int a = Integer.parseInt(vars[1]);
            int b = Integer.parseInt(vars[2]);
            int c = Integer.parseInt(vars[3]);
            int d = Integer.parseInt(vars[4]);
            int n = Integer.parseInt(vars[5]);


            if( n == 1){
                System.out.println("0.00");
                continue;
            }

            double highestPrice = price(p, a, b, c, d, 1);
            double maxDecline = 0;
            for ( int k = 2; k <= n; k++){
                double currentPrice =  price(p, a, b, c, d, k);
                highestPrice = Math.max(highestPrice, currentPrice);
                maxDecline = Math.max(maxDecline, highestPrice - currentPrice);
            }
            System.out.printf("%.6f", maxDecline);
            System.out.println();

        }
    }
}
