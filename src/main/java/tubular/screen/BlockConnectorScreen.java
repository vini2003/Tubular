package tubular.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.minecraft.entity.player.PlayerEntity;

public class BlockConnectorScreen extends CottonScreen<BlockConnectorScreenController> {
	public BlockConnectorScreen(BlockConnectorScreenController container, PlayerEntity player) {
		super(container, player);
	}
}
