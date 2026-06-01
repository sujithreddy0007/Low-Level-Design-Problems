import java.util.*;

class RequestInfo {
    private long timestamp;

    public RequestInfo(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

class UserRequestTracker {
    private Queue<Long> requests;

    public UserRequestTracker() {
        requests = new LinkedList<>();
    }

    public void addRequest(long timestamp) {
        requests.offer(timestamp);
    }

    public void removeExpiredRequests(long currentTimestamp,
                                      long windowSize) {

        while (!requests.isEmpty()
                && currentTimestamp - requests.peek() >= windowSize) {

            requests.poll();
        }
    }

    public int getRequestCount() {
        return requests.size();
    }

    public Queue<Long> getRequests() {
        return requests;
    }
}

class RateLimiter {

    private int maxRequests;
    private long windowSizeInSeconds;

    private Map<String, UserRequestTracker> userMap;

    public RateLimiter(int maxRequests,
                       int windowSizeInSeconds) {

        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;

        userMap = new HashMap<>();
    }

    public boolean allowRequest(String userId) {

        long currentTime =
                System.currentTimeMillis() / 1000;

        UserRequestTracker tracker =
                userMap.computeIfAbsent(
                        userId,
                        k -> new UserRequestTracker()
                );

        tracker.removeExpiredRequests(
                currentTime,
                windowSizeInSeconds
        );

        if (tracker.getRequestCount() >= maxRequests) {
            return false;
        }

        tracker.addRequest(currentTime);

        return true;
    }

    public int getRemainingRequests(String userId) {

        long currentTime =
                System.currentTimeMillis() / 1000;

        UserRequestTracker tracker =
                userMap.get(userId);

        if (tracker == null) {
            return maxRequests;
        }

        tracker.removeExpiredRequests(
                currentTime,
                windowSizeInSeconds
        );

        return maxRequests - tracker.getRequestCount();
    }

    public void resetUser(String userId) {
        userMap.remove(userId);
    }
}