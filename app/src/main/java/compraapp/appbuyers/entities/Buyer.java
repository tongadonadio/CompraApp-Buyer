package compraapp.appbuyers.entities;

/**
 * Created by pabluc on 30/05/2018.
 */

public class Buyer {

    private int Id;
    private String Email;
    private String Name;
    private String Address;
    private String Phone;
    private int Notifications;
    private double Longitud;
    private double Latitud;

    public Buyer() { }

    @Override
    public String toString(){
        return this.Name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getNotifications() {
        return Notifications;
    }

    public void setNotifications(int notifications) {
        Notifications = notifications;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(int latitud) {
        Latitud = latitud;
    }
}
