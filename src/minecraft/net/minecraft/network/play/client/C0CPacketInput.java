package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0CPacketInput extends Packet
{
    private float strafeSpeed;
    private float forwardSpeed;
    private boolean jumping;
    private boolean sneaking;
    private static final String __OBFID = "CL_00001367";

    public C0CPacketInput() {}

    public C0CPacketInput(float p_i45261_1_, float p_i45261_2_, boolean p_i45261_3_, boolean p_i45261_4_)
    {
        this.strafeSpeed = p_i45261_1_;
        this.forwardSpeed = p_i45261_2_;
        this.jumping = p_i45261_3_;
        this.sneaking = p_i45261_4_;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.strafeSpeed = data.readFloat();
        this.forwardSpeed = data.readFloat();
        this.jumping = data.readBoolean();
        this.sneaking = data.readBoolean();
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeFloat(this.strafeSpeed);
        data.writeFloat(this.forwardSpeed);
        data.writeBoolean(this.jumping);
        data.writeBoolean(this.sneaking);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processInput(this);
    }

    public float getStrafeSpeed()
    {
        return this.strafeSpeed;
    }

    public float getForwardSpeed()
    {
        return this.forwardSpeed;
    }

    public boolean isJumping()
    {
        return this.jumping;
    }

    public boolean isSneaking()
    {
        return this.sneaking;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
