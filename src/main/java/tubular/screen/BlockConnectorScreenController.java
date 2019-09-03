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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import tubular.entity.BlockTubeConnectorEntity;
import tubular.utility.BlockMode;
import tubular.utility.Tuple;
import tubular.utility.WFilterSlot;

public class BlockConnectorScreenController extends CottonScreenController {
    private List<Tuple<Direction, WToggleButton>> screenToggles = new ArrayList<>();

    private List<Tuple<Direction, WFilterSlot>> screenSlots = new ArrayList<>();

    private WLabel textName = new WLabel(new TranslatableText("name.tubular.tube_conenctor"), WLabel.DEFAULT_TEXT_COLOR);

    private WToggleButton modeToggleNorth = new WToggleButton();
    private WToggleButton modeToggleSouth = new WToggleButton();
    private WToggleButton modeToggleWest = new WToggleButton();
    private WToggleButton modeToggleEast = new WToggleButton();
    private WToggleButton modeToggleUp = new WToggleButton();
    private WToggleButton modeToggleDown = new WToggleButton();

    private WLabel north = new WLabel("North");
    private WLabel south = new WLabel("South");
    private WLabel west = new WLabel("West");
    private WLabel east = new WLabel("East");
    private WLabel up = new WLabel("Up");
    private WLabel down = new WLabel("Down");

    public void initialize(BlockTubeConnectorEntity blockEntity) {
        screenToggles.add(new Tuple<Direction, WToggleButton>(Direction.NORTH, modeToggleNorth));
        screenToggles.add(new Tuple<Direction, WToggleButton>(Direction.SOUTH, modeToggleSouth));
        screenToggles.add(new Tuple<Direction, WToggleButton>(Direction.WEST,  modeToggleWest));
        screenToggles.add(new Tuple<Direction, WToggleButton>(Direction.EAST,  modeToggleEast));
        screenToggles.add(new Tuple<Direction, WToggleButton>(Direction.UP,    modeToggleUp));
        screenToggles.add(new Tuple<Direction, WToggleButton>(Direction.DOWN,  modeToggleDown));

        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.NORTH, new WFilterSlot(blockInventory, 0, Direction.NORTH, blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.SOUTH, new WFilterSlot(blockInventory, 1, Direction.SOUTH, blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.WEST,  new WFilterSlot(blockInventory, 2, Direction.WEST,  blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.EAST,  new WFilterSlot(blockInventory, 3, Direction.EAST,  blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.UP,    new WFilterSlot(blockInventory, 4, Direction.UP,    blockEntity)));
        screenSlots.add(new Tuple<Direction, WFilterSlot>(Direction.DOWN,  new WFilterSlot(blockInventory, 5, Direction.DOWN,  blockEntity)));
    }

    public BlockTubeConnectorEntity getEntity(BlockContext context) {
        BlockTubeConnectorEntity lambdaBypass[] = { null };

        context.run((world, blockPosition) -> {
            BlockTubeConnectorEntity temporaryEntity = (BlockTubeConnectorEntity)world.getBlockEntity(blockPosition);
            lambdaBypass[0] = temporaryEntity;
        });

        return lambdaBypass[0];
    }

    public BlockPos getPosition(BlockContext context) {
        BlockPos lambdaBypass[] = { null };
        context.run((world, blockPosition) -> {
            lambdaBypass[0] = blockPosition;
        });
        return lambdaBypass[0];
    }

    public BlockConnectorScreenController(int syncId, PlayerInventory playerInventory, BlockContext context, ServerWorld serverWorld) {
        super(RecipeType.CRAFTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel)getRootPanel();

        BlockTubeConnectorEntity blockEntity[] = { (BlockTubeConnectorEntity)world.getBlockEntity(this.getEntity(context).getPos()) };

        setRootPanel(rootPanel);

        initialize(blockEntity[0]);

        if (!world.isClient) {
            BlockTubeConnectorEntity temporaryEntity = this.getEntity(context);
            blockEntity[0] = temporaryEntity;
        } else {
            System.out.println("UwU wut");
        }

        for (Tuple<Direction, WToggleButton> tuple : screenToggles) {
            tuple.getSecond().setOnToggle( () -> {
                blockEntity[0].setMode(tuple.getFirst(), blockEntity[0].getMode(tuple.getFirst()).getOpposite());
                System.out.println("OwO that's wight, and " + world.isClient);
            });
        }

        for (Tuple<Direction, WToggleButton> tuple : screenToggles) {
            switch (blockEntity[0].getMode(tuple.getFirst())) {
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
}