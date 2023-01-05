package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class IntegratedPlayerList extends ServerConfigurationManager
{
    private NBTTagCompound hostPlayerData;
    private static final String __OBFID = "CL_00001128";

    public IntegratedPlayerList(IntegratedServer p_i1314_1_)
    {
        super(p_i1314_1_);
        this.setViewDistance(10);
    }

    protected void writePlayerData(EntityPlayerMP player)
    {
        if (player.getCommandSenderName().equals(this.getServerInstance().getServerOwner()))
        {
            this.hostPlayerData = new NBTTagCompound();
            player.writeToNBT(this.hostPlayerData);
        }

        super.writePlayerData(player);
    }

    public String allowUserToConnect(SocketAddress address, GameProfile profile)
    {
        return profile.getName().equalsIgnoreCase(this.getServerInstance().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }

    public IntegratedServer getServerInstance()
    {
        return (IntegratedServer)super.getServerInstance();
    }

    public NBTTagCompound getHostPlayerData()
    {
        return this.hostPlayerData;
    }
}
