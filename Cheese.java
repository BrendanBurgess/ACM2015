import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by brendan on 4/30/16.
 */
public class Cheese {

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

    public int computeSlice(int slices, double volPerSlice){

    }


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int holesNum = sc.nextInt();
            double slices = sc.nextInt();
            double volume = 0;
            Hole[] holes = new Hole[holesNum];

            for (int hole = 0; hole < holesNum; hole++) {
                int r = sc.nextInt();
                int x = sc.nextInt();
                int y = sc.nextInt();
                int z = sc.nextInt();

                holes[hole] = new Hole(x, y, z, r);
                volume += (4 / 3.0) * Math.PI * Math.pow(r, 3);
            }

            volume = volume / Math.pow(1000, 3);
            double totalVolume = Math.pow(100, 3) - volume;
            double volPerSlice = totalVolume / slices;
            System.out.println("Volume per slice = " + volPerSlice);
        }
    }
}
