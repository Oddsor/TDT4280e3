
package exercise5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Odd
 */
public class InventoryProvider {

    public static List<IItem> getItemList() {
        List<IItem> itemBase = new ArrayList<IItem>();
        //TODO add items
        itemBase.add(new Item("Banana printer", 200));
        itemBase.add(new Item("Television", 400));
        itemBase.add(new Item("Laser printer", 300));
        itemBase.add(new Item("Wii U", 350));
        itemBase.add(new Item("Iron Man dvd", 100));
        itemBase.add(new Item("Avatar 3d bluray", 150));
        itemBase.add(new Item("Russian caviar", 200));
        return itemBase;
    }
    
    public static List<IItem> inventory(int size, List<IItem> excludedItems){
        List<IItem> itemBase = getItemList();
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
        for(IItem excluded: excludedItems){
            for(int i = 0; i < itemBase.size();i++){
                if(excluded.getName().equals(itemBase.get(i).getName())){
                    itemBase.remove(i);
                    break;
                }
            }
        }
        for(int i = 0; i < size; i++){
            int pointer = rand.nextInt(itemBase.size());
            inventory.add(itemBase.get(pointer));
            itemBase.remove(pointer);
        }
        return inventory;
    }
}