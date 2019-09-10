package tubular.registry;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import tubular.entity.BlockTubeConnectorEntity;
import tubular.utility.BlockMode;

public class NetworkRegistry {
    public static Identifier TOGGLE_PACKET = new Identifier("tubular", "connector_packet");

    public static CustomPayloadC2SPacket createTogglePacket(Integer toggle, Integer index, BlockPos position) {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeInt(toggle);
        buffer.writeInt(index);
        buffer.writeBlockPos(position);
        return new CustomPayloadC2SPacket(TOGGLE_PACKET, buffer);
    }

    public static void registerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(TOGGLE_PACKET, (packetContext, packetByteBuffer) -> {
            Integer toggle = packetByteBuffer.readInt();
            Integer index = packetByteBuffer.readInt();
            BlockPos position = packetByteBuffer.readBlockPos();

            if (toggle != null && index != null && position != null) {
                packetContext.getTaskQueue().execute(() -> {
                    BlockTubeConnectorEntity tubeEntity = (BlockTubeConnectorEntity)packetContext.getPlayer().world.getBlockEntity(position);;
                    tubeEntity.setMode((Direction)BlockTubeConnectorEntity.getInterfaceDirection(index), BlockMode.fromInteger(toggle));
                });   
            }
        });
    }
}