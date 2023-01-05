package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0APacketAnimation extends Packet
{
    private int id;
    private int type;
    private static final String __OBFID = "CL_00001345";

    public C0APacketAnimation() {}

    public C0APacketAnimation(Entity p_i45238_1_, int p_i45238_2_)
    {
        this.id = p_i45238_1_.getEntityId();
        this.type = p_i45238_2_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.id = data.readInt();
        this.type = data.readByte();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.id);
        data.writeByte(this.type);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processAnimation(this);
    }

    public String serialize()
    {
        return String.format("id=%d, type=%d", new Object[] {Integer.valueOf(this.id), Integer.valueOf(this.type)});
    }

    public int getType()
    {
        return this.type;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
