package compraapp.appbuyers.entities;

/**
 * Created by pabluc on 30/05/2018.
 */

public class Seller {

    public int Id;
    public String Email;
    public String Name;
    public String Address;
    public String Phone;
    public int Notifications;
    private double Longitud;
    private double Latitud;

    public Seller() { }

    @Override
    public String toString(){
        return Name;
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

    public void setLongitud(int longitud) {
        Longitud = longitud;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(int latitud) {
        Latitud = latitud;
    }
}
