package tubular.utility;

import net.minecraft.block.Block;
import tubular.registry.BlockRegistry;

public enum BlockType {
    DEFAULT,
    CONNECTOR,
    BOTH;

    public static Block asBlock(BlockType blockType) {
        switch (blockType) {
            case DEFAULT:
                return BlockRegistry.TUBE;
            case CONNECTOR:
                return BlockRegistry.TUBE_CONNECTOR;
            default:
                return null;
        }
    }
}