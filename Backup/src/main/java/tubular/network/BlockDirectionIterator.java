package tubular.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.Direction;

public class BlockDirectionIterator {
    protected List<Direction> directionList = new ArrayList<>();
    BlockDirectionIterator() {
        directionList.add(Direction.NORTH);
        directionList.add(Direction.SOUTH);
        directionList.add(Direction.WEST);
        directionList.add(Direction.EAST);
        directionList.add(Direction.UP);
        directionList.add(Direction.DOWN);
    }
}