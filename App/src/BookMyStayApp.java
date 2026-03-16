import java.util.HashMap;
import java.util.Map;

/**
 * CLASS: Room
 * Domain Model providing descriptive information about rooms.
 */
class Room {
    private String type;
    private int beds;
    private int size;
    private double price;

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayDetails(int availableCount) {
        System.out.println(type + " Room:");
        System.out.println("  Beds: " + beds);
        System.out.println("  Size: " + size + " sqft");
        System.out.println("  Price per night: " + price);
        System.out.println("  Available: " + availableCount);
    }
}

/**
 * CLASS: RoomInventory
 * Acts as the State Holder for room counts.
 */
class RoomInventory {
    private Map<String, Integer> roomCounts = new HashMap<>();

    public void updateInventory(String type, int count) {
        roomCounts.put(type, count);
    }

    /**
     * Provides read-only access to current availability.
     * Returns a copy to prevent accidental mutation of internal state.
     */
    public Map<String, Integer> getRoomAvailability() {
        return new HashMap<>(roomCounts);
    }
}

/**
 * CLASS: RoomSearchService
 * Handles the logic for viewing available rooms without changing state.
 */
class RoomSearchService {
    /**
     * Displays available rooms along with their details and pricing.
     * This method performs read-only access to inventory and room data.
     */
    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Room Search");
        System.out.println("---------------------------");

        // Validation Logic: Check and display rooms only if availability > 0
        if (availability.getOrDefault("Single", 0) > 0) {
            singleRoom.displayDetails(availability.get("Single"));
        }

        if (availability.getOrDefault("Double", 0) > 0) {
            doubleRoom.displayDetails(availability.get("Double"));
        }

        if (availability.getOrDefault("Suite", 0) > 0) {
            suiteRoom.displayDetails(availability.get("Suite"));
        }
    }
}

/**
 * MAIN CLASS: UseCase4RoomSearch
 * Demonstrates the execution flow for Use Case 4.
 */
public class UseCase4RoomSearch {
    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        // 1. Initialize Room Definitions (Domain Objects)
        Room single = new Room("Single", 1, 250, 1500.0);
        Room doubleRm = new Room("Double", 2, 400, 2500.0);
        Room suite = new Room("Suite", 3, 750, 5000.0);

        // 2. Initialize Inventory (State Holder)
        RoomInventory inventory = new RoomInventory();
        inventory.updateInventory("Single", 5);
        inventory.updateInventory("Double", 3);
        inventory.updateInventory("Suite", 2);

        // 3. Initiate Search (Read-Only Operation)
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(inventory, single, doubleRm, suite);

        // Note: System state remains unchanged after this call.
    }
}