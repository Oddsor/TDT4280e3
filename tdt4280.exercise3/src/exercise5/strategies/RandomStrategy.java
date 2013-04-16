package exercise5.strategies;

import exercise5.IItem;

public class RandomStrategy implements IStrategy{

	@Override
	public int considerSellersBid(IItem item, int sellersPrice) {
            double cheap = 1.0 - (sellersPrice / (item.getPrice() * 2.0));
            return Math.random() < cheap ? -1 : sellersPrice - (int) (item.getPrice() * 0.2);
	}

	@Override
	public int considerBuyersBid(IItem item, int buyersBid) {
		if(buyersBid < item.getMinimum()){
                    buyersBid = item.getMinimum();
                }
                double risk = (1.0 - (buyersBid / item.getPrice()) * 2);
		return Math.random() > risk ? buyersBid : buyersBid + (int) (item.getPrice() * 0.2);
	}
        
        public double getRisk(int sellersPrice, IItem item){
            double risk = (item.getPrice() - sellersPrice) / item.getPrice();
            return risk;
        }
}
