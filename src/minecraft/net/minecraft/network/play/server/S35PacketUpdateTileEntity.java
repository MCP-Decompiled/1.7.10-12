package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S35PacketUpdateTileEntity extends Packet
{
    private int x;
    private int y;
    private int z;
    private int metadata;
    private NBTTagCompound nbt;
    private static final String __OBFID = "CL_00001285";

    public S35PacketUpdateTileEntity() {}

    public S35PacketUpdateTileEntity(int xcoord, int ycoord, int zcoord, int meta, NBTTagCompound nbtTag)
    {
        this.x = xcoord;
        this.y = ycoord;
        this.z = zcoord;
        this.metadata = meta;
        this.nbt = nbtTag;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.x = data.readInt();
        this.y = data.readShort();
        this.z = data.readInt();
        this.metadata = data.readUnsignedByte();
        this.nbt = data.readNBTTagCompoundFromBuffer();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.x);
        data.writeShort(this.y);
        data.writeInt(this.z);
        data.writeByte((byte)this.metadata);
        data.writeNBTTagCompoundToBuffer(this.nbt);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateTileEntity(this);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    public int getTileEntityType()
    {
        return this.metadata;
    }

    public NBTTagCompound getNbtCompound()
    {
        return this.nbt;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
