package models;

import java.time.LocalDateTime;

public class Booking {
    private Player player;
    private Playground playground;
    private int hours;
    private boolean pendingCancel;
    private LocalDateTime startTime;

    public Booking(Player player, Playground playground, int hours) {
        this.player = player;
        this.playground = playground;
        this.hours = hours;
        this.pendingCancel = false;
        this.startTime = LocalDateTime.now();
    }

    public Player getPlayer() {
        return player;
    }

    public Playground getPlayground() {
        return playground;
    }

    public int getHours() {
        return hours;
    }

    public boolean isPendingCancel() {
        return pendingCancel;
    }

    public void setPendingCancel(boolean pendingCancel) {
        this.pendingCancel = pendingCancel;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}