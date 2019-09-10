package tubular.model;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tubular.block.BlockBase;
import tubular.inventory.InventoryHandler;
import tubular.registry.BlockRegistry;
import tubular.utility.BlockAction;
import tubular.utility.BlockType;
import tubular.utility.Iterator;

public class ModelHandler {
    public static void update(World world, BlockPos blockPosition, BlockAction blockAction, BlockType blockType) {
        if (blockAction == BlockAction.BLOCK_PLACED && blockType == BlockType.DEFAULT) {
            for (Direction offsetDirection : Iterator.getDirList()) {
                if (world.getBlockState(blockPosition.offset(offsetDirection)).getBlock() instanceof BlockBase) {
                    world.setBlockState(blockPosition, world.getBlockState(blockPosition).with(BooleanProperty.of(offsetDirection.asString()), true));
                    world.setBlockState(blockPosition.offset(offsetDirection), world.getBlockState(blockPosition.offset(offsetDirection)).with(BooleanProperty.of(offsetDirection.getOpposite().asString()), true));
                }
            }
        } else if (blockAction == BlockAction.BLOCK_BROKEN && blockType == BlockType.DEFAULT) {
            for (Direction offsetDirection : Iterator.getDirList()) {
                if (world.getBlockState(blockPosition.offset(offsetDirection)).getBlock() instanceof BlockBase) {
                    world.setBlockState(blockPosition.offset(offsetDirection), world.getBlockState(blockPosition.offset(offsetDirection)).with(BooleanProperty.of(offsetDirection.getOpposite().asString()), false));
                }
            }
        } else if (blockAction == BlockAction.BLOCK_PLACED && blockType == BlockType.CONNECTOR) {
            for (Direction offsetDirection : Iterator.getDirList()) {
                if (InventoryHandler.hasInventoryData(world, blockPosition.offset(offsetDirection))) {
                    world.setBlockState(blockPosition, world.getBlockState(blockPosition).with(BooleanProperty.of("connected_" + offsetDirection.asString()), true));             
                }
                if (world.getBlockState(blockPosition.offset(offsetDirection)).getBlock() == BlockRegistry.TUBE_CONNECTOR) {
                    world.setBlockState(blockPosition, world.getBlockState(blockPosition).with(BooleanProperty.of(offsetDirection.asString()), true));
                    world.setBlockState(blockPosition.offset(offsetDirection), world.getBlockState(blockPosition.offset(offsetDirection)).with(BooleanProperty.of(offsetDirection.getOpposite().asString()), true));
                }
            }
        } else if (blockAction == BlockAction.BLOCK_BROKEN && blockType == BlockType.CONNECTOR) {
            for (Direction offsetDirection : Iterator.getDirList()) {
                if (world.getBlockState(blockPosition.offset(offsetDirection)).getBlock() == BlockRegistry.TUBE_CONNECTOR) {
                    world.setBlockState(blockPosition.offset(offsetDirection), world.getBlockState(blockPosition.offset(offsetDirection)).with(BooleanProperty.of(offsetDirection.getOpposite().asString()), false));
                }
            }
        } else if (blockAction == BlockAction.BLOCK_ATTACH && blockType == BlockType.CONNECTOR) {
            for (Direction offsetDirection : Iterator.getDirList()) {
                if (InventoryHandler.hasInventoryData(world, blockPosition.offset(offsetDirection))) {
                    world.setBlockState(blockPosition, world.getBlockState(blockPosition).with(BooleanProperty.of("connected_" + offsetDirection.asString()), true));
                } else {
                    world.setBlockState(blockPosition, world.getBlockState(blockPosition).with(BooleanProperty.of("connected_" + offsetDirection.asString()), false));
                }
            }
        }
    }
}