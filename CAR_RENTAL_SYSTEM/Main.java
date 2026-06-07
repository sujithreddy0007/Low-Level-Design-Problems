import java.util.*;

enum VehicleType {
    SEDAN,
    SUV,
    HATCHBACK
}

enum VehicleStatus {
    AVAILABLE,
    RESERVED,
    RENTED
}

class User {

    private String userId;
    private String name;

    public User(String userId,
                String name) {

        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}

class Vehicle {

    private String vehicleId;
    private VehicleType vehicleType;
    private double pricePerDay;

    private VehicleStatus status;

    public Vehicle(String vehicleId,
                   VehicleType vehicleType,
                   double pricePerDay) {

        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.pricePerDay = pricePerDay;

        this.status = VehicleStatus.AVAILABLE;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return vehicleId + " (" + vehicleType + ")";
    }
}

class Location {

    private String locationId;
    private String city;

    private List<Vehicle> vehicles;

    public Location(String locationId,
                    String city) {

        this.locationId = locationId;
        this.city = city;

        vehicles = new ArrayList<>();
    }

    public String getLocationId() {
        return locationId;
    }

    public String getCity() {
        return city;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }
}

class Reservation {

    private String reservationId;
    private User user;
    private Vehicle vehicle;

    private String startDate;
    private String endDate;

    public Reservation(String reservationId,
                       User user,
                       Vehicle vehicle,
                       String startDate,
                       String endDate) {

        this.reservationId = reservationId;
        this.user = user;
        this.vehicle = vehicle;

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public User getUser() {
        return user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}

class CarRentalSystem {

    private Map<String, User> users;
    private Map<String, Location> locations;
    private Map<String, Vehicle> vehicles;
    private Map<String, Reservation> reservations;

    private int reservationCounter;

    public CarRentalSystem() {

        users = new HashMap<>();
        locations = new HashMap<>();
        vehicles = new HashMap<>();
        reservations = new HashMap<>();

        reservationCounter = 1;
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void addLocation(Location location) {
        locations.put(location.getLocationId(), location);
    }

    public void addVehicle(String locationId,
                           Vehicle vehicle) {

        Location location = locations.get(locationId);

        if (location == null) {
            return;
        }

        location.addVehicle(vehicle);

        vehicles.put(
                vehicle.getVehicleId(),
                vehicle
        );
    }

    public List<Vehicle> searchVehicles(
            String city,
            VehicleType vehicleType) {

        List<Vehicle> result =
                new ArrayList<>();

        for (Location location :
                locations.values()) {

            if (!location.getCity()
                    .equals(city)) {
                continue;
            }

            for (Vehicle vehicle :
                    location.getVehicles()) {

                if (vehicle.getVehicleType()
                                == vehicleType
                        && vehicle.isAvailable()) {

                    result.add(vehicle);
                }
            }
        }

        return result;
    }

    public Reservation reserveVehicle(
            String userId,
            String vehicleId,
            String startDate,
            String endDate) {

        User user = users.get(userId);
        Vehicle vehicle =
                vehicles.get(vehicleId);

        if (user == null ||
                vehicle == null ||
                !vehicle.isAvailable()) {

            return null;
        }

        vehicle.setStatus(
                VehicleStatus.RESERVED
        );

        String reservationId =
                "R" + reservationCounter++;

        Reservation reservation =
                new Reservation(
                        reservationId,
                        user,
                        vehicle,
                        startDate,
                        endDate
                );

        reservations.put(
                reservationId,
                reservation
        );

        return reservation;
    }

    public boolean cancelReservation(
            String reservationId) {

        Reservation reservation =
                reservations.get(
                        reservationId
                );

        if (reservation == null) {
            return false;
        }

        Vehicle vehicle =
                reservation.getVehicle();

        vehicle.setStatus(
                VehicleStatus.AVAILABLE
        );

        reservations.remove(
                reservationId
        );

        return true;
    }

    public boolean returnVehicle(
            String vehicleId) {

        Vehicle vehicle =
                vehicles.get(vehicleId);

        if (vehicle == null) {
            return false;
        }

        vehicle.setStatus(
                VehicleStatus.AVAILABLE
        );

        return true;
    }
}

public class Main {

    public static void main(String[] args) {

        CarRentalSystem system =
                new CarRentalSystem();

        User user =
                new User(
                        "U1",
                        "Sai"
                );

        system.addUser(user);

        Location location =
                new Location(
                        "L1",
                        "Hyderabad"
                );

        system.addLocation(location);

        Vehicle vehicle =
                new Vehicle(
                        "V1",
                        VehicleType.SEDAN,
                        2000
                );

        system.addVehicle(
                "L1",
                vehicle
        );

        System.out.println(
                system.searchVehicles(
                        "Hyderabad",
                        VehicleType.SEDAN
                )
        );

        Reservation reservation =
                system.reserveVehicle(
                        "U1",
                        "V1",
                        "2026-06-01",
                        "2026-06-05"
                );

        if (reservation != null) {

            System.out.println(
                    "Reservation Created: "
                    + reservation.getReservationId()
            );
        }

        System.out.println(
                system.searchVehicles(
                        "Hyderabad",
                        VehicleType.SEDAN
                )
        );

        system.cancelReservation(
                reservation.getReservationId()
        );

        System.out.println(
                system.searchVehicles(
                        "Hyderabad",
                        VehicleType.SEDAN
                )
        );
    }
}