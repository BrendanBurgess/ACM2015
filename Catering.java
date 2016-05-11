import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by brendan on 5/5/16.
 */
public class Catering {

    private static final int inf = 10000000;
    private int[] parent;
    private int[] cost;

    public void input() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int requests = sc.nextInt();
            int teams = sc.nextInt();
            int numOfNodes = 2 + requests + (requests - 1) + teams;
            int[][] aMatrix = new int[numOfNodes][numOfNodes];

            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    aMatrix[i][j] = inf;
                }
            }

            //set weight on start nodes to 0
            for(int start = 1; start< teams +requests; start ++){
                aMatrix[0][start] = 0;
            }

            //set price from request to sink to 0
            for(int request = teams + requests; request < numOfNodes -1; request++ ){
                aMatrix[request][numOfNodes -1] = 0;
            }

            int[] teamValues = new int[requests];
            for (int i = 0; i < teamValues.length; i++) {
                teamValues[i] = sc.nextInt();
            }

            for (int team = 1; team <= teams ; team++) {
                for (int request = 0; request < requests; request++) {
                    aMatrix[team][teams + requests + request] = teamValues[request];
                }
            }

            // builds up costs from location u to location v
            for (int start = 1 + teams; start < teams + requests; start++) {
                for (int finish = start + requests; finish < numOfNodes - 1; finish++) {
                    aMatrix[start][finish] = sc.nextInt();
                }
            }




            /*
            for(int request = teams + requests; request < numOfNodes -1; request++){
                int costToRequest = sc.nextInt();
                for(int team = 1; team <= teams; team++){
                    aMatrix[team][request] = costToRequest;
                }
            }

            int counter = requests -1;
            for(int request = teams + 1; request < teams + requests; request++){
                //System.out.println("request = " + request);
                for( int dest = 0; dest < counter; dest++){
                    aMatrix[request][teams + requests + dest +1] = sc.nextInt();
                }
                counter --;
            }


            for (int i = 0; i < numOfNodes; i++) {
                for (int j = 0; j < numOfNodes; j++) {
                    if(aMatrix[i][j] == inf)
                        System.out.print("Inf ");
                    else
                        System.out.print(aMatrix[i][j] + " ");
                }
                System.out.println();
            }
            */




            minCostMatching(aMatrix, requests, teams);
        }
    }

    private void minCostMatching(int[][] aMatrix, int requests, int teams){
        // copy array
        int[][] newAMatrix = new int[aMatrix.length][aMatrix.length];
        for(int i = 0; i < aMatrix.length; i++){
            for(int j = 0; j < aMatrix.length; j++){
                newAMatrix[i][j] = aMatrix[i][j];
            }
        }

        int nodePrices[] = new int[aMatrix.length];
        for(int path = 0; path < requests; path++) {
            newAMatrix = Djikstras(newAMatrix, aMatrix.length -1);

            int currentNode = aMatrix.length - 1;
            while (currentNode != 0) {
                newAMatrix[currentNode][parent[currentNode]] = newAMatrix[parent[currentNode]][currentNode];
                newAMatrix[parent[currentNode]][currentNode] = inf;
                currentNode = parent[currentNode];
            }


            for (int node = 0; node < aMatrix.length; node++){
                nodePrices[node] += cost[node];
            }

            for(int start = 1; start < teams + requests; start++){
                for(int finish =0; finish < aMatrix.length; finish++){
                    if(finish != 0 && newAMatrix[start][finish] != inf)
                        newAMatrix[start][finish] = nodePrices[start] + aMatrix[start][finish] - nodePrices[finish];

                }
            }

            for(int end = teams + requests; end < aMatrix.length - 1; end++){
                for(int start = 0; start < aMatrix.length; start++){
                    if(start != aMatrix.length -1 && newAMatrix[end][start] != inf)
                        newAMatrix[end][start] = nodePrices[start] + aMatrix[start][end] - nodePrices[end];

                }
            }
        }

        /*
        for (int i = 0; i < aMatrix.length; i++) {
            for (int j = 0; j < aMatrix.length; j++) {
                if(aMatrix[i][j] == inf)
                    System.out.print("Inf ");
                else
                    System.out.print(newAMatrix[i][j] + " ");
            }
            System.out.println();
        }
        */


        int minCost = 0;
        for(int startSpots = 1; startSpots < teams + requests; startSpots++){
            for(int node = 0; node < aMatrix.length; node++){
                if(aMatrix[startSpots][node] != inf && newAMatrix[node][startSpots] != inf){
                    minCost += aMatrix[startSpots][node];
                }
            }
        }
        System.out.println(minCost);

    }

    //di
    private int[][] Djikstras(int[][] aMatrix, int Sink){
        IndexMinPQ<Integer> pq = new IndexMinPQ<>(aMatrix.length);
        cost =  new int[aMatrix.length];
        parent = new int[aMatrix.length];
        for(int i = 0; i < aMatrix.length; i++){
            cost[i] = inf;
            parent[i] = 0;
        }

        cost[0] = 0;
        pq.insert(0, 0);
        while (!pq.isEmpty()){
            int node = pq.delMin();
            for (int nextNode = 0; nextNode < aMatrix.length; nextNode++){
                if(aMatrix[node][nextNode] != inf && cost[nextNode] > cost[node] + aMatrix[node][nextNode]) {
                    cost[nextNode] = cost[node] + aMatrix[node][nextNode];
                    parent[nextNode] = node;
                    if(!pq.contains(nextNode))
                        pq.insert(nextNode, cost[nextNode]);
                    else
                        pq.decreaseKey(nextNode,cost[nextNode]);
                }
            }
        }
        return aMatrix;
    }



    public static void main(String[] args) {
        Catering cat = new Catering();
        cat.input();
    }

    private class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
        private int maxN;        // maximum number of elements on PQ
        private int N;           // number of elements on PQ
        private int[] pq;        // binary heap using 1-based indexing
        private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
        private Key[] keys;      // keys[i] = priority of i

        /**
         * Initializes an empty indexed priority queue with indices between <tt>0</tt>
         * and <tt>maxN - 1</tt>.
         *
         * @param maxN the keys on this priority queue are index from <tt>0</tt>
         *             <tt>maxN - 1</tt>
         * @throws IllegalArgumentException if <tt>maxN</tt> &lt; <tt>0</tt>
         */
        public IndexMinPQ(int maxN) {
            if (maxN < 0) throw new IllegalArgumentException();
            this.maxN = maxN;
            keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
            pq = new int[maxN + 1];
            qp = new int[maxN + 1];                   // make this of length maxN??
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }

        /**
         * Returns true if this priority queue is empty.
         *
         * @return <tt>true</tt> if this priority queue is empty;
         * <tt>false</tt> otherwise
         */
        public boolean isEmpty() {
            return N == 0;
        }

        /**
         * Is <tt>i</tt> an index on this priority queue?
         *
         * @param i an index
         * @return <tt>true</tt> if <tt>i</tt> is an index on this priority queue;
         * <tt>false</tt> otherwise
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         */
        public boolean contains(int i) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            return qp[i] != -1;
        }

        /**
         * Returns the number of keys on this priority queue.
         *
         * @return the number of keys on this priority queue
         */
        public int size() {
            return N;
        }

        /**
         * Associates key with index <tt>i</tt>.
         *
         * @param i   an index
         * @param key the key to associate with index <tt>i</tt>
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @throws IllegalArgumentException  if there already is an item associated
         *                                   with index <tt>i</tt>
         */
        public void insert(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
            N++;
            qp[i] = N;
            pq[N] = i;
            keys[i] = key;
            swim(N);
        }

        /**
         * Returns an index associated with a minimum key.
         *
         * @return an index associated with a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public int minIndex() {
            if (N == 0) throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        /**
         * Returns a minimum key.
         *
         * @return a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public Key minKey() {
            if (N == 0) throw new NoSuchElementException("Priority queue underflow");
            return keys[pq[1]];
        }

        /**
         * Removes a minimum key and returns its associated index.
         *
         * @return an index associated with a minimum key
         * @throws NoSuchElementException if this priority queue is empty
         */
        public int delMin() {
            if (N == 0) throw new NoSuchElementException("Priority queue underflow");
            int min = pq[1];
            exch(1, N--);
            sink(1);
            assert min == pq[N + 1];
            qp[min] = -1;        // delete
            keys[min] = null;    // to help with garbage collection
            pq[N + 1] = -1;        // not needed
            return min;
        }

        /**
         * Returns the key associated with index <tt>i</tt>.
         *
         * @param i the index of the key to return
         * @return the key associated with index <tt>i</tt>
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @throws NoSuchElementException    no key is associated with index <tt>i</tt>
         */
        public Key keyOf(int i) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            else return keys[i];
        }

        /**
         * Change the key associated with index <tt>i</tt> to the specified value.
         *
         * @param i   the index of the key to change
         * @param key change the key associated with index <tt>i</tt> to this key
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @throws NoSuchElementException    no key is associated with index <tt>i</tt>
         */
        public void changeKey(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            keys[i] = key;
            swim(qp[i]);
            sink(qp[i]);
        }

        /**
         * Change the key associated with index <tt>i</tt> to the specified value.
         *
         * @param i   the index of the key to change
         * @param key change the key associated with index <tt>i</tt> to this key
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @deprecated Replaced by {@link #changeKey(int, Key)}.
         */
        public void change(int i, Key key) {
            changeKey(i, key);
        }

        /**
         * Decrease the key associated with index <tt>i</tt> to the specified value.
         *
         * @param i   the index of the key to decrease
         * @param key decrease the key associated with index <tt>i</tt> to this key
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @throws IllegalArgumentException  if key &ge; key associated with index <tt>i</tt>
         * @throws NoSuchElementException    no key is associated with index <tt>i</tt>
         */
        public void decreaseKey(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) <= 0)
                throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
            keys[i] = key;
            swim(qp[i]);
        }

        /**
         * Increase the key associated with index <tt>i</tt> to the specified value.
         *
         * @param i   the index of the key to increase
         * @param key increase the key associated with index <tt>i</tt> to this key
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @throws IllegalArgumentException  if key &le; key associated with index <tt>i</tt>
         * @throws NoSuchElementException    no key is associated with index <tt>i</tt>
         */
        public void increaseKey(int i, Key key) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) >= 0)
                throw new IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key");
            keys[i] = key;
            sink(qp[i]);
        }

        /**
         * Remove the key associated with index <tt>i</tt>.
         *
         * @param i the index of the key to remove
         * @throws IndexOutOfBoundsException unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
         * @throws NoSuchElementException    no key is associated with index <t>i</tt>
         */
        public void delete(int i) {
            if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            int index = qp[i];
            exch(index, N--);
            swim(index);
            sink(index);
            keys[i] = null;
            qp[i] = -1;
        }


        /***************************************************************************
         * General helper functions.
         ***************************************************************************/
        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }


        /***************************************************************************
         * Heap helper functions.
         ***************************************************************************/
        private void swim(int k) {
            while (k > 1 && greater(k / 2, k)) {
                exch(k, k / 2);
                k = k / 2;
            }
        }

        private void sink(int k) {
            while (2 * k <= N) {
                int j = 2 * k;
                if (j < N && greater(j, j + 1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
            }
        }


        /***************************************************************************
         * Iterators.
         ***************************************************************************/

        /**
         * Returns an iterator that iterates over the keys on the
         * priority queue in ascending order.
         * The iterator doesn't implement <tt>remove()</tt> since it's optional.
         *
         * @return an iterator that iterates over the keys in ascending order
         */
        public Iterator<Integer> iterator() {
            return new HeapIterator();
        }

        private class HeapIterator implements Iterator<Integer> {
            // create a new pq
            private IndexMinPQ<Key> copy;

            // add all elements to copy of heap
            // takes linear time since already in heap order so no keys move
            public HeapIterator() {
                copy = new IndexMinPQ<Key>(pq.length - 1);
                for (int i = 1; i <= N; i++)
                    copy.insert(pq[i], keys[pq[i]]);
            }

            public boolean hasNext() {
                return !copy.isEmpty();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return copy.delMin();
            }
        }
    }
}



