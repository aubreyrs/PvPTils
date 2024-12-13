package cat.aubrey.pvptils.client.data;

import net.minecraft.item.Item;

public class PlayerAttackTracker {
    private static int lastAttackedEntityId = -1;
    private static Item lastAttackedWithItem = null;

    public static void setLastAttackedEntityId(int id) {
        lastAttackedEntityId = id;
    }

    public static int getLastAttackedEntityId() {
        return lastAttackedEntityId;
    }

    public static void setLastAttackedWithItem(Item item) {
        lastAttackedWithItem = item;
    }

    public static Item getLastAttackedWithItem() {
        return lastAttackedWithItem;
    }
}
