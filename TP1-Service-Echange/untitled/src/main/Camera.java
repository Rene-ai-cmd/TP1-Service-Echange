package main;

public class Camera extends Device {
    private int nbDetections;

    public Camera(String room,int nbDetections) {
        super(room);
        nbDetections = 0;
    }

    public  Camera () {
        super("");
    }

    public int getNbDetections() {
        return nbDetections;
    }
}
