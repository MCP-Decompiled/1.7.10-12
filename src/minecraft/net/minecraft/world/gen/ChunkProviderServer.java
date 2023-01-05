package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer implements IChunkProvider
{
    private static final Logger logger = LogManager.getLogger();
    private Set droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap());
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    public boolean chunkLoadOverride = true;
    private LongHashMap id2ChunkMap = new LongHashMap();
    private List loadedChunks = new ArrayList();
    private WorldServer worldObj;
    private static final String __OBFID = "CL_00001436";

    public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_, IChunkProvider p_i1520_3_)
    {
        this.dummyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.chunkLoader = p_i1520_2_;
        this.serverChunkGenerator = p_i1520_3_;
    }

    public boolean chunkExists(int p_73149_1_, int p_73149_2_)
    {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_73149_1_, p_73149_2_));
    }

    public List func_152380_a()
    {
        return this.loadedChunks;
    }

    public void dropChunk(int p_73241_1_, int p_73241_2_)
    {
        if (this.worldObj.provider.canRespawnHere())
        {
            ChunkCoordinates var3 = this.worldObj.getSpawnPoint();
            int var4 = p_73241_1_ * 16 + 8 - var3.posX;
            int var5 = p_73241_2_ * 16 + 8 - var3.posZ;
            short var6 = 128;

            if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6)
            {
                this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
            }
        }
        else
        {
            this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
        }
    }

    public void unloadAllChunks()
    {
        Iterator var1 = this.loadedChunks.iterator();

        while (var1.hasNext())
        {
            Chunk var2 = (Chunk)var1.next();
            this.dropChunk(var2.xPosition, var2.zPosition);
        }
    }

    public Chunk loadChunk(int p_73158_1_, int p_73158_2_)
    {
        long var3 = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.droppedChunksSet.remove(Long.valueOf(var3));
        Chunk var5 = (Chunk)this.id2ChunkMap.getValueByKey(var3);

        if (var5 == null)
        {
            var5 = this.loadChunkFromFile(p_73158_1_, p_73158_2_);

            if (var5 == null)
            {
                if (this.serverChunkGenerator == null)
                {
                    var5 = this.dummyChunk;
                }
                else
                {
                    try
                    {
                        var5 = this.serverChunkGenerator.provideChunk(p_73158_1_, p_73158_2_);
                    }
                    catch (Throwable var9)
                    {
                        CrashReport var7 = CrashReport.makeCrashReport(var9, "Exception generating new chunk");
                        CrashReportCategory var8 = var7.makeCategory("Chunk to be generated");
                        var8.addCrashSection("Location", String.format("%d,%d", new Object[] {Integer.valueOf(p_73158_1_), Integer.valueOf(p_73158_2_)}));
                        var8.addCrashSection("Position hash", Long.valueOf(var3));
                        var8.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                        throw new ReportedException(var7);
                    }
                }
            }

            this.id2ChunkMap.add(var3, var5);
            this.loadedChunks.add(var5);
            var5.onChunkLoad();
            var5.populateChunk(this, this, p_73158_1_, p_73158_2_);
        }

        return var5;
    }

    public Chunk provideChunk(int p_73154_1_, int p_73154_2_)
    {
        Chunk var3 = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return var3 == null ? (!this.worldObj.findingSpawnPoint && !this.chunkLoadOverride ? this.dummyChunk : this.loadChunk(p_73154_1_, p_73154_2_)) : var3;
    }

    private Chunk loadChunkFromFile(int p_73239_1_, int p_73239_2_)
    {
        if (this.chunkLoader == null)
        {
            return null;
        }
        else
        {
            try
            {
                Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, p_73239_1_, p_73239_2_);

                if (var3 != null)
                {
                    var3.lastSaveTime = this.worldObj.getTotalWorldTime();

                    if (this.serverChunkGenerator != null)
                    {
                        this.serverChunkGenerator.recreateStructures(p_73239_1_, p_73239_2_);
                    }
                }

                return var3;
            }
            catch (Exception var4)
            {
                logger.error("Couldn\'t load chunk", var4);
                return null;
            }
        }
    }

    private void saveChunkExtraData(Chunk p_73243_1_)
    {
        if (this.chunkLoader != null)
        {
            try
            {
                this.chunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
            }
            catch (Exception var3)
            {
                logger.error("Couldn\'t save entities", var3);
            }
        }
    }

    private void saveChunkData(Chunk p_73242_1_)
    {
        if (this.chunkLoader != null)
        {
            try
            {
                p_73242_1_.lastSaveTime = this.worldObj.getTotalWorldTime();
                this.chunkLoader.saveChunk(this.worldObj, p_73242_1_);
            }
            catch (IOException var3)
            {
                logger.error("Couldn\'t save chunk", var3);
            }
            catch (MinecraftException var4)
            {
                logger.error("Couldn\'t save chunk; already in use by another instance of Minecraft?", var4);
            }
        }
    }

    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {
        Chunk var4 = this.provideChunk(p_73153_2_, p_73153_3_);

        if (!var4.isTerrainPopulated)
        {
            var4.func_150809_p();

            if (this.serverChunkGenerator != null)
            {
                this.serverChunkGenerator.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                var4.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        int var3 = 0;
        ArrayList var4 = Lists.newArrayList(this.loadedChunks);

        for (int var5 = 0; var5 < var4.size(); ++var5)
        {
            Chunk var6 = (Chunk)var4.get(var5);

            if (p_73151_1_)
            {
                this.saveChunkExtraData(var6);
            }

            if (var6.needsSaving(p_73151_1_))
            {
                this.saveChunkData(var6);
                var6.isModified = false;
                ++var3;

                if (var3 == 24 && !p_73151_1_)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void saveExtraData()
    {
        if (this.chunkLoader != null)
        {
            this.chunkLoader.saveExtraData();
        }
    }

    public boolean unloadQueuedChunks()
    {
        if (!this.worldObj.disableLevelSaving)
        {
            for (int var1 = 0; var1 < 100; ++var1)
            {
                if (!this.droppedChunksSet.isEmpty())
                {
                    Long var2 = (Long)this.droppedChunksSet.iterator().next();
                    Chunk var3 = (Chunk)this.id2ChunkMap.getValueByKey(var2.longValue());

                    if (var3 != null)
                    {
                        var3.onChunkUnload();
                        this.saveChunkData(var3);
                        this.saveChunkExtraData(var3);
                        this.loadedChunks.remove(var3);
                    }

                    this.droppedChunksSet.remove(var2);
                    this.id2ChunkMap.remove(var2.longValue());
                }
            }

            if (this.chunkLoader != null)
            {
                this.chunkLoader.chunkTick();
            }
        }

        return this.serverChunkGenerator.unloadQueuedChunks();
    }

    public boolean canSave()
    {
        return !this.worldObj.disableLevelSaving;
    }

    public String makeString()
    {
        return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
    }

    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_)
    {
        return this.serverChunkGenerator.getPossibleCreatures(p_73155_1_, p_73155_2_, p_73155_3_, p_73155_4_);
    }

    public ChunkPosition findClosestStructure(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_)
    {
        return this.serverChunkGenerator.findClosestStructure(p_147416_1_, p_147416_2_, p_147416_3_, p_147416_4_, p_147416_5_);
    }

    public int getLoadedChunkCount()
    {
        return this.id2ChunkMap.getNumHashElements();
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_) {}
}
