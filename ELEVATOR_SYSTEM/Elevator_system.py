from enum import Enum
from collections import deque


class Direction(Enum):
    UP = "UP"
    DOWN = "DOWN"
    IDLE = "IDLE"


class ElevatorState(Enum):
    MOVING = "MOVING"
    STOPPED = "STOPPED"
    IDLE = "IDLE"


class Request:

    def __init__(self, source_floor, destination_floor):
        self.source_floor = source_floor
        self.destination_floor = destination_floor


class Elevator:

    def __init__(self, elevator_id, current_floor):
        self.id = elevator_id
        self.current_floor = current_floor

        self.direction = Direction.IDLE
        self.state = ElevatorState.IDLE

        self.requests = deque()

        self.picked_up = False

    def add_request(self, request):
        self.requests.append(request)

        if self.state == ElevatorState.IDLE:
            self.state = ElevatorState.MOVING

    def move(self):

        if not self.requests:
            self.direction = Direction.IDLE
            self.state = ElevatorState.IDLE
            return

        current_request = self.requests[0]

        if not self.picked_up:
            target_floor = current_request.source_floor
        else:
            target_floor = current_request.destination_floor

        if self.current_floor < target_floor:

            self.current_floor += 1
            self.direction = Direction.UP
            self.state = ElevatorState.MOVING

        elif self.current_floor > target_floor:

            self.current_floor -= 1
            self.direction = Direction.DOWN
            self.state = ElevatorState.MOVING

        else:

            self.state = ElevatorState.STOPPED

            if not self.picked_up:

                self.picked_up = True

            else:

                self.requests.popleft()
                self.picked_up = False

                if not self.requests:
                    self.direction = Direction.IDLE
                    self.state = ElevatorState.IDLE

    def get_current_floor(self):
        return self.current_floor

    def get_direction(self):
        return self.direction

    def get_state(self):
        return self.state

    def get_pending_requests(self):
        return len(self.requests)


class Building:

    def __init__(self, num_floors, num_elevators):

        self.num_floors = num_floors

        self.elevators = []

        for i in range(1, num_elevators + 1):
            self.elevators.append(Elevator(i, 0))

    def request_elevator(self, source_floor, destination_floor):

        best_elevator = None
        min_distance = float("inf")

        for elevator in self.elevators:

            distance = abs(
                elevator.get_current_floor() - source_floor
            )

            if distance < min_distance:
                min_distance = distance
                best_elevator = elevator

        request = Request(
            source_floor,
            destination_floor
        )

        best_elevator.add_request(request)

        return best_elevator

    def step(self):

        for elevator in self.elevators:
            elevator.move()

    def get_elevators(self):
        return self.elevators


if __name__ == "__main__":

    building = Building(10, 2)

    e1 = building.request_elevator(3, 8)
    e2 = building.request_elevator(7, 1)

    print(
        f"Request 1 assigned to Elevator {e1.id}"
    )

    print(
        f"Request 2 assigned to Elevator {e2.id}"
    )

    for step in range(1, 16):

        print(f"\nStep {step}")

        building.step()

        for elevator in building.get_elevators():

            print(
                f"Elevator {elevator.id} | "
                f"Floor={elevator.get_current_floor()} | "
                f"Direction={elevator.get_direction().value} | "
                f"State={elevator.get_state().value}"
            )