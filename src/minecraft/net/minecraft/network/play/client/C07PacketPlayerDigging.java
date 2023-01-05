package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C07PacketPlayerDigging extends Packet
{
    private int diggedBlockX;
    private int diggedBlockY;
    private int diggedBlockZ;
    private int diggingBlockFace;
    private int status;
    private static final String __OBFID = "CL_00001365";

    public C07PacketPlayerDigging() {}

    public C07PacketPlayerDigging(int p_i45258_1_, int p_i45258_2_, int p_i45258_3_, int p_i45258_4_, int p_i45258_5_)
    {
        this.status = p_i45258_1_;
        this.diggedBlockX = p_i45258_2_;
        this.diggedBlockY = p_i45258_3_;
        this.diggedBlockZ = p_i45258_4_;
        this.diggingBlockFace = p_i45258_5_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.status = data.readUnsignedByte();
        this.diggedBlockX = data.readInt();
        this.diggedBlockY = data.readUnsignedByte();
        this.diggedBlockZ = data.readInt();
        this.diggingBlockFace = data.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.status);
        data.writeInt(this.diggedBlockX);
        data.writeByte(this.diggedBlockY);
        data.writeInt(this.diggedBlockZ);
        data.writeByte(this.diggingBlockFace);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerDigging(this);
    }

    public int getDiggedBlockX()
    {
        return this.diggedBlockX;
    }

    public int getDiggedBlockY()
    {
        return this.diggedBlockY;
    }

    public int getDiggedBlockZ()
    {
        return this.diggedBlockZ;
    }

    public int getDiggingBlockFace()
    {
        return this.diggingBlockFace;
    }

    public int getDiggedBlockStatus()
    {
        return this.status;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
