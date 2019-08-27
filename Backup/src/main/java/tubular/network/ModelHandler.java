package tubular.network;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import tubular.block.BlockTube;

public class ModelHandler {
    public static void updateTube(World world, BlockPos blockPosition) {
        BlockDirectionIterator blockDirectionIterator = new BlockDirectionIterator(); 
        for (Direction offsetDirection : blockDirectionIterator.directionList) {
            if (world.getBlockState(blockPosition.offset(offsetDirection)).getBlock() instanceof BlockTube) {
                world.setBlockState(blockPosition, world.getBlockState(blockPosition).with(BooleanProperty.of(offsetDirection.asString()), true));
                world.setBlockState(blockPosition.offset(offsetDirection), world.getBlockState(blockPosition.offset(offsetDirection)).with(BooleanProperty.of(offsetDirection.getOpposite().asString()), true));
            }
        }
    }

    public static void updateTubeConnector(World world, BlockPos blockPosition) {
        BlockDirectionIterator blockDirectionIterator = new BlockDirectionIterator(); 
        for (Direction offsetDirection : blockDirectionIterator.directionList) {
            if (world.getBlockState(blockPosition.offset(offsetDirection)).getBlock() instanceof BlockTube) {
                world.setBlockState(blockPosition.offset(offsetDirection), world.getBlockState(blockPosition.offset(offsetDirection)).with(BooleanProperty.of(offsetDirection.getOpposite().asString()), false));
            }
        }
    }
}