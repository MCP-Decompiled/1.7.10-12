package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S36PacketSignEditorOpen extends Packet
{
    private int field_149133_a;
    private int field_149131_b;
    private int field_149132_c;
    private static final String __OBFID = "CL_00001316";

    public S36PacketSignEditorOpen() {}

    public S36PacketSignEditorOpen(int p_i45207_1_, int p_i45207_2_, int p_i45207_3_)
    {
        this.field_149133_a = p_i45207_1_;
        this.field_149131_b = p_i45207_2_;
        this.field_149132_c = p_i45207_3_;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSignEditorOpen(this);
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149133_a = data.readInt();
        this.field_149131_b = data.readInt();
        this.field_149132_c = data.readInt();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149133_a);
        data.writeInt(this.field_149131_b);
        data.writeInt(this.field_149132_c);
    }

    public int func_149129_c()
    {
        return this.field_149133_a;
    }

    public int func_149128_d()
    {
        return this.field_149131_b;
    }

    public int func_149127_e()
    {
        return this.field_149132_c;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
