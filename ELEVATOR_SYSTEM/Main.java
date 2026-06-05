import java.util.*;

enum Direction {
    UP,
    DOWN,
    IDLE
}

enum ElevatorState {
    MOVING,
    STOPPED,
    IDLE
}

class Request {

    private int sourceFloor;
    private int destinationFloor;

    public Request(int sourceFloor,
                   int destinationFloor) {

        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
}

class Elevator {

    private int id;
    private int currentFloor;

    private Direction direction;
    private ElevatorState state;

    private Queue<Request> requests;

    private boolean pickedUp;

    public Elevator(int id,
                    int currentFloor) {

        this.id = id;
        this.currentFloor = currentFloor;

        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;

        this.requests = new LinkedList<>();
        this.pickedUp = false;
    }

    public void addRequest(Request request) {
        requests.offer(request);

        if(state == ElevatorState.IDLE) {
            state = ElevatorState.MOVING;
        }
    }

    public void move() {

        if(requests.isEmpty()) {

            state = ElevatorState.IDLE;
            direction = Direction.IDLE;

            return;
        }

        Request currentRequest = requests.peek();

        int targetFloor;

        if(!pickedUp) {
            targetFloor = currentRequest.getSourceFloor();
        } else {
            targetFloor = currentRequest.getDestinationFloor();
        }

        if(currentFloor < targetFloor) {

            currentFloor++;
            direction = Direction.UP;
            state = ElevatorState.MOVING;

        } else if(currentFloor > targetFloor) {

            currentFloor--;
            direction = Direction.DOWN;
            state = ElevatorState.MOVING;

        } else {

            state = ElevatorState.STOPPED;

            if(!pickedUp) {

                pickedUp = true;

            } else {

                requests.poll();
                pickedUp = false;

                if(requests.isEmpty()) {
                    direction = Direction.IDLE;
                    state = ElevatorState.IDLE;
                }
            }
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public ElevatorState getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public int getPendingRequests() {
        return requests.size();
    }
}

class Building {

    private int numFloors;
    private List<Elevator> elevators;

    public Building(int numFloors,
                    int numElevators) {

        this.numFloors = numFloors;

        elevators = new ArrayList<>();

        for(int i = 1; i <= numElevators; i++) {
            elevators.add(new Elevator(i, 0));
        }
    }

    public Elevator requestElevator(
            int sourceFloor,
            int destinationFloor) {

        Elevator bestElevator = null;

        int minDistance = Integer.MAX_VALUE;

        for(Elevator elevator : elevators) {

            int distance =
                    Math.abs(
                            elevator.getCurrentFloor()
                            - sourceFloor
                    );

            if(distance < minDistance) {

                minDistance = distance;
                bestElevator = elevator;
            }
        }

        Request request =
                new Request(
                        sourceFloor,
                        destinationFloor
                );

        bestElevator.addRequest(request);

        return bestElevator;
    }

    public void step() {

        for(Elevator elevator : elevators) {
            elevator.move();
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}

public class Main {

    public static void main(String[] args) {

        Building building =
                new Building(10, 2);

        Elevator e1 =
                building.requestElevator(3, 8);

        Elevator e2 =
                building.requestElevator(7, 1);

        System.out.println(
                "Request 1 assigned to Elevator "
                + e1.getId()
        );

        System.out.println(
                "Request 2 assigned to Elevator "
                + e2.getId()
        );

        for(int i = 1; i <= 15; i++) {

            System.out.println(
                    "\nStep " + i
            );

            building.step();

            for(Elevator elevator :
                    building.getElevators()) {

                System.out.println(
                        "Elevator "
                        + elevator.getId()
                        + " Floor="
                        + elevator.getCurrentFloor()
                        + " Direction="
                        + elevator.getDirection()
                        + " State="
                        + elevator.getState()
                );
            }
        }
    }
}