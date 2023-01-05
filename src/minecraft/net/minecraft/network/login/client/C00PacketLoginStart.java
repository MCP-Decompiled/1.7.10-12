package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class C00PacketLoginStart extends Packet
{
    private GameProfile profile;
    private static final String __OBFID = "CL_00001379";

    public C00PacketLoginStart() {}

    public C00PacketLoginStart(GameProfile profileIn)
    {
        this.profile = profileIn;
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.profile = new GameProfile((UUID)null, data.readStringFromBuffer(16));
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.profile.getName());
    }

    public void processPacket(INetHandlerLoginServer handler)
    {
        handler.processLoginStart(this);
    }

    public GameProfile getProfile()
    {
        return this.profile;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerLoginServer)handler);
    }
}
