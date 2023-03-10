package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2DPacketOpenWindow extends Packet
{
    private int windowId;
    private int inventoryType;
    private String windowTitle;
    private int slotCount;
    private boolean useProvidedTitle;
    private int entityId;
    private static final String __OBFID = "CL_00001293";

    public S2DPacketOpenWindow() {}

    public S2DPacketOpenWindow(int p_i45184_1_, int p_i45184_2_, String p_i45184_3_, int p_i45184_4_, boolean p_i45184_5_)
    {
        this.windowId = p_i45184_1_;
        this.inventoryType = p_i45184_2_;
        this.windowTitle = p_i45184_3_;
        this.slotCount = p_i45184_4_;
        this.useProvidedTitle = p_i45184_5_;
    }

    public S2DPacketOpenWindow(int p_i45185_1_, int p_i45185_2_, String p_i45185_3_, int p_i45185_4_, boolean p_i45185_5_, int p_i45185_6_)
    {
        this(p_i45185_1_, p_i45185_2_, p_i45185_3_, p_i45185_4_, p_i45185_5_);
        this.entityId = p_i45185_6_;
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleOpenWindow(this);
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.windowId = data.readUnsignedByte();
        this.inventoryType = data.readUnsignedByte();
        this.windowTitle = data.readStringFromBuffer(32);
        this.slotCount = data.readUnsignedByte();
        this.useProvidedTitle = data.readBoolean();

        if (this.inventoryType == 11)
        {
            this.entityId = data.readInt();
        }
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.windowId);
        data.writeByte(this.inventoryType);
        data.writeStringToBuffer(this.windowTitle);
        data.writeByte(this.slotCount);
        data.writeBoolean(this.useProvidedTitle);

        if (this.inventoryType == 11)
        {
            data.writeInt(this.entityId);
        }
    }

    public int func_148901_c()
    {
        return this.windowId;
    }

    public int func_148899_d()
    {
        return this.inventoryType;
    }

    public String func_148902_e()
    {
        return this.windowTitle;
    }

    public int func_148898_f()
    {
        return this.slotCount;
    }

    public boolean func_148900_g()
    {
        return this.useProvidedTitle;
    }

    public int func_148897_h()
    {
        return this.entityId;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
