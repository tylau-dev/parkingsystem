package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
	fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
	ticket = new Ticket();
    }

    @Test
    public void calculateFareCar() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, false);
	assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, false);
	assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTimeAndMoreThanThirtyMinutes() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
								      // parking fare
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, false);
	assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
								      // parking fare
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, false);
	assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (25 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
									   // parking fare per hour
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, false);
	assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareGetOutTimeNull() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(null);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareIncorrectType() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareExistingUser() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (25 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
									   // parking fare per hour
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);

	Boolean existingUser = true;
	fareCalculatorService.calculateFare(ticket, existingUser);
	assertEquals((24 * Fare.CAR_RATE_PER_HOUR * 0.95), ticket.getPrice());
    }

    @Test
    public void calculateFareExistingUserWithLessThanOneHourParkingTimeAndMoreThanThirtyMinutes() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
								      // parking fare
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, true);
	assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR * 0.95), ticket.getPrice());
    }

    @Test
    public void calculateFareFreeFirstThirtyMinutes() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));// 30 minutes parking time should give 0
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket, true);
	assertEquals((0), ticket.getPrice());
    }

}
