
package exercise5;

/**
 *
 * @author Odd
 */
public interface IStrategy {
    
    /**
     * 
     * @param item
     * @param sellersPrice
     * @return -1 if price is deemed reasonable, otherwise suggest new price
     */
    public int suggestPrice(IItem item, int sellersPrice);
}
