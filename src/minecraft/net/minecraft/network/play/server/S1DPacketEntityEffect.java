package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

public class S1DPacketEntityEffect extends Packet
{
    private int field_149434_a;
    private byte field_149432_b;
    private byte field_149433_c;
    private short field_149431_d;
    private static final String __OBFID = "CL_00001343";

    public S1DPacketEntityEffect() {}

    public S1DPacketEntityEffect(int p_i45237_1_, PotionEffect p_i45237_2_)
    {
        this.field_149434_a = p_i45237_1_;
        this.field_149432_b = (byte)(p_i45237_2_.getPotionID() & 255);
        this.field_149433_c = (byte)(p_i45237_2_.getAmplifier() & 255);

        if (p_i45237_2_.getDuration() > 32767)
        {
            this.field_149431_d = 32767;
        }
        else
        {
            this.field_149431_d = (short)p_i45237_2_.getDuration();
        }
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149434_a = data.readInt();
        this.field_149432_b = data.readByte();
        this.field_149433_c = data.readByte();
        this.field_149431_d = data.readShort();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149434_a);
        data.writeByte(this.field_149432_b);
        data.writeByte(this.field_149433_c);
        data.writeShort(this.field_149431_d);
    }

    public boolean func_149429_c()
    {
        return this.field_149431_d == 32767;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityEffect(this);
    }

    public int func_149426_d()
    {
        return this.field_149434_a;
    }

    public byte func_149427_e()
    {
        return this.field_149432_b;
    }

    public byte func_149428_f()
    {
        return this.field_149433_c;
    }

    public short func_149425_g()
    {
        return this.field_149431_d;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
