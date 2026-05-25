import java.util.*;
class MyHashMap {
    private int size;
    ArrayList<ArrayList<int[]>> buckets;
    public MyHashMap() {
        this.size = 1009;
        this.buckets = new ArrayList<>();
        for(int i=0;i<size;i++){
            buckets.add(new ArrayList<>());
        }
    }
    public int _hash(int key){
        return key % size;
    }
    public void put(int key, int value) {
       int h = _hash(key);
       ArrayList<int[]>bucket = buckets.get(h);
       for(int[] pair : bucket){
        if(pair[0] == key){
            pair[1] = value;
            return;
        }
       }
       bucket.add(new int[]{key,value});
    }

    public int get(int key) {
       int h = _hash(key);
       ArrayList<int[]> bucket = buckets.get(h);
       for(int[] pair : bucket){
        if(pair[0] == key){
            return pair[1];
        }
       }
       return -1;
    }

    public void remove(int key) {
        int h = _hash(key);
        ArrayList<int[]> bucket = buckets.get(h);
        for(int i = 0;i<bucket.size();i++){
            if(bucket.get(i)[0] == key){
                bucket.remove(i);
                return;
            }
        }
    }
}