package exercise5;

/**
 * Item, nothing moah, nothing less
 *
 * @author Andreas
 *
 */
public class Item implements IItem {

    private int price;
    private int lowestAccepted;

    @Override
    public int getLowestAccepted() {
        return lowestAccepted;
    }

    @Override
    public void setLowestAccepted(int lowestAccepted) {
        this.lowestAccepted = lowestAccepted;
    }
    
    private String name;
    
    private int minimum;
    private int maximum;

    public int getMaximum() {
        return maximum;
    }

    public Item(String name, int price) {

        this.name = name;
        this.price = price;
        
        minimum = (int) (price * 0.5);
        maximum = (int) (price * 1.5);
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
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
    public int getMinimum() {
        return minimum;
    }
}
