package exercise5;

/**
 *
 * @author Odd
 */
public interface IItem {
    /**
     * The name of the item up for auction
     * @return 
     */
    public String getName();
    
    /**
     * The item's value
     * @return 
     */
    public int getPrice();
    
    public void setPrice(int price);
    
    public void setLowestAccepted(int price);
    
    public int getLowestAccepted();
    
    public int getMinimum();
    
    public int getMaximum();
}
