package tubular.block;

import net.minecraft.util.StringIdentifiable;

public enum BlockTubeConnectorMode implements StringIdentifiable {
    REQUESTER("requester"),
    PROVIDER("provider");

    private final String name;

    private BlockTubeConnectorMode(String string) {
        this.name = string;
    }

    public String toString() {
       return this.name;
    }
  
    public String asString() {
       return this.name;
    }
}