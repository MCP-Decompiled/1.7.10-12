package net.minecraft.network;

import net.minecraft.util.IChatComponent;

public interface INetHandler
{
    void onDisconnect(IChatComponent reason);

    void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState);

    void onNetworkTick();
}
