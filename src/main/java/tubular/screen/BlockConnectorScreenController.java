package tubular.screen;

import java.util.ArrayList;
import java.util.List;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import tubular.entity.BlockTubeConnectorEntity;
import tubular.registry.NetworkRegistry;
import tubular.utility.BlockMode;
import tubular.utility.Tuple;
import tubular.utility.WFilterSlot;
import tubular.utility.WToggleSprite;

public class BlockConnectorScreenController extends CottonCraftingController {
    private List<Tuple<Direction, WToggleSprite>> screenSprites = new ArrayList<>();

    private List<Tuple<Direction, WFilterSlot>> screenSlots = new ArrayList<>();

    private WLabel textName = new WLabel(new TranslatableText("name.tubular.tube_conenctor"), WLabel.DEFAULT_TEXT_COLOR);
    
    private WToggleSprite modeButtonNorth = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonSouth = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonWest = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonEast = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonUp = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonDown = new WToggleSprite(WToggleSprite.imageNone);

    private WLabel north = new WLabel("North");
    private WLabel south = new WLabel("South");
    private WLabel west = new WLabel("West");
    private WLabel east = new WLabel("East");
    private WLabel up = new WLabel("Up");
    private WLabel down = new WLabel("Down");

    public void setInitialData(BlockTubeConnectorEntity blockEntity) {
        screenSprites.add(new Tuple<Direction, WToggleSprite>(Direction.NORTH, modeButtonNorth));
        screenSprites.add(new Tuple<Direction, WToggleSprite>(Direction.SOUTH, modeButtonSouth));
        screenSprites.add(new Tuple<Direction, WToggleSprite>(Direction.WEST,  modeButtonWest));
        screenSprites.add(new Tuple<Direction, WToggleSprite>(Direction.EAST,  modeButtonEast));
        screenSprites.add(new Tuple<Direction, WToggleSprite>(Direction.UP,    modeButtonUp));
        screenSprites.add(new Tuple<Direction, WToggleSprite>(Direction.DOWN,  modeButtonDown));

        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.NORTH, new WFilterSlot(blockInventory, 0, Direction.NORTH, blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.SOUTH, new WFilterSlot(blockInventory, 1, Direction.SOUTH, blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.WEST,  new WFilterSlot(blockInventory, 2, Direction.WEST,  blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.EAST,  new WFilterSlot(blockInventory, 3, Direction.EAST,  blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.UP,    new WFilterSlot(blockInventory, 4, Direction.UP,    blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.DOWN,  new WFilterSlot(blockInventory, 5, Direction.DOWN,  blockEntity)));
    }

    public BlockTubeConnectorEntity getTubeEntity(BlockContext context) {
        BlockTubeConnectorEntity lambdaBypass[] = { null };

        context.run((world, blockPosition) -> {
            BlockTubeConnectorEntity temporaryEntity = (BlockTubeConnectorEntity)world.getBlockEntity(blockPosition);
            lambdaBypass[0] = temporaryEntity;
        });

        return lambdaBypass[0];
    }

    public WSprite asSprite(BlockMode mode) {
        switch(mode) {
            case REQUESTER:
                return WToggleSprite.spriteRequester;
            case PROVIDER:
                return WToggleSprite.spriteProvider;
            case NONE:
                return WToggleSprite.spriteNone;
            default:
                return null;
        }    
    }

    public Tuple<Integer, Integer> asPosition(Direction direction) {
        switch(direction) {
            case NORTH:
                return new Tuple<Integer, Integer>(0, 1);
            case SOUTH:
                return new Tuple<Integer, Integer>(0, 4);
            case WEST:
                return new Tuple<Integer, Integer>(3, 1);
            case EAST:
                return new Tuple<Integer, Integer>(3, 4);
            case UP:
                return new Tuple<Integer, Integer>(6, 1);
            case DOWN:
                return new Tuple<Integer, Integer>(6, 4);
            default:
                return null;
        }
    }
    
    public BlockConnectorScreenController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel)getRootPanel();

        BlockTubeConnectorEntity blockEntity = (BlockTubeConnectorEntity)world.getBlockEntity(this.getTubeEntity(context).getPos());

        setRootPanel(rootPanel);

        setInitialData(blockEntity);

        for (Tuple<Direction, WToggleSprite> tuple : screenSprites) {
            tuple.second.setOnClick( () -> {
                System.out.println(this.asPosition(tuple.getFirst()).getFirst().toString() + ":" + this.asPosition(tuple.getFirst()).getSecond().toString());
                switch (blockEntity.getMode(tuple.getFirst())) {
                    case PROVIDER:
                        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(NetworkRegistry.createTogglePacket(BlockMode.toInteger(BlockMode.REQUESTER), BlockTubeConnectorEntity.getIntegerTranslated(tuple.getFirst()), blockEntity.getPos()));
                        blockEntity.setMode(tuple.getFirst(), BlockMode.REQUESTER);
                        tuple.getSecond().setImage(WToggleSprite.imageRequester);
                        break;
                    case REQUESTER:
                        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(NetworkRegistry.createTogglePacket(BlockMode.toInteger(BlockMode.NONE), BlockTubeConnectorEntity.getIntegerTranslated(tuple.getFirst()), blockEntity.getPos()));
                        blockEntity.setMode(tuple.getFirst(), BlockMode.NONE);
                        tuple.getSecond().setImage(WToggleSprite.imageNone);
                        break; 
                    case NONE:
                        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(NetworkRegistry.createTogglePacket(BlockMode.toInteger(BlockMode.PROVIDER), BlockTubeConnectorEntity.getIntegerTranslated(tuple.getFirst()), blockEntity.getPos()));
                        blockEntity.setMode(tuple.getFirst(), BlockMode.PROVIDER);
                        tuple.getSecond().setImage(WToggleSprite.imageProvider);
                        break;
                }
            });
        }

        for (Tuple<Direction, WToggleSprite> tuple : screenSprites) {
            switch (blockEntity.getMode(tuple.getFirst())) {
                case PROVIDER:
                    tuple.getSecond().setImage(WToggleSprite.imageProvider);
                    break;
                case REQUESTER:
                    tuple.getSecond().setImage(WToggleSprite.imageRequester);
                    break;
                case NONE:
                    tuple.getSecond().setImage(WToggleSprite.imageNone);
                    break;
            }
        }

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 7);
        
        rootPanel.add(textName, 0, 0);

        rootPanel.add(screenSprites.get(0).getSecond(), this.asPosition(Direction.NORTH).getFirst(), this.asPosition(Direction.NORTH).getSecond());
        rootPanel.add(screenSprites.get(1).getSecond(), this.asPosition(Direction.SOUTH).getFirst(), this.asPosition(Direction.SOUTH).getSecond());
        rootPanel.add(screenSprites.get(2).getSecond(), this.asPosition(Direction.WEST).getFirst(), this.asPosition(Direction.WEST).getSecond());
        rootPanel.add(screenSprites.get(3).getSecond(), this.asPosition(Direction.EAST).getFirst(), this.asPosition(Direction.EAST).getSecond());
        rootPanel.add(screenSprites.get(4).getSecond(), this.asPosition(Direction.UP).getFirst(), this.asPosition(Direction.UP).getSecond());
        rootPanel.add(screenSprites.get(5).getSecond(), this.asPosition(Direction.DOWN).getFirst(), this.asPosition(Direction.DOWN).getSecond());
    
        rootPanel.add(screenSlots.get(0).getSecond(), 0, 2);
        rootPanel.add(screenSlots.get(1).getSecond(), 0, 5);
        rootPanel.add(screenSlots.get(2).getSecond(), 3, 2);
        rootPanel.add(screenSlots.get(3).getSecond(), 3, 5);
        rootPanel.add(screenSlots.get(4).getSecond(), 6, 2);
        rootPanel.add(screenSlots.get(5).getSecond(), 6, 5);

        rootPanel.add(north, 0, 3);
        rootPanel.add(south, 0, 6);
        rootPanel.add(west,  3, 3);
        rootPanel.add(east,  3, 6);
        rootPanel.add(up,    6, 3);
        rootPanel.add(down,  6, 6);
            
        rootPanel.validate(this);
    }

    @Override   
	public int getCraftingResultSlotIndex() {
		return -1;
    }
    
    @Override
    public boolean canUse(PlayerEntity entity) {
        return true;
    }
}