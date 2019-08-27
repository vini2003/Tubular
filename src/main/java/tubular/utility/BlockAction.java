package tubular.utility;

public enum BlockAction {
    BLOCK_PLACED,
    BLOCK_BROKEN,
    BLOCK_ATTACH;

    public Boolean asBoolean(BlockAction blockAction) {
        switch (blockAction) {
            case BLOCK_PLACED:
                return true;
            case BLOCK_BROKEN:
                return false;
            default:
                return true;
        }
    }
}