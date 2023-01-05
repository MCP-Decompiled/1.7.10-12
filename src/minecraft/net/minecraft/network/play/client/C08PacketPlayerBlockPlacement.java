package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C08PacketPlayerBlockPlacement extends Packet
{
    private int placedBlockX;
    private int placedBlockY;
    private int placedBlockZ;
    private int placedBlockDirection;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    private static final String __OBFID = "CL_00001371";

    public C08PacketPlayerBlockPlacement() {}

    public C08PacketPlayerBlockPlacement(int p_i45265_1_, int p_i45265_2_, int p_i45265_3_, int p_i45265_4_, ItemStack p_i45265_5_, float p_i45265_6_, float p_i45265_7_, float p_i45265_8_)
    {
        this.placedBlockX = p_i45265_1_;
        this.placedBlockY = p_i45265_2_;
        this.placedBlockZ = p_i45265_3_;
        this.placedBlockDirection = p_i45265_4_;
        this.stack = p_i45265_5_ != null ? p_i45265_5_.copy() : null;
        this.facingX = p_i45265_6_;
        this.facingY = p_i45265_7_;
        this.facingZ = p_i45265_8_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.placedBlockX = data.readInt();
        this.placedBlockY = data.readUnsignedByte();
        this.placedBlockZ = data.readInt();
        this.placedBlockDirection = data.readUnsignedByte();
        this.stack = data.readItemStackFromBuffer();
        this.facingX = (float)data.readUnsignedByte() / 16.0F;
        this.facingY = (float)data.readUnsignedByte() / 16.0F;
        this.facingZ = (float)data.readUnsignedByte() / 16.0F;
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.placedBlockX);
        data.writeByte(this.placedBlockY);
        data.writeInt(this.placedBlockZ);
        data.writeByte(this.placedBlockDirection);
        data.writeItemStackToBuffer(this.stack);
        data.writeByte((int)(this.facingX * 16.0F));
        data.writeByte((int)(this.facingY * 16.0F));
        data.writeByte((int)(this.facingZ * 16.0F));
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerBlockPlacement(this);
    }

    public int getPlacedBlockX()
    {
        return this.placedBlockX;
    }

    public int getPlacedBlockY()
    {
        return this.placedBlockY;
    }

    public int getPlacedBlockZ()
    {
        return this.placedBlockZ;
    }

    public int getPlacedBlockDirection()
    {
        return this.placedBlockDirection;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public float getPlacedBlockOffsetX()
    {
        return this.facingX;
    }

    public float getPlacedBlockOffsetY()
    {
        return this.facingY;
    }

    public float getPlacedBlockOffsetZ()
    {
        return this.facingZ;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
