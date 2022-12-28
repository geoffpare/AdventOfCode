package y2022.day13;

import java.util.Objects;

/**
 * A pair of packets, their position number, and stored results if they are in the right order
 */
public class PacketPair {
    private Item left;
    private Item right;
    private Integer position;
    private Boolean isCorrectOrder;

    public PacketPair(Item left, Item right, Integer position) {
        this.left = left;
        this.right = right;
        this.position = position;
    }

    public Boolean isCorrectOrder() {
        if (isCorrectOrder != null) {
            return isCorrectOrder;
        }

        Item l = left;
        Item r = right;

        // Lazy load
        Order order = correctOrderRecurse(l, r);

        if (order == Order.IN) {
            isCorrectOrder = true;
            return true;
        } else if (order == Order.OUT) {
            isCorrectOrder = false;
            return false;
        } else {
            throw new RuntimeException("Input ended in a tie, which I don't think is valid for the examples?");
        }

    }

    public static Order correctOrderRecurse(Item l, Item r) {
        if (l.getType() == ItemType.VALUE && r.getType() == ItemType.VALUE) {
            //System.out.println("Comparing " + l.getValue() + " == " + r.getValue());
            if (l.getValue() < r.getValue()) {
                return Order.IN;
            }
            else if (l.getValue().equals(r.getValue())) {
                return Order.CONTINUE;
            }
            else {
                return Order.OUT;
            }
        }
        // L is value and R is list, convert L to [L] and recurse
        else if (l.getType() == ItemType.VALUE) {
            return correctOrderRecurse(Item.newListItem(l), r);
        }
        // R is value and L is list, convert R to [R] and recurse
        else if (r.getType() == ItemType.VALUE) {
            return correctOrderRecurse(l, Item.newListItem(r));
        }
        // Both lists, iterate over them comparing each item in the list to the other list
        else if (l.getType() == ItemType.LIST && r.getType() == ItemType.LIST) {
            boolean matches = true;
            int minListSize = Math.min(l.getItems().size(), r.getItems().size());

            for (int i=0; i<minListSize; i++) {
                Item tleft = l.getItems().get(i);
                Item tright = r.getItems().get(i);

                // Either inOrder or outOfOrder are terminal, CONTINUE means continue comparing the list members
                Order tMatches = correctOrderRecurse(tleft, tright);
                if (tMatches == Order.OUT || tMatches == Order.IN) {
                    return tMatches;
                }
            }

            // We've gone through the lists for the min size, if they are the same size, they must match, but a tie, so CONTINUE
            if (l.getItems().size() == r.getItems().size()) {
                return Order.CONTINUE;
            }
            // If the left runs out of items first, then they are in order
            else if (l.getItems().size() < r.getItems().size()) {
                return Order.IN;
            }
            // If the right runs out of items first, then they are out of order
            else if (l.getItems().size() > r.getItems().size()) {
                return Order.OUT;
            }
            else {
                throw new RuntimeException("We shouldn't get here");
            }
        }

        throw new RuntimeException("We shouldn't get here either");
    }

    public void setCorrectOrder(Boolean correctOrder) {
        isCorrectOrder = correctOrder;
    }

    public Item getLeft() {
        return left;
    }

    public void setLeft(Item left) {
        this.left = left;
    }

    public Item getRight() {
        return right;
    }

    public void setRight(Item right) {
        this.right = right;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketPair that = (PacketPair) o;
        return Objects.equals(left, that.left) && Objects.equals(right, that.right) && Objects.equals(position, that.position) && Objects.equals(isCorrectOrder, that.isCorrectOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, position, isCorrectOrder);
    }
}
