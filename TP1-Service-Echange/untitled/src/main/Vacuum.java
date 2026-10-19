package cal.info.java3;

import java.util.Random;

public class Vacuum extends Device {
    private double chargeLevel;
    private String task;

    public Vacuum(String room) {
        super(room);
        this.stop();
    }

    public Vacuum() {
        this(null);
    }

    @Override
    public void turnOff() {
        this.stop();
        super.turnOff();
    }

    @Override
    public String getRoom() {
        if (task.equals("Nettoyage de la maison")) {
            try {
                this.cleanAll();
            }
            catch (IllegalStateException e) {
                this.stop();
            }
        }
        return super.getRoom();
    }

    public int getChargeLevel() {
        return (int)(chargeLevel * 100);
    }

    public String getTask() {
        return task;
    }

    public void clean(String room) {
        if (!isOn())
            throw new IllegalStateException("Aspirateur éteint");
        if (chargeLevel < 0.2)
            throw new IllegalStateException("Charge insuffisante");
        this.setRoom(room);
        this.task = "Nettoyage d'une pièce";
        this.chargeLevel -= Math.random() * 0.2;
    }

    public void cleanAll() {
        Random rng = new Random();
        String room = rooms.get(rng.nextInt(rooms.size()));
        this.clean(room);
        this.task = "Nettoyage de la maison";
    }

    public void stop() {
        this.task = "idle";
        this.chargeLevel = 1.0;
    }
}
