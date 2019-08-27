package tubular.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier("tubular", "tube_connector"), new BlockItem(BlockRegistry.TUBE_CONNECTOR, new Item.Settings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM, new Identifier("tubular", "tube"), new BlockItem(BlockRegistry.TUBE, new Item.Settings().group(ItemGroup.MISC)));
    }
}