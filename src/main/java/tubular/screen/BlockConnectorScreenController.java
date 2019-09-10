package tubular.screen;

import java.util.ArrayList;
import java.util.List;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;
import tubular.entity.BlockTubeConnectorEntity;
import tubular.registry.NetworkRegistry;
import tubular.utility.BlockMode;
import tubular.utility.Tuple;
import tubular.utility.WFilterSlot;
import tubular.utility.WToggleSprite;

public class BlockConnectorScreenController extends CottonCraftingController {
    private WPlainPanel rootPanel = null;

    private BlockTubeConnectorEntity blockEntity = null;

    private List<Tuple<Direction, WFilterSlot>> screenSlots = new ArrayList<>();

    private List<Tuple<Direction, WToggleSprite>> screenSprites = new ArrayList<>();

    private WLabel textName = new WLabel(new TranslatableText("name.tubular.tube_conenctor"), WLabel.DEFAULT_TEXT_COLOR);
    
    private WToggleSprite modeButtonNorth = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonSouth = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonWest = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonEast = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonUp = new WToggleSprite(WToggleSprite.imageNone);
    private WToggleSprite modeButtonDown = new WToggleSprite(WToggleSprite.imageNone);

    private WLabel labelNorth = new WLabel("North");
    private WLabel labelSouth = new WLabel("South");
    private WLabel labelWest = new WLabel("West");
    private WLabel labelEast = new WLabel("East");
    private WLabel labelUp = new WLabel("Up");
    private WLabel labelDown = new WLabel("Down");

    private final Integer sectionX = 48;
    private final Integer sectionY = 42;

    public void initInterface(BlockTubeConnectorEntity blockEntity) {
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

    public void postInterface(BlockTubeConnectorEntity blockEntity) {
        for (Tuple<Direction, WToggleSprite> tuple : screenSprites) {
            switch (blockEntity.getMode(tuple.getFirst())) {
                case REQUESTER:
                    tuple.getSecond().setImage(WToggleSprite.getIdentifier(BlockMode.REQUESTER, false));
                    break;
                case PROVIDER:
                    tuple.getSecond().setImage(WToggleSprite.getIdentifier(BlockMode.PROVIDER, false));
                    break;
                case NONE:
                    tuple.getSecond().setImage(WToggleSprite.getIdentifier(BlockMode.NONE, false));
                    break;
            }
        }

        for (Tuple<Direction, WToggleSprite> tuple : screenSprites) {
            tuple.getSecond().setOnClick( () -> {
                BlockMode tubeMode = null;
                switch (blockEntity.getMode(tuple.getFirst())) {
                    case REQUESTER:
                        tubeMode = BlockMode.NONE;
                        tuple.getSecond().setImage(WToggleSprite.getIdentifier(BlockMode.NONE, false));
                        break; 
                    case PROVIDER:
                        tubeMode = BlockMode.REQUESTER;
                        tuple.getSecond().setImage(WToggleSprite.getIdentifier(BlockMode.REQUESTER, false));
                        break;
                    case NONE:
                        tubeMode = BlockMode.PROVIDER;
                        tuple.getSecond().setImage(WToggleSprite.getIdentifier(BlockMode.PROVIDER, false));
                        break;
                }
                MinecraftClient.getInstance().getNetworkHandler().getConnection().send(NetworkRegistry.createTogglePacket(BlockMode.toInteger(tubeMode), BlockTubeConnectorEntity.getInterfaceInteger(tuple.getFirst()), blockEntity.getPos()));
                blockEntity.setMode(tuple.getFirst(), tubeMode);
            });
        }
    }

    public void initPanel(WPlainPanel rootPanel) {
        rootPanel = new WPlainPanel();

        setRootPanel(rootPanel);

        rootPanel.add(textName, 40, 0);

        rootPanel.add(this.createPlayerInventoryPanel(), 0, sectionY * 3);

        rootPanel.add(screenSprites.get(0).getSecond(), this.getInterfacePosition(Direction.NORTH).getFirst() - 1, this.getInterfacePosition(Direction.NORTH).getSecond() + 20);
        rootPanel.add(screenSprites.get(1).getSecond(), this.getInterfacePosition(Direction.SOUTH).getFirst() - 1, this.getInterfacePosition(Direction.SOUTH).getSecond() + 20);
        rootPanel.add(screenSprites.get(2).getSecond(), this.getInterfacePosition(Direction.WEST).getFirst() - 1, this.getInterfacePosition(Direction.WEST).getSecond() + 20);
        rootPanel.add(screenSprites.get(3).getSecond(), this.getInterfacePosition(Direction.EAST).getFirst() - 1, this.getInterfacePosition(Direction.EAST).getSecond() + 20);
        rootPanel.add(screenSprites.get(4).getSecond(), this.getInterfacePosition(Direction.UP).getFirst() - 1, this.getInterfacePosition(Direction.UP).getSecond() + 20);
        rootPanel.add(screenSprites.get(5).getSecond(), this.getInterfacePosition(Direction.DOWN).getFirst() - 1, this.getInterfacePosition(Direction.DOWN).getSecond() + 20);
    
        rootPanel.add(screenSlots.get(0).getSecond(), this.getInterfacePosition(Direction.NORTH).getFirst(), this.getInterfacePosition(Direction.NORTH).getSecond() + sectionY);
        rootPanel.add(screenSlots.get(1).getSecond(), this.getInterfacePosition(Direction.SOUTH).getFirst(), this.getInterfacePosition(Direction.SOUTH).getSecond() + sectionY);
        rootPanel.add(screenSlots.get(2).getSecond(), this.getInterfacePosition(Direction.WEST).getFirst(), this.getInterfacePosition(Direction.WEST).getSecond() + sectionY);
        rootPanel.add(screenSlots.get(3).getSecond(), this.getInterfacePosition(Direction.EAST).getFirst(), this.getInterfacePosition(Direction.EAST).getSecond() + sectionY);
        rootPanel.add(screenSlots.get(4).getSecond(), this.getInterfacePosition(Direction.UP).getFirst(), this.getInterfacePosition(Direction.UP).getSecond() + sectionY);
        rootPanel.add(screenSlots.get(5).getSecond(), this.getInterfacePosition(Direction.DOWN).getFirst(), this.getInterfacePosition(Direction.DOWN).getSecond() + sectionY);

        rootPanel.add(labelNorth, this.getInterfacePosition(Direction.NORTH).getFirst() - 4, this.getInterfacePosition(Direction.NORTH).getSecond() + sectionY + 18);
        rootPanel.add(labelSouth, this.getInterfacePosition(Direction.SOUTH).getFirst() - 5, this.getInterfacePosition(Direction.SOUTH).getSecond() + sectionY + 18);
        rootPanel.add(labelWest,  this.getInterfacePosition(Direction.WEST).getFirst() - 1, this.getInterfacePosition(Direction.WEST).getSecond() + sectionY  + 18);
        rootPanel.add(labelEast,  this.getInterfacePosition(Direction.EAST).getFirst() - 2, this.getInterfacePosition(Direction.EAST).getSecond() + sectionY + 18);
        rootPanel.add(labelUp,    this.getInterfacePosition(Direction.UP).getFirst() + 3, this.getInterfacePosition(Direction.UP).getSecond() + sectionY + 18);
        rootPanel.add(labelDown,  this.getInterfacePosition(Direction.DOWN).getFirst() - 3, this.getInterfacePosition(Direction.DOWN).getSecond() + sectionY + 18);
            
        rootPanel.validate(this);
    }

    public BlockTubeConnectorEntity getBlockEntity(BlockContext context) {
        BlockTubeConnectorEntity lambdaBypass[] = { null };

        context.run((world, blockPosition) -> {
            BlockTubeConnectorEntity temporaryEntity = (BlockTubeConnectorEntity)world.getBlockEntity(blockPosition);
            lambdaBypass[0] = temporaryEntity;
        });

        return lambdaBypass[0];
    }

    public Tuple<Integer, Integer> getInterfacePosition(Direction direction) {
        switch(direction) {
            case NORTH:
                return new Tuple<Integer, Integer>(sectionX * 1 - 36, 0);
            case SOUTH:
                return new Tuple<Integer, Integer>(sectionX * 1 - 36, sectionY + 12);
            case WEST:
                return new Tuple<Integer, Integer>(sectionX * 2 - 27, 0);
            case EAST:
                return new Tuple<Integer, Integer>(sectionX * 2 - 27, sectionY + 12);
            case UP:
                return new Tuple<Integer, Integer>(sectionX * 3 - 18, 0);
            case DOWN:
                return new Tuple<Integer, Integer>(sectionX * 3 - 18, sectionY + 12);
            default:
                return null;
        }
    }
    
    public BlockConnectorScreenController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        blockEntity = (BlockTubeConnectorEntity)world.getBlockEntity(this.getBlockEntity(context).getPos());

        this.initInterface(blockEntity);

        this.postInterface(blockEntity);
        
        this.initPanel(rootPanel);
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