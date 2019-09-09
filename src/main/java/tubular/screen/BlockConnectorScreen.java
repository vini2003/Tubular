package tubular.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class BlockConnectorScreen extends CottonInventoryScreen<BlockConnectorScreenController> {
	public BlockConnectorScreen(BlockConnectorScreenController container, PlayerEntity player) {
		super(container, player);
	}
}
