package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraft.world.storage.IPlayerFileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServerConfigurationManager
{
    public static final File FILE_PLAYERBANS = new File("banned-players.json");
    public static final File FILE_IPBANS = new File("banned-ips.json");
    public static final File FILE_OPS = new File("ops.json");
    public static final File FILE_WHITELIST = new File("whitelist.json");
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
    private final MinecraftServer mcServer;
    public final List playerEntityList = new ArrayList();
    private final UserListBans bannedPlayers;
    private final BanList bannedIPs;
    private final UserListOps ops;
    private final UserListWhitelist whiteListedPlayers;
    private final Map playerStatFiles;
    private IPlayerFileData playerNBTManagerObj;
    private boolean whiteListEnforced;
    protected int maxPlayers;
    private int viewDistance;
    private WorldSettings.GameType gameType;
    private boolean commandsAllowedForAll;
    private int playerPingIndex;
    private static final String __OBFID = "CL_00001423";

    public ServerConfigurationManager(MinecraftServer server)
    {
        this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
        this.bannedIPs = new BanList(FILE_IPBANS);
        this.ops = new UserListOps(FILE_OPS);
        this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
        this.playerStatFiles = Maps.newHashMap();
        this.mcServer = server;
        this.bannedPlayers.setLanServer(false);
        this.bannedIPs.setLanServer(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP player)
    {
        GameProfile var3 = player.getGameProfile();
        PlayerProfileCache var4 = this.mcServer.getPlayerProfileCache();
        GameProfile var5 = var4.func_152652_a(var3.getId());
        String var6 = var5 == null ? var3.getName() : var5.getName();
        var4.func_152649_a(var3);
        NBTTagCompound var7 = this.readPlayerDataFromFile(player);
        player.setWorld(this.mcServer.worldServerForDimension(player.dimension));
        player.theItemInWorldManager.setWorld((WorldServer)player.worldObj);
        String var8 = "local";

        if (netManager.getRemoteAddress() != null)
        {
            var8 = netManager.getRemoteAddress().toString();
        }

        logger.info(player.getCommandSenderName() + "[" + var8 + "] logged in with entity id " + player.getEntityId() + " at (" + player.posX + ", " + player.posY + ", " + player.posZ + ")");
        WorldServer var9 = this.mcServer.worldServerForDimension(player.dimension);
        ChunkCoordinates var10 = var9.getSpawnPoint();
        this.func_72381_a(player, (EntityPlayerMP)null, var9);
        NetHandlerPlayServer var11 = new NetHandlerPlayServer(this.mcServer, netManager, player);
        var11.sendPacket(new S01PacketJoinGame(player.getEntityId(), player.theItemInWorldManager.getGameType(), var9.getWorldInfo().isHardcoreModeEnabled(), var9.provider.dimensionId, var9.difficultySetting, this.getMaxPlayers(), var9.getWorldInfo().getTerrainType()));
        var11.sendPacket(new S3FPacketCustomPayload("MC|Brand", this.getServerInstance().getServerModName().getBytes(Charsets.UTF_8)));
        var11.sendPacket(new S05PacketSpawnPosition(var10.posX, var10.posY, var10.posZ));
        var11.sendPacket(new S39PacketPlayerAbilities(player.capabilities));
        var11.sendPacket(new S09PacketHeldItemChange(player.inventory.currentItem));
        player.getStatFile().func_150877_d();
        player.getStatFile().func_150884_b(player);
        this.func_96456_a((ServerScoreboard)var9.getScoreboard(), player);
        this.mcServer.refreshStatusNextTick();
        ChatComponentTranslation var12;

        if (!player.getCommandSenderName().equalsIgnoreCase(var6))
        {
            var12 = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] {player.getFormattedCommandSenderName(), var6});
        }
        else
        {
            var12 = new ChatComponentTranslation("multiplayer.player.joined", new Object[] {player.getFormattedCommandSenderName()});
        }

        var12.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.sendChatMsg(var12);
        this.playerLoggedIn(player);
        var11.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        this.updateTimeAndWeatherForPlayer(player, var9);

        if (this.mcServer.getTexturePack().length() > 0)
        {
            player.requestTexturePackLoad(this.mcServer.getTexturePack());
        }

        Iterator var13 = player.getActivePotionEffects().iterator();

        while (var13.hasNext())
        {
            PotionEffect var14 = (PotionEffect)var13.next();
            var11.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), var14));
        }

        player.addSelfToInternalCraftingInventory();

        if (var7 != null && var7.hasKey("Riding", 10))
        {
            Entity var15 = EntityList.createEntityFromNBT(var7.getCompoundTag("Riding"), var9);

            if (var15 != null)
            {
                var15.forceSpawn = true;
                var9.spawnEntityInWorld(var15);
                player.mountEntity(var15);
                var15.forceSpawn = false;
            }
        }
    }

    protected void func_96456_a(ServerScoreboard scoreboardIn, EntityPlayerMP player)
    {
        HashSet var3 = new HashSet();
        Iterator var4 = scoreboardIn.getTeams().iterator();

        while (var4.hasNext())
        {
            ScorePlayerTeam var5 = (ScorePlayerTeam)var4.next();
            player.playerNetServerHandler.sendPacket(new S3EPacketTeams(var5, 0));
        }

        for (int var9 = 0; var9 < 3; ++var9)
        {
            ScoreObjective var10 = scoreboardIn.getObjectiveInDisplaySlot(var9);

            if (var10 != null && !var3.contains(var10))
            {
                List var6 = scoreboardIn.func_96550_d(var10);
                Iterator var7 = var6.iterator();

                while (var7.hasNext())
                {
                    Packet var8 = (Packet)var7.next();
                    player.playerNetServerHandler.sendPacket(var8);
                }

                var3.add(var10);
            }
        }
    }

    public void setPlayerManager(WorldServer[] p_72364_1_)
    {
        this.playerNBTManagerObj = p_72364_1_[0].getSaveHandler().getPlayerNBTManager();
    }

    public void func_72375_a(EntityPlayerMP player, WorldServer worldIn)
    {
        WorldServer var3 = player.getServerForPlayer();

        if (worldIn != null)
        {
            worldIn.getPlayerManager().removePlayer(player);
        }

        var3.getPlayerManager().addPlayer(player);
        var3.theChunkProviderServer.loadChunk((int)player.posX >> 4, (int)player.posZ >> 4);
    }

    public int getEntityViewDistance()
    {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }

    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP player)
    {
        NBTTagCompound var2 = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound var3;

        if (player.getCommandSenderName().equals(this.mcServer.getServerOwner()) && var2 != null)
        {
            player.readFromNBT(var2);
            var3 = var2;
            logger.debug("loading single player");
        }
        else
        {
            var3 = this.playerNBTManagerObj.readPlayerData(player);
        }

        return var3;
    }

    protected void writePlayerData(EntityPlayerMP player)
    {
        this.playerNBTManagerObj.writePlayerData(player);
        StatisticsFile var2 = (StatisticsFile)this.playerStatFiles.get(player.getUniqueID());

        if (var2 != null)
        {
            var2.func_150883_b();
        }
    }

    public void playerLoggedIn(EntityPlayerMP player)
    {
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(player.getCommandSenderName(), true, 1000));
        this.playerEntityList.add(player);
        WorldServer var2 = this.mcServer.worldServerForDimension(player.dimension);
        var2.spawnEntityInWorld(player);
        this.func_72375_a(player, (WorldServer)null);

        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3)
        {
            EntityPlayerMP var4 = (EntityPlayerMP)this.playerEntityList.get(var3);
            player.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(var4.getCommandSenderName(), true, var4.ping));
        }
    }

    public void serverUpdateMountedMovingPlayer(EntityPlayerMP player)
    {
        player.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(player);
    }

    public void playerLoggedOut(EntityPlayerMP player)
    {
        player.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(player);
        WorldServer var2 = player.getServerForPlayer();

        if (player.ridingEntity != null)
        {
            var2.removePlayerEntityDangerously(player.ridingEntity);
            logger.debug("removing player mount");
        }

        var2.removeEntity(player);
        var2.getPlayerManager().removePlayer(player);
        this.playerEntityList.remove(player);
        this.playerStatFiles.remove(player.getUniqueID());
        this.sendPacketToAllPlayers(new S38PacketPlayerListItem(player.getCommandSenderName(), false, 9999));
    }

    public String allowUserToConnect(SocketAddress address, GameProfile profile)
    {
        String var4;

        if (this.bannedPlayers.isBanned(profile))
        {
            UserListBansEntry var5 = (UserListBansEntry)this.bannedPlayers.getEntry(profile);
            var4 = "You are banned from this server!\nReason: " + var5.getBanReason();

            if (var5.getBanEndDate() != null)
            {
                var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var5.getBanEndDate());
            }

            return var4;
        }
        else if (!this.canJoin(profile))
        {
            return "You are not white-listed on this server!";
        }
        else if (this.bannedIPs.isBanned(address))
        {
            IPBanEntry var3 = this.bannedIPs.getBanEntry(address);
            var4 = "Your IP address is banned from this server!\nReason: " + var3.getBanReason();

            if (var3.getBanEndDate() != null)
            {
                var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var3.getBanEndDate());
            }

            return var4;
        }
        else
        {
            return this.playerEntityList.size() >= this.maxPlayers ? "The server is full!" : null;
        }
    }

    public EntityPlayerMP createPlayerForUser(GameProfile profile)
    {
        UUID var2 = EntityPlayer.getUUID(profile);
        ArrayList var3 = Lists.newArrayList();
        EntityPlayerMP var5;

        for (int var4 = 0; var4 < this.playerEntityList.size(); ++var4)
        {
            var5 = (EntityPlayerMP)this.playerEntityList.get(var4);

            if (var5.getUniqueID().equals(var2))
            {
                var3.add(var5);
            }
        }

        Iterator var6 = var3.iterator();

        while (var6.hasNext())
        {
            var5 = (EntityPlayerMP)var6.next();
            var5.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }

        Object var7;

        if (this.mcServer.isDemo())
        {
            var7 = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
        }
        else
        {
            var7 = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        }

        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), profile, (ItemInWorldManager)var7);
    }

    public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP player, int dimension, boolean conqueredEnd)
    {
        player.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(player);
        player.getServerForPlayer().getEntityTracker().untrackEntity(player);
        player.getServerForPlayer().getPlayerManager().removePlayer(player);
        this.playerEntityList.remove(player);
        this.mcServer.worldServerForDimension(player.dimension).removePlayerEntityDangerously(player);
        ChunkCoordinates var4 = player.getBedLocation();
        boolean var5 = player.isSpawnForced();
        player.dimension = dimension;
        Object var6;

        if (this.mcServer.isDemo())
        {
            var6 = new DemoWorldManager(this.mcServer.worldServerForDimension(player.dimension));
        }
        else
        {
            var6 = new ItemInWorldManager(this.mcServer.worldServerForDimension(player.dimension));
        }

        EntityPlayerMP var7 = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(player.dimension), player.getGameProfile(), (ItemInWorldManager)var6);
        var7.playerNetServerHandler = player.playerNetServerHandler;
        var7.clonePlayer(player, conqueredEnd);
        var7.setEntityId(player.getEntityId());
        WorldServer var8 = this.mcServer.worldServerForDimension(player.dimension);
        this.func_72381_a(var7, player, var8);
        ChunkCoordinates var9;

        if (var4 != null)
        {
            var9 = EntityPlayer.verifyRespawnCoordinates(this.mcServer.worldServerForDimension(player.dimension), var4, var5);

            if (var9 != null)
            {
                var7.setLocationAndAngles((double)((float)var9.posX + 0.5F), (double)((float)var9.posY + 0.1F), (double)((float)var9.posZ + 0.5F), 0.0F, 0.0F);
                var7.setSpawnChunk(var4, var5);
            }
            else
            {
                var7.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
            }
        }

        var8.theChunkProviderServer.loadChunk((int)var7.posX >> 4, (int)var7.posZ >> 4);

        while (!var8.getCollidingBoundingBoxes(var7, var7.boundingBox).isEmpty())
        {
            var7.setPosition(var7.posX, var7.posY + 1.0D, var7.posZ);
        }

        var7.playerNetServerHandler.sendPacket(new S07PacketRespawn(var7.dimension, var7.worldObj.difficultySetting, var7.worldObj.getWorldInfo().getTerrainType(), var7.theItemInWorldManager.getGameType()));
        var9 = var8.getSpawnPoint();
        var7.playerNetServerHandler.setPlayerLocation(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
        var7.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(var9.posX, var9.posY, var9.posZ));
        var7.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(var7.experience, var7.experienceTotal, var7.experienceLevel));
        this.updateTimeAndWeatherForPlayer(var7, var8);
        var8.getPlayerManager().addPlayer(var7);
        var8.spawnEntityInWorld(var7);
        this.playerEntityList.add(var7);
        var7.addSelfToInternalCraftingInventory();
        var7.setHealth(var7.getHealth());
        return var7;
    }

    public void transferPlayerToDimension(EntityPlayerMP player, int dimension)
    {
        int var3 = player.dimension;
        WorldServer var4 = this.mcServer.worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer var5 = this.mcServer.worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        var4.removePlayerEntityDangerously(player);
        player.isDead = false;
        this.transferEntityToWorld(player, var3, var4, var5);
        this.func_72375_a(player, var4);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(player, var5);
        this.syncPlayerInventory(player);
        Iterator var6 = player.getActivePotionEffects().iterator();

        while (var6.hasNext())
        {
            PotionEffect var7 = (PotionEffect)var6.next();
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), var7));
        }
    }

    public void transferEntityToWorld(Entity entityIn, int p_82448_2_, WorldServer p_82448_3_, WorldServer p_82448_4_)
    {
        double var5 = entityIn.posX;
        double var7 = entityIn.posZ;
        double var9 = 8.0D;
        double var11 = entityIn.posX;
        double var13 = entityIn.posY;
        double var15 = entityIn.posZ;
        float var17 = entityIn.rotationYaw;
        p_82448_3_.theProfiler.startSection("moving");

        if (entityIn.dimension == -1)
        {
            var5 /= var9;
            var7 /= var9;
            entityIn.setLocationAndAngles(var5, entityIn.posY, var7, entityIn.rotationYaw, entityIn.rotationPitch);

            if (entityIn.isEntityAlive())
            {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else if (entityIn.dimension == 0)
        {
            var5 *= var9;
            var7 *= var9;
            entityIn.setLocationAndAngles(var5, entityIn.posY, var7, entityIn.rotationYaw, entityIn.rotationPitch);

            if (entityIn.isEntityAlive())
            {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }
        else
        {
            ChunkCoordinates var18;

            if (p_82448_2_ == 1)
            {
                var18 = p_82448_4_.getSpawnPoint();
            }
            else
            {
                var18 = p_82448_4_.getEntrancePortalLocation();
            }

            var5 = (double)var18.posX;
            entityIn.posY = (double)var18.posY;
            var7 = (double)var18.posZ;
            entityIn.setLocationAndAngles(var5, entityIn.posY, var7, 90.0F, 0.0F);

            if (entityIn.isEntityAlive())
            {
                p_82448_3_.updateEntityWithOptionalForce(entityIn, false);
            }
        }

        p_82448_3_.theProfiler.endSection();

        if (p_82448_2_ != 1)
        {
            p_82448_3_.theProfiler.startSection("placing");
            var5 = (double)MathHelper.clamp_int((int)var5, -29999872, 29999872);
            var7 = (double)MathHelper.clamp_int((int)var7, -29999872, 29999872);

            if (entityIn.isEntityAlive())
            {
                entityIn.setLocationAndAngles(var5, entityIn.posY, var7, entityIn.rotationYaw, entityIn.rotationPitch);
                p_82448_4_.getDefaultTeleporter().placeInPortal(entityIn, var11, var13, var15, var17);
                p_82448_4_.spawnEntityInWorld(entityIn);
                p_82448_4_.updateEntityWithOptionalForce(entityIn, false);
            }

            p_82448_3_.theProfiler.endSection();
        }

        entityIn.setWorld(p_82448_4_);
    }

    public void onTick()
    {
        if (++this.playerPingIndex > 600)
        {
            this.playerPingIndex = 0;
        }

        if (this.playerPingIndex < this.playerEntityList.size())
        {
            EntityPlayerMP var1 = (EntityPlayerMP)this.playerEntityList.get(this.playerPingIndex);
            this.sendPacketToAllPlayers(new S38PacketPlayerListItem(var1.getCommandSenderName(), true, var1.ping));
        }
    }

    public void sendPacketToAllPlayers(Packet packetIn)
    {
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            ((EntityPlayerMP)this.playerEntityList.get(var2)).playerNetServerHandler.sendPacket(packetIn);
        }
    }

    public void sendPacketToAllPlayersInDimension(Packet packetIn, int dimension)
    {
        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3)
        {
            EntityPlayerMP var4 = (EntityPlayerMP)this.playerEntityList.get(var3);

            if (var4.dimension == dimension)
            {
                var4.playerNetServerHandler.sendPacket(packetIn);
            }
        }
    }

    public String getPlayerNamesString(boolean includeUuid)
    {
        String var2 = "";
        ArrayList var3 = Lists.newArrayList(this.playerEntityList);

        for (int var4 = 0; var4 < var3.size(); ++var4)
        {
            if (var4 > 0)
            {
                var2 = var2 + ", ";
            }

            var2 = var2 + ((EntityPlayerMP)var3.get(var4)).getCommandSenderName();

            if (includeUuid)
            {
                var2 = var2 + " (" + ((EntityPlayerMP)var3.get(var4)).getUniqueID().toString() + ")";
            }
        }

        return var2;
    }

    public String[] getAllUsernames()
    {
        String[] var1 = new String[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).getCommandSenderName();
        }

        return var1;
    }

    public GameProfile[] getAllProfiles()
    {
        GameProfile[] var1 = new GameProfile[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).getGameProfile();
        }

        return var1;
    }

    public UserListBans getBannedPlayers()
    {
        return this.bannedPlayers;
    }

    public BanList getBannedIPs()
    {
        return this.bannedIPs;
    }

    public void addOp(GameProfile profile)
    {
        this.ops.addEntry(new UserListOpsEntry(profile, this.mcServer.getOpPermissionLevel()));
    }

    public void removeOp(GameProfile profile)
    {
        this.ops.removeEntry(profile);
    }

    public boolean canJoin(GameProfile profile)
    {
        return !this.whiteListEnforced || this.ops.hasEntry(profile) || this.whiteListedPlayers.hasEntry(profile);
    }

    public boolean canSendCommands(GameProfile profile)
    {
        return this.ops.hasEntry(profile) || this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(profile.getName()) || this.commandsAllowedForAll;
    }

    public EntityPlayerMP getPlayerByUsername(String username)
    {
        Iterator var2 = this.playerEntityList.iterator();
        EntityPlayerMP var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (EntityPlayerMP)var2.next();
        }
        while (!var3.getCommandSenderName().equalsIgnoreCase(username));

        return var3;
    }

    public List findPlayers(ChunkCoordinates coordinates, int minRadius, int maxRadius, int maxAmount, int gameMode, int minXp, int maxXp, Map scoreboardData, String username, String teamName, World worldIn)
    {
        if (this.playerEntityList.isEmpty())
        {
            return Collections.emptyList();
        }
        else
        {
            Object var12 = new ArrayList();
            boolean var13 = maxAmount < 0;
            boolean var14 = username != null && username.startsWith("!");
            boolean var15 = teamName != null && teamName.startsWith("!");
            int var16 = minRadius * minRadius;
            int var17 = maxRadius * maxRadius;
            maxAmount = MathHelper.abs_int(maxAmount);

            if (var14)
            {
                username = username.substring(1);
            }

            if (var15)
            {
                teamName = teamName.substring(1);
            }

            for (int var18 = 0; var18 < this.playerEntityList.size(); ++var18)
            {
                EntityPlayerMP var19 = (EntityPlayerMP)this.playerEntityList.get(var18);

                if ((worldIn == null || var19.worldObj == worldIn) && (username == null || var14 != username.equalsIgnoreCase(var19.getCommandSenderName())))
                {
                    if (teamName != null)
                    {
                        Team var20 = var19.getTeam();
                        String var21 = var20 == null ? "" : var20.getRegisteredName();

                        if (var15 == teamName.equalsIgnoreCase(var21))
                        {
                            continue;
                        }
                    }

                    if (coordinates != null && (minRadius > 0 || maxRadius > 0))
                    {
                        float var22 = coordinates.getDistanceSquaredToChunkCoordinates(var19.getCommandSenderPosition());

                        if (minRadius > 0 && var22 < (float)var16 || maxRadius > 0 && var22 > (float)var17)
                        {
                            continue;
                        }
                    }

                    if (this.matchesScoreboardCriteria(var19, scoreboardData) && (gameMode == WorldSettings.GameType.NOT_SET.getID() || gameMode == var19.theItemInWorldManager.getGameType().getID()) && (minXp <= 0 || var19.experienceLevel >= minXp) && var19.experienceLevel <= maxXp)
                    {
                        ((List)var12).add(var19);
                    }
                }
            }

            if (coordinates != null)
            {
                Collections.sort((List)var12, new PlayerPositionComparator(coordinates));
            }

            if (var13)
            {
                Collections.reverse((List)var12);
            }

            if (maxAmount > 0)
            {
                var12 = ((List)var12).subList(0, Math.min(maxAmount, ((List)var12).size()));
            }

            return (List)var12;
        }
    }

    private boolean matchesScoreboardCriteria(EntityPlayer player, Map scoreboardCriteria)
    {
        if (scoreboardCriteria != null && scoreboardCriteria.size() != 0)
        {
            Iterator var3 = scoreboardCriteria.entrySet().iterator();
            Entry var4;
            boolean var6;
            int var10;

            do
            {
                if (!var3.hasNext())
                {
                    return true;
                }

                var4 = (Entry)var3.next();
                String var5 = (String)var4.getKey();
                var6 = false;

                if (var5.endsWith("_min") && var5.length() > 4)
                {
                    var6 = true;
                    var5 = var5.substring(0, var5.length() - 4);
                }

                Scoreboard var7 = player.getWorldScoreboard();
                ScoreObjective var8 = var7.getObjective(var5);

                if (var8 == null)
                {
                    return false;
                }

                Score var9 = player.getWorldScoreboard().getValueFromObjective(player.getCommandSenderName(), var8);
                var10 = var9.getScorePoints();

                if (var10 < ((Integer)var4.getValue()).intValue() && var6)
                {
                    return false;
                }
            }
            while (var10 <= ((Integer)var4.getValue()).intValue() || var6);

            return false;
        }
        else
        {
            return true;
        }
    }

    public void sendToAllNear(double x, double y, double z, double radius, int dimension, Packet packetIn)
    {
        this.sendToAllNearExcept((EntityPlayer)null, x, y, z, radius, dimension, packetIn);
    }

    public void sendToAllNearExcept(EntityPlayer p_148543_1_, double x, double y, double z, double radius, int dimension, Packet p_148543_11_)
    {
        for (int var12 = 0; var12 < this.playerEntityList.size(); ++var12)
        {
            EntityPlayerMP var13 = (EntityPlayerMP)this.playerEntityList.get(var12);

            if (var13 != p_148543_1_ && var13.dimension == dimension)
            {
                double var14 = x - var13.posX;
                double var16 = y - var13.posY;
                double var18 = z - var13.posZ;

                if (var14 * var14 + var16 * var16 + var18 * var18 < radius * radius)
                {
                    var13.playerNetServerHandler.sendPacket(p_148543_11_);
                }
            }
        }
    }

    public void saveAllPlayerData()
    {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1)
        {
            this.writePlayerData((EntityPlayerMP)this.playerEntityList.get(var1));
        }
    }

    public void addWhitelistedPlayer(GameProfile profile)
    {
        this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(profile));
    }

    public void removePlayerFromWhitelist(GameProfile profile)
    {
        this.whiteListedPlayers.removeEntry(profile);
    }

    public UserListWhitelist getWhitelistedPlayers()
    {
        return this.whiteListedPlayers;
    }

    public String[] getWhitelistedPlayerNames()
    {
        return this.whiteListedPlayers.getKeys();
    }

    public UserListOps getOppedPlayers()
    {
        return this.ops;
    }

    public String[] getOppedPlayerNames()
    {
        return this.ops.getKeys();
    }

    public void loadWhiteList() {}

    public void updateTimeAndWeatherForPlayer(EntityPlayerMP player, WorldServer worldIn)
    {
        player.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getGameRuleBooleanValue("doDaylightCycle")));

        if (worldIn.isRaining())
        {
            player.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
            player.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
            player.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, worldIn.getWeightedThunderStrength(1.0F)));
        }
    }

    public void syncPlayerInventory(EntityPlayerMP player)
    {
        player.sendContainerToPlayer(player.inventoryContainer);
        player.setPlayerHealthUpdated();
        player.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(player.inventory.currentItem));
    }

    public int getCurrentPlayerCount()
    {
        return this.playerEntityList.size();
    }

    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    public String[] getAvailablePlayerDat()
    {
        return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
    }

    public void setWhiteListEnabled(boolean whitelistEnabled)
    {
        this.whiteListEnforced = whitelistEnabled;
    }

    public List getPlayersMatchingAddress(String address)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.playerEntityList.iterator();

        while (var3.hasNext())
        {
            EntityPlayerMP var4 = (EntityPlayerMP)var3.next();

            if (var4.getPlayerIP().equals(address))
            {
                var2.add(var4);
            }
        }

        return var2;
    }

    public int getViewDistance()
    {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance()
    {
        return this.mcServer;
    }

    public NBTTagCompound getHostPlayerData()
    {
        return null;
    }

    public void func_152604_a(WorldSettings.GameType p_152604_1_)
    {
        this.gameType = p_152604_1_;
    }

    private void func_72381_a(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_, World p_72381_3_)
    {
        if (p_72381_2_ != null)
        {
            p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
        }
        else if (this.gameType != null)
        {
            p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
        }

        p_72381_1_.theItemInWorldManager.initializeGameType(p_72381_3_.getWorldInfo().getGameType());
    }

    public void setCommandsAllowedForAll(boolean p_72387_1_)
    {
        this.commandsAllowedForAll = p_72387_1_;
    }

    public void removeAllPlayers()
    {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1)
        {
            ((EntityPlayerMP)this.playerEntityList.get(var1)).playerNetServerHandler.kickPlayerFromServer("Server closed");
        }
    }

    public void sendChatMsgImpl(IChatComponent component, boolean isChat)
    {
        this.mcServer.addChatMessage(component);
        this.sendPacketToAllPlayers(new S02PacketChat(component, isChat));
    }

    public void sendChatMsg(IChatComponent component)
    {
        this.sendChatMsgImpl(component, true);
    }

    public StatisticsFile getPlayerStatsFile(EntityPlayer player)
    {
        UUID var2 = player.getUniqueID();
        StatisticsFile var3 = var2 == null ? null : (StatisticsFile)this.playerStatFiles.get(var2);

        if (var3 == null)
        {
            File var4 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
            File var5 = new File(var4, var2.toString() + ".json");

            if (!var5.exists())
            {
                File var6 = new File(var4, player.getCommandSenderName() + ".json");

                if (var6.exists() && var6.isFile())
                {
                    var6.renameTo(var5);
                }
            }

            var3 = new StatisticsFile(this.mcServer, var5);
            var3.func_150882_a();
            this.playerStatFiles.put(var2, var3);
        }

        return var3;
    }

    public void setViewDistance(int distance)
    {
        this.viewDistance = distance;

        if (this.mcServer.worldServers != null)
        {
            WorldServer[] var2 = this.mcServer.worldServers;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                WorldServer var5 = var2[var4];

                if (var5 != null)
                {
                    var5.getPlayerManager().func_152622_a(distance);
                }
            }
        }
    }
}
