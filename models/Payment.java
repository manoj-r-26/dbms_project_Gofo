package models;

public class Payment {
    private int id;
    private int bookingId;
    private double amount;

    public Payment(int id, int bookingId, double amount) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
    }

    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
}
