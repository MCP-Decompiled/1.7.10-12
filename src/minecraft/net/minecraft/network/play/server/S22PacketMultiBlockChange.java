package net.minecraft.network.play.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class S22PacketMultiBlockChange extends Packet
{
    private static final Logger logger = LogManager.getLogger();
    private ChunkCoordIntPair field_148925_b;
    private byte[] field_148926_c;
    private int field_148924_d;
    private static final String __OBFID = "CL_00001290";

    public S22PacketMultiBlockChange() {}

    public S22PacketMultiBlockChange(int p_i45181_1_, short[] p_i45181_2_, Chunk p_i45181_3_)
    {
        this.field_148925_b = new ChunkCoordIntPair(p_i45181_3_.xPosition, p_i45181_3_.zPosition);
        this.field_148924_d = p_i45181_1_;
        int var4 = 4 * p_i45181_1_;

        try
        {
            ByteArrayOutputStream var5 = new ByteArrayOutputStream(var4);
            DataOutputStream var6 = new DataOutputStream(var5);

            for (int var7 = 0; var7 < p_i45181_1_; ++var7)
            {
                int var8 = p_i45181_2_[var7] >> 12 & 15;
                int var9 = p_i45181_2_[var7] >> 8 & 15;
                int var10 = p_i45181_2_[var7] & 255;
                var6.writeShort(p_i45181_2_[var7]);
                var6.writeShort((short)((Block.getIdFromBlock(p_i45181_3_.getBlock(var8, var10, var9)) & 4095) << 4 | p_i45181_3_.getBlockMetadata(var8, var10, var9) & 15));
            }

            this.field_148926_c = var5.toByteArray();

            if (this.field_148926_c.length != var4)
            {
                throw new RuntimeException("Expected length " + var4 + " doesn\'t match received length " + this.field_148926_c.length);
            }
        }
        catch (IOException var11)
        {
            logger.error("Couldn\'t create bulk block update packet", var11);
            this.field_148926_c = null;
        }
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148925_b = new ChunkCoordIntPair(data.readInt(), data.readInt());
        this.field_148924_d = data.readShort() & 65535;
        int var2 = data.readInt();

        if (var2 > 0)
        {
            this.field_148926_c = new byte[var2];
            data.readBytes(this.field_148926_c);
        }
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_148925_b.chunkXPos);
        data.writeInt(this.field_148925_b.chunkZPos);
        data.writeShort((short)this.field_148924_d);

        if (this.field_148926_c != null)
        {
            data.writeInt(this.field_148926_c.length);
            data.writeBytes(this.field_148926_c);
        }
        else
        {
            data.writeInt(0);
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleMultiBlockChange(this);
    }

    public String serialize()
    {
        return String.format("xc=%d, zc=%d, count=%d", new Object[] {Integer.valueOf(this.field_148925_b.chunkXPos), Integer.valueOf(this.field_148925_b.chunkZPos), Integer.valueOf(this.field_148924_d)});
    }

    public ChunkCoordIntPair func_148920_c()
    {
        return this.field_148925_b;
    }

    public byte[] func_148921_d()
    {
        return this.field_148926_c;
    }

    public int func_148922_e()
    {
        return this.field_148924_d;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
