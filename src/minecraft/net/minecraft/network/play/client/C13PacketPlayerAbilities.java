package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C13PacketPlayerAbilities extends Packet
{
    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;
    private static final String __OBFID = "CL_00001364";

    public C13PacketPlayerAbilities() {}

    public C13PacketPlayerAbilities(PlayerCapabilities capabilities)
    {
        this.setInvulnerable(capabilities.disableDamage);
        this.setFlying(capabilities.isFlying);
        this.setAllowFlying(capabilities.allowFlying);
        this.setCreativeMode(capabilities.isCreativeMode);
        this.setFlySpeed(capabilities.getFlySpeed());
        this.setWalkSpeed(capabilities.getWalkSpeed());
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        byte var2 = data.readByte();
        this.setInvulnerable((var2 & 1) > 0);
        this.setFlying((var2 & 2) > 0);
        this.setAllowFlying((var2 & 4) > 0);
        this.setCreativeMode((var2 & 8) > 0);
        this.setFlySpeed(data.readFloat());
        this.setWalkSpeed(data.readFloat());
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        byte var2 = 0;

        if (this.isInvulnerable())
        {
            var2 = (byte)(var2 | 1);
        }

        if (this.isFlying())
        {
            var2 = (byte)(var2 | 2);
        }

        if (this.isAllowFlying())
        {
            var2 = (byte)(var2 | 4);
        }

        if (this.isCreativeMode())
        {
            var2 = (byte)(var2 | 8);
        }

        data.writeByte(var2);
        data.writeFloat(this.flySpeed);
        data.writeFloat(this.walkSpeed);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerAbilities(this);
    }

    public String serialize()
    {
        return String.format("invuln=%b, flying=%b, canfly=%b, instabuild=%b, flyspeed=%.4f, walkspped=%.4f", new Object[] {Boolean.valueOf(this.isInvulnerable()), Boolean.valueOf(this.isFlying()), Boolean.valueOf(this.isAllowFlying()), Boolean.valueOf(this.isCreativeMode()), Float.valueOf(this.getFlySpeed()), Float.valueOf(this.getWalkSpeed())});
    }

    public boolean isInvulnerable()
    {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean isInvulnerable)
    {
        this.invulnerable = isInvulnerable;
    }

    public boolean isFlying()
    {
        return this.flying;
    }

    public void setFlying(boolean isFlying)
    {
        this.flying = isFlying;
    }

    public boolean isAllowFlying()
    {
        return this.allowFlying;
    }

    public void setAllowFlying(boolean isAllowFlying)
    {
        this.allowFlying = isAllowFlying;
    }

    public boolean isCreativeMode()
    {
        return this.creativeMode;
    }

    public void setCreativeMode(boolean isCreativeMode)
    {
        this.creativeMode = isCreativeMode;
    }

    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    public void setFlySpeed(float flySpeedIn)
    {
        this.flySpeed = flySpeedIn;
    }

    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    public void setWalkSpeed(float walkSpeedIn)
    {
        this.walkSpeed = walkSpeedIn;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
