package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus extends Packet
{
    private C16PacketClientStatus.EnumState status;
    private static final String __OBFID = "CL_00001348";

    public C16PacketClientStatus() {}

    public C16PacketClientStatus(C16PacketClientStatus.EnumState statusIn)
    {
        this.status = statusIn;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.status = C16PacketClientStatus.EnumState.BY_ID[data.readByte() % C16PacketClientStatus.EnumState.BY_ID.length];
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.status.id);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processClientStatus(this);
    }

    public C16PacketClientStatus.EnumState getStatus()
    {
        return this.status;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }

    public static enum EnumState
    {
        PERFORM_RESPAWN("PERFORM_RESPAWN", 0, 0),
        REQUEST_STATS("REQUEST_STATS", 1, 1),
        OPEN_INVENTORY_ACHIEVEMENT("OPEN_INVENTORY_ACHIEVEMENT", 2, 2);
        private final int id;
        private static final C16PacketClientStatus.EnumState[] BY_ID = new C16PacketClientStatus.EnumState[values().length];

        private static final C16PacketClientStatus.EnumState[] $VALUES = new C16PacketClientStatus.EnumState[]{PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY_ACHIEVEMENT};
        private static final String __OBFID = "CL_00001349";

        private EnumState(String p_i45241_1_, int p_i45241_2_, int statusId)
        {
            this.id = statusId;
        }

        static {
            C16PacketClientStatus.EnumState[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                C16PacketClientStatus.EnumState var3 = var0[var2];
                BY_ID[var3.id] = var3;
            }
        }
    }
}
