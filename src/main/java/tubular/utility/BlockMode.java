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

    public BlockMode getOpposite() {
        switch (this) {
            case REQUESTER:
                return PROVIDER;
            case PROVIDER:
                return REQUESTER;
            case NONE:
                return PROVIDER;
            default:
                return PROVIDER;
        }
    }
}