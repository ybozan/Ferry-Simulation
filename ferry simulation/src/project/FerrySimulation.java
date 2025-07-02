package project;

import java.util.*;

public class FerrySimulation {

    enum Side { LEFT, RIGHT }

    enum VehicleType {
        CAR(1),    // Car takes 1 unit space
        MINIBUS(2), // Minibus takes 2 unit space
        TRUCK(3);  // Truck takes 3 unit space

        private final int size;

        VehicleType(int size) {
            this.size = size;
        }

        public int getSize() { return size; }
    }

    static class Vehicle {
        private static int carCount = 0;
        private static int minibusCount = 0;
        private static int truckCount = 0;

        final int id;
        final VehicleType type;
        final Side homeSide;
        Side currentSide;

        public Vehicle(VehicleType type, Side side) {
            this.type = type;
            this.homeSide = side;
            this.currentSide = side;
            switch (type) {
                case CAR:
                    this.id = ++carCount;
                    break;
                case MINIBUS:
                    this.id = ++minibusCount;
                    break;
                case TRUCK:
                    this.id = ++truckCount;
                    break;
                default:
                    this.id = 0; // Should not happen
            }
        }

        @Override
        public String toString() {
            String name;
            switch (type) {
                case CAR:      name = "car";    break;
                case MINIBUS:  name = "minibus"; break;
                case TRUCK:    name = "truck";  break;
                default:       name = type.name().toLowerCase();
            }
            return String.format("%s #%d", name, id);
        }
    }

    static class Ferry {
        private final int capacity = 20; // Kapasite 20 olarak ayarlandı
        private Side position = randomSide(); // Ferry rastgele bir yakadan başlasın
        private final List<Vehicle> onboard = new ArrayList<>();
        private long crossTimeMs = 5000; // Time it takes for the ferry to cross

        public Side getPosition() { return position; }

        public void cross() {
            System.out.printf("Ferry departing from %s side. Loaded vehicle units: %d%n",
                                position, onboard.stream().mapToInt(v -> v.type.getSize()).sum());
            try {
                Thread.sleep(crossTimeMs); // Simulate crossing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Ferry crossing interrupted.");
            }
            position = (position == Side.LEFT ? Side.RIGHT : Side.LEFT);
            System.out.printf("Ferry arrived at %s side.%n", position);
        }

        public boolean canLoad(Vehicle v) {
            int used = onboard.stream().mapToInt(x -> x.type.getSize()).sum();
            return used + v.type.getSize() <= capacity;
        }

        public void load(Vehicle v) {
            onboard.add(v);
            System.out.println("  Loaded: " + v);
        }

        public List<Vehicle> unloadAll() {
            List<Vehicle> out = new ArrayList<>(onboard);
            onboard.clear();
            for (Vehicle v : out) {
                v.currentSide = position;
                System.out.println("  Unloaded: " + v);
            }
            return out;
        }

        public boolean isEmpty() { return onboard.isEmpty(); }
    }

    static class TollBooth {
        private final String name;

        public TollBooth(String name) {
            this.name = name;
        }

        public void processVehicle(Vehicle vehicle) {
            System.out.printf("    %s passed through %s.%n", vehicle, name);
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Vehicle> all = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            all.add(new Vehicle(VehicleType.CAR, randomSide()));
        for (int i = 0; i < 10; i++)
            all.add(new Vehicle(VehicleType.MINIBUS, randomSide()));
        for (int i = 0; i < 8; i++)
            all.add(new Vehicle(VehicleType.TRUCK, randomSide()));

        Deque<Vehicle> leftQueue = new ArrayDeque<>();
        Deque<Vehicle> rightQueue = new ArrayDeque<>();
        Collections.shuffle(all);
        for (Vehicle v : all) {
            if (v.currentSide == Side.LEFT)  leftQueue.addLast(v);
            else                             rightQueue.addLast(v);
        }

        Ferry ferry = new Ferry();

        TollBooth leftTollBooth1 = new TollBooth("Gate 1");
        TollBooth leftTollBooth2 = new TollBooth("Gate 2");
        TollBooth rightTollBooth1 = new TollBooth("Gate 1");
        TollBooth rightTollBooth2 = new TollBooth("Gate 2");

        TollBooth[] leftTollBooths = {leftTollBooth1, leftTollBooth2};
        TollBooth[] rightTollBooths = {rightTollBooth1, rightTollBooth2};
        Random random = new Random();

        int trip = 0;

        while (!(leftQueue.isEmpty() && rightQueue.isEmpty() && ferry.isEmpty())) {
            trip++;
            System.out.println("\n=== Trip " + trip + " (at " + ferry.getPosition() + " side) ===");

            Deque<Vehicle> currentQueue = (ferry.getPosition() == Side.LEFT ? leftQueue : rightQueue);
            TollBooth[] currentTollBooths = (ferry.getPosition() == Side.LEFT ? leftTollBooths : rightTollBooths);

            // Her aracı tek tek kontrol ederek yükle
            while (!currentQueue.isEmpty() && ferry.canLoad(currentQueue.peekFirst())) {
                Vehicle vehicle = currentQueue.pollFirst();
                TollBooth selectedTollBooth = currentTollBooths[random.nextInt(currentTollBooths.length)];
                selectedTollBooth.processVehicle(vehicle);
                ferry.load(vehicle); // Direkt olarak ferry'ye yükle
            }

            ferry.cross();

            List<Vehicle> landed = ferry.unloadAll();
            for (Vehicle v : landed) {
                if (v.currentSide != v.homeSide) {
                    if (v.currentSide == Side.LEFT)  leftQueue.addLast(v);
                    else                             rightQueue.addLast(v);
                }
            }
        }

        System.out.println("\nAll vehicles returned home.");
        System.out.printf("Total number of trips: %d%n", trip);
    }

    private static Side randomSide() {
        return Math.random() < 0.5 ? Side.LEFT : Side.RIGHT;
    }
}
