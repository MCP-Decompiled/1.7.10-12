package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0BPacketEntityAction extends Packet
{
    private int field_149517_a;
    private int field_149515_b;
    private int field_149516_c;
    private static final String __OBFID = "CL_00001366";

    public C0BPacketEntityAction() {}

    public C0BPacketEntityAction(Entity p_i45259_1_, int p_i45259_2_)
    {
        this(p_i45259_1_, p_i45259_2_, 0);
    }

    public C0BPacketEntityAction(Entity p_i45260_1_, int p_i45260_2_, int p_i45260_3_)
    {
        this.field_149517_a = p_i45260_1_.getEntityId();
        this.field_149515_b = p_i45260_2_;
        this.field_149516_c = p_i45260_3_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149517_a = data.readInt();
        this.field_149515_b = data.readByte();
        this.field_149516_c = data.readInt();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149517_a);
        data.writeByte(this.field_149515_b);
        data.writeInt(this.field_149516_c);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processEntityAction(this);
    }

    public int func_149513_d()
    {
        return this.field_149515_b;
    }

    public int func_149512_e()
    {
        return this.field_149516_c;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
