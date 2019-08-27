package tubular.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import tubular.entity.BlockTubeConnectorEntity;
import tubular.utility.MixinInformation;

public class BlockTubeConnector extends BlockTube implements BlockEntityProvider {
    //BlockTubeConnectorInformation connectorInformation;

    public static final DirectionProperty FACING = FacingBlock.FACING;

    protected static final VoxelShape upShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 10D, 6D, 10D, 14D, 10D), Block.createCuboidShape(5D, 13D, 5D, 11D, 16D, 11D));
    protected static final VoxelShape downShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 2D, 6D, 10D, 6D, 10D), Block.createCuboidShape(5D, 0D, 5D, 11D, 3D, 11D));

    protected static final VoxelShape northShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 6D, 2D, 10D, 10D, 6D), Block.createCuboidShape(5D, 5D, 0D, 11D, 11D, 3D));
    protected static final VoxelShape southShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 14D), Block.createCuboidShape(5D, 5D, 13D, 11D, 11D, 16D));
    
    protected static final VoxelShape eastShapeConnector = VoxelShapes.union(Block.createCuboidShape(10D, 6D, 6D, 14D, 10D, 10D), Block.createCuboidShape(13D, 5D, 5D, 16D, 11D, 11D));
    protected static final VoxelShape westShapeConnector = VoxelShapes.union(Block.createCuboidShape(2D, 6D, 6D, 6D, 10D, 10D), Block.createCuboidShape(0D, 5D, 5D, 3D, 11D, 11D));

    public static final EnumProperty<BlockTubeConnectorMode> TUBE_MODE;
    public static final BooleanProperty WILDCARD;

    public BlockTubeConnector(Settings block$Settings) {
        super(block$Settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateFactory.getDefaultState()).with(WILDCARD, true).with(TUBE_MODE, BlockTubeConnectorMode.PROVIDER).with(FACING, Direction.NORTH).with(NORTH, false).with(SOUTH, false).with(WEST, false).with(EAST, false).with(UP, false).with(DOWN, false)));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockTubeConnectorEntity();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        Direction direction = itemPlacementContext.getSide();
        BlockState blockState = itemPlacementContext.getWorld().getBlockState(itemPlacementContext.getBlockPos().offset(direction.getOpposite()));
        return blockState.getBlock() == this && blockState.get(FACING) == direction ? (BlockState)this.getDefaultState().with(FACING, direction) : (BlockState)this.getDefaultState().with(FACING, direction.getOpposite());
    }

    public VoxelShape getNewShape(VoxelShape voxelShape, BlockState blockState) {
        if (blockState.get(FACING) == Direction.NORTH) {
            return VoxelShapes.union(VoxelShapes.union(voxelShape, NORTH_SHAPE), northShapeConnector);
        }
        if (blockState.get(FACING) == Direction.SOUTH) {
            return VoxelShapes.union(VoxelShapes.union(voxelShape, SOUTH_SHAPE), southShapeConnector);
        }
        if (blockState.get(FACING) == Direction.WEST) {
            return VoxelShapes.union(VoxelShapes.union(voxelShape, WEST_SHAPE), westShapeConnector);
        }
        if (blockState.get(FACING) == Direction.EAST) {
            return VoxelShapes.union(VoxelShapes.union(voxelShape, EAST_SHAPE), eastShapeConnector);
        }
        if (blockState.get(FACING) == Direction.UP) {
            return VoxelShapes.union(VoxelShapes.union(voxelShape, UP_SHAPE), upShapeConnector);
        }
        if (blockState.get(FACING) == Direction.DOWN) {
            return VoxelShapes.union(VoxelShapes.union(voxelShape, DOWN_SHAPE), downShapeConnector);
        }
        return NORTH_SHAPE;     
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext) {
        return this.getNewShape(this.getBaseShape(blockState), blockState);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPosition, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        super.onPlaced(world, blockPosition, blockState, livingEntity, itemStack);
    }

    @Override
    public void onBroken(IWorld iWorld, BlockPos blockPosition, BlockState blockState) {
        super.onBroken(iWorld, blockPosition, blockState);
    }

	@Override
	public boolean activate(BlockState blockState, World world, BlockPos blockPosition, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        if (playerEntity.getMainHandStack().isEmpty() && !playerEntity.isSneaking()) {
            switch (blockState.get(TUBE_MODE)) {
                case PROVIDER: 
                    world.setBlockState(blockPosition, blockState.with(TUBE_MODE, BlockTubeConnectorMode.REQUESTER));
                    MixinInformation.setMode(BlockTubeConnectorMode.REQUESTER);
                    MixinInformation.setTicks(100);
                    break;
                case REQUESTER:
                    world.setBlockState(blockPosition, blockState.with(TUBE_MODE, BlockTubeConnectorMode.PROVIDER));       
                    MixinInformation.setMode(BlockTubeConnectorMode.PROVIDER);
                    MixinInformation.setTicks(100);
                    break;
            }
        }
        else if (playerEntity.getMainHandStack().isEmpty() && playerEntity.isSneaking()) {
            world.setBlockState(blockPosition, blockState.with(BlockTubeConnector.WILDCARD, !blockState.get(WILDCARD)));
            MixinInformation.setWildcard(!blockState.get(WILDCARD));
            MixinInformation.setTicks(100);
        }
        else if (!playerEntity.getMainHandStack().isEmpty() && !playerEntity.isSneaking()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPosition);
            if (blockEntity instanceof BlockTubeConnectorEntity) {
                ((BlockTubeConnectorEntity)blockEntity).filterItem = playerEntity.getMainHandStack().getItem();
                ((BlockTubeConnectorEntity)blockEntity).markDirty();
            }
        }
        return true;
	}

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder) {
        super.appendProperties(stateFactory$Builder);
        stateFactory$Builder.add(TUBE_MODE, WILDCARD);
    }

    static {
        TUBE_MODE = EnumProperty.of("tube_mode", BlockTubeConnectorMode.class);
        WILDCARD = BooleanProperty.of("wildcard");
    }
}