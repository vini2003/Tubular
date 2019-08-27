package tubular.utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Iterator {
    private static List<Direction> directionList = new ArrayList<>();
    
    public static List<Direction> getDirList() {
        return directionList;
    }

    public static List<BlockPos> getOffList(BlockPos blockPosition) {
        List<BlockPos> offsetList = new ArrayList<>();
        for (Direction direction : getDirList()) {
            offsetList.add(blockPosition.offset(direction));
        }
        return offsetList;
    }

    public static List<BlockPos> getOffList(BlockPos blockPosition, BlockType blockType, World world) {
        List<BlockPos> offsetList = new ArrayList<>();
        for (BlockPos offsetPosition : getOffList(blockPosition)) {
            Block offsetBlock = world.getBlockState(offsetPosition).getBlock();
            if (blockType == BlockType.BOTH) {
                if (offsetBlock == BlockType.asBlock(BlockType.DEFAULT) || offsetBlock == BlockType.asBlock(BlockType.CONNECTOR)) {
                    offsetList.add(offsetPosition);
                }
            } else if (offsetBlock == BlockType.asBlock(blockType)) {
                    offsetList.add(offsetPosition);
            }
        }
        return offsetList;
    }

    static {
        directionList.add(Direction.NORTH);
        directionList.add(Direction.SOUTH);
        directionList.add(Direction.WEST);
        directionList.add(Direction.EAST);
        directionList.add(Direction.UP);
        directionList.add(Direction.DOWN);
    }
}