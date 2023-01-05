package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S40PacketDisconnect extends Packet
{
    private IChatComponent reason;
    private static final String __OBFID = "CL_00001298";

    public S40PacketDisconnect() {}

    public S40PacketDisconnect(IChatComponent reasonIn)
    {
        this.reason = reasonIn;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.reason = IChatComponent.Serializer.jsonToComponent(data.readStringFromBuffer(32767));
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(IChatComponent.Serializer.componentToJson(this.reason));
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleDisconnect(this);
    }

    public boolean hasPriority()
    {
        return true;
    }

    public IChatComponent func_149165_c()
    {
        return this.reason;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
