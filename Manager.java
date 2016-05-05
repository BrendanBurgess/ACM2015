
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by brendan on 5/2/16.
 */
public class Manager {
    private ArrayList<WindowRep> winds;
    private int MAX_X;
    private int MAX_Y;


    private  class WindowRep implements Comparable<WindowRep>{
        public int x;
        public int y;
        public int w;
        public int h;

        public WindowRep(int x, int y, int w, int h){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public int compareTo(WindowRep other){
            if( this.x < other.x)
                return -1;
            else if(this.x > other.x)
                return 1;
            else
                return this.y - other.y;
        }

        public boolean contains(int x, int y){
            if(x >= this.x && x <= this.x + this.w){
                if (y >= this.y && y <= this.y + this.h)
                    return true;
            }
            return false;
        }

        public boolean overlap(WindowRep other){
            //add mids?
            if(contains(other.x, other.y))
                return true;
            if(contains(other.x + other.w, other.y))
                return true;
            if(contains(other.x + other.w, other.y + other.h))
                return true;
            if (contains(other.x, other.y + other.h))
                return true;
            if(other.contains(x,y) || other.contains(x + w, y))
                return true;
            if(other.contains(x+ w, y +h)|| other.contains(x + w, y))
                return true;
            return false;
        }
    }

    public  void Open( int x, int y, int w, int h){
        WindowRep poss = new WindowRep(x, w, y, h);
        for(WindowRep rep: winds){
            if(rep.overlap(poss)) {
                System.out.println("Open - window does not fit");
                return;
            }
        }
        winds.add(poss);
        Collections.sort(winds);
    }

    public  void Close( int x, int y){
        for(WindowRep rep : winds){
            if(rep.contains(x, y)) {
                winds.remove(rep);
                return;
            }
        }
        System.out.println("CLOSE - no window at given position");
    }

    public void Resize( int x, int y, int w, int h){
        WindowRep canidate = null;
        for(WindowRep rep : winds){
            if(rep.contains(x, y)) {
                canidate = rep;
                winds.remove(rep);
            }
        }

        if(canidate == null) {
            System.out.println("RESIZE - no window at given position");
            return;
        }

        WindowRep newCanidate = new WindowRep(canidate.x, canidate.y, w, h);

        boolean ovelap = false;
        for(WindowRep rep : winds){
            if(rep.overlap(newCanidate)) {
                ovelap = true;
            }
        }

        if( ovelap){
            winds.add(canidate);
            System.out.println("RESIZE - window does not fit");
            return;
        }

        winds.add(canidate);
    }

    public void Move( int x, int y, int dx, int dy){

    }

    public int moveDist(WindowRep wind, int dx, int dy){
        WindowRep moveSpace;  //virtual rectange represnting movespace;
        boolean neg = false;
        int newMove
        if(dx > 0) {
            newMove = dx;
            if(wind.x + wind.w + dx > MAX_X)
                newMove = MAX_X - (wind.x + wind.w);
            moveSpace = new WindowRep(wind.x + wind.w, wind.y, newMove, wind.h);
        }else if(dx < 0) {
            neg = true;
            newMove = dx;
            if(wind.x - dx < 0)
                newMove = wind.x;
            moveSpace = new WindowRep(wind.x - newMove, wind.y, newMove, wind.h);
        }else if(dy > 0) {
            newMove = dy;
            if(wind.y + wind.h + dy > MAX_Y)
                newMove = MAX_Y - wind.y;
            moveSpace = new WindowRep(wind.x, wind.y + wind.h, wind.w, newMove);
        }else {
            neg = true;
            newMove = dy;
            if(wind.y - dy < 0)
                newMove = wind.y;

            moveSpace = new WindowRep(wind.x, wind.y - newMove, wind.w, newMove);
        }

        if(neg) {

            for (WindowRep possWind : winds) {
                if
            }
        } else {
            for (WindowRep possWind)
        }


    }


    public void input(){
        Scanner sc = new Scanner(System.in);
        MAX_X = sc.nextInt();
        MAX_Y = sc.nextInt();
        winds = new ArrayList<>();
        while (sc.hasNext()){
            String command = sc.next();

            if (command.equals("OPEN")){
                Open(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
            } else if(command.equals("CLOSE")){
                Close(sc.nextInt(), sc.nextInt());
            } else if(command.equals("RESIZE")){
                Resize(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
            } else if(command.equals("MOVE")){
                Move(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
            }

        }
    }

    public static void main(String[] args){
        Manager man = new Manager();
        man.input();
    }
}
