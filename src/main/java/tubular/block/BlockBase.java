package tubular.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import tubular.model.ModelHandler;
import tubular.network.NetworkHandler;
import tubular.utility.BlockAction;
import tubular.utility.BlockType;
import tubular.utility.Iterator;

public class BlockBase extends Block {
    protected static final VoxelShape centerShape = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    protected static final VoxelShape upShape = Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D);
    protected static final VoxelShape downShape = Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D);

    protected static final VoxelShape northShape = Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D);
    protected static final VoxelShape southShape = Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D);
 
    protected static final VoxelShape eastShape = Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D);
    protected static final VoxelShape westShape = Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D);

    public static final BooleanProperty north = BooleanProperty.of("north");
    public static final BooleanProperty south = BooleanProperty.of("south");
    public static final BooleanProperty west = BooleanProperty.of("west");
    public static final BooleanProperty east = BooleanProperty.of("east");
    public static final BooleanProperty up = BooleanProperty.of("up");
    public static final BooleanProperty down = BooleanProperty.of("down");

    public BlockBase(Settings block$Settings) {
        super(block$Settings);
        this.setDefaultState(this.stateFactory.getDefaultState()
                            .with(north, false)
                            .with(south, false)
                            .with(west, false)
                            .with(east, false)
                            .with(up, false)
                            .with(down, false));
    }

    public VoxelShape getShape(BlockState blockState) {
        VoxelShape baseShape = centerShape;
        if (blockState.get(north) == true) {
            baseShape = VoxelShapes.union(baseShape, northShape);
        }
        if (blockState.get(south) == true) {
            baseShape = VoxelShapes.union(baseShape, southShape);
        }
        if (blockState.get(west) == true) {
            baseShape = VoxelShapes.union(baseShape, westShape);
        }
        if (blockState.get(east) == true) {
            baseShape = VoxelShapes.union(baseShape, eastShape);
        }
        if (blockState.get(up) == true) {
            baseShape = VoxelShapes.union(baseShape, upShape);
        }
        if (blockState.get(down) == true) {
            baseShape = VoxelShapes.union(baseShape, downShape);
        }
        return baseShape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext) {
        return getShape(blockState);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPosition, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        super.onPlaced(world, blockPosition, blockState, livingEntity, itemStack);
        NetworkHandler.updateNetwork(blockPosition, world);
        ModelHandler.update(world, blockPosition, BlockAction.BLOCK_PLACED, BlockType.DEFAULT);
    }

    @Override
    public void onBroken(IWorld iWorld, BlockPos blockPosition, BlockState blockState) {
        super.onBroken(iWorld, blockPosition, blockState);
        for (BlockPos scanPosition : Iterator.getOffList(blockPosition, BlockType.BOTH, iWorld.getWorld())) {
            NetworkHandler.updateNetwork(scanPosition, iWorld);
        }
        ModelHandler.update(iWorld.getWorld(), blockPosition, BlockAction.BLOCK_BROKEN, BlockType.DEFAULT);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder) {
        stateFactory$Builder.add(north, south, west, east, up, down);
    }
}