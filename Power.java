import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by brendan on 4/30/16.
 */
public class Power {

    public static int consider(int[] batteries, int n, int k){
        int lo = 0;
        int hi = batteries[batteries.length -1];
        int mid = -1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            mid = lo + (hi - lo) / 2;
            if      (validate(batteries, mid, n, k)) hi = mid - 1;
            else if (!validate(batteries, mid, n, k)) lo = mid + 1;
            else return mid;
        }
        if(!validate(batteries, mid, n, k))
            mid++;
        return mid;
    }

    public static boolean validate(int[] batteries, int maxDif, int n, int k){
        int startDiff = batteries[1] - batteries[0];
        if (startDiff > maxDif)
            return false;

        int necessaryMatchs = n -1;

        int buffer = 2*k-2;
        if(2*n*k > 2) {
            for (int i = 3; i < batteries.length; i++) {
                if( buffer < 0)
                    return false;
                int currDiff = batteries[i] - batteries[i -1];
                if(currDiff > maxDif){
                    buffer --;
                    continue;
                }
                necessaryMatchs --;
                if (necessaryMatchs == 0)
                    break;
                i++;
                buffer += 2*k -2;
            }

            if( necessaryMatchs > 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            int n = sc.nextInt();
            int k = sc.nextInt();
            int[] batteries = new int[2*n*k];
            for( int batt = 0; batt < 2*n*k; batt ++){
                batteries[batt] = sc.nextInt();
            }

            Arrays.sort(batteries);

            System.out.println(consider(batteries, n, k));
        }
    }
}
