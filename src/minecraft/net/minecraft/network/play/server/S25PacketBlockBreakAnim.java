package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S25PacketBlockBreakAnim extends Packet
{
    private int field_148852_a;
    private int field_148850_b;
    private int field_148851_c;
    private int field_148848_d;
    private int field_148849_e;
    private static final String __OBFID = "CL_00001284";

    public S25PacketBlockBreakAnim() {}

    public S25PacketBlockBreakAnim(int p_i45174_1_, int p_i45174_2_, int p_i45174_3_, int p_i45174_4_, int p_i45174_5_)
    {
        this.field_148852_a = p_i45174_1_;
        this.field_148850_b = p_i45174_2_;
        this.field_148851_c = p_i45174_3_;
        this.field_148848_d = p_i45174_4_;
        this.field_148849_e = p_i45174_5_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148852_a = data.readVarIntFromBuffer();
        this.field_148850_b = data.readInt();
        this.field_148851_c = data.readInt();
        this.field_148848_d = data.readInt();
        this.field_148849_e = data.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.field_148852_a);
        data.writeInt(this.field_148850_b);
        data.writeInt(this.field_148851_c);
        data.writeInt(this.field_148848_d);
        data.writeByte(this.field_148849_e);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleBlockBreakAnim(this);
    }

    public int func_148845_c()
    {
        return this.field_148852_a;
    }

    public int func_148844_d()
    {
        return this.field_148850_b;
    }

    public int func_148843_e()
    {
        return this.field_148851_c;
    }

    public int func_148842_f()
    {
        return this.field_148848_d;
    }

    public int func_148846_g()
    {
        return this.field_148849_e;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
