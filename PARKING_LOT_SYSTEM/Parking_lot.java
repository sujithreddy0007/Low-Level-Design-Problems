import java.util.*;

enum VehicleType {
    MOTORCYCLE("Motorcycle"),
    CAR("Car"),
    BUS("Bus");

    private final String value;

    VehicleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

enum SpotSize {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large");

    private final String value;

    SpotSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

class Vehicle {
    String licensePlate;
    VehicleType vehicleType;

    public Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }
}

class ParkingSpot {
    String spotId;
    SpotSize size;
    Vehicle vehicle;

    public ParkingSpot(String spotId, SpotSize size) {
        this.spotId = spotId;
        this.size = size;
        this.vehicle = null;
    }

    public boolean isAvailable() {
        return vehicle == null;
    }

    public boolean canFitVehicle(Vehicle vehicle) {
        Map<VehicleType, SpotSize> map = new HashMap<>();

        map.put(VehicleType.MOTORCYCLE, SpotSize.SMALL);
        map.put(VehicleType.CAR, SpotSize.MEDIUM);
        map.put(VehicleType.BUS, SpotSize.LARGE);

        return size == map.get(vehicle.vehicleType);
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if(isAvailable() && canFitVehicle(vehicle)){
            this.vehicle = vehicle;
            return true;
        }
        return false;
    }

    public Vehicle removeVehicle() {
        Vehicle temp = vehicle;
        vehicle = null;
        return temp;
    }
}

class ParkingLevel {
    String levelId;
    List<ParkingSpot> spots;

    public ParkingLevel(String levelId, int numSpots) {
        this.levelId = levelId;
        spots = new ArrayList<>();
        initializeSpots(numSpots);
    }

    private void initializeSpots(int numSpots) {

        int small = (int)(numSpots*0.2);
        int medium = (int)(numSpots*0.5);
        int large = numSpots-small-medium;

        int idx=1;

        for(int i=0;i<small;i++)
            spots.add(new ParkingSpot(levelId+"_SPOT_"+idx++,SpotSize.SMALL));

        for(int i=0;i<medium;i++)
            spots.add(new ParkingSpot(levelId+"_SPOT_"+idx++,SpotSize.MEDIUM));

        for(int i=0;i<large;i++)
            spots.add(new ParkingSpot(levelId+"_SPOT_"+idx++,SpotSize.LARGE));
    }

    public ParkingSpot findAvailableSpot(Vehicle vehicle) {

        for(ParkingSpot spot:spots){
            if(spot.isAvailable() && spot.canFitVehicle(vehicle))
                return spot;
        }

        return null;
    }

    public int getAvailableSpotsCount(VehicleType vehicleType) {

        Vehicle dummy = new Vehicle("",vehicleType);

        int count=0;

        for(ParkingSpot spot:spots){
            if(spot.isAvailable() && spot.canFitVehicle(dummy))
                count++;
        }

        return count;
    }
}

class ParkingLot {

    List<ParkingLevel> levels;
    Map<String,String> occupied;
    Map<String,ParkingSpot> spotMap;

    public ParkingLot(int numLevels,int spotsPerLevel) {

    levels = new ArrayList<>();
    occupied = new HashMap<>();
    spotMap = new HashMap<>();

    for(int i=0;i<numLevels;i++){

        ParkingLevel level =
                new ParkingLevel(
                        "LEVEL_"+(i+1),
                        spotsPerLevel
                );

        levels.add(level);

        for(ParkingSpot spot: level.spots)
            spotMap.put(
                    spot.spotId,
                    spot
            );
    }
}

    public boolean parkVehicle(Vehicle vehicle) {

        if(occupied.containsKey(vehicle.licensePlate))
            return false;

        for(ParkingLevel level:levels){

            ParkingSpot spot =
                    level.findAvailableSpot(vehicle);

            if(spot!=null && spot.parkVehicle(vehicle)){

                occupied.put(
                        vehicle.licensePlate,
                        spot.spotId
                );

                return true;
            }
        }

        return false;
    }

    public boolean removeVehicle(String licensePlate) {

        String spotId =
                occupied.get(licensePlate);

        if(spotId==null)
            return false;

        ParkingSpot spot =
                spotMap.get(spotId);

        if(spot!=null &&
                spot.vehicle!=null &&
                spot.vehicle.licensePlate.equals(licensePlate)){

            spot.removeVehicle();
            occupied.remove(licensePlate);

            return true;
        }

        return false;
    }

    public int getAvailableSpots(VehicleType vehicleType) {

        int count=0;

        for(ParkingLevel level:levels)
            count+=level.getAvailableSpotsCount(vehicleType);

        return count;
    }
}