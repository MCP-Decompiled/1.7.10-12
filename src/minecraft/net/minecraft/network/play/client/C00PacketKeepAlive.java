package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C00PacketKeepAlive extends Packet
{
    private int key;
    private static final String __OBFID = "CL_00001359";

    public C00PacketKeepAlive() {}

    public C00PacketKeepAlive(int p_i45252_1_)
    {
        this.key = p_i45252_1_;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processKeepAlive(this);
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.key = data.readInt();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.key);
    }

    public boolean hasPriority()
    {
        return true;
    }

    public int getKey()
    {
        return this.key;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
