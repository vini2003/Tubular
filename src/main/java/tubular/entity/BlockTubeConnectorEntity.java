package tubular.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blue.endless.jankson.annotation.Nullable;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import tubular.inventory.InventoryHandler;
import tubular.inventory.InventoryHolder;
import tubular.network.NetworkHandler;
import tubular.network.NetworkHolder;
import tubular.registry.EntityRegistry;
import tubular.utility.BlockMode;
import tubular.utility.Tuple;

public class BlockTubeConnectorEntity extends BlockEntity implements Tickable, InventoryProvider, BlockEntityClientSerializable {
    public Integer ticksBase = 5;
    public Integer ticksRemaining = 0;

    public Boolean initialUpdate = false;

    public NetworkHolder networkHolder;

    public List<InventoryHolder> inputInventories = new ArrayList<>();
    public List<InventoryHolder> outputInventories = new ArrayList<>();

    public List<Tuple<Direction, Item>> connectorFilters = new ArrayList<>();

    public List<Tuple<Direction, Boolean>> connectorWildcards = new ArrayList<>();

    public List<Tuple<Direction, BlockMode>> connectorModes = new ArrayList<>();

    public ConnectorInventory blockInventory = new ConnectorInventory();

    public BlockTubeConnectorEntity() {
        super(EntityRegistry.TUBE_CONNECTOR_ENTITY);

        this.connectorFilters.add(new Tuple<>(Direction.NORTH, Items.AIR));
        this.connectorFilters.add(new Tuple<>(Direction.SOUTH, Items.AIR));
        this.connectorFilters.add(new Tuple<>(Direction.WEST,  Items.AIR));
        this.connectorFilters.add(new Tuple<>(Direction.EAST,  Items.AIR));
        this.connectorFilters.add(new Tuple<>(Direction.UP,    Items.AIR));
        this.connectorFilters.add(new Tuple<>(Direction.DOWN,  Items.AIR));

        this.connectorWildcards.add(new Tuple<>(Direction.NORTH, false));
        this.connectorWildcards.add(new Tuple<>(Direction.SOUTH, false));
        this.connectorWildcards.add(new Tuple<>(Direction.WEST,  false));
        this.connectorWildcards.add(new Tuple<>(Direction.EAST,  false));
        this.connectorWildcards.add(new Tuple<>(Direction.UP,    false));
        this.connectorWildcards.add(new Tuple<>(Direction.DOWN,  false));

        this.connectorModes.add(new Tuple<>(Direction.NORTH, BlockMode.NONE));
        this.connectorModes.add(new Tuple<>(Direction.SOUTH, BlockMode.NONE));
        this.connectorModes.add(new Tuple<>(Direction.WEST,  BlockMode.NONE));
        this.connectorModes.add(new Tuple<>(Direction.EAST,  BlockMode.NONE));
        this.connectorModes.add(new Tuple<>(Direction.UP,    BlockMode.NONE));
        this.connectorModes.add(new Tuple<>(Direction.DOWN,  BlockMode.NONE));

        networkHolder = new NetworkHolder();
    }
    
    @Override
    public CompoundTag toTag(CompoundTag blockTag) {
        for (Tuple<Direction, Item> tuple : this.connectorFilters) {
            blockTag.putString(tuple.getFirst().asString() + "_filter", tuple.getSecond().toString());
        }
        for (Tuple<Direction, Boolean> tuple : this.connectorWildcards) {
            blockTag.putBoolean(tuple.getFirst().asString() + "_wildcard", tuple.getSecond());
        }
        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            blockTag.putString(tuple.getFirst().asString() + "_mode", tuple.getSecond().toString());
        }
        return super.toTag(blockTag);
    }

    @Override
    public void fromTag(CompoundTag blockTag) {
        for (Tuple<Direction, Item> tuple : this.connectorFilters) {
            this.setFilter(tuple.getFirst(), Registry.ITEM.get(new Identifier(blockTag.getString(tuple.getFirst().asString() + "_filter"))));
        }
        for (Tuple<Direction, Boolean> tuple : this.connectorWildcards) {
            this.setWildcard(tuple.getFirst(), blockTag.getBoolean(tuple.getFirst() + "_wildcard"));
        }
        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            this.setMode(tuple.getFirst(), BlockMode.fromString(blockTag.getString(tuple.getFirst() + "_mode")));
        }
        super.fromTag(blockTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag blockTag) {
        for (Tuple<Direction, Item> tuple : this.connectorFilters) {
            blockTag.putString(tuple.getFirst().asString() + "_filter", tuple.getSecond().toString());
        }
        for (Tuple<Direction, Boolean> tuple : this.connectorWildcards) {
            blockTag.putBoolean(tuple.getFirst().asString() + "_wildcard", tuple.getSecond());
        }
        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            blockTag.putString(tuple.getFirst().asString() + "_mode", tuple.getSecond().toString());
        }
        return super.toTag(blockTag);
    }

    @Override
    public void fromClientTag(CompoundTag blockTag) {
        for (Tuple<Direction, Item> tuple : this.connectorFilters) {
            this.setFilter(tuple.getFirst(), Registry.ITEM.get(new Identifier(blockTag.getString(tuple.getFirst().asString() + "_filter"))));
        }
        for (Tuple<Direction, Boolean> tuple : this.connectorWildcards) {
            this.setWildcard(tuple.getFirst(), blockTag.getBoolean(tuple.getFirst() + "_wildcard"));
        }
        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            this.setMode(tuple.getFirst(), BlockMode.fromString(blockTag.getString(tuple.getFirst() + "_mode")));
        }
        super.fromTag(blockTag);
    }

    class ConnectorInventory extends BasicInventory implements SidedInventory {
        public ConnectorInventory() {
            super(6);
        }

        public int getInvMaxStackAmount() {
            return 1;
        }

        public int[] getInvAvailableSlots(Direction direction_1) {
            return new int[]{0, 1, 2, 3, 4, 5};
        }   

        public boolean canInsertInvStack(int int_1, ItemStack itemStack_1, @Nullable Direction direction_1) {
            return false;
        }

        public boolean canExtractInvStack(int int_1, ItemStack itemStack_1, Direction direction_1) {
            return false;
        }
    }

    public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
        return blockInventory;
    }

    public Boolean getWildcard(Direction direction) {
        for (Tuple<Direction, Boolean> tuple : connectorWildcards) {
            if (tuple.getFirst() == direction) {
                return tuple.getSecond();
            }
        }
        return null;
    }

    public void setWildcard(Direction direction, Boolean bool) {
        for (Tuple<Direction, Boolean> tuple : connectorWildcards) {
            if (tuple.getFirst() == direction) {
                tuple.setSecond(bool);
                this.markDirty();
            }
        }
    }

    public Item getFilter(Direction direction) {
        for (Tuple<Direction, Item> tuple : connectorFilters) {
            if (tuple.getFirst() == direction) {
                return tuple.getSecond();
            }
        }
        return null;
    }

    public void setFilter(Direction direction, Item item) {
        for (Tuple<Direction, Item> tuple : connectorFilters) {
            if (tuple.getFirst() == direction) {
                tuple.setSecond(item);
                this.markDirty();
            }
        }
    }

    public BlockMode getMode(Direction direction) {
        for (Tuple<Direction, BlockMode> tuple : connectorModes) {
            if (tuple.getFirst() == direction) {
                return tuple.getSecond();
            }
        }
        return null;
    }

    public void setMode(Direction direction, BlockMode mode) {
        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            if (tuple.getFirst() == direction) {
                tuple.setSecond(mode);
                this.markDirty();
            }
        }
    }

    public static Direction getDirectionTranslated(Integer translateBuffer) {
        if (translateBuffer instanceof Integer) {
            Integer integer = (Integer)translateBuffer;
            switch (integer) {
                case 0:
                    return Direction.NORTH;
                case 1:
                    return Direction.SOUTH;
                case 2:
                    return Direction.WEST;
                case 3:
                    return Direction.EAST;
                case 4:
                    return Direction.UP;
                case 5:
                    return Direction.DOWN;
            }
        }
        return null;
    }

    public static Integer getIntegerTranslated(Direction translateBuffer) {
        switch (translateBuffer) {
            case NORTH:
                return 0;
            case SOUTH:
                return 1;
            case WEST:
                return 2;
            case EAST:
                return 3;
            case UP:
                return 4;
            case DOWN:
                return 5;
            default:
                return -1;
        }
    }

    public void updateWildcards() {
        for (Tuple<Direction, Item> tuple : this.connectorFilters) {
            Item filterItem = blockInventory.getInvStack(getIntegerTranslated(tuple.getFirst())).getItem();
            tuple.setSecond(filterItem);
            if (filterItem != Items.AIR) {
                setWildcard(tuple.getFirst(), false);
            } else {
                setWildcard(tuple.getFirst(), true);
            }
            this.markDirty();
        }
    }

    public void clear() {
        this.networkHolder.connectorList.clear();
        this.inputInventories.clear();
        this.outputInventories.clear();
    }

    public void tick() {
        // (false) INPUT  -> { source }
        // (true)  OUTPUT -> { destination }

        // if itemFilter = Items.AIR -> { wildcard = true }
        // else -> { wildcard = false }

        --ticksRemaining;
        
        if (!initialUpdate) {
            initialUpdate = true;

            for (Tuple<Direction, Item> tuple : this.connectorFilters) {
                this.blockInventory.setInvStack(BlockTubeConnectorEntity.getIntegerTranslated(tuple.getFirst()), new ItemStack(tuple.getSecond()));
            }
    

            NetworkHandler.updateNetwork(this.getPos(), this.getWorld());
        }

        this.updateWildcards();

        if (this.world.isClient()) {
            return;
        }

        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            if (tuple.getSecond() == BlockMode.REQUESTER && ticksRemaining <= 0 && !this.world.isClient) {
                this.ticksRemaining = ticksBase;
                this.inputInventories.clear();
                this.outputInventories.clear();
                inputInventories = InventoryHandler.getInput(networkHolder, this.getPos(), world, true);
                outputInventories = InventoryHandler.getOutput(this.getPos(), world, true);
                if (outputInventories != null && inputInventories != null) {
                    Collections.shuffle(inputInventories);
                    Collections.shuffle(outputInventories);
                    for (InventoryHolder inputInventory : inputInventories) {
                        if (inputInventory != null && outputInventories != null) {
                            for (InventoryHolder outputInventory : outputInventories) {
                                int[] availableInputSlots = InventoryHandler.getAvailableSlotList(inputInventory, world).toArray();
                                int[] availableOutputSlots = InventoryHandler.getAvailableSlotList(outputInventory, world).toArray();
                                for (Integer inputSlot : availableInputSlots) {
                                    if (InventoryHandler.canExtract(inputInventory.getInventory(), inputSlot, this.getFilter(tuple.getFirst())) && !this.getWildcard(tuple.getFirst())) {
                                        for (Integer outputSlot : availableOutputSlots) {
                                            if (InventoryHandler.canInsert(outputInventory.getInventory(), outputSlot, this.getFilter(inputInventory.getAccessDirection()))) {
                                                InventoryHandler.makeTransfer(inputInventory.getInventory(), outputInventory.getInventory(), inputSlot, outputSlot, getFilter(inputInventory.getAccessDirection()), outputInventory.getInventory().getInvStack(outputSlot).isEmpty());
                                                return;
                                            }
                                        }
                                    }
                                    else if (InventoryHandler.canExtractWildcard(inputInventory, inputSlot) && this.getWildcard(tuple.getFirst())) {
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
    }
}