package tubular.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

import tubular.inventory.InventoryHandler;
import tubular.inventory.InventoryHolder;
import tubular.network.NetworkHandler;
import tubular.network.NetworkHolder;
import tubular.registry.EntityRegistry;
import tubular.utility.BlockMode;
import tubular.utility.Tuple;

public class BlockTubeConnectorEntity extends BlockEntity implements Tickable, InventoryProvider {
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

    private ServerWorld serverWorld;

    public BlockTubeConnectorEntity() {
        super(EntityRegistry.TUBE_CONNECTOR_ENTITY);
        networkHolder = new NetworkHolder();
    }
    
    //@Override
    //public CompoundTag toTag(CompoundTag blockTag) {
    //    if (this.filterItem != null) {
    //        blockTag.putString("filterItem", filterItem.toString());
    //    }
    //    if (this.wildcard != null) {
    //        blockTag.putBoolean("wildcard", wildcard);
    //    }
    //    if (this.mode != null) {
    //        blockTag.putString("mode", this.mode.asString());
    //    }
    //    return super.toTag(blockTag);
    //}

    //@Override
    //public void fromTag(CompoundTag tag) {
    //    super.fromTag(tag);
    //    this.filterItem = Registry.ITEM.get(new Identifier(tag.getString("filterItem")));
    //    this.wildcard = tag.getBoolean("wildcard");
    //    this.mode = BlockMode.fromString(tag.getString("mode"));
    //}

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
            }
        }
    }

    public ServerWorld getServerWorld() {
        return this.serverWorld;
    }

    public void setServerWorld(ServerWorld world) {
        this.serverWorld = world;
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

            NetworkHandler.updateNetwork(this.getPos(), this.getWorld());

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
        }

        if (this.world.isClient()) {
            return;
        }

        for (Tuple<Direction, BlockMode> tuple : this.connectorModes) {
            if (tuple.getSecond() == BlockMode.REQUESTER && ticksRemaining <= 0 && !this.world.isClient) {
                this.ticksRemaining = ticksBase;
                this.inputInventories.clear();
                this.outputInventories.clear();
                inputInventories = InventoryHandler.getInput(networkHolder, world, true);
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
                                            if (InventoryHandler.canInsert(outputInventory.getInventory(), outputSlot, getFilter(inputInventory.getAccessDirection().getOpposite()))) {
                                                InventoryHandler.makeTransfer(inputInventory.getInventory(), outputInventory.getInventory(), inputSlot, outputSlot, getFilter(inputInventory.getAccessDirection().getOpposite()), outputInventory.getInventory().getInvStack(outputSlot).isEmpty());
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