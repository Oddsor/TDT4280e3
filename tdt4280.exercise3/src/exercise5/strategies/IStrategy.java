
package exercise5.strategies;

import exercise5.IItem;

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
