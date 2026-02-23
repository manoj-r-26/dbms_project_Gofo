import models.Booking;
import models.Player;
import models.Playground;
import models.Owner;
import utils.OwnerRegistry;

import java.util.ArrayList;

public class BookingManager {
    private static BookingManager instance;
    private ArrayList<Booking> bookings;

    private BookingManager() {
        bookings = new ArrayList<>();
    }

    public static BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    public Booking book(Player player, Playground playground, int hours) {
        double amount = hours * 50;

        // Deduct from player's wallet
        player.setWallet(player.getWallet() - amount);

        // ✅ Credit to actual owner instance
        Owner playgroundOwner = OwnerRegistry.get(playground.getOwner().getUsername());
        if (playgroundOwner != null) {
            playgroundOwner.setWallet(playgroundOwner.getWallet() + amount);
        }

        // Create and save booking
        Booking booking = new Booking(player, playground, hours);
        bookings.add(booking);
        return booking;
    }

    public ArrayList<Booking> getBookingsByPlayer(Player player) {
        ArrayList<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getPlayer().getUsername().equals(player.getUsername())) {
                result.add(b);
            }
        }
        return result;
    }

    public ArrayList<Booking> getAllBookings() {
        return bookings;
    }

    public ArrayList<Booking> getPendingCancelRequests() {
        ArrayList<Booking> pending = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.isPendingCancel()) {
                pending.add(b);
            }
        }
        return pending;
    }

    public void approveCancel(Booking booking) {
        booking.setPendingCancel(false);
        double refundAmount = booking.getHours() * 50;
        booking.getPlayer().addToWallet(refundAmount);
        bookings.remove(booking);
    }
}