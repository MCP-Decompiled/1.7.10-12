package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S24PacketBlockAction extends Packet
{
    private int field_148876_a;
    private int field_148874_b;
    private int field_148875_c;
    private int field_148872_d;
    private int field_148873_e;
    private Block field_148871_f;
    private static final String __OBFID = "CL_00001286";

    public S24PacketBlockAction() {}

    public S24PacketBlockAction(int p_i45176_1_, int p_i45176_2_, int p_i45176_3_, Block p_i45176_4_, int p_i45176_5_, int p_i45176_6_)
    {
        this.field_148876_a = p_i45176_1_;
        this.field_148874_b = p_i45176_2_;
        this.field_148875_c = p_i45176_3_;
        this.field_148872_d = p_i45176_5_;
        this.field_148873_e = p_i45176_6_;
        this.field_148871_f = p_i45176_4_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148876_a = data.readInt();
        this.field_148874_b = data.readShort();
        this.field_148875_c = data.readInt();
        this.field_148872_d = data.readUnsignedByte();
        this.field_148873_e = data.readUnsignedByte();
        this.field_148871_f = Block.getBlockById(data.readVarIntFromBuffer() & 4095);
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_148876_a);
        data.writeShort(this.field_148874_b);
        data.writeInt(this.field_148875_c);
        data.writeByte(this.field_148872_d);
        data.writeByte(this.field_148873_e);
        data.writeVarIntToBuffer(Block.getIdFromBlock(this.field_148871_f) & 4095);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleBlockAction(this);
    }

    public Block getBlockType()
    {
        return this.field_148871_f;
    }

    public int getX()
    {
        return this.field_148876_a;
    }

    public int getY()
    {
        return this.field_148874_b;
    }

    public int getZ()
    {
        return this.field_148875_c;
    }

    public int getData1()
    {
        return this.field_148872_d;
    }

    public int getData2()
    {
        return this.field_148873_e;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
