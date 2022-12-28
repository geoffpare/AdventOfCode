package y2022.day13;

import java.util.Comparator;
import java.util.Objects;

/**
 * Simply represents a known parsed packet, which is really just a handle to a fully formed/parsed Item
 * But unlike Item, this will implement Comparable so we can sort packets per part 2
 */
public class Packet implements Comparable {
    private Item item;

    public Packet (Item i) {
        item = i;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public int compareTo(Object o) {
        Packet otherPacket = (Packet)o;
        Order order = PacketPair.correctOrderRecurse(item, otherPacket.getItem());

        if (order == Order.IN) {
            return -1;
        } else if (order == Order.OUT) {
            return 1;
        }

        throw new RuntimeException("There shouldn't be ties amongst packets");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return Objects.equals(item, packet.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }


}
