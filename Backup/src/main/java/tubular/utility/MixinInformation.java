package tubular.utility;

import tubular.block.BlockTubeConnectorMode;

public class MixinInformation {
    private static Boolean isWildcard = false;
    private static BlockTubeConnectorMode modeToDraw;
    private static Integer ticksRemaining = 0;

    public static BlockTubeConnectorMode getMode() {
        return modeToDraw;
    }

    public static void setMode(BlockTubeConnectorMode mode) {
        modeToDraw = mode;
    }

    public static Integer getTicks() {
        return ticksRemaining;
    }

    public static void setTicks(Integer ticks) {
        ticksRemaining = ticks;
    }

    public static void decrementTick() {
        if (ticksRemaining > 0) {
            --ticksRemaining;
        }
    }

    public static Boolean isWildcard() {
        return isWildcard;
    }

    public static void setWildcard(Boolean wildcard) {
        isWildcard = wildcard;
    }
}