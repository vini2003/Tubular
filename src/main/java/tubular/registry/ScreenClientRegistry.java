package tubular.registry;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;

import tubular.screen.BlockConnectorScreen;
import tubular.screen.BlockConnectorScreenController;

public class ScreenClientRegistry {
    public static void registerScreens() {
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier("tubular", "tube_connector"), (syncId, identifier, player, buf) -> new BlockConnectorScreen(new BlockConnectorScreenController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }
}