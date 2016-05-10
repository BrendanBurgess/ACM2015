import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by brendan on 4/30/16.
 */
public class Cheese {

    public static Hole[] holes;
    public static double slices;
    public static final double ERROR = .001;

    private static class Hole implements Comparable<Hole>{
        public int x;
        public int y;
        public int z;
        public int r;

        public Hole(int x, int y, int z, int r){
            this.x = x;
            this.y = y;
            this.z = z;
            this.r = r;
        }

        public int compareTo(Hole other){
            return (this.x - this.r) - (other.x - other.r);
        }

        public boolean contains(int sliceX){
            if(sliceX > (x -r) && sliceX < (x+r))
                return true;
            return false;
        }
    }

    public static double modBS(double lowerBound, double sliceVolume){
        //System.out.println("lo = " + lowerBound);
        double hi = 100 *1000;
        double lo = lowerBound;
        while(hi > lo){
            double local = (hi + lo) / 2.0;
            //System.out.println("local = " + local);
            double volDiff = sliceVolume - findVolume(lowerBound, local, sliceVolume);
            //System.out.println("Slice Vol = " + volDiff);
            if(Math.abs(volDiff) < ERROR){
                //System.out.println("returned " + local);
                return local;
            }
            if(volDiff < 0){
                hi = local;
            } else {
                lo = local;
            }
        }

        return  -1;
    }

    public static double sphereCap(double r, double d, double h){
        //formula I found online is pih(R^2-d^2-hd-1/3h^2).
        double inside = Math.pow(r,2) - Math.pow(d,2) - h*d - (1/3.0)*Math.pow(h,3);
        return Math.PI * h * inside;
    }

    public static double findVolume(double lower, double higher, double sliceVol){
        double startVolume = (higher - lower) * 100000 * 100000;
        System.out.println("startVolume = " + startVolume);
        for(Hole hole : holes){
            double leftmost = hole.x - hole.r;
            double rightmost = hole.x + hole.r;
            if(rightmost < lower)
                continue;
            if (leftmost > higher)
                break;

            // if fully contained
            if(leftmost >= lower && rightmost <= higher) {
                startVolume -= ((4 / 3.0) * Math.PI * Math.pow(hole.r, 3));
            } else if(leftmost < lower && rightmost <= higher){
                double d = hole.x - lower;
                double h = rightmost - lower;
                startVolume -= sphereCap(hole.r, d, h);
            }else if(leftmost >= lower && rightmost >  higher){
                double d = higher - hole.x;
                double h = higher - lower;
                startVolume -= sphereCap(hole.r, d, h);
            } else{
                startVolume -= sphereCap(hole.r, hole.x - lower, higher - lower);
            }

        }
        return startVolume;
    }


    public static void calcVolume(double sliceVolume){
        int slicesSliced = 0;
        double blockSliced = 0;
        while (slicesSliced < slices -1){
            double temp = modBS(blockSliced, sliceVolume);
            System.out.printf("%.6f", (temp - blockSliced) / 1000);
            System.out.println();
            blockSliced = temp;
            slicesSliced ++;
        }
        System.out.printf("%.6f", (100 * 1000 - blockSliced) / 1000);
        System.out.println();
    }


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int holesNum = sc.nextInt();
            slices = sc.nextInt();
            double volume = 0;
            holes = new Hole[holesNum];

            for (int hole = 0; hole < holesNum; hole++) {
                int r = sc.nextInt();
                int x = sc.nextInt();
                int y = sc.nextInt();
                int z = sc.nextInt();

                holes[hole] = new Hole(x, y, z, r);
                volume += (4 / 3.0) * Math.PI * Math.pow(r, 3);
            }

            Arrays.sort(holes);
            double totalVolume = Math.pow(100 * 1000, 3) - volume;
            double volPerSlice = totalVolume / slices;
            System.out.println("Slice vol = " + volPerSlice);
            calcVolume(volPerSlice);
        }
    }
}
