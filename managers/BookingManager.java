// ✅ Fix for owner wallet update during booking
// Make sure to include this in BookingManager.java

package managers;

import models.Booking;
import models.Playground;
import models.Player;
import models.Owner;
import utils.OwnerRegistry;

import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private static BookingManager instance;
    private List<Booking> allBookings = new ArrayList<>();
    private List<Booking> cancelRequests = new ArrayList<>();

    private BookingManager() {}

    public static BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    public Booking book(Player player, Playground playground, int hours) {
        Booking booking = new Booking(player, playground, hours);
        allBookings.add(booking);

        double amount = hours * 50;
        player.setWallet(player.getWallet() - amount);

        // ✅ Credit the real owner from OwnerRegistry
        Owner realOwner = OwnerRegistry.get(playground.getOwner().getUsername());
        if (realOwner != null) {
            realOwner.addToWallet(amount);
            System.out.println("[✔] Credited ₹" + amount + " to owner: " + realOwner.getUsername());
        } else {
            System.out.println("[!] Owner not found in registry: " + playground.getOwner().getUsername());
        }

        return booking;
    }

    public List<Booking> getAllBookings() {
        return allBookings;
    }

    public List<Booking> getBookingsByPlayer(Player player) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : allBookings) {
            if (b.getPlayer().getUsername().equals(player.getUsername())) {
                result.add(b);
            }
        }
        return result;
    }

    public void requestCancel(Booking booking) {
        if (!cancelRequests.contains(booking)) {
            cancelRequests.add(booking);
        }
    }

    public List<Booking> getCancelRequests() {
        return cancelRequests;
    }

    public void cancelBooking(Booking booking) {
        allBookings.remove(booking);
        cancelRequests.remove(booking);
    }

    public void rejectCancelRequest(Booking booking) {
        cancelRequests.remove(booking);
    }
}