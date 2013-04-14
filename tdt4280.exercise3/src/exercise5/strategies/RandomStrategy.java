package exercise5.strategies;

import exercise5.IItem;

public class RandomStrategy implements IStrategy{

	@Override
	public int considerSellersBid(IItem item, int sellersPrice) {
		// TODO Auto-generated method stub
		return Math.random() < 0.3 ? -1 : sellersPrice-20;
	}

	@Override
	public int considerBuyersBid(IItem item, int buyersBid) {
		// TODO Auto-generated method stub
		return Math.random() < 0.7 ? -1 : buyersBid + 20;
	}

	

	
}
