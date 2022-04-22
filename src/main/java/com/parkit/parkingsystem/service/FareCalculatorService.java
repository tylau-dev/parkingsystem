package com.parkit.parkingsystem.service;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long timeDiffInMillies = Math.abs(ticket.getOutTime().getTime() - ticket.getInTime().getTime());        
        double timeDiffInMinutes = TimeUnit.MINUTES.convert(timeDiffInMillies, TimeUnit.MILLISECONDS);        
        double duration;

        // Duration fixed to 24 hours when the Time Difference is more than 24 hours
        if (timeDiffInMinutes > 1440) {
        	duration = 24;
        }
        else {
        	duration = timeDiffInMinutes/60;
        }
        
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}