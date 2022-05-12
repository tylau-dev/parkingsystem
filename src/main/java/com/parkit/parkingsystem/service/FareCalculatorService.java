package com.parkit.parkingsystem.service;

import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/*
 * Service handling Fate Calculation
 */
public class FareCalculatorService {

    /*
     * Compute Parking Fare based on Duration, Existing Users
     * 
     * @param Ticket, Boolean existingUser
     */
    public void calculateFare(Ticket ticket, Boolean existingUser) {

	if (ticket.getOutTime() == null) {
	    throw new IllegalArgumentException("Out time provided is null");
	}

	// Check whether OutTime is not before InTime and that OutTime and InTime are
	// not equal
	if (ticket.getOutTime().before(ticket.getInTime()) && ticket.getOutTime() != ticket.getInTime()) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	// existingUserDiscount variable controlling the rate for existing user, set to
	// 0.95 for the 5% discount if it's an existing user
	double existingUserDiscount = 1;
	if (existingUser) {
	    existingUserDiscount = 0.95;
	}

	long timeDiffInMillies = Math.abs(ticket.getOutTime().getTime() - ticket.getInTime().getTime());
	double timeDiffInMinutes = TimeUnit.MINUTES.convert(timeDiffInMillies, TimeUnit.MILLISECONDS);
	double duration;

	// Duration fixed to 24 hours (1440 minutes) when the Time Difference is more
	// than 24 hours
	if (timeDiffInMinutes > 1440) {
	    duration = 24;
	} else {
	    duration = timeDiffInMinutes / 60;
	}

	// freeRate variable controls whether the user stayed for less than 30 minutes
	double freeRate = 1;

	if (timeDiffInMinutes <= 30) {
	    freeRate = 0;
	}

	switch (ticket.getParkingSpot().getParkingType()) {
	case CAR: {
	    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * existingUserDiscount * freeRate);
	    break;
	}
	case BIKE: {
	    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * existingUserDiscount * freeRate);
	    break;
	}
	default:
	    throw new NullPointerException("Unkown Parking Type");
	}
    }
}