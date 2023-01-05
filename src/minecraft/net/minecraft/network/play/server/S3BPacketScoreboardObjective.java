package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScoreObjective;

public class S3BPacketScoreboardObjective extends Packet
{
    private String field_149343_a;
    private String field_149341_b;
    private int field_149342_c;
    private static final String __OBFID = "CL_00001333";

    public S3BPacketScoreboardObjective() {}

    public S3BPacketScoreboardObjective(ScoreObjective p_i45224_1_, int p_i45224_2_)
    {
        this.field_149343_a = p_i45224_1_.getName();
        this.field_149341_b = p_i45224_1_.getDisplayName();
        this.field_149342_c = p_i45224_2_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149343_a = data.readStringFromBuffer(16);
        this.field_149341_b = data.readStringFromBuffer(32);
        this.field_149342_c = data.readByte();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.field_149343_a);
        data.writeStringToBuffer(this.field_149341_b);
        data.writeByte(this.field_149342_c);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleScoreboardObjective(this);
    }

    public String func_149339_c()
    {
        return this.field_149343_a;
    }

    public String func_149337_d()
    {
        return this.field_149341_b;
    }

    public int func_149338_e()
    {
        return this.field_149342_c;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
