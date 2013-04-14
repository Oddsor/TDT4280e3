package exercise5.strategies;

import exercise5.IItem;

public class RandomStrategy implements IStrategy{

	@Override
	public int suggestPrice(IItem item, int sellersPrice) {
		
		return Math.random() < 0.5 ? -1 : sellersPrice-20;
	}

	
}
