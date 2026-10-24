package main;

public class Light extends Device {
    private int intensity;
    private int colorTemp;

    public Light(String room, int intensity, int colorTemp ) {
        super(room);
        this.intensity = intensity;
        this.colorTemp = colorTemp;
    }

    public Light() { this (null, 50, 3200); }

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
        if ( intensity < 0 || intensity > 100)
            throw  new IllegalArgumentException("intensité invalide (0-100)");
        this.intensity = intensity;

    }
}
