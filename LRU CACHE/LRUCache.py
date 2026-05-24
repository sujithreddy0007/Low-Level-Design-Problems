class Node:
    def __init__(self,key,value):
            self.prev = None
            self.next = None
            self.key = key
            self.value = value
class LRUCache:
    def __init__(self, capacity: int):
        self.cache = {}
        self.capacity = capacity

        self.head = Node(0,0)
        self.tail = Node(0,0)

        self.head.next = self.tail
        self.tail.prev = self.head
    def insert(self,node):
        prevLast = self.tail.prev

        prevLast.next = node
        node.prev = prevLast
        
        node.next = self.tail
        self.tail.prev = node
    def remove(self,node):
        prevNode = node.prev
        nextNode = node.next

        prevNode.next = nextNode
        nextNode.prev = prevNode
    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        
        node = self.cache[key]
        self.remove(node)
        self.insert(node)
        return node.value

    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            oldNode = self.cache[key]
            self.remove(oldNode)
        node = Node(key,value)
        self.cache[key] = node
        self.insert(node)
        if len(self.cache) > self.capacity:
            lru = self.head.next
            self.remove(lru)
            del self.cache[lru.key]

## Your code will be instantiated and invoked as follows:
# cache = LRUCache(2)
# cache.put(1, 1)
# cache.put(2, 2)
# cache.get(1)       # returns 1
# cache.put(3, 3)    # evicts key 2
# cache.get(2)       # returns -1
