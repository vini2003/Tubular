package tubular.registry;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tubular.block.BlockTube;
import tubular.block.BlockTubeConnector;

public class BlockRegistry {
    public static final BlockTube TUBE = new BlockTube(FabricBlockSettings.of(Material.BAMBOO).strength(3.0F, 0.0F).sounds(BlockSoundGroup.BAMBOO).build());
    public static final BlockTubeConnector TUBE_CONNECTOR = new BlockTubeConnector(FabricBlockSettings.of(Material.BAMBOO).strength(3.0F, 0.0F).sounds(BlockSoundGroup.BAMBOO).build());
    
    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier("tubular", "tube_connector"), TUBE_CONNECTOR);
        Registry.register(Registry.BLOCK, new Identifier("tubular", "tube"), TUBE);
    }
}