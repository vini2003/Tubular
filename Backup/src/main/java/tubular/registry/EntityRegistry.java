package tubular.registry;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import tubular.entity.BlockTubeConnectorEntity;

public class EntityRegistry {
    public static final BlockEntityType<BlockTubeConnectorEntity> TUBE_CONNECTOR_ENTITY = BlockEntityType.Builder.create(BlockTubeConnectorEntity::new, BlockRegistry.TUBE_CONNECTOR).build(null);
    
    public static void registerEntities() {
        Registry.register(Registry.BLOCK_ENTITY, new Identifier("tubular", "tube_connector"), TUBE_CONNECTOR_ENTITY);
    }
}