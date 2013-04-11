package exercise5;

/**
 * Item, nothing moah, nothing less
 *
 * @author Andreas
 *
 */
public class Item implements IItem {

    private int price;
    private String name;

    public Item(String name, int price) {

        this.name = name;
        this.price = price;

    }

    @Override
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int productionCost() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
