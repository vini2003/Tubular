package tubular.utility;

import net.minecraft.util.StringIdentifiable;

public enum BlockMode implements StringIdentifiable {
    REQUESTER("requester"),
    PROVIDER("provider"),
    NONE("none");

    private final String name;

    private BlockMode(String string) {
        this.name = string;
    }

    public String toString() {
       return this.name;
    }
  
    public String asString() {
       return this.name;
    }

    public static BlockMode fromString(String string) {
        switch(string) {
            case "requester": 
                return REQUESTER;
            case "provider":
                return PROVIDER;
            default:
                return NONE;
        }
    }

    public static BlockMode fromBoolean(Boolean bool) {
        if (bool) {
            return REQUESTER;
        } else {
            return PROVIDER;
        }
    }

    public static BlockMode fromInteger(Integer integer) {
        switch (integer) {
            case 0:
                return NONE;
            case 1:
                return PROVIDER;
            case 2:
                return REQUESTER;
            default:
                return NONE;
        }
    }

    public static Integer toInteger(BlockMode mode) {
        switch (mode) {
            case NONE:
                return 0;
            case PROVIDER:
                return 1;
            case REQUESTER:
                return 2;
            default:
                return 0;
        }
    }

    public BlockMode getOpposite() {
        switch (this) {
            case REQUESTER:
                return NONE;
            case PROVIDER:
                return REQUESTER;
            case NONE:
                return PROVIDER;
            default:
                return NONE;
        }
    }
}