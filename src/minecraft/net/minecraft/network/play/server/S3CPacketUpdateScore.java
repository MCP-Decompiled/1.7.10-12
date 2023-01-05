package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;

public class S3CPacketUpdateScore extends Packet
{
    private String field_149329_a = "";
    private String field_149327_b = "";
    private int field_149328_c;
    private int field_149326_d;
    private static final String __OBFID = "CL_00001335";

    public S3CPacketUpdateScore() {}

    public S3CPacketUpdateScore(Score p_i45227_1_, int p_i45227_2_)
    {
        this.field_149329_a = p_i45227_1_.getPlayerName();
        this.field_149327_b = p_i45227_1_.func_96645_d().getName();
        this.field_149328_c = p_i45227_1_.getScorePoints();
        this.field_149326_d = p_i45227_2_;
    }

    public S3CPacketUpdateScore(String p_i45228_1_)
    {
        this.field_149329_a = p_i45228_1_;
        this.field_149327_b = "";
        this.field_149328_c = 0;
        this.field_149326_d = 1;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149329_a = data.readStringFromBuffer(16);
        this.field_149326_d = data.readByte();

        if (this.field_149326_d != 1)
        {
            this.field_149327_b = data.readStringFromBuffer(16);
            this.field_149328_c = data.readInt();
        }
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149329_a);
        data.writeByte(this.field_149326_d);

        if (this.field_149326_d != 1)
        {
            data.writeStringToBuffer(this.field_149327_b);
            data.writeInt(this.field_149328_c);
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateScore(this);
    }

    public String func_149324_c()
    {
        return this.field_149329_a;
    }

    public String func_149321_d()
    {
        return this.field_149327_b;
    }

    public int func_149323_e()
    {
        return this.field_149328_c;
    }

    public int func_149322_f()
    {
        return this.field_149326_d;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
