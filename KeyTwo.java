
import javax.swing.*;
import java.util.*;

/**
 * Created by brendan on 5/1/16.
 */
public class KeyTwo {

    public static HashMap<Character, ArrayList<Integer>> locations;
    public static HashMap<Integer, LinkedList<Integer>> adjacents;

    public static int modBFS( char[][] keyboard, String mess){
        LinkedList<Integer> poss = new LinkedList<>();
        String message = mess + "*";
        boolean[][] seen = new boolean[message.length()][adjacents.size()];

        int startCounter = 0;
        while(message.charAt(startCounter) == keyboard[0][0]){
            startCounter ++;
            seen[startCounter][0] = true;
        }

        poss.add(startCounter);
        poss.add(0);
        poss.add(0);
        while (!poss.isEmpty()){
            int index = poss.remove();
            int flatValue = poss.remove();
            int cost = poss.remove();
            cost++;

            //System.out.println("Current node row = " + flatValue / keyboard[0].length);
            //System.out.println("Current node col = " + flatValue / keyboard[0].length);


            for(Integer adjacent: adjacents.get(flatValue)){
                //System.out.println("adjacent = " + adjacent);
                if(!seen[index][adjacent]){
                    seen[index][adjacent] = true;
                    int row = adjacent / keyboard[0].length;
                    int col = adjacent % keyboard[0].length;
                    if(keyboard[row][col] == message.charAt(index)){
                        int repeats = 0;
                        while (message.charAt(index + repeats) == keyboard[row][col]){
                            repeats++;
                            if(index + repeats == message.length())
                                return cost;
                        }
                        poss.add(index + repeats);
                        seen[index + repeats][adjacent] = true;
                    } else {
                        poss.add(index);
                    }
                    poss.add(adjacent);
                    poss.add(cost);
                }
            }

        }
        return Integer.MAX_VALUE;
    }


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while( sc.hasNext()){
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            int keys = cols*rows;

            char[][] keyboard = new char[rows][cols];
            locations = new HashMap<>();
            adjacents = new HashMap<>();

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


            for( int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // check down
                    LinkedList<Integer> connected = new LinkedList<>();
                    int row2 = row;
                    while(row2 >= 0){
                        if(keyboard[row][col] != keyboard[row2][col]){
                            connected.add(row2*cols + col);
                            break;
                        }
                        row2 --;
                    }
                    // check up
                    row2 = row;
                    while(row2 < rows){
                        if(keyboard[row][col] != keyboard[row2][col]){
                           connected.add(row2*cols + col);
                            break;
                        }
                        row2 ++;
                    }

                    //check left
                    int col2 = col;
                    while(col2 >= 0){
                        if(keyboard[row][col] != keyboard[row][col2]){
                            connected.add(row*cols + col2);
                            break;
                        }
                        col2--;
                    }

                    //check right
                    col2 = col;
                    while(col2 < cols){
                        if(keyboard[row][col] != keyboard[row][col2]){
                            connected.add(row*cols + col2);
                            break;
                        }
                        col2++;
                    }
                    adjacents.put(row*cols + col, connected);
                }
            }

            //System.out.println(adjacents.toString());
            String message = sc.next();
            System.out.println(modBFS(keyboard, message) + message.length() + 1);


        }
    }
}
