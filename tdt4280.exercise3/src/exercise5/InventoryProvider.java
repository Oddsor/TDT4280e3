
package exercise5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Odd
 */
public class InventoryProvider {
    
    private List<IItem> itemBase;

    public InventoryProvider() {
        this.itemBase = new ArrayList<IItem>();
        //TODO add items
        itemBase.add(new Item("Banana printer", 200));
        itemBase.add(new Item("Television", 400));
        itemBase.add(new Item("Laser printer", 300));
        itemBase.add(new Item("Wii U", 350));
        itemBase.add(new Item("Iron Man dvd", 100));
        itemBase.add(new Item("Avatar 3d bluray", 150));
        itemBase.add(new Item("Russian caviar", 200));
        
    }
    
    public List<IItem> inventory(int size, List<IItem> excludedItems){
        if(size > itemBase.size()){
            try{
                throw new Exception("Inventory too large (" + size + "), itembase"
                        + "holds " + itemBase.size() + " items");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
            
        List<IItem> inventory = new ArrayList<IItem>();
        Random rand = new Random();
        for(int i = 0; i < size; i++){
            List<IItem> culledItems = new ArrayList<IItem>(itemBase);
            if(excludedItems != null){
                culledItems.removeAll(excludedItems);
            }
            culledItems.removeAll(inventory);
            inventory.add(culledItems.get(rand.nextInt(culledItems.size())));
        }
        return inventory;
    }

    public static InventoryProvider getInstance() {
        return InventoryProviderHolder.INSTANCE;
    }
    
    private static class InventoryProviderHolder {

        private static final InventoryProvider INSTANCE = new InventoryProvider();
    }
}
