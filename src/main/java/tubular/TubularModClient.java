package tubular;

import net.fabricmc.api.ClientModInitializer;

import tubular.registry.ScreenClientRegistry;

public class TubularModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenClientRegistry.registerScreens();
    }
}