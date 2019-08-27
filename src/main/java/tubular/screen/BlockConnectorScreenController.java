package tubular.screen;

import java.util.ArrayList;
import java.util.List;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;

import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

import tubular.entity.BlockTubeConnectorEntity;
import tubular.utility.Tuple;
import tubular.utility.WFilterSlot;

public class BlockConnectorScreenController extends CottonScreenController {
    public static List<Tuple<Direction, WToggleButton>> screenToggles = new ArrayList<>();

    public static List<Tuple<Direction, WFilterSlot>> screenSlots = new ArrayList<>();

    private static WLabel textName = new WLabel(new TranslatableText("name.tubular.tube_conenctor"), WLabel.DEFAULT_TEXT_COLOR);

    private static WToggleButton modeToggleNorth = new WToggleButton();
    private static WToggleButton modeToggleSouth = new WToggleButton();
    private static WToggleButton modeToggleWest = new WToggleButton();
    private static WToggleButton modeToggleEast = new WToggleButton();
    private static WToggleButton modeToggleUp = new WToggleButton();
    private static WToggleButton modeToggleDown = new WToggleButton();

    private static WLabel north = new WLabel("North");
    private static WLabel south = new WLabel("South");
    private static WLabel west = new WLabel("West");
    private static WLabel east = new WLabel("East");
    private static WLabel up = new WLabel("Up");
    private static WLabel down = new WLabel("Down");

    private static WFilterSlot filterSlotNorth = null;
    private static WFilterSlot filterSlotSouth = null;
    private static WFilterSlot filterSlotWest =  null;
    private static WFilterSlot filterSlotEast =  null;
    private static WFilterSlot filterSlotUp =    null;
    private static WFilterSlot filterSlotDown =  null;

    public BlockTubeConnectorEntity getEntity(BlockContext context) {
        BlockTubeConnectorEntity lambdaBypass[] = { null };

        context.run((world, blockPosition) -> {
            BlockTubeConnectorEntity temporaryEntity = (BlockTubeConnectorEntity)world.getBlockEntity(blockPosition);
            lambdaBypass[0] = temporaryEntity;
        });

        return lambdaBypass[0];
    }

    public static void updateWildcards(BlockTubeConnectorEntity blockEntity) {
        for (Tuple<Direction, WFilterSlot> tuple : screenSlots) {
            if (tuple.getSecond().isEmpty()) {
                blockEntity.setWildcard(tuple.getFirst(), true);
            } else {
                blockEntity.setWildcard(tuple.getFirst(), false);
            }
        }
    }

    public BlockConnectorScreenController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        BlockTubeConnectorEntity blockEntity = this.getEntity(context);

        WGridPanel rootPanel = (WGridPanel)getRootPanel();

        setRootPanel(rootPanel);

        screenSlots.set(0, new Tuple<>(Direction.NORTH, new WFilterSlot(blockInventory, 0, Direction.NORTH, blockEntity)));
        screenSlots.set(1, new Tuple<>(Direction.SOUTH, new WFilterSlot(blockInventory, 1, Direction.SOUTH, blockEntity)));
        screenSlots.set(2, new Tuple<>(Direction.WEST,  new WFilterSlot(blockInventory, 2, Direction.WEST,  blockEntity)));
        screenSlots.set(3, new Tuple<>(Direction.EAST,  new WFilterSlot(blockInventory, 3, Direction.EAST,  blockEntity)));
        screenSlots.set(4, new Tuple<>(Direction.UP,    new WFilterSlot(blockInventory, 4, Direction.UP,    blockEntity)));
        screenSlots.set(5, new Tuple<>(Direction.DOWN,  new WFilterSlot(blockInventory, 5, Direction.DOWN,  blockEntity)));

        if (blockEntity != null) {
            for (Tuple<Direction, WToggleButton> tuple : screenToggles) {
                switch (blockEntity.getMode(tuple.getFirst())) {
                    case PROVIDER:
                        tuple.getSecond().setToggle(false);
                        break;
                    case REQUESTER:
                        tuple.getSecond().setToggle(true);
                        break;
                    default:
                        break;
                }
            }

            for (Tuple<Direction, WToggleButton> tuple : screenToggles) {
                tuple.getSecond().setOnToggle( () -> {
                    blockEntity.setMode(tuple.getFirst(), blockEntity.getMode(tuple.getFirst()).getOpposite());
                    System.out.println(blockEntity.getMode(tuple.getFirst()).asString());
                });
            };

            updateWildcards(blockEntity);
        }

        rootPanel.add(this.createPlayerInventoryPanel(), 0, 7);
        
        rootPanel.add(textName, 0, 0);

        rootPanel.add(screenToggles.get(0).getSecond(), 0, 1);
        rootPanel.add(screenToggles.get(1).getSecond(), 0, 4);
        rootPanel.add(screenToggles.get(2).getSecond(), 3, 1);
        rootPanel.add(screenToggles.get(3).getSecond(), 3, 4);
        rootPanel.add(screenToggles.get(4).getSecond(), 6, 1);
        rootPanel.add(screenToggles.get(5).getSecond(), 6, 4);
 
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

    static {
        screenToggles.add(new Tuple<>(Direction.NORTH, modeToggleNorth));
        screenToggles.add(new Tuple<>(Direction.SOUTH, modeToggleSouth));
        screenToggles.add(new Tuple<>(Direction.WEST,  modeToggleWest));
        screenToggles.add(new Tuple<>(Direction.EAST,  modeToggleEast));
        screenToggles.add(new Tuple<>(Direction.UP,    modeToggleUp));
        screenToggles.add(new Tuple<>(Direction.DOWN,  modeToggleDown));

        screenSlots.add(new Tuple<>(Direction.NORTH, filterSlotNorth));
        screenSlots.add(new Tuple<>(Direction.SOUTH, filterSlotSouth));
        screenSlots.add(new Tuple<>(Direction.WEST,  filterSlotWest));
        screenSlots.add(new Tuple<>(Direction.EAST,  filterSlotEast));
        screenSlots.add(new Tuple<>(Direction.UP,    filterSlotUp));
        screenSlots.add(new Tuple<>(Direction.DOWN,  filterSlotDown));
    }
}