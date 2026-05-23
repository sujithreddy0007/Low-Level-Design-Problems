import java.util.*;
class Node{
    int key;
    int value;
    Node next;
    Node prev;
    public Node(int key,int value){
        this.key = key;
        this.value = value;
        this.next = null;
        this.prev = null;
    }
}
class LRUCache {
    int capacity;
    HashMap<Integer,Node> cache;
    Node head;
    Node tail;
    public LRUCache(int capacity) {
        cache = new HashMap<>();
        this.capacity = capacity;

        head = new Node(0,0);
        tail = new Node(0,0);

        head.next = tail;
        tail.prev = head;
    }
    public void insert(Node node){
        Node prevLast = tail.prev;

        prevLast.next = node;
        node.prev = prevLast;

        node.next = tail;
        tail.prev = node;
    }
    public void remove(Node node){
        Node prevNode = node.prev;
        Node nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    public int get(int key) {
        if(!cache.containsKey(key)){
            return -1;
        }
        Node node = cache.get(key);
        remove(node);
        insert(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        if(cache.containsKey(key)){
            Node oldNode = cache.get(key);
            remove(oldNode);
        }
        Node node = new Node(key,value);
        cache.put(key,node);
        insert(node);
        if(cache.size() > capacity){
            Node lru = head.next;
            head.next = lru.next;
            remove(lru);
            cache.remove(lru.key);
        } 

    }
}

// Your code will be instantiated and invoked as follows:
// LRUCache cache = new LRUCache(2);
// cache.put(1, 1);
// cache.put(2, 2);
// cache.get(1);       // returns 1
// cache.put(3, 3);    // evicts key 2
// cache.get(2);       // returns -1
