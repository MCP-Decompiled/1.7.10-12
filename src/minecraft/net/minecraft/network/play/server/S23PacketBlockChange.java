package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S23PacketBlockChange extends Packet
{
    private int field_148887_a;
    private int field_148885_b;
    private int field_148886_c;
    private Block field_148883_d;
    private int field_148884_e;
    private static final String __OBFID = "CL_00001287";

    public S23PacketBlockChange() {}

    public S23PacketBlockChange(int p_i45177_1_, int p_i45177_2_, int p_i45177_3_, World p_i45177_4_)
    {
        this.field_148887_a = p_i45177_1_;
        this.field_148885_b = p_i45177_2_;
        this.field_148886_c = p_i45177_3_;
        this.field_148883_d = p_i45177_4_.getBlock(p_i45177_1_, p_i45177_2_, p_i45177_3_);
        this.field_148884_e = p_i45177_4_.getBlockMetadata(p_i45177_1_, p_i45177_2_, p_i45177_3_);
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148887_a = data.readInt();
        this.field_148885_b = data.readUnsignedByte();
        this.field_148886_c = data.readInt();
        this.field_148883_d = Block.getBlockById(data.readVarIntFromBuffer());
        this.field_148884_e = data.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_148887_a);
        data.writeByte(this.field_148885_b);
        data.writeInt(this.field_148886_c);
        data.writeVarIntToBuffer(Block.getIdFromBlock(this.field_148883_d));
        data.writeByte(this.field_148884_e);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleBlockChange(this);
    }

    public String serialize()
    {
        return String.format("type=%d, data=%d, x=%d, y=%d, z=%d", new Object[] {Integer.valueOf(Block.getIdFromBlock(this.field_148883_d)), Integer.valueOf(this.field_148884_e), Integer.valueOf(this.field_148887_a), Integer.valueOf(this.field_148885_b), Integer.valueOf(this.field_148886_c)});
    }

    public Block func_148880_c()
    {
        return this.field_148883_d;
    }

    public int func_148879_d()
    {
        return this.field_148887_a;
    }

    public int func_148878_e()
    {
        return this.field_148885_b;
    }

    public int func_148877_f()
    {
        return this.field_148886_c;
    }

    public int func_148881_g()
    {
        return this.field_148884_e;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
