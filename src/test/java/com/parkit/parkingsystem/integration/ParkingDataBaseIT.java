package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void testParkingACar() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processIncomingVehicle();

	// Retrieve ticket data from Database and check whether the retrieved ticket has
	// the same Vehicle Reg Number
	Ticket checkTicket = ticketDAO.getTicket("ABCDEF");
	assertEquals(("ABCDEF"), checkTicket.getVehicleRegNumber());
    }

    @Test
    public void testParkingLotExit() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	parkingService.processIncomingVehicle();
	parkingService.processExitingVehicle();

	// Retrieve ticket data from Database and check whether the retrieved ticket has
	// the same Vehicle Reg Number
	Ticket checkTicket = ticketDAO.getTicket("ABCDEF");
	Boolean isExistingUser = ticketDAO.getExistingVehicle("ABCDEF");

	assertEquals(true, isExistingUser);
	assertNotNull(checkTicket.getOutTime());
    }
}
