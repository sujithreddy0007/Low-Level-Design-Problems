from collections import deque
import time


class RequestInfo:
    def __init__(self, timestamp):
        self.timestamp = timestamp

    def get_timestamp(self):
        return self.timestamp


class UserRequestTracker:
    def __init__(self):
        self.requests = deque()

    def add_request(self, timestamp):
        self.requests.append(timestamp)

    def remove_expired_requests(self, current_timestamp, window_size):
        while (self.requests and
               current_timestamp - self.requests[0] >= window_size):
            self.requests.popleft()

    def get_request_count(self):
        return len(self.requests)

    def get_requests(self):
        return self.requests


class RateLimiter:
    def __init__(self, max_requests, window_size_in_seconds):
        self.max_requests = max_requests
        self.window_size_in_seconds = window_size_in_seconds
        self.user_map = {}

    def allow_request(self, user_id):
        current_time = int(time.time())

        if user_id not in self.user_map:
            self.user_map[user_id] = UserRequestTracker()

        tracker = self.user_map[user_id]

        tracker.remove_expired_requests(
            current_time,
            self.window_size_in_seconds
        )

        if tracker.get_request_count() >= self.max_requests:
            return False

        tracker.add_request(current_time)
        return True

    def get_remaining_requests(self, user_id):
        current_time = int(time.time())

        if user_id not in self.user_map:
            return self.max_requests

        tracker = self.user_map[user_id]

        tracker.remove_expired_requests(
            current_time,
            self.window_size_in_seconds
        )

        return self.max_requests - tracker.get_request_count()

    def reset_user(self, user_id):
        self.user_map.pop(user_id, None)