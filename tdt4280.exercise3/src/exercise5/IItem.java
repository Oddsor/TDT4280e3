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
     * The cost of this object, selling at any lower will result in a loss
     * @return 
     */
    public int productionCost();
    
    public int getPrice();
}
