package main;

public class Camera extends Device {
    private int nbDetections;

    public Camera(String room) {
        super(room);
        this.nbDetections = 0;
    }

    public  Camera () {
        this(null);
    }

    public int getNbDetections() {
        return nbDetections;
    }
}
