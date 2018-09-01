package compraapp.appbuyers.entities;

/**
 * Created by pabluc on 30/05/2018.
 */

public class Offer {

    private int Id;
    private int IdSeller;
    private int IdPublication;
    private int State;
    private int PriceItem;
    private int DeliveryItem;
    private String DescriptionItem;
    private int StateItem;
    private String DeliveryZoneItem;
    private String PhotoItem;

    private Seller Seller;

    public Offer() { }

    @Override
    public String toString(){
        return DescriptionItem;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIdSeller() {
        return IdSeller;
    }

    public void setIdSeller(int idSeller) {
        IdSeller = idSeller;
    }

    public int getIdPublication() {
        return IdPublication;
    }

    public void setIdPublication(int idPublication) {
        IdPublication = idPublication;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getPriceItem() {
        return PriceItem;
    }

    public void setPriceItem(int priceItem) {
        PriceItem = priceItem;
    }

    public int getDeliveryItem() {
        return DeliveryItem;
    }

    public void setDeliveryItem(int deliveryItem) {
        DeliveryItem = deliveryItem;
    }

    public String getDescriptionItem() {
        return DescriptionItem;
    }

    public void setDescriptionItem(String descriptionItem) {
        DescriptionItem = descriptionItem;
    }

    public int getStateItem() {
        return StateItem;
    }

    public void setStateItem(int stateItem) {
        StateItem = stateItem;
    }

    public String getDeliveryZoneItem() {
        return DeliveryZoneItem;
    }

    public void setDeliveryZoneItem(String deliveryZoneItem) {
        DeliveryZoneItem = deliveryZoneItem;
    }

    public String getPhotoItem() {
        return PhotoItem;
    }

    public void setPhotoItem(String photoItem) {
        PhotoItem = photoItem;
    }

    public Seller getSeller() {
        return Seller;
    }

    public void setSeller(Seller seller) {
        this.Seller = seller;
    }
}
