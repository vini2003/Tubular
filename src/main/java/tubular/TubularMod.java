package tubular;



import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import tubular.registry.BlockRegistry;
import tubular.registry.EntityRegistry;
import tubular.registry.ItemRegistry;
import tubular.registry.NetworkRegistry;
import tubular.registry.ScreenServerRegistry;

public class TubularMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Logger logger = LogManager.getLogger();
        logger.log(Level.WARN, "[Tubular] I'm doing a very bad thing, the game is still loading, don't worry.");

        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        EntityRegistry.registerEntities();
        ScreenServerRegistry.registerScreens();
        NetworkRegistry.registerPackets();
    }
}