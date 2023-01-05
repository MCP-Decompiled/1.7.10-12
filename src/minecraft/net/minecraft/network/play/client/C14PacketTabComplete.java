package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import org.apache.commons.lang3.StringUtils;

public class C14PacketTabComplete extends Packet
{
    private String message;
    private static final String __OBFID = "CL_00001346";

    public C14PacketTabComplete() {}

    public C14PacketTabComplete(String msg)
    {
        this.message = msg;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.message = data.readStringFromBuffer(32767);
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(StringUtils.substring(this.message, 0, 32767));
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processTabComplete(this);
    }

    public String getMessage()
    {
        return this.message;
    }

    public String serialize()
    {
        return String.format("message=\'%s\'", new Object[] {this.message});
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
