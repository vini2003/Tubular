package tubular.registry;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;
import tubular.screen.BlockConnectorScreenController;

public class ScreenServerRegistry {
    public static void registerScreens() {
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("tubular", "tube_connector"), (syncId, id, player, buf) -> new BlockConnectorScreenController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
    }
}