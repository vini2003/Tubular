package tubular.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;

import tubular.block.BlockTubeConnectorMode;
import tubular.utility.MixinInformation;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Inject(method = "render(F)V", at = @At("RETURN"))
    protected void onRender(CallbackInfo info) {
        if (MixinInformation.getTicks() > 0) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            Integer width = minecraftClient.window.getScaledWidth();
            Integer height = minecraftClient.window.getScaledHeight();
            if (MixinInformation.getMode() == BlockTubeConnectorMode.PROVIDER) {
                minecraftClient.textRenderer.drawWithShadow("Provider", ((width / 2) - minecraftClient.textRenderer.getStringWidth("Provider") / 2), height / 2 + 10, 0xFF3C86);
            }
            else if (MixinInformation.getMode() == BlockTubeConnectorMode.REQUESTER) {
                minecraftClient.textRenderer.drawWithShadow("Requester", ((width / 2) - minecraftClient.textRenderer.getStringWidth("Requester") / 2), height / 2 + 10, 0xC6FF00);
            }
            if (MixinInformation.isWildcard() && MixinInformation.getMode() != BlockTubeConnectorMode.PROVIDER) {
                minecraftClient.textRenderer.drawWithShadow("Wildcard", ((width / 2) - minecraftClient.textRenderer.getStringWidth("Wildcard") / 2), height / 2 + 20, 0xFCFCFC);
            }
            else if (!MixinInformation.isWildcard() && MixinInformation.getMode() != BlockTubeConnectorMode.PROVIDER) {
                minecraftClient.textRenderer.drawWithShadow("Filter", ((width / 2) - minecraftClient.textRenderer.getStringWidth("Filter") / 2), height / 2 + 20, 0xB9B99D);
            }
            MixinInformation.decrementTick();
        }
    }
}