

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by brendan on 5/3/16.
 */
public class Ships {

    public LinkedList<Interval> blockedTimes;
    private class Interval implements Comparable<Interval>{
        public double start;
        public double finish;
        public Interval(double start, double finish){
            this.start = start;
            this.finish = finish;
        }

        public int compareTo(Interval other){  // potentially needs some work
            if (this.start < other.start)
                return -1;
            return 1;
        }
    }


    public void insert(double intervalStart, double intervalFinish){
        blockedTimes.add(new Interval(intervalStart, intervalFinish));
    }

    public void determine(int T0, int TF){
        // to Array
        int startSize = blockedTimes.size();
        Interval[] sortTimes = new Interval[startSize];
        for(int i = 0; i < startSize; i++){
            sortTimes[i]= blockedTimes.poll();
        }
        Arrays.sort(sortTimes);

        //if no ships
        if(sortTimes.length== 0) {
            System.out.println(TF - T0);
            return;
        }

        //else
        double maxGap = sortTimes[0].start - T0;
        double maxTime = sortTimes[0].finish;
        for(int blockedTime = 0; blockedTime + 1 < sortTimes.length; blockedTime++){
            if(sortTimes[blockedTime+1].start > maxTime) {
                maxGap = Math.max(maxGap, sortTimes[blockedTime + 1].start - maxTime);
                maxTime = sortTimes[blockedTime + 1].finish; //check out
            } else {
                maxTime = Math.max(maxTime, sortTimes[blockedTime+1].finish);
            }

        }

        maxGap = Math.max(maxGap, TF - maxTime);
       // System.out.println("MAx Gap  = " + maxGap);
        System.out.printf("%.6f", maxGap);
        System.out.println();

    }

    public void input(){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            int lanes = sc.nextInt();
            int width = sc.nextInt();
            int sSpeed = sc.nextInt();
            int fSpeed = sc.nextInt();
            int sT = sc.nextInt();
            int fT = sc.nextInt();
            blockedTimes = new LinkedList<>();
            //blockedTimes.add(new Interval(fT - (width/(double)fSpeed), (double) fT) );
            for(int lane = 0; lane < lanes; lane++){
                if(sc.next().equals("E")){
                    int ships = sc.nextInt();
                    for(int ship = 0; ship < ships; ship++) {
                        int length = sc.nextInt();
                        int position = sc.nextInt() * -1;
                        double intvStart = (position / (double) sSpeed) - ((lane + 1) * width / (double) fSpeed);
                        double intvFinish = intvStart + (length / (double) sSpeed + (width/ (double) fSpeed));
                        //System.out.println("Start " + intvStart);
                        //System.out.println("Start " + intvFinish);
                        if(intvStart > fT || intvFinish < sT)
                            continue;
                        intvStart = Math.max(intvStart, sT);
                        intvFinish = Math.min(intvFinish, fT);
                        insert(intvStart, intvFinish);
                    }
                } else {
                    int ships = sc.nextInt();
                    for(int ship = 0; ship < ships; ship++) {
                        double length = sc.nextInt();
                        int position = sc.nextInt();
                        double intvStart = (position / (double) sSpeed) - ((lane + 1) * width / (double) fSpeed);
                        double intvFinish = intvStart + (length / (double) sSpeed + (width/ (double) fSpeed));
                        if(intvStart > fT || intvFinish < sT)
                            continue;
                        intvStart = Math.max(intvStart, sT);
                        intvFinish = Math.min(intvFinish, fT);
                        insert(intvStart, intvFinish);
                    }
                }
            }
            //System.out.println("reached deterimine");
            determine(sT,fT);

        }

    }

    public static void main(String[] args){
        Ships ship = new Ships();
        ship.input();
    }
}
