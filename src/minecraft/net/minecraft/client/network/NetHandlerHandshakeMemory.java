package net.minecraft.client.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;

public class NetHandlerHandshakeMemory implements INetHandlerHandshakeServer
{
    private final MinecraftServer field_147385_a;
    private final NetworkManager field_147384_b;
    private static final String __OBFID = "CL_00001445";

    public NetHandlerHandshakeMemory(MinecraftServer p_i45287_1_, NetworkManager p_i45287_2_)
    {
        this.field_147385_a = p_i45287_1_;
        this.field_147384_b = p_i45287_2_;
    }

    public void processHandshake(C00Handshake packetIn)
    {
        this.field_147384_b.setConnectionState(packetIn.getRequestedState());
    }

    public void onDisconnect(IChatComponent reason) {}

    public void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState)
    {
        Validate.validState(newState == EnumConnectionState.LOGIN || newState == EnumConnectionState.STATUS, "Unexpected protocol " + newState, new Object[0]);

        switch (NetHandlerHandshakeMemory.SwitchEnumConnectionState.field_151263_a[newState.ordinal()])
        {
            case 1:
                this.field_147384_b.setNetHandler(new NetHandlerLoginServer(this.field_147385_a, this.field_147384_b));
                break;

            case 2:
                throw new UnsupportedOperationException("NYI");

            default:
        }
    }

    public void onNetworkTick() {}

    static final class SwitchEnumConnectionState
    {
        static final int[] field_151263_a = new int[EnumConnectionState.values().length];
        private static final String __OBFID = "CL_00001446";

        static
        {
            try
            {
                field_151263_a[EnumConnectionState.LOGIN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_151263_a[EnumConnectionState.STATUS.ordinal()] = 2;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
