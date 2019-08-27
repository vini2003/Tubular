package tubular.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import tubular.block.BlockTubeConnector;
import tubular.block.BlockTubeConnectorMode;
import tubular.inventory.InventoryHandler;
import tubular.inventory.InventoryHolder;
import tubular.network.NetworkHandler;
import tubular.network.NetworkHolder;
import tubular.registry.EntityRegistry;

public class BlockTubeConnectorEntity extends BlockEntity implements Tickable {
    public final Integer ticksBase = 5;
    public Integer ticksRemaining = 0;
    public Boolean initialUpdate = false;
    public InventoryHolder outputInventory;
    public Item filterItem;
    public List<InventoryHolder> inputInventories = new ArrayList<>();
    public NetworkHolder networkHolder;



    public BlockTubeConnectorEntity() {
        super(EntityRegistry.TUBE_CONNECTOR_ENTITY);
        networkHolder = new NetworkHolder();
    }
    
    @Override
    public CompoundTag toTag(CompoundTag blockTag) {
        if (filterItem != null) {
            blockTag.putString("filterItem", filterItem.toString());
        }
        return super.toTag(blockTag);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        filterItem = Registry.ITEM.get(new Identifier(tag.getString("filterItem")));
    }

    public void clear() {
        networkHolder.connectorList.clear();
        inputInventories.clear();
        outputInventory = null;
    }

    public void tick() {
        // (false) INPUT  : from where it COMES
        // (true)  OUTPUT : to  where  it  GOES

        --ticksRemaining;

        BlockPos thisPosition = this.getPos();
        BlockState thisBlockState = world.getBlockState(thisPosition);

        if (!initialUpdate) {
            NetworkHandler.updateNetwork(this.getPos(), this.getWorld());
            initialUpdate = true;
        }
        if (thisBlockState.get(BlockTubeConnector.TUBE_MODE) == BlockTubeConnectorMode.REQUESTER && ticksRemaining <= 0) {
            ticksRemaining = ticksBase; 
            inputInventories.clear();
            inputInventories = InventoryHandler.getInput(networkHolder, world, true);
            outputInventory = InventoryHandler.getInventoryData(world, thisPosition);
            if (outputInventory != null) {
                for (InventoryHolder inputInventory : inputInventories) {
                    if (inputInventory != null && outputInventory != null) {
                        int[] availableInputSlots = InventoryHandler.getAvailableSlotList(inputInventory, world).toArray();
                        int[] availableOutputSlots = InventoryHandler.getAvailableSlotList(outputInventory, world).toArray();
                        for (Integer inputSlot : availableInputSlots) {
                            if (InventoryHandler.canExtract(inputInventory.getInventory(), inputSlot, filterItem) && !world.getBlockState(this.getPos()).get(BlockTubeConnector.WILDCARD)) {
                                for (Integer outputSlot : availableOutputSlots) {
                                    if (InventoryHandler.canInsert(outputInventory.getInventory(), outputSlot, filterItem)) {
                                        InventoryHandler.makeTransfer(inputInventory.getInventory(), outputInventory.getInventory(), inputSlot, outputSlot, filterItem, outputInventory.getInventory().getInvStack(outputSlot).isEmpty());
                                        return;
                                    }
                                }
                            }
                            else if (InventoryHandler.canExtractWildcard(inputInventory, inputSlot) && world.getBlockState(this.getPos()).get(BlockTubeConnector.WILDCARD)) {
                                for (Integer outputSlot : availableOutputSlots) {
                                    if (InventoryHandler.canInsertWildcard(outputInventory.getInventory(), outputSlot, inputInventory.getInventory().getInvStack(inputSlot))) {
                                        InventoryHandler.makeTransfer(inputInventory.getInventory(), outputInventory.getInventory(), inputSlot, outputSlot, inputInventory.getInventory().getInvStack(inputSlot).getItem(), outputInventory.getInventory().getInvStack(outputSlot).isEmpty());
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }      
            }
        }
    }
}