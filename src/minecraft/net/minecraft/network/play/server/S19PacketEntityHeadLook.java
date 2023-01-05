package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityHeadLook extends Packet
{
    private int field_149384_a;
    private byte field_149383_b;
    private static final String __OBFID = "CL_00001323";

    public S19PacketEntityHeadLook() {}

    public S19PacketEntityHeadLook(Entity p_i45214_1_, byte p_i45214_2_)
    {
        this.field_149384_a = p_i45214_1_.getEntityId();
        this.field_149383_b = p_i45214_2_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149384_a = data.readInt();
        this.field_149383_b = data.readByte();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149384_a);
        data.writeByte(this.field_149383_b);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityHeadLook(this);
    }

    public String serialize()
    {
        return String.format("id=%d, rot=%d", new Object[] {Integer.valueOf(this.field_149384_a), Byte.valueOf(this.field_149383_b)});
    }

    public Entity func_149381_a(World p_149381_1_)
    {
        return p_149381_1_.getEntityByID(this.field_149384_a);
    }

    public byte func_149380_c()
    {
        return this.field_149383_b;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
