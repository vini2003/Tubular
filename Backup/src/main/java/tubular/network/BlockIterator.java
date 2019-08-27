package tubular.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class BlockIterator {
    public List<BlockPos> blockPosList = new ArrayList<>();
    public BlockIterator(BlockPos blockPos) {
        blockPosList.add(blockPos.north());
        blockPosList.add(blockPos.south());
        blockPosList.add(blockPos.west());
        blockPosList.add(blockPos.east());
        blockPosList.add(blockPos.up());
        blockPosList.add(blockPos.down());
        blockPosList.add(blockPos);
    }
}