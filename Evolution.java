import java.util.*;

/**
 * Created by brendan on 5/4/16.
 */
public class Evolution {

    private class comparator implements Comparator<String> {
        public int compare(String One, String Two){
            return Integer.compare(One.length(), Two.length());
        }
    }

    public boolean isAnscestor(String ansc, String desc){
        int descChar = 0;
        int ansChar = 0;
        while (descChar < desc.length()){
            ansChar = ansc.indexOf(desc.charAt(descChar), ansChar);
            if(ansChar == -1)
                return false;
            ansChar++;
            descChar++;
        }
        return true;
    }

    public void findPath(String base, String[] subs){
        Stack<String> left = new Stack<>();
        Stack<String> right = new Stack<>();
        LinkedList<String> both = new LinkedList<>();
        String endOfLinkedList = "";
        left.push(base);
        right.push(base);
        for(int midpoint = subs.length - 1; midpoint >= 0; midpoint--){
            //System.out.println("currentsubstring = " + subs[midpoint]);
            if(isAnscestor(left.peek(), subs[midpoint]) && isAnscestor(right.peek(), subs[midpoint])){
                if(isAnscestor(endOfLinkedList, subs[midpoint]) || both.size() == 0) {
                    both.push(subs[midpoint]);
                    endOfLinkedList = subs[midpoint];
                }else{
                    left.push(subs[midpoint]);
                    while (both.size() > 0)
                        right.push(both.removeLast());
                }
            } else if(isAnscestor(left.peek(), subs[midpoint])){
                left.push(subs[midpoint]);
                while (both.size() > 0)
                    right.push(both.removeLast());
            } else if(isAnscestor(right.peek(), subs[midpoint])){
                right.push(subs[midpoint]);
                while (both.size() > 0)
                    left.push(both.removeLast());
            } else {
                System.out.println("impossible");
                return;
            }
        }

        if(both.size() > 0){
            while (both.size() > 0)
                left.push(both.removeLast());
        }

        //System.out.println(left.toString());
        //System.out.print(right.toString());

        System.out.print(left.size() -1);
        System.out.println(" " + (right.size() -1));
        while (left.size() > 1){
            System.out.println(left.pop());
        }

        while (right.size() > 1){
            System.out.println(right.pop());
        }
    }


    public void input(){
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            int substringsNum = sc.nextInt();
            String modernGenome = sc.next();
            String[] substrings = new String[substringsNum];
            for( int substring = 0; substring < substringsNum; substring++){
                substrings[substring] = sc.next();
            }
            Arrays.sort(substrings, new comparator());
            findPath(modernGenome, substrings);
        }

    }
    public static void main(String[] args){
        Evolution evo = new Evolution();
        //System.out.println(evo.isAnscestor("baab", "bcb"));
        evo.input();
    }
}
