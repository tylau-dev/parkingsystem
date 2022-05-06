package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public ParkingSpot getParkingSpot() {
	return new ParkingSpot(this.parkingSpot.getId(), this.parkingSpot.getParkingType(),
		this.parkingSpot.isAvailable());
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
	this.parkingSpot = new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(),
		parkingSpot.isAvailable());
    }

    public String getVehicleRegNumber() {
	String vehicleRegNumberCopy = this.vehicleRegNumber;
	return vehicleRegNumberCopy;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
	String newVehicleRegNumber = vehicleRegNumber;
	this.vehicleRegNumber = newVehicleRegNumber;
    }

    public double getPrice() {
	double priceCopy = this.price;
	return priceCopy;
    }

    public void setPrice(double price) {
	double newPrice = price;
	this.price = newPrice;
    }

    public Date getInTime() {
	return new Date(this.inTime.getTime());
    }

    public void setInTime(Date inTime) {
	this.inTime = new Date(inTime.getTime());
    }

    public Date getOutTime() {
	if (outTime == null) {
	    return null;
	} else {
	    return new Date(this.outTime.getTime());
	}
    }

    public void setOutTime(Date outTime) {
	if (outTime == null) {
	    this.outTime = null;
	} else {
	    this.outTime = new Date(outTime.getTime());
	}
    }
}
