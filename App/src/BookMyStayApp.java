import java.util.*;

// -------------------- Reservation --------------------
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// -------------------- Booking Queue --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- Room Inventory --------------------
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();
    private Map<String, Integer> counters = new HashMap<>();

    public RoomInventory() {
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

    public int getAvailability(String type) {
        return rooms.get(type);
    }

    public Map<String, Integer> getAllRooms() {
        return rooms;
    }
}

// -------------------- Allocation Service --------------------
class RoomAllocationService {
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomId = inventory.allocateRoom(reservation.roomType);

        if (roomId != null) {
            System.out.println("Booking confirmed for Guest: " 
                + reservation.guestName + ", Room ID: " + roomId);
        } else {
            System.out.println("No rooms available for Guest: " 
                + reservation.guestName);
        }
    }
}

// -------------------- Concurrent Processor --------------------
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService) {

        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation;

            // 🔒 Critical Section 1: Queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                reservation = bookingQueue.getRequest();
            }

            // 🔒 Critical Section 2: Inventory access
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService));

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService));

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Final Inventory
        System.out.println("\nRemaining Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getAllRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
