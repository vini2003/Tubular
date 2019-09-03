package tubular.utility;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import tubular.entity.BlockTubeConnectorEntity;

public class WFilterSlot extends WItemSlot {
    final Direction direction;
    final Integer slot;
    Inventory inventory;
    BlockTubeConnectorEntity entity;
    public WFilterSlot(Inventory inventory, int slot, Direction direction, BlockTubeConnectorEntity entity) {
        super(inventory, slot, 1, 1, false, false);
        this.direction = direction;
        this.slot = slot;
        this.inventory = inventory;
        this.entity = entity;
    }

    @Override
	public void onClick(int x, int y, int button) {
        super.onClick(x, y, button);
        if (inventory.getInvStack(slot).isEmpty()) {
            entity.setFilter(direction, Items.AIR);
            entity.setWildcard(direction, false);
        } else {
            entity.setFilter(direction, inventory.getInvStack(slot).getItem());
            entity.setWildcard(direction, true);
        }
    }
    
    public Boolean isEmpty() {
        if (this.inventory.getInvStack(slot).getItem() == Items.AIR) {
            return true;
        } else {
            return false;
        }
    }
};