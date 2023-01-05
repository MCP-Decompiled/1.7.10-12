package net.minecraft.world;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess
{
    private MinecraftServer mcServer;
    private WorldServer theWorldServer;
    private static final String __OBFID = "CL_00001433";

    public WorldManager(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_)
    {
        this.mcServer = p_i1517_1_;
        this.theWorldServer = p_i1517_2_;
    }

    public void spawnParticle(String p_72708_1_, double p_72708_2_, double p_72708_4_, double p_72708_6_, double p_72708_8_, double p_72708_10_, double p_72708_12_) {}

    public void onEntityCreate(Entity p_72703_1_)
    {
        this.theWorldServer.getEntityTracker().trackEntity(p_72703_1_);
    }

    public void onEntityDestroy(Entity p_72709_1_)
    {
        this.theWorldServer.getEntityTracker().untrackEntity(p_72709_1_);
    }

    public void playSound(String soundName, double x, double y, double z, float volume, float pitch)
    {
        this.mcServer.getConfigurationManager().sendToAllNear(x, y, z, volume > 1.0F ? (double)(16.0F * volume) : 16.0D, this.theWorldServer.provider.dimensionId, new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    public void playSoundToNearExcept(EntityPlayer p_85102_1_, String p_85102_2_, double p_85102_3_, double p_85102_5_, double p_85102_7_, float p_85102_9_, float p_85102_10_)
    {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(p_85102_1_, p_85102_3_, p_85102_5_, p_85102_7_, p_85102_9_ > 1.0F ? (double)(16.0F * p_85102_9_) : 16.0D, this.theWorldServer.provider.dimensionId, new S29PacketSoundEffect(p_85102_2_, p_85102_3_, p_85102_5_, p_85102_7_, p_85102_9_, p_85102_10_));
    }

    public void markBlockRangeForRenderUpdate(int p_147585_1_, int p_147585_2_, int p_147585_3_, int p_147585_4_, int p_147585_5_, int p_147585_6_) {}

    public void markBlockForUpdate(int p_147586_1_, int p_147586_2_, int p_147586_3_)
    {
        this.theWorldServer.getPlayerManager().markBlockForUpdate(p_147586_1_, p_147586_2_, p_147586_3_);
    }

    public void markBlockForRenderUpdate(int p_147588_1_, int p_147588_2_, int p_147588_3_) {}

    public void playRecord(String p_72702_1_, int p_72702_2_, int p_72702_3_, int p_72702_4_) {}

    public void playAuxSFX(EntityPlayer p_72706_1_, int p_72706_2_, int p_72706_3_, int p_72706_4_, int p_72706_5_, int p_72706_6_)
    {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(p_72706_1_, (double)p_72706_3_, (double)p_72706_4_, (double)p_72706_5_, 64.0D, this.theWorldServer.provider.dimensionId, new S28PacketEffect(p_72706_2_, p_72706_3_, p_72706_4_, p_72706_5_, p_72706_6_, false));
    }

    public void broadcastSound(int p_82746_1_, int p_82746_2_, int p_82746_3_, int p_82746_4_, int p_82746_5_)
    {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(p_82746_1_, p_82746_2_, p_82746_3_, p_82746_4_, p_82746_5_, true));
    }

    public void destroyBlockPartially(int p_147587_1_, int p_147587_2_, int p_147587_3_, int p_147587_4_, int p_147587_5_)
    {
        Iterator var6 = this.mcServer.getConfigurationManager().playerEntityList.iterator();

        while (var6.hasNext())
        {
            EntityPlayerMP var7 = (EntityPlayerMP)var6.next();

            if (var7 != null && var7.worldObj == this.theWorldServer && var7.getEntityId() != p_147587_1_)
            {
                double var8 = (double)p_147587_2_ - var7.posX;
                double var10 = (double)p_147587_3_ - var7.posY;
                double var12 = (double)p_147587_4_ - var7.posZ;

                if (var8 * var8 + var10 * var10 + var12 * var12 < 1024.0D)
                {
                    var7.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(p_147587_1_, p_147587_2_, p_147587_3_, p_147587_4_, p_147587_5_));
                }
            }
        }
    }

    public void onStaticEntitiesChanged() {}
}
