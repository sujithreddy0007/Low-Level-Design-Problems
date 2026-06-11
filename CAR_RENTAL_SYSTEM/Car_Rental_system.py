from enum import Enum


class VehicleType(Enum):
    SEDAN = "SEDAN"
    SUV = "SUV"
    HATCHBACK = "HATCHBACK"


class VehicleStatus(Enum):
    AVAILABLE = "AVAILABLE"
    RESERVED = "RESERVED"
    RENTED = "RENTED"


class User:

    def __init__(self, user_id, name):
        self.user_id = user_id
        self.name = name


class Vehicle:

    def __init__(self, vehicle_id,
                 vehicle_type,
                 price_per_day):

        self.vehicle_id = vehicle_id
        self.vehicle_type = vehicle_type
        self.price_per_day = price_per_day

        self.status = VehicleStatus.AVAILABLE

    def is_available(self):
        return self.status == VehicleStatus.AVAILABLE

    def __str__(self):
        return f"{self.vehicle_id} ({self.vehicle_type.value})"


class Location:

    def __init__(self,
                 location_id,
                 city):

        self.location_id = location_id
        self.city = city

        self.vehicles = []

    def add_vehicle(self, vehicle):
        self.vehicles.append(vehicle)


class Reservation:

    def __init__(self,
                 reservation_id,
                 user,
                 vehicle,
                 start_date,
                 end_date):

        self.reservation_id = reservation_id
        self.user = user
        self.vehicle = vehicle

        self.start_date = start_date
        self.end_date = end_date


class CarRentalSystem:

    def __init__(self):

        self.users = {}
        self.locations = {}
        self.vehicles = {}
        self.reservations = {}

        self.reservation_counter = 1

    def add_user(self, user):
        self.users[user.user_id] = user

    def add_location(self, location):
        self.locations[location.location_id] = location

    def add_vehicle(self,
                    location_id,
                    vehicle):

        location = self.locations.get(location_id)

        if not location:
            return

        location.add_vehicle(vehicle)

        self.vehicles[vehicle.vehicle_id] = vehicle

    def search_vehicles(self,
                        city,
                        vehicle_type):

        result = []

        for location in self.locations.values():

            if location.city != city:
                continue

            for vehicle in location.vehicles:

                if (vehicle.vehicle_type == vehicle_type
                        and vehicle.is_available()):

                    result.append(vehicle)

        return result

    def reserve_vehicle(self,
                        user_id,
                        vehicle_id,
                        start_date,
                        end_date):

        user = self.users.get(user_id)
        vehicle = self.vehicles.get(vehicle_id)

        if (not user or
                not vehicle or
                not vehicle.is_available()):

            return None

        vehicle.status = VehicleStatus.RESERVED

        reservation_id = (
            f"R{self.reservation_counter}"
        )

        self.reservation_counter += 1

        reservation = Reservation(
            reservation_id,
            user,
            vehicle,
            start_date,
            end_date
        )

        self.reservations[
            reservation_id
        ] = reservation

        return reservation

    def cancel_reservation(
            self,
            reservation_id):

        reservation = self.reservations.get(
            reservation_id
        )

        if not reservation:
            return False

        reservation.vehicle.status = (
            VehicleStatus.AVAILABLE
        )

        del self.reservations[
            reservation_id
        ]

        return True

    def return_vehicle(self,
                       vehicle_id):

        vehicle = self.vehicles.get(
            vehicle_id
        )

        if not vehicle:
            return False

        vehicle.status = (
            VehicleStatus.AVAILABLE
        )

        return True


# ----------------------------
# Driver Code
# ----------------------------

if __name__ == "__main__":

    system = CarRentalSystem()

    user = User(
        "U1",
        "Sai"
    )

    system.add_user(user)

    location = Location(
        "L1",
        "Hyderabad"
    )

    system.add_location(location)

    vehicle = Vehicle(
        "V1",
        VehicleType.SEDAN,
        2000
    )

    system.add_vehicle(
        "L1",
        vehicle
    )

    print(
        system.search_vehicles(
            "Hyderabad",
            VehicleType.SEDAN
        )
    )

    reservation = (
        system.reserve_vehicle(
            "U1",
            "V1",
            "2026-06-01",
            "2026-06-05"
        )
    )

    if reservation:
        print(
            "Reservation Created:",
            reservation.reservation_id
        )

    print(
        system.search_vehicles(
            "Hyderabad",
            VehicleType.SEDAN
        )
    )

    system.cancel_reservation(
        reservation.reservation_id
    )

    print(
        system.search_vehicles(
            "Hyderabad",
            VehicleType.SEDAN
        )
    )