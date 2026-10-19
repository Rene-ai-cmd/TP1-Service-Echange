package main;

public class Light extends Device {
    private int intensity;
    private int colorTemp;

    public Light(String room, int intensity, int colorTemp ) {
        super(room);
    }

    public Light() {
        super("");
    }

    public int getColorTemp() {
        return colorTemp;
    }

    public void setColorTemp(int colorTemp) {
        this.colorTemp = colorTemp;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
        if (!(intensity > 0) && intensity < 100) {
           System.out.println("L'intensité doit être 0 et 100");
        };
    }
}
