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
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import tubular.network.BlockIterator;
import tubular.network.ModelHandler;
import tubular.network.NetworkHandler;

public class BlockTube extends Block {
    private static final DirectionProperty FACING = FacingBlock.FACING;

    protected static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    protected static final VoxelShape UP_SHAPE = Block.createCuboidShape(6D, 10D, 6D, 10D, 16D, 10D);
    protected static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(6D, 0D, 6D, 10D, 6D, 10D);

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(6D, 6D, 0D, 10D, 10D, 6D);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 16D);
 
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(10D, 6D, 6D, 16D, 10D, 10D);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0D, 6D, 6D, 6D, 10D, 10D);

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");

    public BlockTube(Settings block$Settings) {
        super(block$Settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateFactory.getDefaultState()).with(NORTH, false).with(SOUTH, false).with(WEST, false).with(EAST, false).with(UP, false).with(DOWN, false)));
    }

    public VoxelShape getBaseShape(BlockState blockState) {
        VoxelShape returnShape = DEFAULT_SHAPE;
        if (blockState.get(NORTH) == true) {
            returnShape = VoxelShapes.union(returnShape, NORTH_SHAPE);
        }
        if (blockState.get(SOUTH) == true) {
            returnShape = VoxelShapes.union(returnShape, SOUTH_SHAPE);
        }
        if (blockState.get(WEST) == true) {
            returnShape = VoxelShapes.union(returnShape, WEST_SHAPE);
        }
        if (blockState.get(EAST) == true) {
            returnShape = VoxelShapes.union(returnShape, EAST_SHAPE);
        }
        if (blockState.get(UP) == true) {
            returnShape = VoxelShapes.union(returnShape, UP_SHAPE);
        }
        if (blockState.get(DOWN) == true) {
            returnShape = VoxelShapes.union(returnShape, DOWN_SHAPE);
        }
        return returnShape;
    }

    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext) {
        return getBaseShape(blockState);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPosition, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        super.onPlaced(world, blockPosition, blockState, livingEntity, itemStack);
        NetworkHandler.updateNetwork(blockPosition, world);
        ModelHandler.updateTube(world, blockPosition);
    }

    @Override
    public void onBroken(IWorld iWorld, BlockPos blockPosition, BlockState blockState) {
        super.onBroken(iWorld, blockPosition, blockState);
        BlockIterator blockIterator = new BlockIterator(blockPosition);
        for (BlockPos scanPosition : blockIterator.blockPosList) {
            if (iWorld.getBlockState(scanPosition).getBlock() instanceof BlockTube) {
                NetworkHandler.updateNetwork(scanPosition, iWorld);
            }
        }
        ModelHandler.updateTubeConnector(iWorld.getWorld(), blockPosition);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder) {
        stateFactory$Builder.add(FACING, NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }
}