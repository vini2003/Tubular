package tubular.block;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import tubular.entity.BlockTubeConnectorEntity;
import tubular.model.ModelHandler;
import tubular.utility.BlockAction;
import tubular.utility.BlockMode;
import tubular.utility.BlockType;
import tubular.utility.MixinInformation;
import tubular.utility.Tuple;

public class BlockTubeConnector extends BlockBase implements BlockEntityProvider {
    protected static final VoxelShape upShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 10D, 6D, 10D, 14D, 10D));
    protected static final VoxelShape downShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 2D, 6D, 10D, 6D, 10D));

    protected static final VoxelShape northShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 6D, 2D, 10D, 10D, 6D));
    protected static final VoxelShape southShapeConnector = VoxelShapes.union(Block.createCuboidShape(6D, 6D, 10D, 10D, 10D, 14D));
    
    protected static final VoxelShape eastShapeConnector = VoxelShapes.union(Block.createCuboidShape(10D, 6D, 6D, 14D, 10D, 10D));
    protected static final VoxelShape westShapeConnector = VoxelShapes.union(Block.createCuboidShape(2D, 6D, 6D, 6D, 10D, 10D));

    public static final BooleanProperty connectedNorth = BooleanProperty.of("connected_north");
    public static final BooleanProperty connectedSouth = BooleanProperty.of("connected_south");

    public static final BooleanProperty connectedWest = BooleanProperty.of("connected_west");
    public static final BooleanProperty connectedEast = BooleanProperty.of("connected_east");

    public static final BooleanProperty connectedUp = BooleanProperty.of("connected_up");
    public static final BooleanProperty connectedDown = BooleanProperty.of("connected_down");

    public static final List<Tuple<Direction, BooleanProperty>> connectorDirections = new ArrayList<>();

    public BlockTubeConnector(Settings block$Settings) {
        super(block$Settings);
        this.setDefaultState(this.stateFactory.getDefaultState()
                            .with(north, false)
                            .with(south, false)
                            .with(west, false)
                            .with(east, false)
                            .with(up, false)
                            .with(down, false)
                            .with(connectedNorth, false)
                            .with(connectedSouth, false)
                            .with(connectedWest, false)
                            .with(connectedEast, false)
                            .with(connectedUp, false)
                            .with(connectedDown, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new BlockTubeConnectorEntity();
    }

    @Override
    public VoxelShape getShape(BlockState blockState) {
        VoxelShape baseShape = super.getShape(blockState);
        if (blockState.get(connectedNorth)) {
            baseShape = VoxelShapes.union(baseShape, northShapeConnector);
        }
        if (blockState.get(connectedSouth)) {
            baseShape = VoxelShapes.union(baseShape, southShapeConnector);
        }
        if (blockState.get(connectedWest)) {
            baseShape = VoxelShapes.union(baseShape, westShapeConnector);
        }
        if (blockState.get(connectedEast)) {
            baseShape = VoxelShapes.union(baseShape, eastShapeConnector);
        }
        if (blockState.get(connectedUp)) {
            baseShape = VoxelShapes.union(baseShape, upShapeConnector);
        }
        if (blockState.get(connectedDown)) {
            baseShape = VoxelShapes.union(baseShape, downShapeConnector);
        }
        return baseShape;     
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext) {
        return this.getShape(blockState);
    }
    
    @Override
    public void onPlaced(World world, BlockPos blockPosition, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        super.onPlaced(world, blockPosition, blockState, livingEntity, itemStack);
        ModelHandler.update(world, blockPosition, BlockAction.BLOCK_PLACED, BlockType.CONNECTOR);
    }

    @Override
    public void onBroken(IWorld iWorld, BlockPos blockPosition, BlockState blockState) {
        super.onBroken(iWorld, blockPosition, blockState);
        ModelHandler.update(iWorld.getWorld(), blockPosition, BlockAction.BLOCK_BROKEN, BlockType.CONNECTOR);
    }

	@Override
	public boolean activate(BlockState blockState, World world, BlockPos blockPosition, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        BlockEntity blockEntityTemporary = world.getBlockEntity(blockPosition);
        BlockTubeConnectorEntity blockEntity = null;
        if (blockEntityTemporary != null && blockEntityTemporary instanceof BlockTubeConnectorEntity) {
            blockEntity = (BlockTubeConnectorEntity)blockEntityTemporary;
        } else {
            return false;
        }
        //if (playerEntity.getMainHandStack().isEmpty() && !playerEntity.isSneaking()) {
        //    switch (((BlockTubeConnectorEntity)world.getBlockEntity(blockPosition)).mode) {
        //        case PROVIDER: 
        //            blockEntity.mode = BlockMode.REQUESTER;
        //            MixinInformation.setMode(BlockMode.REQUESTER);
        //            MixinInformation.setTicks(100);
        //            break;
        //        case REQUESTER:
        //            blockEntity.mode = BlockMode.PROVIDER;   
        //            MixinInformation.setMode(BlockMode.PROVIDER);
        //            MixinInformation.setTicks(100);
        //            break;
        //    }
        //}
        //else if (playerEntity.getMainHandStack().isEmpty() && playerEntity.isSneaking()) {
        //    ((BlockTubeConnectorEntity)world.getBlockEntity(blockPosition)).wildcard = !((BlockTubeConnectorEntity)world.getBlockEntity(blockPosition)).wildcard;   
        //    MixinInformation.setWildcard(blockEntity.wildcard);
        //    MixinInformation.setTicks(100);
        //}
        //else if (!playerEntity.getMainHandStack().isEmpty() && !playerEntity.isSneaking()) {
        //    if (blockEntity instanceof BlockTubeConnectorEntity) {
        //        blockEntity.filterItem = playerEntity.getMainHandStack().getItem();
        //        blockEntity.markDirty();
        //    }
        //}
        if (!world.isClient) {
            if (blockEntity != null && blockEntity.getClass() == BlockTubeConnectorEntity.class) {
                ContainerProviderRegistry.INSTANCE.openContainer(new Identifier("tubular", "tube_connector"), playerEntity, (buffer)->{
                    buffer.writeBlockPos(blockPosition);
                });
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
        stateFactory$Builder.add(connectedNorth, connectedSouth, connectedWest, connectedEast, connectedUp, connectedDown);
    }

    static {
        connectorDirections.add(new Tuple<Direction, BooleanProperty>(Direction.NORTH, connectedNorth));
        connectorDirections.add(new Tuple<Direction, BooleanProperty>(Direction.SOUTH, connectedSouth));
    
        connectorDirections.add(new Tuple<Direction, BooleanProperty>(Direction.WEST, connectedWest));
        connectorDirections.add(new Tuple<Direction, BooleanProperty>(Direction.EAST, connectedEast));
    
        connectorDirections.add(new Tuple<Direction, BooleanProperty>(Direction.UP, connectedUp));
        connectorDirections.add(new Tuple<Direction, BooleanProperty>(Direction.DOWN, connectedDown));
    }
}