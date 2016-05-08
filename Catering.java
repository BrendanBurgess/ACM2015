import java.util.Scanner;

/**
 * Created by brendan on 5/5/16.
 */
public class Catering {

    private static final int inf = 1000000;

    public void input(){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            int n = sc.nextInt();
            int k = sc.nextInt();
            int numOfNodes = 2 + n + (n-1) + k;
            int[][] aMatrix = new int[numOfNodes][numOfNodes];

            for(int i = 0; i < numOfNodes; i ++){
                for(int j = 0; j < numOfNodes; j++){
                    aMatrix[i][j] = inf;
                }
            }

            


        }

    }

    public static void main(String[] args){
        Catering cat = new Catering();
        cat.input();
    }
}
