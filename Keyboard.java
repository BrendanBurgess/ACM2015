
import java.util.*;

/**
 * Created by brendan on 5/1/16.
 */
public class Keyboard {

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

    public static int message(String mess){
        //System.out.println("Origional message = " + mess + "*");
        return message(mess + "*", 0);
    }

    public static int message(String remMess, int lastChar){
        char currChar = remMess.charAt(0);
        //System.out.println("currChar = " + remMess);
        //check base case
        if(currChar == '*'){
            ArrayList<Integer> choices = locations.get(currChar);
            int min = 100000;
            for( int choice : choices){
                int possDistance = aMatrix[lastChar][choice];
                if( possDistance < min)
                    min = possDistance;
            }
            return min + 1;
        }

        // normal case
        String newString = remMess.substring(1);
        ArrayList<Integer> choices = locations.get(currChar);
        int min = 100000;
        for( int choice : choices){
            if(aMatrix[lastChar][choice] == 1000000)
                continue;
            int possDistance = aMatrix[lastChar][choice] + message(newString, choice);
            if( possDistance < min)
                min = possDistance;
        }
        return min + 1;
    }
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while( sc.hasNext()){
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            int keys = cols*rows;

            char[][] keyboard = new char[rows][cols];
            locations = new HashMap<>();

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

            /*
            for( int row = 0; row < rows; row++) {
                System.out.println(Arrays.toString(keyboard[row]));
            }
            */

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

/*
            // testing Hash Table
            for(char car : locations.keySet()){
                System.out.println("For " + car + " the following locations are present");
                ArrayList<Integer> locals = locations.get(car);
                for (int local : locals){
                    System.out.println(local);
                }
            }
            // Testing Adjacency Matrix
            for(int row = 0; row < keys; row++){
                System.out.println(Arrays.toString(aMatrix[row]));
            }
*/


            String message = sc.next();
            aMatrix = Floyd(aMatrix, keys);



            //System.out.println(message(message));
            message = message + "*";
            char[] messChars = message.toCharArray();
            HashMap<Character, ArrayList<Integer>>  newLocations= new HashMap<>();

            //off by one
            int nessChars = 1;
            for(char car : messChars){
                //System.out.println("current char = " + car);
                ArrayList<Integer> neededChar = locations.get(car);

                ArrayList<Integer> updtCharLocals;
                if(!newLocations.containsKey(car)) {
                    updtCharLocals = new ArrayList<>();
                }else{
                    updtCharLocals = newLocations.get(car);
                }


                for(Integer i: neededChar){
                    updtCharLocals.add(nessChars);
                    nessChars++;
                }
                //System.out.println(updtCharLocals.toString());
                newLocations.put(car, updtCharLocals);
            }

            int[][] newAMatrix = new int[nessChars][nessChars];

            for (int i = 0; i < nessChars; i++){
                for( int j = 0; j < nessChars; j++){
                    if(i == j)
                        newAMatrix[i][j] = 0;
                    else
                        newAMatrix[i][j] = Integer.MAX_VALUE;
                }
            }

            ArrayList<Integer> firstChar = newLocations.get(messChars[0]);
            ArrayList<Integer> firstOrgLocations = locations.get(messChars[0]);
            int counter = 0;
            for (int i: firstOrgLocations){
                if(aMatrix[0][i] == Integer.MAX_VALUE)
                    continue;
                newAMatrix[0][firstChar.get(counter)] = aMatrix[0][i];
                counter++;
            }

            //System.out.println("FirstLine = " + nessChars);

            for(int currChar = 0; currChar + 1 < messChars.length; currChar++){
                firstChar = newLocations.get(messChars[currChar]);
                firstOrgLocations = locations.get(messChars[currChar]);
                ArrayList<Integer> secChar = newLocations.get(messChars[currChar + 1]);
                ArrayList<Integer> secOrgLocations = locations.get(messChars[currChar + 1]);
                for(int startChar : firstOrgLocations){
                    int secCount = 0;
                    for(int stopChar : secOrgLocations){
                        if(aMatrix[startChar][stopChar] == Integer.MAX_VALUE){
                            secCount++;
                            continue;
                        }
                        newAMatrix[firstChar.get(0)][secChar.get(secCount)] = aMatrix[startChar][stopChar];
                        secCount++;
                    }
                    firstChar.remove(0);
                }
                newLocations.put(messChars[currChar], firstChar);
            }

            /*
            for(int row = 0; row < nessChars; row++){
                System.out.println(Arrays.toString(newAMatrix[row]));
            }

            Floyd(newAMatrix, nessChars);

            System.out.println();
            System.out.println("After Floyd");
            System.out.println();

            for(int row = 0; row < nessChars; row++){
                System.out.println(Arrays.toString(newAMatrix[row]));
            } */

            Floyd(newAMatrix, nessChars);


            int min = Integer.MAX_VALUE;
            for (int finalChar: newLocations.get('*')){
                min = Math.min(min, newAMatrix[0][finalChar]);
            }
            System.out.println(min + messChars.length);
        }
    }
}
