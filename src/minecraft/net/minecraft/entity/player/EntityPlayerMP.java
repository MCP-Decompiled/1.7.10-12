package net.minecraft.entity.player;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayerMP extends EntityPlayer implements ICrafting
{
    private static final Logger logger = LogManager.getLogger();
    private String translator = "en_US";
    public NetHandlerPlayServer playerNetServerHandler;
    public final MinecraftServer mcServer;
    public final ItemInWorldManager theItemInWorldManager;
    public double managedPosX;
    public double managedPosZ;
    public final List loadedChunks = new LinkedList();
    private final List destroyedItemsNetCache = new LinkedList();
    private final StatisticsFile field_147103_bO;
    private float field_130068_bO = Float.MIN_VALUE;
    private float lastHealth = -1.0E8F;
    private int lastFoodLevel = -99999999;
    private boolean wasHungry = true;
    private int lastExperience = -99999999;
    private int field_147101_bU = 60;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean chatColours = true;
    private long playerLastActiveTime = System.currentTimeMillis();
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean playerConqueredTheEnd;
    private static final String __OBFID = "CL_00001440";

    public EntityPlayerMP(MinecraftServer p_i45285_1_, WorldServer p_i45285_2_, GameProfile p_i45285_3_, ItemInWorldManager p_i45285_4_)
    {
        super(p_i45285_2_, p_i45285_3_);
        p_i45285_4_.thisPlayerMP = this;
        this.theItemInWorldManager = p_i45285_4_;
        ChunkCoordinates var5 = p_i45285_2_.getSpawnPoint();
        int var6 = var5.posX;
        int var7 = var5.posZ;
        int var8 = var5.posY;

        if (!p_i45285_2_.provider.hasNoSky && p_i45285_2_.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE)
        {
            int var9 = Math.max(5, p_i45285_1_.getSpawnProtectionSize() - 6);
            var6 += this.rand.nextInt(var9 * 2) - var9;
            var7 += this.rand.nextInt(var9 * 2) - var9;
            var8 = p_i45285_2_.getTopSolidOrLiquidBlock(var6, var7);
        }

        this.mcServer = p_i45285_1_;
        this.field_147103_bO = p_i45285_1_.getConfigurationManager().getPlayerStatsFile(this);
        this.stepHeight = 0.0F;
        this.yOffset = 0.0F;
        this.setLocationAndAngles((double)var6 + 0.5D, (double)var8, (double)var7 + 0.5D, 0.0F, 0.0F);

        while (!p_i45285_2_.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty())
        {
            this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("playerGameType", 99))
        {
            if (MinecraftServer.getServer().getForceGamemode())
            {
                this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
            }
            else
            {
                this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
    }

    public void addExperienceLevel(int p_82242_1_)
    {
        super.addExperienceLevel(p_82242_1_);
        this.lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory()
    {
        this.openContainer.onCraftGuiOpened(this);
    }

    protected void resetHeight()
    {
        this.yOffset = 0.0F;
    }

    public float getEyeHeight()
    {
        return 1.62F;
    }

    public void onUpdate()
    {
        this.theItemInWorldManager.updateBlockRemoving();
        --this.field_147101_bU;

        if (this.hurtResistantTime > 0)
        {
            --this.hurtResistantTime;
        }

        this.openContainer.detectAndSendChanges();

        if (!this.worldObj.isRemote && !this.openContainer.canInteractWith(this))
        {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }

        while (!this.destroyedItemsNetCache.isEmpty())
        {
            int var1 = Math.min(this.destroyedItemsNetCache.size(), 127);
            int[] var2 = new int[var1];
            Iterator var3 = this.destroyedItemsNetCache.iterator();
            int var4 = 0;

            while (var3.hasNext() && var4 < var1)
            {
                var2[var4++] = ((Integer)var3.next()).intValue();
                var3.remove();
            }

            this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(var2));
        }

        if (!this.loadedChunks.isEmpty())
        {
            ArrayList var6 = new ArrayList();
            Iterator var7 = this.loadedChunks.iterator();
            ArrayList var8 = new ArrayList();
            Chunk var5;

            while (var7.hasNext() && var6.size() < S26PacketMapChunkBulk.func_149258_c())
            {
                ChunkCoordIntPair var9 = (ChunkCoordIntPair)var7.next();

                if (var9 != null)
                {
                    if (this.worldObj.blockExists(var9.chunkXPos << 4, 0, var9.chunkZPos << 4))
                    {
                        var5 = this.worldObj.getChunkFromChunkCoords(var9.chunkXPos, var9.chunkZPos);

                        if (var5.func_150802_k())
                        {
                            var6.add(var5);
                            var8.addAll(((WorldServer)this.worldObj).func_147486_a(var9.chunkXPos * 16, 0, var9.chunkZPos * 16, var9.chunkXPos * 16 + 16, 256, var9.chunkZPos * 16 + 16));
                            var7.remove();
                        }
                    }
                }
                else
                {
                    var7.remove();
                }
            }

            if (!var6.isEmpty())
            {
                this.playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(var6));
                Iterator var10 = var8.iterator();

                while (var10.hasNext())
                {
                    TileEntity var11 = (TileEntity)var10.next();
                    this.func_147097_b(var11);
                }

                var10 = var6.iterator();

                while (var10.hasNext())
                {
                    var5 = (Chunk)var10.next();
                    this.getServerForPlayer().getEntityTracker().func_85172_a(this, var5);
                }
            }
        }
    }

    public void onUpdateEntity()
    {
        try
        {
            super.onUpdate();

            for (int var1 = 0; var1 < this.inventory.getSizeInventory(); ++var1)
            {
                ItemStack var6 = this.inventory.getStackInSlot(var1);

                if (var6 != null && var6.getItem().isMap())
                {
                    Packet var8 = ((ItemMapBase)var6.getItem()).createMapDataPacket(var6, this.worldObj, this);

                    if (var8 != null)
                    {
                        this.playerNetServerHandler.sendPacket(var8);
                    }
                }
            }

            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry)
            {
                this.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
            }

            if (this.getHealth() + this.getAbsorptionAmount() != this.field_130068_bO)
            {
                this.field_130068_bO = this.getHealth() + this.getAbsorptionAmount();
                Collection var5 = this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.health);
                Iterator var7 = var5.iterator();

                while (var7.hasNext())
                {
                    ScoreObjective var9 = (ScoreObjective)var7.next();
                    this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var9).func_96651_a(Arrays.asList(new EntityPlayer[] {this}));
                }
            }

            if (this.experienceTotal != this.lastExperience)
            {
                this.lastExperience = this.experienceTotal;
                this.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }

            if (this.ticksExisted % 20 * 5 == 0 && !this.getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes))
            {
                this.func_147098_j();
            }
        }
        catch (Throwable var4)
        {
            CrashReport var2 = CrashReport.makeCrashReport(var4, "Ticking player");
            CrashReportCategory var3 = var2.makeCategory("Player being ticked");
            this.addEntityCrashInfo(var3);
            throw new ReportedException(var2);
        }
    }

    protected void func_147098_j()
    {
        BiomeGenBase var1 = this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));

        if (var1 != null)
        {
            String var2 = var1.biomeName;
            JsonSerializableSet var3 = (JsonSerializableSet)this.getStatFile().func_150870_b(AchievementList.exploreAllBiomes);

            if (var3 == null)
            {
                var3 = (JsonSerializableSet)this.getStatFile().func_150872_a(AchievementList.exploreAllBiomes, new JsonSerializableSet());
            }

            var3.add(var2);

            if (this.getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && var3.size() == BiomeGenBase.explorationBiomesList.size())
            {
                HashSet var4 = Sets.newHashSet(BiomeGenBase.explorationBiomesList);
                Iterator var5 = var3.iterator();

                while (var5.hasNext())
                {
                    String var6 = (String)var5.next();
                    Iterator var7 = var4.iterator();

                    while (var7.hasNext())
                    {
                        BiomeGenBase var8 = (BiomeGenBase)var7.next();

                        if (var8.biomeName.equals(var6))
                        {
                            var7.remove();
                        }
                    }

                    if (var4.isEmpty())
                    {
                        break;
                    }
                }

                if (var4.isEmpty())
                {
                    this.triggerAchievement(AchievementList.exploreAllBiomes);
                }
            }
        }
    }

    public void onDeath(DamageSource p_70645_1_)
    {
        this.mcServer.getConfigurationManager().sendChatMsg(this.getCombatTracker().func_151521_b());

        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            this.inventory.dropAllItems();
        }

        Collection var2 = this.worldObj.getScoreboard().func_96520_a(IScoreObjectiveCriteria.deathCount);
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            ScoreObjective var4 = (ScoreObjective)var3.next();
            Score var5 = this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var4);
            var5.func_96648_a();
        }

        EntityLivingBase var6 = this.func_94060_bK();

        if (var6 != null)
        {
            int var7 = EntityList.getEntityID(var6);
            EntityList.EntityEggInfo var8 = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(var7));

            if (var8 != null)
            {
                this.addStat(var8.field_151513_e, 1);
            }

            var6.addToPlayerScore(this, this.scoreValue);
        }

        this.addStat(StatList.deathsStat, 1);
        this.getCombatTracker().func_94549_h();
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            boolean var3 = this.mcServer.isDedicatedServer() && this.mcServer.isPVPEnabled() && "fall".equals(source.damageType);

            if (!var3 && this.field_147101_bU > 0 && source != DamageSource.outOfWorld)
            {
                return false;
            }
            else
            {
                if (source instanceof EntityDamageSource)
                {
                    Entity var4 = source.getEntity();

                    if (var4 instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)var4))
                    {
                        return false;
                    }

                    if (var4 instanceof EntityArrow)
                    {
                        EntityArrow var5 = (EntityArrow)var4;

                        if (var5.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)var5.shootingEntity))
                        {
                            return false;
                        }
                    }
                }

                return super.attackEntityFrom(source, amount);
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer p_96122_1_)
    {
        return !this.mcServer.isPVPEnabled() ? false : super.canAttackPlayer(p_96122_1_);
    }

    public void travelToDimension(int dimensionId)
    {
        if (this.dimension == 1 && dimensionId == 1)
        {
            this.triggerAchievement(AchievementList.theEnd2);
            this.worldObj.removeEntity(this);
            this.playerConqueredTheEnd = true;
            this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
        }
        else
        {
            if (this.dimension == 0 && dimensionId == 1)
            {
                this.triggerAchievement(AchievementList.theEnd);
                ChunkCoordinates var2 = this.mcServer.worldServerForDimension(dimensionId).getEntrancePortalLocation();

                if (var2 != null)
                {
                    this.playerNetServerHandler.setPlayerLocation((double)var2.posX, (double)var2.posY, (double)var2.posZ, 0.0F, 0.0F);
                }

                dimensionId = 1;
            }
            else
            {
                this.triggerAchievement(AchievementList.portal);
            }

            this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
            this.lastExperience = -1;
            this.lastHealth = -1.0F;
            this.lastFoodLevel = -1;
        }
    }

    private void func_147097_b(TileEntity p_147097_1_)
    {
        if (p_147097_1_ != null)
        {
            Packet var2 = p_147097_1_.getDescriptionPacket();

            if (var2 != null)
            {
                this.playerNetServerHandler.sendPacket(var2);
            }
        }
    }

    public void onItemPickup(Entity p_71001_1_, int p_71001_2_)
    {
        super.onItemPickup(p_71001_1_, p_71001_2_);
        this.openContainer.detectAndSendChanges();
    }

    public EntityPlayer.EnumStatus sleepInBedAt(int x, int y, int z)
    {
        EntityPlayer.EnumStatus var4 = super.sleepInBedAt(x, y, z);

        if (var4 == EntityPlayer.EnumStatus.OK)
        {
            S0APacketUseBed var5 = new S0APacketUseBed(this, x, y, z);
            this.getServerForPlayer().getEntityTracker().sendToAllTrackingEntity(this, var5);
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.playerNetServerHandler.sendPacket(var5);
        }

        return var4;
    }

    public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn)
    {
        if (this.isPlayerSleeping())
        {
            this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 2));
        }

        super.wakeUpPlayer(p_70999_1_, updateWorldFlag, setSpawn);

        if (this.playerNetServerHandler != null)
        {
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    public void mountEntity(Entity entityIn)
    {
        super.mountEntity(entityIn);
        this.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this, this.ridingEntity));
        this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    protected void updateFallState(double distanceFallenThisTick, boolean isOnGround) {}

    public void handleFalling(double p_71122_1_, boolean p_71122_3_)
    {
        super.updateFallState(p_71122_1_, p_71122_3_);
    }

    public void displayGUIEditSign(TileEntity p_146100_1_)
    {
        if (p_146100_1_ instanceof TileEntitySign)
        {
            ((TileEntitySign)p_146100_1_).func_145912_a(this);
            this.playerNetServerHandler.sendPacket(new S36PacketSignEditorOpen(p_146100_1_.xCoord, p_146100_1_.yCoord, p_146100_1_.zCoord));
        }
    }

    private void getNextWindowId()
    {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }

    public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 1, "Crafting", 9, true));
        this.openContainer = new ContainerWorkbench(this.inventory, this.worldObj, p_71058_1_, p_71058_2_, p_71058_3_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 4, p_71002_4_ == null ? "" : p_71002_4_, 9, p_71002_4_ != null));
        this.openContainer = new ContainerEnchantment(this.inventory, this.worldObj, p_71002_1_, p_71002_2_, p_71002_3_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 8, "Repairing", 9, true));
        this.openContainer = new ContainerRepair(this.inventory, this.worldObj, p_82244_1_, p_82244_2_, p_82244_3_, this);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void displayGUIChest(IInventory p_71007_1_)
    {
        if (this.openContainer != this.inventoryContainer)
        {
            this.closeScreen();
        }

        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 0, p_71007_1_.getInventoryName(), p_71007_1_.getSizeInventory(), p_71007_1_.isCustomInventoryName()));
        this.openContainer = new ContainerChest(this.inventory, p_71007_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void func_146093_a(TileEntityHopper p_146093_1_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 9, p_146093_1_.getInventoryName(), p_146093_1_.getSizeInventory(), p_146093_1_.isCustomInventoryName()));
        this.openContainer = new ContainerHopper(this.inventory, p_146093_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 9, p_96125_1_.getInventoryName(), p_96125_1_.getSizeInventory(), p_96125_1_.isCustomInventoryName()));
        this.openContainer = new ContainerHopper(this.inventory, p_96125_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void func_146101_a(TileEntityFurnace p_146101_1_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 2, p_146101_1_.getInventoryName(), p_146101_1_.getSizeInventory(), p_146101_1_.isCustomInventoryName()));
        this.openContainer = new ContainerFurnace(this.inventory, p_146101_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void func_146102_a(TileEntityDispenser p_146102_1_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, p_146102_1_ instanceof TileEntityDropper ? 10 : 3, p_146102_1_.getInventoryName(), p_146102_1_.getSizeInventory(), p_146102_1_.isCustomInventoryName()));
        this.openContainer = new ContainerDispenser(this.inventory, p_146102_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void func_146098_a(TileEntityBrewingStand p_146098_1_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 5, p_146098_1_.getInventoryName(), p_146098_1_.getSizeInventory(), p_146098_1_.isCustomInventoryName()));
        this.openContainer = new ContainerBrewingStand(this.inventory, p_146098_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void func_146104_a(TileEntityBeacon p_146104_1_)
    {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 7, p_146104_1_.getInventoryName(), p_146104_1_.getSizeInventory(), p_146104_1_.isCustomInventoryName()));
        this.openContainer = new ContainerBeacon(this.inventory, p_146104_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_)
    {
        this.getNextWindowId();
        this.openContainer = new ContainerMerchant(this.inventory, p_71030_1_, this.worldObj);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
        InventoryMerchant var3 = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 6, p_71030_2_ == null ? "" : p_71030_2_, var3.getSizeInventory(), p_71030_2_ != null));
        MerchantRecipeList var4 = p_71030_1_.getRecipes(this);

        if (var4 != null)
        {
            PacketBuffer var5 = new PacketBuffer(Unpooled.buffer());

            try
            {
                var5.writeInt(this.currentWindowId);
                var4.func_151391_a(var5);
                this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|TrList", var5));
            }
            catch (IOException var10)
            {
                logger.error("Couldn\'t send trade list", var10);
            }
            finally
            {
                var5.release();
            }
        }
    }

    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_)
    {
        if (this.openContainer != this.inventoryContainer)
        {
            this.closeScreen();
        }

        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, 11, p_110298_2_.getInventoryName(), p_110298_2_.getSizeInventory(), p_110298_2_.isCustomInventoryName(), p_110298_1_.getEntityId()));
        this.openContainer = new ContainerHorseInventory(this.inventory, p_110298_2_, p_110298_1_);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }

    public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_)
    {
        if (!(p_71111_1_.getSlot(p_71111_2_) instanceof SlotCrafting))
        {
            if (!this.isChangingQuantityOnly)
            {
                this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(p_71111_1_.windowId, p_71111_2_, p_71111_3_));
            }
        }
    }

    public void sendContainerToPlayer(Container p_71120_1_)
    {
        this.updateCraftingInventory(p_71120_1_, p_71120_1_.getInventory());
    }

    public void updateCraftingInventory(Container p_71110_1_, List p_71110_2_)
    {
        this.playerNetServerHandler.sendPacket(new S30PacketWindowItems(p_71110_1_.windowId, p_71110_2_));
        this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
    }

    public void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_)
    {
        this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(p_71112_1_.windowId, p_71112_2_, p_71112_3_));
    }

    public void closeScreen()
    {
        this.playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(this.openContainer.windowId));
        this.closeContainer();
    }

    public void updateHeldItem()
    {
        if (!this.isChangingQuantityOnly)
        {
            this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }

    public void closeContainer()
    {
        this.openContainer.onContainerClosed(this);
        this.openContainer = this.inventoryContainer;
    }

    public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean p_110430_4_)
    {
        if (this.ridingEntity != null)
        {
            if (p_110430_1_ >= -1.0F && p_110430_1_ <= 1.0F)
            {
                this.moveStrafing = p_110430_1_;
            }

            if (p_110430_2_ >= -1.0F && p_110430_2_ <= 1.0F)
            {
                this.moveForward = p_110430_2_;
            }

            this.isJumping = p_110430_3_;
            this.setSneaking(p_110430_4_);
        }
    }

    public void addStat(StatBase p_71064_1_, int p_71064_2_)
    {
        if (p_71064_1_ != null)
        {
            this.field_147103_bO.func_150871_b(this, p_71064_1_, p_71064_2_);
            Iterator var3 = this.getWorldScoreboard().func_96520_a(p_71064_1_.func_150952_k()).iterator();

            while (var3.hasNext())
            {
                ScoreObjective var4 = (ScoreObjective)var3.next();
                this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var4).func_96648_a();
            }

            if (this.field_147103_bO.func_150879_e())
            {
                this.field_147103_bO.func_150876_a(this);
            }
        }
    }

    public void mountEntityAndWakeUp()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.mountEntity(this);
        }

        if (this.sleeping)
        {
            this.wakeUpPlayer(true, false, false);
        }
    }

    public void setPlayerHealthUpdated()
    {
        this.lastHealth = -1.0E8F;
    }

    public void addChatComponentMessage(IChatComponent p_146105_1_)
    {
        this.playerNetServerHandler.sendPacket(new S02PacketChat(p_146105_1_));
    }

    protected void onItemUseFinish()
    {
        this.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(this, (byte)9));
        super.onItemUseFinish();
    }

    public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_)
    {
        super.setItemInUse(p_71008_1_, p_71008_2_);

        if (p_71008_1_ != null && p_71008_1_.getItem() != null && p_71008_1_.getItem().getItemUseAction(p_71008_1_) == EnumAction.eat)
        {
            this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 3));
        }
    }

    public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_)
    {
        super.clonePlayer(p_71049_1_, p_71049_2_);
        this.lastExperience = -1;
        this.lastHealth = -1.0F;
        this.lastFoodLevel = -1;
        this.destroyedItemsNetCache.addAll(((EntityPlayerMP)p_71049_1_).destroyedItemsNetCache);
    }

    protected void onNewPotionEffect(PotionEffect p_70670_1_)
    {
        super.onNewPotionEffect(p_70670_1_);
        this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.getEntityId(), p_70670_1_));
    }

    protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_)
    {
        super.onChangedPotionEffect(p_70695_1_, p_70695_2_);
        this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.getEntityId(), p_70695_1_));
    }

    protected void onFinishedPotionEffect(PotionEffect p_70688_1_)
    {
        super.onFinishedPotionEffect(p_70688_1_);
        this.playerNetServerHandler.sendPacket(new S1EPacketRemoveEntityEffect(this.getEntityId(), p_70688_1_));
    }

    public void setPositionAndUpdate(double p_70634_1_, double p_70634_3_, double p_70634_5_)
    {
        this.playerNetServerHandler.setPlayerLocation(p_70634_1_, p_70634_3_, p_70634_5_, this.rotationYaw, this.rotationPitch);
    }

    public void onCriticalHit(Entity p_71009_1_)
    {
        this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(p_71009_1_, 4));
    }

    public void onEnchantmentCritical(Entity p_71047_1_)
    {
        this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(p_71047_1_, 5));
    }

    public void sendPlayerAbilities()
    {
        if (this.playerNetServerHandler != null)
        {
            this.playerNetServerHandler.sendPacket(new S39PacketPlayerAbilities(this.capabilities));
        }
    }

    public WorldServer getServerForPlayer()
    {
        return (WorldServer)this.worldObj;
    }

    public void setGameType(WorldSettings.GameType gameType)
    {
        this.theItemInWorldManager.setGameType(gameType);
        this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, (float)gameType.getID()));
    }

    public void addChatMessage(IChatComponent message)
    {
        this.playerNetServerHandler.sendPacket(new S02PacketChat(message));
    }

    public boolean canCommandSenderUseCommand(int permissionLevel, String command)
    {
        if ("seed".equals(command) && !this.mcServer.isDedicatedServer())
        {
            return true;
        }
        else if (!"tell".equals(command) && !"help".equals(command) && !"me".equals(command))
        {
            if (this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile()))
            {
                UserListOpsEntry var3 = (UserListOpsEntry)this.mcServer.getConfigurationManager().getOppedPlayers().getEntry(this.getGameProfile());
                return var3 != null ? var3.func_152644_a() >= permissionLevel : this.mcServer.getOpPermissionLevel() >= permissionLevel;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public String getPlayerIP()
    {
        String var1 = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
        var1 = var1.substring(var1.indexOf("/") + 1);
        var1 = var1.substring(0, var1.indexOf(":"));
        return var1;
    }

    public void func_147100_a(C15PacketClientSettings p_147100_1_)
    {
        this.translator = p_147100_1_.getLang();
        int var2 = 256 >> p_147100_1_.getView();

        if (var2 > 3 && var2 < 20)
        {
            ;
        }

        this.chatVisibility = p_147100_1_.getChatVisibility();
        this.chatColours = p_147100_1_.isColorsEnabled();

        if (this.mcServer.isSinglePlayer() && this.mcServer.getServerOwner().equals(this.getCommandSenderName()))
        {
            this.mcServer.setDifficultyForAllWorlds(p_147100_1_.getDifficulty());
        }

        this.setHideCape(1, !p_147100_1_.isShowCape());
    }

    public EntityPlayer.EnumChatVisibility func_147096_v()
    {
        return this.chatVisibility;
    }

    public void requestTexturePackLoad(String p_147095_1_)
    {
        this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|RPack", p_147095_1_.getBytes(Charsets.UTF_8)));
    }

    public ChunkCoordinates getCommandSenderPosition()
    {
        return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
    }

    public void markPlayerActive()
    {
        this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }

    public StatisticsFile getStatFile()
    {
        return this.field_147103_bO;
    }

    public void func_152339_d(Entity p_152339_1_)
    {
        if (p_152339_1_ instanceof EntityPlayer)
        {
            this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(new int[] {p_152339_1_.getEntityId()}));
        }
        else
        {
            this.destroyedItemsNetCache.add(Integer.valueOf(p_152339_1_.getEntityId()));
        }
    }

    public long getLastActiveTime()
    {
        return this.playerLastActiveTime;
    }
}
