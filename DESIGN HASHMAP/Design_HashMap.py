class MyHashMap:
    def __init__(self):
        # Your code here
        self.size = 1009
        self.buckets = [[] for _ in range(self.size)]
    def _hash(self,key):
        return key % self.size
    def put(self, key: int, value: int) -> None:
        # Your code here
        h = self._hash(key)
        bucket = self.buckets[h]
        for i,(k,v) in enumerate(bucket):
            if k == key:
                bucket[i] = (key,value)
                return
        bucket.append((key,value))

    def get(self, key: int) -> int:
        h = self._hash(key)
        bucket = self.buckets[h]
        for i ,(k,v) in enumerate(bucket):
            if k == key:
                return v
        return -1

    def remove(self, key: int) -> None:
        h = self._hash(key)
        bucket = self.buckets[h]
        for i, (k,v) in enumerate(bucket):
            if k == key:
                bucket.pop(i)
                return