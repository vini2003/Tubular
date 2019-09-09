package tubular.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import tubular.entity.BlockTubeConnectorEntity;
import tubular.network.NetworkHolder;
import tubular.utility.BlockMode;
import tubular.utility.Tuple;

public class InventoryHandler {
    public static List<InventoryHolder> getInput(NetworkHolder networkHolder, BlockPos position, World world, Boolean connectorMode) {
        List<InventoryHolder> inputList = new ArrayList<>();
        List<BlockPos> scanPositions = new ArrayList<>();
        scanPositions = networkHolder.connectorList;
        scanPositions.add(position);
        for (BlockPos blockPosition : networkHolder.connectorList) {
            BlockEntity blockEntityTemporary = world.getBlockEntity(blockPosition);
            BlockTubeConnectorEntity blockEntity = null;
            if (blockEntityTemporary != null && blockEntityTemporary instanceof BlockTubeConnectorEntity) {
                blockEntity = (BlockTubeConnectorEntity)blockEntityTemporary;
            } else {
                return null;
            }
            for (Tuple<Direction, BlockMode> tuple : blockEntity.connectorModes) {
                if (tuple.getSecond() == BlockMode.PROVIDER && hasInventoryData(world, blockPosition.offset(tuple.getFirst()))) {
                    inputList.add(getInventoryData(world, blockPosition.offset(tuple.getFirst()), tuple.getFirst().getOpposite()));
                }
            }
        }
        return inputList;
    }
    
    public static List<InventoryHolder> getOutput(BlockPos blockPosition, World world, Boolean connectorMode) {
        List<InventoryHolder> inputList = new ArrayList<>();
        BlockEntity blockEntityTemporary = world.getBlockEntity(blockPosition);
        BlockTubeConnectorEntity blockEntity = null;
        if (blockEntityTemporary != null && blockEntityTemporary instanceof BlockTubeConnectorEntity) {
            blockEntity = (BlockTubeConnectorEntity)blockEntityTemporary;
        } else {
            return null;
        }
        for (Tuple<Direction, BlockMode> tuple : blockEntity.connectorModes) {
            if (tuple.getSecond() == BlockMode.REQUESTER && hasInventoryData(world, blockPosition.offset(tuple.getFirst()))) {
                inputList.add(getInventoryData(world, blockPosition.offset(tuple.getFirst()), tuple.getFirst().getOpposite()));
            }
        }
        return inputList;
    }

    public static InventoryHolder getInventoryData(World world, BlockPos blockPosition, Direction direction) {
        Direction accessDirection = direction;
        BlockPos targetPosition = blockPosition;
        BlockState targetState = world.getBlockState(targetPosition);
        Block targetBlock = targetState.getBlock();
        BlockEntity targetEntity = null;

        if (targetBlock.hasBlockEntity()) {
            targetEntity = world.getBlockEntity(targetPosition);
        }
        if (targetBlock instanceof InventoryProvider) {
            return (new InventoryHolder(((InventoryProvider)targetBlock).getInventory(targetState, world, targetPosition),
                                        targetPosition, 
                                        blockPosition,
                                        accessDirection));
        } else if (targetEntity != null) {
            if (targetEntity instanceof Inventory) {
                if (targetEntity instanceof ChestBlockEntity) {
                    return (new InventoryHolder(ChestBlock.getInventory(targetState, world, targetPosition, true),
                                                targetPosition, 
                                                blockPosition,
                                                accessDirection));
                }
                else if (targetEntity instanceof SidedInventory) {
                    return (new InventoryHolder((SidedInventory)targetEntity,
                                                targetPosition, 
                                                blockPosition,
                                                accessDirection));
                }
                else if (targetEntity instanceof Inventory) {
                    return (new InventoryHolder((Inventory)targetEntity,
                                                targetPosition, 
                                                blockPosition,
                                                accessDirection));
                }
            }
        }
        return null;
    }

    public static Boolean hasInventoryData(World world, BlockPos targetPosition) {
        BlockState targetState = world.getBlockState(targetPosition);
        Block targetBlock = targetState.getBlock();
        BlockEntity targetEntity = null;

        if (targetBlock.hasBlockEntity()) {
            targetEntity = world.getBlockEntity(targetPosition);
        }
        if (targetBlock instanceof InventoryProvider) {
            return true;
        } else if (targetEntity != null) {
            if (targetEntity instanceof Inventory) {
                if (targetEntity instanceof ChestBlockEntity) {
                    return true;
                }
                else if (targetEntity instanceof SidedInventory) {
                    return true;
                }
                else if (targetEntity instanceof Inventory) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void makeTransfer(Inventory inputInventory, Inventory outputInventory, Integer inputSlot, Integer outputSlot, Item filterItem, Boolean isEmpty) {
        if (!isEmpty) {
            inputInventory.getInvStack(inputSlot).decrement(1);
            outputInventory.getInvStack(outputSlot).increment(1);
            return;
        }
        else {
            inputInventory.getInvStack(inputSlot).decrement(1);
            outputInventory.setInvStack(outputSlot, new ItemStack(filterItem));
            return;
        }
    }

    public static Boolean canInsert(Inventory outputInventory, Integer outputSlot, Item filterItem) {
        return (outputInventory.getInvStack(outputSlot).getItem() == filterItem && outputInventory.getInvStack(outputSlot).getCount() < outputInventory.getInvStack(outputSlot).getMaxCount() || outputInventory.getInvStack(outputSlot).isEmpty());
    }

    public static Boolean canInsertWildcard(Inventory outputInventory, Integer outputSlot, ItemStack itemStack) {
        return (outputInventory.getInvStack(outputSlot).isEmpty() || outputInventory.getInvStack(outputSlot).getItem() == itemStack.getItem() && itemStack.getItem() != Items.AIR && outputInventory.getInvStack(outputSlot).getCount() < outputInventory.getInvStack(outputSlot).getMaxCount());
    }

    public static Boolean canExtract(Inventory inputInventory, Integer inputSlot, Item filterItem) {
        return (inputInventory.getInvStack(inputSlot).getItem() == filterItem);
    }
    
    public static Boolean canExtractWildcard(InventoryHolder inputInventory, Integer inputSlot) {
        switch (inputInventory.getInventoryType()) {
            case INVENTORY_BASE:
                return (inputInventory.getInventory().getInvStack(inputSlot).getItem() != Items.AIR);
            case INVENTORY_SIDED:
                return (((SidedInventory)inputInventory.getInventory()).canExtractInvStack(inputSlot, inputInventory.getInventory().getInvStack(inputSlot), inputInventory.getAccessDirection()) && inputInventory.getInventory().getInvStack(inputSlot).getItem() != Items.AIR);
        }
        return null;
    }
//   boolean canExtractInvStack(int var1, ItemStack var2, Direction var3);
    public static IntStream getAvailableSlotList(InventoryHolder targetInventory, World world) {
        switch (targetInventory.getInventoryType()) {
            case INVENTORY_BASE:
                return IntStream.range(0, targetInventory.getInventory().getInvSize());
            case INVENTORY_SIDED:
                return IntStream.of(((SidedInventory)targetInventory.getInventory()).getInvAvailableSlots(targetInventory.getAccessDirection()));
        }
        return null;
    }
}