import java.util.*;

class AddonService {
    private String serviceName;
    private double cost;

    public AddonService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

class AddonServiceManager {
    private Map<String, List<AddonService>> servicesByReservation;

    public AddonServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    public void addService(String reservationId, AddonService service) {
        servicesByReservation
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);
    }

    public double calculateTotalServiceCost(String reservationId) {
        List<AddonService> services = servicesByReservation.get(reservationId);
        if (services == null) return 0.0;

        double total = 0.0;
        for (AddonService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        AddonServiceManager manager = new AddonServiceManager();

        String reservationId = "Single-1";

        manager.addService(reservationId, new AddonService("Breakfast", 500));
        manager.addService(reservationId, new AddonService("Spa", 1000));

        double totalCost = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Add-On Service Selection");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost:");
        System.out.println(totalCost);
    }
}
