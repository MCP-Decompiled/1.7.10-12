package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S05PacketSpawnPosition extends Packet
{
    private int field_149364_a;
    private int field_149362_b;
    private int field_149363_c;
    private static final String __OBFID = "CL_00001336";

    public S05PacketSpawnPosition() {}

    public S05PacketSpawnPosition(int p_i45229_1_, int p_i45229_2_, int p_i45229_3_)
    {
        this.field_149364_a = p_i45229_1_;
        this.field_149362_b = p_i45229_2_;
        this.field_149363_c = p_i45229_3_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149364_a = data.readInt();
        this.field_149362_b = data.readInt();
        this.field_149363_c = data.readInt();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149364_a);
        data.writeInt(this.field_149362_b);
        data.writeInt(this.field_149363_c);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnPosition(this);
    }

    public boolean hasPriority()
    {
        return false;
    }

    public String serialize()
    {
        return String.format("x=%d, y=%d, z=%d", new Object[] {Integer.valueOf(this.field_149364_a), Integer.valueOf(this.field_149362_b), Integer.valueOf(this.field_149363_c)});
    }

    public int func_149360_c()
    {
        return this.field_149364_a;
    }

    public int func_149359_d()
    {
        return this.field_149362_b;
    }

    public int func_149358_e()
    {
        return this.field_149363_c;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
