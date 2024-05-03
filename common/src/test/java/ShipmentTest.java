import com.wama.DatabaseManager;
import com.wama.Shipment;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
class ShipmentTest {

    @org.junit.jupiter.api.Test
    void getShipmentsByCustomerId() {
        // f1e06ef5-578c-42c5-9be7-efc9a25ce2ea should have 5 shipments
        ArrayList<HashMap<String, String>> shipments = Shipment.getShipmentsByCustomerId("f1e06ef5-578c-42c5-9be7-efc9a25ce2ea");
        assertEquals(5, shipments.size());
    }

    @org.junit.jupiter.api.Test
    void runRawQueryTest() {
        String query = "SELECT Shipments.id, Shipments.order_id, Shipments.shipment_date, Shipments.status, Shipments.tracking_number " +
                "FROM Shipments " +
                "JOIN Orders ON Shipments.order_id = Orders.id " +
                "WHERE Orders.customer_id = 'f1e06ef5-578c-42c5-9be7-efc9a25ce2ea'";
        ArrayList<HashMap<String, String>> shipments = DatabaseManager.getCustomerShipments(query);
        assertEquals(5, shipments.size());
    }
}