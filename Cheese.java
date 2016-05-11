import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Created by brendan on 4/30/16.
 */
public class Cheese {

    public static Hole[] holes;
    public static double slices;

    private static class Hole implements Comparable<Hole>{
        public int z;
        public int r;

        public Hole(int z, int r){
            this.z = z;
            this.r = r;
        }

        public int compareTo(Hole other){
            return (this.z - this.r) - (other.z - other.r);
        }

    }

    public static double modBS(double lowerBound, double sliceVolume){
        //System.out.println("lo = " + lowerBound);
        double hi = 100 *1000;
        double lo = lowerBound;
        while(hi > lo){
            double mid = (hi + lo) / 2.0;
            //System.out.println("local = " + mid);
            double volDiff = sliceVolume - findVolume(lowerBound, mid, sliceVolume);
            //System.out.println("Vol Diff = " + volDiff);
            if(Math.abs(volDiff) < 1000){
                //System.out.println("returned " + local);
                return mid;
            }
            if(volDiff < 0){
                hi = mid;
            } else {
                lo = mid;
            }
        }

        return  -1;
    }

    public static double sphereCap(double r, double h){
        double inside = (3*r) - h;
        return (1.0/3.0)*Math.PI*Math.pow(h,2) * inside;
    }

    public static double findVolume(double lower, double higher, double sliceVol){
        double startVolume = (higher - lower) * 100000 * 100000;
        //System.out.println("startVolume = " + startVolume);
        //System.out.println("slice volume = " + sliceVol);
        //System.out.println("upper = " + higher);
        for(Hole hole : holes){
            double leftmost = hole.z - hole.r;
            double rightmost = hole.z + hole.r;
            if(rightmost < lower)
                continue;
            if (leftmost > higher)
                break;

            // if fully contained
            if(leftmost >= lower && rightmost <= higher) {
                //System.out.println("Fully");
                startVolume -= ((4.0 / 3.0) * Math.PI * Math.pow(hole.r, 3.0));
            } else if(leftmost < lower && rightmost <= higher){
                //System.out.println("leftbound");
                double h = rightmost - lower;
                startVolume -= sphereCap(hole.r, h);
            }else if(leftmost >= lower && rightmost >  higher){
                //System.out.println("right");
                //System.out.println(hole.r);
                double h = higher - leftmost;
                startVolume = startVolume - sphereCap(hole.r, h);
            } else{
                //System.out.println("overlap");
                double HoleVol = ((4.0 / 3.0) * Math.PI * Math.pow(hole.r, 3.0));
                HoleVol -= sphereCap(hole.r, (hole.z + hole.r)- higher);
                HoleVol -= sphereCap(hole.r, lower - (hole.z - hole.r));
                startVolume -= HoleVol;
            }

        }
        //System.out.println("returned volume = " + startVolume);
        return startVolume;
    }


    public static void calcVolume(double sliceVolume){
        int slicesSliced = 0;
        double blockSliced = 0;
        while (slicesSliced < slices -1){
            double temp = modBS(blockSliced, sliceVolume);
            System.out.printf("%.9f", (temp - blockSliced) / 1000);
            System.out.println();
            blockSliced = temp;
            slicesSliced ++;
        }
        System.out.printf("%.9f", (100 * 1000 - blockSliced) / 1000);
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

                holes[hole] = new Hole(z, r);
                volume += (4.0 / 3.0) * Math.PI * Math.pow(r, 3.0);
            }

            Arrays.sort(holes);
            double totalVolume = Math.pow(100 * 1000, 3.0) - volume;
            double volPerSlice = totalVolume / slices;
            //System.out.println(totalVolume);
            //System.out.println("Slice vol = " + volPerSlice);
            calcVolume(volPerSlice);
        }
    }
}


