
package exercise5.strategies;

import exercise5.IItem;

/**
 *
 * @author Odd
 * @author Andreas
 */
public interface IStrategy {
    
    /**
     * 
     * @param item
     * @param sellersPrice
     * @return -1 if price is deemed reasonable, otherwise suggest new price
     */
    public int considerSellersBid(IItem item, int sellersPrice);
    public int considerBuyersBid(IItem item, int buyersBid);
}
