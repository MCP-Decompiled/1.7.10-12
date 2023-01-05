package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C01PacketPing extends Packet
{
    private long clientTime;
    private static final String __OBFID = "CL_00001392";

    public C01PacketPing() {}

    public C01PacketPing(long p_i45276_1_)
    {
        this.clientTime = p_i45276_1_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.clientTime = data.readLong();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeLong(this.clientTime);
    }

    public void processPacket(INetHandlerStatusServer handler)
    {
        handler.processPing(this);
    }

    public boolean hasPriority()
    {
        return true;
    }

    public long getClientTime()
    {
        return this.clientTime;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerStatusServer)handler);
    }
}
