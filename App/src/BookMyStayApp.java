import java.util.*;

class RoomInventory {
    private Map<String, Integer> rooms;
    private Map<String, Integer> counters;

    public RoomInventory() {
        rooms = new HashMap<>();
        counters = new HashMap<>();

        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);

        counters.put("Single", 1);
        counters.put("Double", 1);
        counters.put("Suite", 1);
    }

    public String allocateRoom(String type) {
        if (rooms.containsKey(type) && rooms.get(type) > 0) {
            String id = type + "-" + counters.get(type);
            counters.put(type, counters.get(type) + 1);
            rooms.put(type, rooms.get(type) - 1);
            return id;
        }
        return null;
    }

    public void releaseRoom(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }

    public int getAvailability(String type) {
        return rooms.get(type);
    }
}

class CancellationService {
    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid cancellation request.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        inventory.releaseRoom(roomType);
        releasedRoomIds.push(reservationId);
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    public void showRollbackHistory() {
        System.out.println("Rollback History (Most Recent First):");
        while (!releasedRoomIds.isEmpty()) {
            System.out.println("Released Reservation ID: " + releasedRoomIds.pop());
        }
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        String reservationId = inventory.allocateRoom("Single");
        service.registerBooking(reservationId, "Single");

        service.cancelBooking(reservationId, inventory);

        service.showRollbackHistory();

        System.out.println("Updated Single Room Availability: " + inventory.getAvailability("Single"));
    }
}
