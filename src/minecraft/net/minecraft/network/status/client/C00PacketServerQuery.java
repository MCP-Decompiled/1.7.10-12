package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C00PacketServerQuery extends Packet
{
    private static final String __OBFID = "CL_00001393";

    public void readPacketData(PacketBuffer data) throws IOException {}

    public void writePacketData(PacketBuffer data) throws IOException {}

    public void processPacket(INetHandlerStatusServer handler)
    {
        handler.processServerQuery(this);
    }

    public boolean hasPriority()
    {
        return true;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerStatusServer)handler);
    }
}
