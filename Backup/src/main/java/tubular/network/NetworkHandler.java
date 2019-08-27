package tubular.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import tubular.entity.BlockTubeConnectorEntity;
import tubular.registry.BlockRegistry;
import tubular.registry.EntityRegistry;

public class NetworkHandler {
    public static void updateBlock(NetworkHolder networkHolder, BlockPos blockPosition, BlockView blockView, List<BlockPos> blockCache) {
        BlockIterator blockIterator = new BlockIterator(blockPosition);
        for (BlockPos scanPosition : blockIterator.blockPosList) {
            if (!blockCache.contains(scanPosition)) {
                Block scanBlock = blockView.getBlockState(scanPosition).getBlock();
                if (scanBlock == BlockRegistry.TUBE) {
                    blockCache.add(scanPosition);
                    updateBlock(networkHolder, scanPosition, blockView, blockCache);
                }
                else if (scanBlock == BlockRegistry.TUBE_CONNECTOR) {
                    networkHolder.connectorList.add(scanPosition);
                    blockCache.add(scanPosition);
                    updateBlock(networkHolder, scanPosition, blockView, blockCache);
                }
            }
        }
    }
    
    public static void updateNetwork(BlockPos blockPosition, BlockView blockView) {
        List<BlockPos> blockCache = new ArrayList<>();
        NetworkHolder networkHolder = new NetworkHolder();
        updateBlock(networkHolder, blockPosition, blockView, blockCache);
        for (BlockPos connectorPosition : networkHolder.connectorList) {
            BlockEntity connectorEntity = blockView.getBlockEntity(connectorPosition);
            if (connectorEntity.getType() == EntityRegistry.TUBE_CONNECTOR_ENTITY ) {
                BlockTubeConnectorEntity connectorPipeEntity = (BlockTubeConnectorEntity)connectorEntity;
                connectorPipeEntity.clear();
                for (BlockPos temporaryPosition : networkHolder.connectorList) {
                    BlockEntity temporaryEntity = blockView.getBlockEntity(temporaryPosition);
                    if (temporaryEntity.getType() == EntityRegistry.TUBE_CONNECTOR_ENTITY && temporaryEntity != connectorPipeEntity) {
                        BlockTubeConnectorEntity temporaryPipeEntity = (BlockTubeConnectorEntity)temporaryEntity;
                        connectorPipeEntity.networkHolder.connectorList.add(temporaryPipeEntity.getPos());
                        connectorPipeEntity.initialUpdate = true;
                    }
                }
            }
        }
    }
}