
import javax.swing.*;
import java.util.*;

/**
 * Created by brendan on 5/1/16.
 */
public class Key {

    public static HashMap<Character, ArrayList<Integer>> locations;
    public static int[][] aMatrix;

    public static int[][] Floyd(int[][] map, int keys){
        for(int i = 0; i < keys; i ++){
            for(int j = 0; j < keys; j++){
                for(int k = 0; k < keys; k++){
                    if(map[j][i] == Integer.MAX_VALUE || map[i][k] == Integer.MAX_VALUE )
                        continue;
                    int intermediate = map[j][i] + map[i][k];
                    map[j][k] = Math.min(map[j][k], intermediate);
                }
            }
        }
        return map;
    }


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while( sc.hasNext()){
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            int keys = cols*rows;

            char[][] keyboard = new char[rows][cols];
            locations = new HashMap<>();

            long startTime = System.currentTimeMillis();
            for( int row = 0; row < rows; row++) {
                String curRow = sc.next();
                char[] chars = curRow.toCharArray();
                for (int col = 0; col < cols; col++) {
                    keyboard[row][col] = chars[col];
                    ArrayList<Integer> currLocations;
                    if (locations.containsKey(chars[col]))
                        currLocations = locations.get(chars[col]);
                    else
                        currLocations = new ArrayList<>();
                    currLocations.add(row * cols + col);
                    locations.put(chars[col], currLocations);
                }
            }
            System.out.println("Elapsed time for first constructions = " + (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();

            aMatrix = new int[keys][keys];
            for (int i = 0; i < keys; i++){
                for( int j = 0; j < keys; j++){
                    if(i == j)
                        aMatrix[i][j] = 0;
                    else
                        aMatrix[i][j] = Integer.MAX_VALUE;
                }
            }


            for( int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // check down
                    int row2 = row;
                    while(row2 >= 0){
                        if(keyboard[row][col] != keyboard[row2][col]){
                            aMatrix[row* cols + col][row2*cols + col] = 1;
                            break;
                        }
                        row2 --;
                    }
                    // check up
                    row2 = row;
                    while(row2 < rows){
                        if(keyboard[row][col] != keyboard[row2][col]){
                            aMatrix[row *cols + col][row2*cols + col] = 1;
                            break;
                        }
                        row2 ++;
                    }

                    //check left
                    int col2 = col;
                    while(col2 >= 0){
                        if(keyboard[row][col] != keyboard[row][col2]){
                            aMatrix[row* cols + col][row*cols + col2] = 1;
                            break;
                        }
                        col2--;
                    }

                    //check right
                    col2 = col;
                    while(col2 < cols){
                        if(keyboard[row][col] != keyboard[row][col2]){
                            aMatrix[row* cols + col][row*cols + col2] = 1;
                            break;
                        }
                        col2++;
                    }
                }
            }
            System.out.println("Elapsed time for Adjacency Construct = " + (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();



            String message = sc.next();
            aMatrix = Floyd(aMatrix, keys);

            System.out.println("Elapsed time After Floyd = " + (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();



            //System.out.println(message(message));
            message = message + "*";
            char[] messChars = message.toCharArray();

            int[][] newAMatrix = new int[keys][messChars.length];

            for (int i = 0; i < keys; i++){
                for( int j = 0; j < messChars.length; j++){
                        newAMatrix[i][j] = Integer.MAX_VALUE;
                }
            }


            for(int start: locations.get(messChars[0])){
                if(aMatrix[0][start] == Integer.MAX_VALUE)
                    continue;
                newAMatrix[start][0] = aMatrix[0][start];
            }


            for(int prob = 0; prob + 1 < messChars.length; prob++){
                for(int lett = 0; lett < keys; lett++){
                    if(newAMatrix[lett][prob] == Integer.MAX_VALUE)
                        continue;
                    ArrayList<Integer> poss = locations.get(messChars[prob + 1]);
                    for(int car : poss){
                        if(aMatrix[lett][car] == Integer.MAX_VALUE)
                            continue;
                        newAMatrix[car][prob+ 1] = Math.min(newAMatrix[car][prob+ 1], aMatrix[lett][car] + newAMatrix[lett][prob]);
                    }
                }
            }

            int min = Integer.MAX_VALUE;
            for(int finish: locations.get('*')){

                min = Math.min(min, newAMatrix[finish][messChars.length -1]);
            }

            System.out.println("Elapsed time after TSPer= " + (System.currentTimeMillis() - startTime));

            System.out.println(min + messChars.length);
        }
    }
}
