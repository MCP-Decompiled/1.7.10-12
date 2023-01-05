package net.minecraft.server.network;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;

public class NetHandlerStatusServer implements INetHandlerStatusServer
{
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private static final String __OBFID = "CL_00001464";

    public NetHandlerStatusServer(MinecraftServer serverIn, NetworkManager netManager)
    {
        this.server = serverIn;
        this.networkManager = netManager;
    }

    public void onDisconnect(IChatComponent reason) {}

    public void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState)
    {
        if (newState != EnumConnectionState.STATUS)
        {
            throw new UnsupportedOperationException("Unexpected change in protocol to " + newState);
        }
    }

    public void onNetworkTick() {}

    public void processServerQuery(C00PacketServerQuery packetIn)
    {
        this.networkManager.scheduleOutboundPacket(new S00PacketServerInfo(this.server.getServerStatusResponse()), new GenericFutureListener[0]);
    }

    public void processPing(C01PacketPing packetIn)
    {
        this.networkManager.scheduleOutboundPacket(new S01PacketPong(packetIn.getClientTime()), new GenericFutureListener[0]);
    }
}
