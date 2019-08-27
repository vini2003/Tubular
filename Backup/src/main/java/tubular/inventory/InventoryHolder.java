package tubular.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;



public class InventoryHolder {
    private InventoryType inventoryType;
    private Inventory inventoryBase;
    private SidedInventory inventorySided;
    private BlockPos entityPosition;
    private BlockPos accessPosition;
    private Direction accessDirection;

    public InventoryHolder(Inventory temporaryInventory, BlockPos temporaryEntityPosition, BlockPos temporaryAccessDirection, Direction temporaryDirection) {
        this.inventoryType = InventoryType.INVENTORY_BASE;
        this.inventoryBase = temporaryInventory;
        this.entityPosition = temporaryEntityPosition;
        this.accessPosition = temporaryAccessDirection;
        this.accessDirection = temporaryDirection;
    }
    public InventoryHolder(SidedInventory temporaryInventory, BlockPos temporaryEntityPosition, BlockPos temporaryAccessDirection, Direction temporaryDirection) {
        this.inventoryType = InventoryType.INVENTORY_SIDED;
        this.inventorySided = temporaryInventory;
        this.entityPosition = temporaryEntityPosition;
        this.accessPosition = temporaryAccessDirection;
        this.accessDirection = temporaryDirection;
    }

    public InventoryType getInventoryType() {
        return this.inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public BlockPos getInventoryPosition() {
        return this.entityPosition;
    }

    public void setInventoryPosition(BlockPos blockPosition) {
        this.entityPosition = blockPosition;
    }

    public BlockPos getAccessPosition() {
        return this.accessPosition;
    }

    public void setAccessPosition(BlockPos blockPosition) {
        this.accessPosition = blockPosition;
    }

    public Direction getAccessDirection() {
        return this.accessDirection;
    }

    public void setAccessDirection(Direction direction) {
        this.accessDirection = direction;
    }

    public Inventory getInventory() {
        switch (this.inventoryType) {
            case INVENTORY_BASE:
                return this.inventoryBase;
            case INVENTORY_SIDED:
                return this.inventorySided;
        }
        return null;
    }
}