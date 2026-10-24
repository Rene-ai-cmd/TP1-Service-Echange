package main;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//import tools.jackson.databind.annotation.JsonSubTypes;
//import tools.jackson.databind.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "subtype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Light.class, name = "Light"),
        @JsonSubTypes.Type(value = Camera.class, name = "Camera"),
        @JsonSubTypes.Type(value = Vacuum.class, name = "Vacuum"),
})

public abstract class Device {
    protected static final List<String> rooms = new ArrayList<>(Arrays.asList("Chambre", "Cuisine", "Salon"));

    public static List<String> listRooms() {
        return rooms;
    }

    public static void addRoom(String room) {
        if (room.isBlank())
            throw new IllegalArgumentException("Pièce invalide");
        if (rooms.contains(room))
            throw new IllegalArgumentException("Pièce existante");
        rooms.add(room);
    }

    private static int lastId = 0;
    private final int id;
    private final String subtype;

    private boolean on = false;
    private String room = null;

    public Device(String room) {
        this.id = ++Device.lastId;
        this.subtype = this.getClass().getSimpleName();
        if (room != null)
            this.setRoom(room);
    }

    public int getId() {
        return id;
    }

    public boolean isOn() {
        return on;
    }

    public void turnOn() {
        this.on = true;
    }

    public void turnOff() {
        this.on = false;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        if (rooms.contains(room))
            this.room = room;
        else
            throw new IllegalArgumentException("Pièce inconnue");
    }

    @Override
    public String toString() {
        return String.format("#%d %s (%s)", id, subtype, room);
    }
}
