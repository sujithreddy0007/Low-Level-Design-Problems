from enum import Enum
from typing import Optional, List


class VehicleType(Enum):
    MOTORCYCLE = "Motorcycle"
    CAR = "Car"
    BUS = "Bus"


class SpotSize(Enum):
    SMALL = "Small"
    MEDIUM = "Medium"
    LARGE = "Large"


class Vehicle:
    def __init__(self, license_plate: str, vehicle_type: VehicleType):
        self.license_plate = license_plate
        self.vehicle_type = vehicle_type


class ParkingSpot:
    def __init__(self, spot_id: str, size: SpotSize):
        self.spot_id = spot_id
        self.size = size
        self.vehicle = None

    def isAvailable(self) -> bool:
        return self.vehicle is None

    def canFitVehicle(self, vehicle: Vehicle) -> bool:
        size_map = {
            VehicleType.MOTORCYCLE: SpotSize.SMALL,
            VehicleType.CAR: SpotSize.MEDIUM,
            VehicleType.BUS: SpotSize.LARGE
        }
        return self.size == size_map[vehicle.vehicle_type]

    def parkVehicle(self, vehicle: Vehicle) -> bool:
        if self.isAvailable() and self.canFitVehicle(vehicle):
            self.vehicle = vehicle
            return True
        return False

    def removeVehicle(self):
        temp = self.vehicle
        self.vehicle = None
        return temp


class ParkingLevel:
    def __init__(self, level_id: str, num_spots: int):
        self.level_id = level_id
        self.spots = []
        self._initializeSpots(num_spots)

    def _initializeSpots(self, num_spots: int):
        small = int(num_spots * 0.2)
        medium = int(num_spots * 0.5)
        large = num_spots - small - medium

        idx = 1

        for _ in range(small):
            self.spots.append(ParkingSpot(f"{self.level_id}_SPOT_{idx}", SpotSize.SMALL))
            idx += 1

        for _ in range(medium):
            self.spots.append(ParkingSpot(f"{self.level_id}_SPOT_{idx}", SpotSize.MEDIUM))
            idx += 1

        for _ in range(large):
            self.spots.append(ParkingSpot(f"{self.level_id}_SPOT_{idx}", SpotSize.LARGE))
            idx += 1

    def findAvailableSpot(self, vehicle):
        for spot in self.spots:
            if spot.isAvailable() and spot.canFitVehicle(vehicle):
                return spot
        return None

    def getAvailableSpotsCount(self, vehicle_type):
        dummy = Vehicle("", vehicle_type)
        return sum(1 for spot in self.spots
                   if spot.isAvailable() and spot.canFitVehicle(dummy))


class ParkingLot:
    def __init__(self, num_levels: int, spots_per_level: int):
        self.levels = [ParkingLevel(f"LEVEL_{i+1}", spots_per_level)
                       for i in range(num_levels)]

        self.occupied = {}
        self.spot_map = {}

        for level in self.levels:
            for spot in level.spots:
                self.spot_map[spot.spot_id] = spot

    def parkVehicle(self, vehicle):
        if vehicle.license_plate in self.occupied:
            return False

        for level in self.levels:
            spot = level.findAvailableSpot(vehicle)

            if spot and spot.parkVehicle(vehicle):
                self.occupied[vehicle.license_plate] = spot.spot_id
                return True

        return False

    def removeVehicle(self, license_plate):
        spot_id = self.occupied.get(license_plate)

        if not spot_id:
            return False

        spot = self.spot_map.get(spot_id)

        if spot and spot.vehicle and spot.vehicle.license_plate == license_plate:
            spot.removeVehicle()
            del self.occupied[license_plate]
            return True

        return False

    def getAvailableSpots(self, vehicle_type):
        return sum(level.getAvailableSpotsCount(vehicle_type)
                   for level in self.levels)