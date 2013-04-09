package exercise5;


/**
 * Item, nothing moah, nothing less 
 * @author Andreas
 *
 */
public class Item {
	
	private int price;
	private String name;
	
	public Item(String name, int price){
		
			this.name = name;
			this.price = price;
		
		  }
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
