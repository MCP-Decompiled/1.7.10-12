package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C12PacketUpdateSign extends Packet
{
    private int x;
    private int y;
    private int z;
    private String[] lines;
    private static final String __OBFID = "CL_00001370";

    public C12PacketUpdateSign() {}

    public C12PacketUpdateSign(int p_i45264_1_, int p_i45264_2_, int p_i45264_3_, String[] p_i45264_4_)
    {
        this.x = p_i45264_1_;
        this.y = p_i45264_2_;
        this.z = p_i45264_3_;
        this.lines = new String[] {p_i45264_4_[0], p_i45264_4_[1], p_i45264_4_[2], p_i45264_4_[3]};
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.x = data.readInt();
        this.y = data.readShort();
        this.z = data.readInt();
        this.lines = new String[4];

        for (int var2 = 0; var2 < 4; ++var2)
        {
            this.lines[var2] = data.readStringFromBuffer(15);
        }
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.x);
        data.writeShort(this.y);
        data.writeInt(this.z);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            data.writeStringToBuffer(this.lines[var2]);
        }
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processUpdateSign(this);
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

    public String[] getLines()
    {
        return this.lines;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
