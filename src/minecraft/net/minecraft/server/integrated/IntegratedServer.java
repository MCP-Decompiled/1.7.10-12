package net.minecraft.server.integrated;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final WorldSettings theWorldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;
    private static final String __OBFID = "CL_00001129";

    public IntegratedServer(Minecraft p_i1317_1_, String p_i1317_2_, String p_i1317_3_, WorldSettings p_i1317_4_)
    {
        super(new File(p_i1317_1_.mcDataDir, "saves"), p_i1317_1_.getProxy());
        this.setServerOwner(p_i1317_1_.getSession().getUsername());
        this.setFolderName(p_i1317_2_);
        this.setWorldName(p_i1317_3_);
        this.setDemo(p_i1317_1_.isDemo());
        this.canCreateBonusChest(p_i1317_4_.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager(new IntegratedPlayerList(this));
        this.mc = p_i1317_1_;
        this.theWorldSettings = p_i1317_4_;
    }

    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long p_71247_3_, WorldType p_71247_5_, String p_71247_6_)
    {
        this.convertMapIfNeeded(p_71247_1_);
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler var7 = this.getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);

        for (int var8 = 0; var8 < this.worldServers.length; ++var8)
        {
            byte var9 = 0;

            if (var8 == 1)
            {
                var9 = -1;
            }

            if (var8 == 2)
            {
                var9 = 1;
            }

            if (var8 == 0)
            {
                if (this.isDemo())
                {
                    this.worldServers[var8] = new DemoWorldServer(this, var7, p_71247_2_, var9, this.theProfiler);
                }
                else
                {
                    this.worldServers[var8] = new WorldServer(this, var7, p_71247_2_, var9, this.theWorldSettings, this.theProfiler);
                }
            }
            else
            {
                this.worldServers[var8] = new WorldServerMulti(this, var7, p_71247_2_, var9, this.theWorldSettings, this.worldServers[0], this.theProfiler);
            }

            this.worldServers[var8].addWorldAccess(new WorldManager(this, this.worldServers[var8]));
            this.getConfigurationManager().setPlayerManager(this.worldServers);
        }

        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    protected boolean startServer() throws IOException
    {
        logger.info("Starting integrated minecraft server version 1.7.10");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.func_82749_j());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        return true;
    }

    public void tick()
    {
        boolean var1 = this.isGamePaused;
        this.isGamePaused = Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused();

        if (!var1 && this.isGamePaused)
        {
            logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllWorlds(false);
        }

        if (!this.isGamePaused)
        {
            super.tick();

            if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance())
            {
                logger.info("Changing view distance to {}, from {}", new Object[] {Integer.valueOf(this.mc.gameSettings.renderDistanceChunks), Integer.valueOf(this.getConfigurationManager().getViewDistance())});
                this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }
        }
    }

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public WorldSettings.GameType getGameType()
    {
        return this.theWorldSettings.getGameType();
    }

    public EnumDifficulty getDifficulty()
    {
        return this.mc.gameSettings.difficulty;
    }

    public boolean isHardcore()
    {
        return this.theWorldSettings.getHardcoreEnabled();
    }

    public boolean func_152363_m()
    {
        return false;
    }

    protected File getDataDirectory()
    {
        return this.mc.mcDataDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    protected void finalTick(CrashReport report)
    {
        this.mc.crashed(report);
    }

    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", new Callable()
        {
            private static final String __OBFID = "CL_00001130";
            public String call()
            {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addCrashSectionCallable("Is Modded", new Callable()
        {
            private static final String __OBFID = "CL_00001131";
            public String call()
            {
                String var1 = ClientBrandRetriever.getClientModName();

                if (!var1.equals("vanilla"))
                {
                    return "Definitely; Client brand changed to \'" + var1 + "\'";
                }
                else
                {
                    var1 = IntegratedServer.this.getServerModName();
                    return !var1.equals("vanilla") ? "Definitely; Server brand changed to \'" + var1 + "\'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
                }
            }
        });
        return report;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
    {
        super.addServerStatsToSnooper(playerSnooper);
        playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    public boolean isSnooperEnabled()
    {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    public String shareToLAN(WorldSettings.GameType type, boolean allowCheats)
    {
        try
        {
            int var3 = -1;

            try
            {
                var3 = HttpUtil.getSuitableLanPort();
            }
            catch (IOException var5)
            {
                ;
            }

            if (var3 <= 0)
            {
                var3 = 25564;
            }

            this.getNetworkSystem().addLanEndpoint((InetAddress)null, var3);
            logger.info("Started on " + var3);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), var3 + "");
            this.lanServerPing.start();
            this.getConfigurationManager().func_152604_a(type);
            this.getConfigurationManager().setCommandsAllowedForAll(allowCheats);
            return var3 + "";
        }
        catch (IOException var6)
        {
            return null;
        }
    }

    public void stopServer()
    {
        super.stopServer();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    public void initiateShutdown()
    {
        super.initiateShutdown();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    public boolean getPublic()
    {
        return this.isPublic;
    }

    public void setGameType(WorldSettings.GameType gameMode)
    {
        this.getConfigurationManager().func_152604_a(gameMode);
    }

    public boolean isCommandBlockEnabled()
    {
        return true;
    }

    public int getOpPermissionLevel()
    {
        return 4;
    }
}
