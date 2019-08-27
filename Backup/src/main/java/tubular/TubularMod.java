package tubular;

import net.fabricmc.api.ModInitializer;

import tubular.registry.BlockRegistry;
import tubular.registry.EntityRegistry;
import tubular.registry.ItemRegistry;

public class TubularMod implements ModInitializer {
    @Override
    public void onInitialize() {
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        EntityRegistry.registerEntities();
    }
}