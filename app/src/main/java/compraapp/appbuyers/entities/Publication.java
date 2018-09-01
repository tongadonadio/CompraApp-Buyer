package compraapp.appbuyers.entities;


/**
 * Created by pabluc on 30/05/2018.
 */

public class Publication {

    private int Id;
    private int Price;
    private String Description;
    private int IdBuyer;
    private int State;
    private int DeliveryItem;
    private String DescriptionItem;
    private int PriceMinItem;
    private int PriceMaxItem;
    private int StateItem;
    private int CountOffers;

    public Publication() { }

    @Override
    public String toString(){
        return this.Description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getIdBuyer() {
        return IdBuyer;
    }

    public void setIdBuyer(int idBuyer) {
        IdBuyer = idBuyer;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
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

    public int getPriceMinItem() {
        return PriceMinItem;
    }

    public void setPriceMinItem(int priceMinItem) {
        PriceMinItem = priceMinItem;
    }

    public int getPriceMaxItem() {
        return PriceMaxItem;
    }

    public void setPriceMaxItem(int priceMaxItem) {
        PriceMaxItem = priceMaxItem;
    }

    public int getStateItem() {
        return StateItem;
    }

    public void setStateItem(int stateItem) {
        StateItem = stateItem;
    }

    public int getCountOffers() {
        return CountOffers;
    }

    public void setCountOffers(int countOffers) {
        CountOffers = countOffers;
    }
}
