package y2022.day13;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Class can represent an entire packet as a packet via the outer most item in the packet, or an individual item
 * within a packet
 * eg packet: [1,[2,[3,[4,[5,6,7]]]],8,9]
 * eg packet: []
 */
public class Item {
    private ItemType type;
    private List<Item> items;
    private Integer value;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void addItem(Item i) {
        items.add(i);
    }

    public static Item newValueItem(Integer value) {
        Item item = new Item();
        item.setItems(Lists.newArrayList());
        item.setValue(value);
        item.setType(ItemType.VALUE);
        return item;
    }

    public static Item newListItem() {
        Item item = new Item();
        item.setItems(Lists.newArrayList());
        item.setType(ItemType.LIST);
        return item;
    }

    public static Item newListItem(Item i) {
        Item item = newListItem();
        item.addItem(i);
        return item;
    }

    public static Item newListItem(List<Item> is) {
        Item item = new Item();
        item.setItems(is);
        item.setType(ItemType.LIST);
        return item;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == ItemType.VALUE) {
            sb.append(value + ",");
        } else if (type == ItemType.LIST) {
            sb.append("[");
            for (Item i : items) {
                sb.append(i.toString());
            }
            sb.append("]");
        }

        return sb.toString();
    }

}
