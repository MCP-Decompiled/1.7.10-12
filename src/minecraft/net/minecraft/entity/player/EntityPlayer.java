package net.minecraft.entity.player;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class EntityPlayer extends EntityLivingBase implements ICommandSender
{
    public InventoryPlayer inventory = new InventoryPlayer(this);
    private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
    public Container inventoryContainer;
    public Container openContainer;
    protected FoodStats foodStats = new FoodStats();
    protected int flyToggleTimer;
    public float prevCameraYaw;
    public float cameraYaw;
    public int xpCooldown;
    public double field_71091_bM;
    public double field_71096_bN;
    public double field_71097_bO;
    public double field_71094_bP;
    public double field_71095_bQ;
    public double field_71085_bR;
    protected boolean sleeping;
    public ChunkCoordinates playerLocation;
    private int sleepTimer;
    public float field_71079_bU;
    public float field_71082_cx;
    public float field_71089_bV;
    private ChunkCoordinates spawnChunk;
    private boolean spawnForced;
    private ChunkCoordinates startMinecartRidingCoordinate;
    public PlayerCapabilities capabilities = new PlayerCapabilities();
    public int experienceLevel;
    public int experienceTotal;
    public float experience;
    private ItemStack itemInUse;
    private int itemInUseCount;
    protected float speedOnGround = 0.1F;
    protected float speedInAir = 0.02F;
    private int field_82249_h;
    private final GameProfile gameProfile;
    public EntityFishHook fishEntity;
    private static final String __OBFID = "CL_00001711";

    public EntityPlayer(World p_i45324_1_, GameProfile p_i45324_2_)
    {
        super(p_i45324_1_);
        this.entityUniqueID = getUUID(p_i45324_2_);
        this.gameProfile = p_i45324_2_;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !p_i45324_1_.isRemote, this);
        this.openContainer = this.inventoryContainer;
        this.yOffset = 1.62F;
        ChunkCoordinates var3 = p_i45324_1_.getSpawnPoint();
        this.setLocationAndAngles((double)var3.posX + 0.5D, (double)(var3.posY + 1), (double)var3.posZ + 0.5D, 0.0F, 0.0F);
        this.field_70741_aB = 180.0F;
        this.fireResistance = 20;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, Float.valueOf(0.0F));
        this.dataWatcher.addObject(18, Integer.valueOf(0));
    }

    public ItemStack getItemInUse()
    {
        return this.itemInUse;
    }

    public int getItemInUseCount()
    {
        return this.itemInUseCount;
    }

    public boolean isUsingItem()
    {
        return this.itemInUse != null;
    }

    public int getItemInUseDuration()
    {
        return this.isUsingItem() ? this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount : 0;
    }

    public void stopUsingItem()
    {
        if (this.itemInUse != null)
        {
            this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
        }

        this.clearItemInUse();
    }

    public void clearItemInUse()
    {
        this.itemInUse = null;
        this.itemInUseCount = 0;

        if (!this.worldObj.isRemote)
        {
            this.setEating(false);
        }
    }

    public boolean isBlocking()
    {
        return this.isUsingItem() && this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.block;
    }

    public void onUpdate()
    {
        if (this.itemInUse != null)
        {
            ItemStack var1 = this.inventory.getCurrentItem();

            if (var1 == this.itemInUse)
            {
                if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0)
                {
                    this.updateItemUse(var1, 5);
                }

                if (--this.itemInUseCount == 0 && !this.worldObj.isRemote)
                {
                    this.onItemUseFinish();
                }
            }
            else
            {
                this.clearItemInUse();
            }
        }

        if (this.xpCooldown > 0)
        {
            --this.xpCooldown;
        }

        if (this.isPlayerSleeping())
        {
            ++this.sleepTimer;

            if (this.sleepTimer > 100)
            {
                this.sleepTimer = 100;
            }

            if (!this.worldObj.isRemote)
            {
                if (!this.isInBed())
                {
                    this.wakeUpPlayer(true, true, false);
                }
                else if (this.worldObj.isDaytime())
                {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        }
        else if (this.sleepTimer > 0)
        {
            ++this.sleepTimer;

            if (this.sleepTimer >= 110)
            {
                this.sleepTimer = 0;
            }
        }

        super.onUpdate();

        if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this))
        {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }

        if (this.isBurning() && this.capabilities.disableDamage)
        {
            this.extinguish();
        }

        this.field_71091_bM = this.field_71094_bP;
        this.field_71096_bN = this.field_71095_bQ;
        this.field_71097_bO = this.field_71085_bR;
        double var9 = this.posX - this.field_71094_bP;
        double var3 = this.posY - this.field_71095_bQ;
        double var5 = this.posZ - this.field_71085_bR;
        double var7 = 10.0D;

        if (var9 > var7)
        {
            this.field_71091_bM = this.field_71094_bP = this.posX;
        }

        if (var5 > var7)
        {
            this.field_71097_bO = this.field_71085_bR = this.posZ;
        }

        if (var3 > var7)
        {
            this.field_71096_bN = this.field_71095_bQ = this.posY;
        }

        if (var9 < -var7)
        {
            this.field_71091_bM = this.field_71094_bP = this.posX;
        }

        if (var5 < -var7)
        {
            this.field_71097_bO = this.field_71085_bR = this.posZ;
        }

        if (var3 < -var7)
        {
            this.field_71096_bN = this.field_71095_bQ = this.posY;
        }

        this.field_71094_bP += var9 * 0.25D;
        this.field_71085_bR += var5 * 0.25D;
        this.field_71095_bQ += var3 * 0.25D;

        if (this.ridingEntity == null)
        {
            this.startMinecartRidingCoordinate = null;
        }

        if (!this.worldObj.isRemote)
        {
            this.foodStats.onUpdate(this);
            this.addStat(StatList.minutesPlayedStat, 1);
        }
    }

    public int getMaxInPortalTime()
    {
        return this.capabilities.disableDamage ? 0 : 80;
    }

    protected String getSwimSound()
    {
        return "game.player.swim";
    }

    protected String getSplashSound()
    {
        return "game.player.swim.splash";
    }

    public int getPortalCooldown()
    {
        return 10;
    }

    public void playSound(String name, float volume, float pitch)
    {
        this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
    }

    protected void updateItemUse(ItemStack itemStackIn, int p_71010_2_)
    {
        if (itemStackIn.getItemUseAction() == EnumAction.drink)
        {
            this.playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (itemStackIn.getItemUseAction() == EnumAction.eat)
        {
            for (int var3 = 0; var3 < p_71010_2_; ++var3)
            {
                Vec3 var4 = Vec3.createVectorHelper(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                var4.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
                var4.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
                Vec3 var5 = Vec3.createVectorHelper(((double)this.rand.nextFloat() - 0.5D) * 0.3D, (double)(-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
                var5.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
                var5.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
                var5 = var5.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
                String var6 = "iconcrack_" + Item.getIdFromItem(itemStackIn.getItem());

                if (itemStackIn.getHasSubtypes())
                {
                    var6 = var6 + "_" + itemStackIn.getMetadata();
                }

                this.worldObj.spawnParticle(var6, var5.xCoord, var5.yCoord, var5.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord);
            }

            this.playSound("random.eat", 0.5F + 0.5F * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected void onItemUseFinish()
    {
        if (this.itemInUse != null)
        {
            this.updateItemUse(this.itemInUse, 16);
            int var1 = this.itemInUse.stackSize;
            ItemStack var2 = this.itemInUse.onItemUseFinish(this.worldObj, this);

            if (var2 != this.itemInUse || var2 != null && var2.stackSize != var1)
            {
                this.inventory.mainInventory[this.inventory.currentItem] = var2;

                if (var2.stackSize == 0)
                {
                    this.inventory.mainInventory[this.inventory.currentItem] = null;
                }
            }

            this.clearItemInUse();
        }
    }

    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 9)
        {
            this.onItemUseFinish();
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    protected boolean isMovementBlocked()
    {
        return this.getHealth() <= 0.0F || this.isPlayerSleeping();
    }

    protected void closeScreen()
    {
        this.openContainer = this.inventoryContainer;
    }

    public void mountEntity(Entity entityIn)
    {
        if (this.ridingEntity != null && entityIn == null)
        {
            if (!this.worldObj.isRemote)
            {
                this.dismountEntity(this.ridingEntity);
            }

            if (this.ridingEntity != null)
            {
                this.ridingEntity.riddenByEntity = null;
            }

            this.ridingEntity = null;
        }
        else
        {
            super.mountEntity(entityIn);
        }
    }

    public void updateRidden()
    {
        if (!this.worldObj.isRemote && this.isSneaking())
        {
            this.mountEntity((Entity)null);
            this.setSneaking(false);
        }
        else
        {
            double var1 = this.posX;
            double var3 = this.posY;
            double var5 = this.posZ;
            float var7 = this.rotationYaw;
            float var8 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0F;
            this.addMountedMovementStat(this.posX - var1, this.posY - var3, this.posZ - var5);

            if (this.ridingEntity instanceof EntityPig)
            {
                this.rotationPitch = var8;
                this.rotationYaw = var7;
                this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
            }
        }
    }

    public void preparePlayerToSpawn()
    {
        this.yOffset = 1.62F;
        this.setSize(0.6F, 1.8F);
        super.preparePlayerToSpawn();
        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }

    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
    }

    public void onLivingUpdate()
    {
        if (this.flyToggleTimer > 0)
        {
            --this.flyToggleTimer;
        }

        if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL && this.getHealth() < this.getMaxHealth() && this.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && this.ticksExisted % 20 * 12 == 0)
        {
            this.heal(1.0F);
        }

        this.inventory.decrementAnimations();
        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

        if (!this.worldObj.isRemote)
        {
            var1.setBaseValue((double)this.capabilities.getWalkSpeed());
        }

        this.jumpMovementFactor = this.speedInAir;

        if (this.isSprinting())
        {
            this.jumpMovementFactor = (float)((double)this.jumpMovementFactor + (double)this.speedInAir * 0.3D);
        }

        this.setAIMoveSpeed((float)var1.getAttributeValue());
        float var2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var3 = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;

        if (var2 > 0.1F)
        {
            var2 = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0.0F)
        {
            var2 = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0.0F)
        {
            var3 = 0.0F;
        }

        this.cameraYaw += (var2 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (var3 - this.cameraPitch) * 0.8F;

        if (this.getHealth() > 0.0F)
        {
            AxisAlignedBB var4 = null;

            if (this.ridingEntity != null && !this.ridingEntity.isDead)
            {
                var4 = this.boundingBox.union(this.ridingEntity.boundingBox).expand(1.0D, 0.0D, 1.0D);
            }
            else
            {
                var4 = this.boundingBox.expand(1.0D, 0.5D, 1.0D);
            }

            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, var4);

            if (var5 != null)
            {
                for (int var6 = 0; var6 < var5.size(); ++var6)
                {
                    Entity var7 = (Entity)var5.get(var6);

                    if (!var7.isDead)
                    {
                        this.collideWithPlayer(var7);
                    }
                }
            }
        }
    }

    private void collideWithPlayer(Entity p_71044_1_)
    {
        p_71044_1_.onCollideWithPlayer(this);
    }

    public int getScore()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setScore(int p_85040_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
    }

    public void addScore(int p_85039_1_)
    {
        int var2 = this.getScore();
        this.dataWatcher.updateObject(18, Integer.valueOf(var2 + p_85039_1_));
    }

    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);
        this.setSize(0.2F, 0.2F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612D;

        if (this.getCommandSenderName().equals("Notch"))
        {
            this.func_146097_a(new ItemStack(Items.apple, 1), true, false);
        }

        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            this.inventory.dropAllItems();
        }

        if (p_70645_1_ != null)
        {
            this.motionX = (double)(-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
            this.motionZ = (double)(-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F);
        }
        else
        {
            this.motionX = this.motionZ = 0.0D;
        }

        this.yOffset = 0.1F;
        this.addStat(StatList.deathsStat, 1);
    }

    protected String getHurtSound()
    {
        return "game.player.hurt";
    }

    protected String getDeathSound()
    {
        return "game.player.die";
    }

    public void addToPlayerScore(Entity entityIn, int amount)
    {
        this.addScore(amount);
        Collection var3 = this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.totalKillCount);

        if (entityIn instanceof EntityPlayer)
        {
            this.addStat(StatList.playerKillsStat, 1);
            var3.addAll(this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.playerKillCount));
        }
        else
        {
            this.addStat(StatList.mobKillsStat, 1);
        }

        Iterator var4 = var3.iterator();

        while (var4.hasNext())
        {
            ScoreObjective var5 = (ScoreObjective)var4.next();
            Score var6 = this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var5);
            var6.func_96648_a();
        }
    }

    public EntityItem dropOneItem(boolean p_71040_1_)
    {
        return this.func_146097_a(this.inventory.decrStackSize(this.inventory.currentItem, p_71040_1_ && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1), false, true);
    }

    public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStackIn, boolean p_71019_2_)
    {
        return this.func_146097_a(itemStackIn, false, false);
    }

    public EntityItem func_146097_a(ItemStack p_146097_1_, boolean p_146097_2_, boolean p_146097_3_)
    {
        if (p_146097_1_ == null)
        {
            return null;
        }
        else if (p_146097_1_.stackSize == 0)
        {
            return null;
        }
        else
        {
            EntityItem var4 = new EntityItem(this.worldObj, this.posX, this.posY - 0.30000001192092896D + (double)this.getEyeHeight(), this.posZ, p_146097_1_);
            var4.delayBeforeCanPickup = 40;

            if (p_146097_3_)
            {
                var4.setThrower(this.getCommandSenderName());
            }

            float var5 = 0.1F;
            float var6;

            if (p_146097_2_)
            {
                var6 = this.rand.nextFloat() * 0.5F;
                float var7 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                var4.motionX = (double)(-MathHelper.sin(var7) * var6);
                var4.motionZ = (double)(MathHelper.cos(var7) * var6);
                var4.motionY = 0.20000000298023224D;
            }
            else
            {
                var5 = 0.3F;
                var4.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var5);
                var4.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var5);
                var4.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * var5 + 0.1F);
                var5 = 0.02F;
                var6 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                var5 *= this.rand.nextFloat();
                var4.motionX += Math.cos((double)var6) * (double)var5;
                var4.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                var4.motionZ += Math.sin((double)var6) * (double)var5;
            }

            this.joinEntityItemWithWorld(var4);
            this.addStat(StatList.dropStat, 1);
            return var4;
        }
    }

    protected void joinEntityItemWithWorld(EntityItem p_71012_1_)
    {
        this.worldObj.spawnEntityInWorld(p_71012_1_);
    }

    public float getBreakSpeed(Block p_146096_1_, boolean p_146096_2_)
    {
        float var3 = this.inventory.getStrVsBlock(p_146096_1_);

        if (var3 > 1.0F)
        {
            int var4 = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack var5 = this.inventory.getCurrentItem();

            if (var4 > 0 && var5 != null)
            {
                float var6 = (float)(var4 * var4 + 1);

                if (!var5.canItemHarvestBlock(p_146096_1_) && var3 <= 1.0F)
                {
                    var3 += var6 * 0.08F;
                }
                else
                {
                    var3 += var6;
                }
            }
        }

        if (this.isPotionActive(Potion.digSpeed))
        {
            var3 *= 1.0F + (float)(this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (this.isPotionActive(Potion.digSlowdown))
        {
            var3 *= 1.0F - (float)(this.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
        }

        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this))
        {
            var3 /= 5.0F;
        }

        if (!this.onGround)
        {
            var3 /= 5.0F;
        }

        return var3;
    }

    public boolean canHarvestBlock(Block p_146099_1_)
    {
        return this.inventory.func_146025_b(p_146099_1_);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.entityUniqueID = getUUID(this.gameProfile);
        NBTTagList var2 = tagCompund.getTagList("Inventory", 10);
        this.inventory.readFromNBT(var2);
        this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
        this.sleeping = tagCompund.getBoolean("Sleeping");
        this.sleepTimer = tagCompund.getShort("SleepTimer");
        this.experience = tagCompund.getFloat("XpP");
        this.experienceLevel = tagCompund.getInteger("XpLevel");
        this.experienceTotal = tagCompund.getInteger("XpTotal");
        this.setScore(tagCompund.getInteger("Score"));

        if (this.sleeping)
        {
            this.playerLocation = new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
            this.wakeUpPlayer(true, true, false);
        }

        if (tagCompund.hasKey("SpawnX", 99) && tagCompund.hasKey("SpawnY", 99) && tagCompund.hasKey("SpawnZ", 99))
        {
            this.spawnChunk = new ChunkCoordinates(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
            this.spawnForced = tagCompund.getBoolean("SpawnForced");
        }

        this.foodStats.readNBT(tagCompund);
        this.capabilities.readCapabilitiesFromNBT(tagCompund);

        if (tagCompund.hasKey("EnderItems", 9))
        {
            NBTTagList var3 = tagCompund.getTagList("EnderItems", 10);
            this.theInventoryEnderChest.loadInventoryFromNBT(var3);
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        tagCompound.setBoolean("Sleeping", this.sleeping);
        tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
        tagCompound.setFloat("XpP", this.experience);
        tagCompound.setInteger("XpLevel", this.experienceLevel);
        tagCompound.setInteger("XpTotal", this.experienceTotal);
        tagCompound.setInteger("Score", this.getScore());

        if (this.spawnChunk != null)
        {
            tagCompound.setInteger("SpawnX", this.spawnChunk.posX);
            tagCompound.setInteger("SpawnY", this.spawnChunk.posY);
            tagCompound.setInteger("SpawnZ", this.spawnChunk.posZ);
            tagCompound.setBoolean("SpawnForced", this.spawnForced);
        }

        this.foodStats.writeNBT(tagCompound);
        this.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
    }

    public void displayGUIChest(IInventory p_71007_1_) {}

    public void func_146093_a(TileEntityHopper p_146093_1_) {}

    public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {}

    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {}

    public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_) {}

    public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {}

    public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_) {}

    public float getEyeHeight()
    {
        return 0.12F;
    }

    protected void resetHeight()
    {
        this.yOffset = 1.62F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (this.capabilities.disableDamage && !source.canHarmInCreative())
        {
            return false;
        }
        else
        {
            this.entityAge = 0;

            if (this.getHealth() <= 0.0F)
            {
                return false;
            }
            else
            {
                if (this.isPlayerSleeping() && !this.worldObj.isRemote)
                {
                    this.wakeUpPlayer(true, true, false);
                }

                if (source.isDifficultyScaled())
                {
                    if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
                    {
                        amount = 0.0F;
                    }

                    if (this.worldObj.difficultySetting == EnumDifficulty.EASY)
                    {
                        amount = amount / 2.0F + 1.0F;
                    }

                    if (this.worldObj.difficultySetting == EnumDifficulty.HARD)
                    {
                        amount = amount * 3.0F / 2.0F;
                    }
                }

                if (amount == 0.0F)
                {
                    return false;
                }
                else
                {
                    Entity var3 = source.getEntity();

                    if (var3 instanceof EntityArrow && ((EntityArrow)var3).shootingEntity != null)
                    {
                        var3 = ((EntityArrow)var3).shootingEntity;
                    }

                    this.addStat(StatList.damageTakenStat, Math.round(amount * 10.0F));
                    return super.attackEntityFrom(source, amount);
                }
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer p_96122_1_)
    {
        Team var2 = this.getTeam();
        Team var3 = p_96122_1_.getTeam();
        return var2 == null ? true : (!var2.isSameTeam(var3) ? true : var2.getAllowFriendlyFire());
    }

    protected void damageArmor(float p_70675_1_)
    {
        this.inventory.damageArmor(p_70675_1_);
    }

    public int getTotalArmorValue()
    {
        return this.inventory.getTotalArmorValue();
    }

    public float getArmorVisibility()
    {
        int var1 = 0;
        ItemStack[] var2 = this.inventory.armorInventory;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];

            if (var5 != null)
            {
                ++var1;
            }
        }

        return (float)var1 / (float)this.inventory.armorInventory.length;
    }

    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_)
    {
        if (!this.isEntityInvulnerable())
        {
            if (!p_70665_1_.isUnblockable() && this.isBlocking() && p_70665_2_ > 0.0F)
            {
                p_70665_2_ = (1.0F + p_70665_2_) * 0.5F;
            }

            p_70665_2_ = this.applyArmorCalculations(p_70665_1_, p_70665_2_);
            p_70665_2_ = this.applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
            float var3 = p_70665_2_;
            p_70665_2_ = Math.max(p_70665_2_ - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - p_70665_2_));

            if (p_70665_2_ != 0.0F)
            {
                this.addExhaustion(p_70665_1_.getHungerDamage());
                float var4 = this.getHealth();
                this.setHealth(this.getHealth() - p_70665_2_);
                this.getCombatTracker().func_94547_a(p_70665_1_, var4, p_70665_2_);
            }
        }
    }

    public void func_146101_a(TileEntityFurnace p_146101_1_) {}

    public void func_146102_a(TileEntityDispenser p_146102_1_) {}

    public void displayGUIEditSign(TileEntity p_146100_1_) {}

    public void func_146095_a(CommandBlockLogic p_146095_1_) {}

    public void func_146098_a(TileEntityBrewingStand p_146098_1_) {}

    public void func_146104_a(TileEntityBeacon p_146104_1_) {}

    public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {}

    public void displayGUIBook(ItemStack p_71048_1_) {}

    public boolean interactWith(Entity p_70998_1_)
    {
        ItemStack var2 = this.getCurrentEquippedItem();
        ItemStack var3 = var2 != null ? var2.copy() : null;

        if (!p_70998_1_.interactFirst(this))
        {
            if (var2 != null && p_70998_1_ instanceof EntityLivingBase)
            {
                if (this.capabilities.isCreativeMode)
                {
                    var2 = var3;
                }

                if (var2.interactWithEntity(this, (EntityLivingBase)p_70998_1_))
                {
                    if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode)
                    {
                        this.destroyCurrentEquippedItem();
                    }

                    return true;
                }
            }

            return false;
        }
        else
        {
            if (var2 != null && var2 == this.getCurrentEquippedItem())
            {
                if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode)
                {
                    this.destroyCurrentEquippedItem();
                }
                else if (var2.stackSize < var3.stackSize && this.capabilities.isCreativeMode)
                {
                    var2.stackSize = var3.stackSize;
                }
            }

            return true;
        }
    }

    public ItemStack getCurrentEquippedItem()
    {
        return this.inventory.getCurrentItem();
    }

    public void destroyCurrentEquippedItem()
    {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
    }

    public double getYOffset()
    {
        return (double)(this.yOffset - 0.5F);
    }

    public void attackTargetEntityWithCurrentItem(Entity targetEntity)
    {
        if (targetEntity.canAttackWithItem())
        {
            if (!targetEntity.hitByEntity(this))
            {
                float var2 = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
                int var3 = 0;
                float var4 = 0.0F;

                if (targetEntity instanceof EntityLivingBase)
                {
                    var4 = EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)targetEntity);
                    var3 += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)targetEntity);
                }

                if (this.isSprinting())
                {
                    ++var3;
                }

                if (var2 > 0.0F || var4 > 0.0F)
                {
                    boolean var5 = this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase;

                    if (var5 && var2 > 0.0F)
                    {
                        var2 *= 1.5F;
                    }

                    var2 += var4;
                    boolean var6 = false;
                    int var7 = EnchantmentHelper.getFireAspectModifier(this);

                    if (targetEntity instanceof EntityLivingBase && var7 > 0 && !targetEntity.isBurning())
                    {
                        var6 = true;
                        targetEntity.setFire(1);
                    }

                    boolean var8 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), var2);

                    if (var8)
                    {
                        if (var3 > 0)
                        {
                            targetEntity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)var3 * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)var3 * 0.5F));
                            this.motionX *= 0.6D;
                            this.motionZ *= 0.6D;
                            this.setSprinting(false);
                        }

                        if (var5)
                        {
                            this.onCriticalHit(targetEntity);
                        }

                        if (var4 > 0.0F)
                        {
                            this.onEnchantmentCritical(targetEntity);
                        }

                        if (var2 >= 18.0F)
                        {
                            this.triggerAchievement(AchievementList.overkill);
                        }

                        this.setLastAttacker(targetEntity);

                        if (targetEntity instanceof EntityLivingBase)
                        {
                            EnchantmentHelper.func_151384_a((EntityLivingBase)targetEntity, this);
                        }

                        EnchantmentHelper.func_151385_b(this, targetEntity);
                        ItemStack var9 = this.getCurrentEquippedItem();
                        Object var10 = targetEntity;

                        if (targetEntity instanceof EntityDragonPart)
                        {
                            IEntityMultiPart var11 = ((EntityDragonPart)targetEntity).entityDragonObj;

                            if (var11 != null && var11 instanceof EntityLivingBase)
                            {
                                var10 = (EntityLivingBase)var11;
                            }
                        }

                        if (var9 != null && var10 instanceof EntityLivingBase)
                        {
                            var9.hitEntity((EntityLivingBase)var10, this);

                            if (var9.stackSize <= 0)
                            {
                                this.destroyCurrentEquippedItem();
                            }
                        }

                        if (targetEntity instanceof EntityLivingBase)
                        {
                            this.addStat(StatList.damageDealtStat, Math.round(var2 * 10.0F));

                            if (var7 > 0)
                            {
                                targetEntity.setFire(var7 * 4);
                            }
                        }

                        this.addExhaustion(0.3F);
                    }
                    else if (var6)
                    {
                        targetEntity.extinguish();
                    }
                }
            }
        }
    }

    public void onCriticalHit(Entity p_71009_1_) {}

    public void onEnchantmentCritical(Entity p_71047_1_) {}

    public void respawnPlayer() {}

    public void setDead()
    {
        super.setDead();
        this.inventoryContainer.onContainerClosed(this);

        if (this.openContainer != null)
        {
            this.openContainer.onContainerClosed(this);
        }
    }

    public boolean isEntityInsideOpaqueBlock()
    {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }

    public GameProfile getGameProfile()
    {
        return this.gameProfile;
    }

    public EntityPlayer.EnumStatus sleepInBedAt(int x, int y, int z)
    {
        if (!this.worldObj.isRemote)
        {
            if (this.isPlayerSleeping() || !this.isEntityAlive())
            {
                return EntityPlayer.EnumStatus.OTHER_PROBLEM;
            }

            if (!this.worldObj.provider.isSurfaceWorld())
            {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
            }

            if (this.worldObj.isDaytime())
            {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.posX - (double)x) > 3.0D || Math.abs(this.posY - (double)y) > 2.0D || Math.abs(this.posZ - (double)z) > 3.0D)
            {
                return EntityPlayer.EnumStatus.TOO_FAR_AWAY;
            }

            double var4 = 8.0D;
            double var6 = 5.0D;
            List var8 = this.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox((double)x - var4, (double)y - var6, (double)z - var4, (double)x + var4, (double)y + var6, (double)z + var4));

            if (!var8.isEmpty())
            {
                return EntityPlayer.EnumStatus.NOT_SAFE;
            }
        }

        if (this.isRiding())
        {
            this.mountEntity((Entity)null);
        }

        this.setSize(0.2F, 0.2F);
        this.yOffset = 0.2F;

        if (this.worldObj.blockExists(x, y, z))
        {
            int var9 = this.worldObj.getBlockMetadata(x, y, z);
            int var5 = BlockBed.getDirection(var9);
            float var10 = 0.5F;
            float var7 = 0.5F;

            switch (var5)
            {
                case 0:
                    var7 = 0.9F;
                    break;

                case 1:
                    var10 = 0.1F;
                    break;

                case 2:
                    var7 = 0.1F;
                    break;

                case 3:
                    var10 = 0.9F;
            }

            this.func_71013_b(var5);
            this.setPosition((double)((float)x + var10), (double)((float)y + 0.9375F), (double)((float)z + var7));
        }
        else
        {
            this.setPosition((double)((float)x + 0.5F), (double)((float)y + 0.9375F), (double)((float)z + 0.5F));
        }

        this.sleeping = true;
        this.sleepTimer = 0;
        this.playerLocation = new ChunkCoordinates(x, y, z);
        this.motionX = this.motionZ = this.motionY = 0.0D;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        return EntityPlayer.EnumStatus.OK;
    }

    private void func_71013_b(int p_71013_1_)
    {
        this.field_71079_bU = 0.0F;
        this.field_71089_bV = 0.0F;

        switch (p_71013_1_)
        {
            case 0:
                this.field_71089_bV = -1.8F;
                break;

            case 1:
                this.field_71079_bU = 1.8F;
                break;

            case 2:
                this.field_71089_bV = 1.8F;
                break;

            case 3:
                this.field_71079_bU = -1.8F;
        }
    }

    public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn)
    {
        this.setSize(0.6F, 1.8F);
        this.resetHeight();
        ChunkCoordinates var4 = this.playerLocation;
        ChunkCoordinates var5 = this.playerLocation;

        if (var4 != null && this.worldObj.getBlock(var4.posX, var4.posY, var4.posZ) == Blocks.bed)
        {
            BlockBed.setBedOccupied(this.worldObj, var4.posX, var4.posY, var4.posZ, false);
            var5 = BlockBed.getSafeExitLocation(this.worldObj, var4.posX, var4.posY, var4.posZ, 0);

            if (var5 == null)
            {
                var5 = new ChunkCoordinates(var4.posX, var4.posY + 1, var4.posZ);
            }

            this.setPosition((double)((float)var5.posX + 0.5F), (double)((float)var5.posY + this.yOffset + 0.1F), (double)((float)var5.posZ + 0.5F));
        }

        this.sleeping = false;

        if (!this.worldObj.isRemote && updateWorldFlag)
        {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        if (p_70999_1_)
        {
            this.sleepTimer = 0;
        }
        else
        {
            this.sleepTimer = 100;
        }

        if (setSpawn)
        {
            this.setSpawnChunk(this.playerLocation, false);
        }
    }

    private boolean isInBed()
    {
        return this.worldObj.getBlock(this.playerLocation.posX, this.playerLocation.posY, this.playerLocation.posZ) == Blocks.bed;
    }

    public static ChunkCoordinates verifyRespawnCoordinates(World p_71056_0_, ChunkCoordinates p_71056_1_, boolean p_71056_2_)
    {
        IChunkProvider var3 = p_71056_0_.getChunkProvider();
        var3.loadChunk(p_71056_1_.posX - 3 >> 4, p_71056_1_.posZ - 3 >> 4);
        var3.loadChunk(p_71056_1_.posX + 3 >> 4, p_71056_1_.posZ - 3 >> 4);
        var3.loadChunk(p_71056_1_.posX - 3 >> 4, p_71056_1_.posZ + 3 >> 4);
        var3.loadChunk(p_71056_1_.posX + 3 >> 4, p_71056_1_.posZ + 3 >> 4);

        if (p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ) == Blocks.bed)
        {
            ChunkCoordinates var8 = BlockBed.getSafeExitLocation(p_71056_0_, p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ, 0);
            return var8;
        }
        else
        {
            Material var4 = p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ).getMaterial();
            Material var5 = p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY + 1, p_71056_1_.posZ).getMaterial();
            boolean var6 = !var4.isSolid() && !var4.isLiquid();
            boolean var7 = !var5.isSolid() && !var5.isLiquid();
            return p_71056_2_ && var6 && var7 ? p_71056_1_ : null;
        }
    }

    public float getBedOrientationInDegrees()
    {
        if (this.playerLocation != null)
        {
            int var1 = this.worldObj.getBlockMetadata(this.playerLocation.posX, this.playerLocation.posY, this.playerLocation.posZ);
            int var2 = BlockBed.getDirection(var1);

            switch (var2)
            {
                case 0:
                    return 90.0F;

                case 1:
                    return 0.0F;

                case 2:
                    return 270.0F;

                case 3:
                    return 180.0F;
            }
        }

        return 0.0F;
    }

    public boolean isPlayerSleeping()
    {
        return this.sleeping;
    }

    public boolean isPlayerFullyAsleep()
    {
        return this.sleeping && this.sleepTimer >= 100;
    }

    public int getSleepTimer()
    {
        return this.sleepTimer;
    }

    protected boolean getHideCape(int p_82241_1_)
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1 << p_82241_1_) != 0;
    }

    protected void setHideCape(int p_82239_1_, boolean p_82239_2_)
    {
        byte var3 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_82239_2_)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var3 | 1 << p_82239_1_)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var3 & ~(1 << p_82239_1_))));
        }
    }

    public void addChatComponentMessage(IChatComponent p_146105_1_) {}

    public ChunkCoordinates getBedLocation()
    {
        return this.spawnChunk;
    }

    public boolean isSpawnForced()
    {
        return this.spawnForced;
    }

    public void setSpawnChunk(ChunkCoordinates p_71063_1_, boolean p_71063_2_)
    {
        if (p_71063_1_ != null)
        {
            this.spawnChunk = new ChunkCoordinates(p_71063_1_);
            this.spawnForced = p_71063_2_;
        }
        else
        {
            this.spawnChunk = null;
            this.spawnForced = false;
        }
    }

    public void triggerAchievement(StatBase p_71029_1_)
    {
        this.addStat(p_71029_1_, 1);
    }

    public void addStat(StatBase p_71064_1_, int p_71064_2_) {}

    public void jump()
    {
        super.jump();
        this.addStat(StatList.jumpStat, 1);

        if (this.isSprinting())
        {
            this.addExhaustion(0.8F);
        }
        else
        {
            this.addExhaustion(0.2F);
        }
    }

    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        double var3 = this.posX;
        double var5 = this.posY;
        double var7 = this.posZ;

        if (this.capabilities.isFlying && this.ridingEntity == null)
        {
            double var9 = this.motionY;
            float var11 = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed();
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            this.motionY = var9 * 0.6D;
            this.jumpMovementFactor = var11;
        }
        else
        {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }

        this.addMovementStat(this.posX - var3, this.posY - var5, this.posZ - var7);
    }

    public float getAIMoveSpeed()
    {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    }

    public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_)
    {
        if (this.ridingEntity == null)
        {
            int var7;

            if (this.isInsideOfMaterial(Material.water))
            {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 0)
                {
                    this.addStat(StatList.distanceDoveStat, var7);
                    this.addExhaustion(0.015F * (float)var7 * 0.01F);
                }
            }
            else if (this.isInWater())
            {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 0)
                {
                    this.addStat(StatList.distanceSwumStat, var7);
                    this.addExhaustion(0.015F * (float)var7 * 0.01F);
                }
            }
            else if (this.isOnLadder())
            {
                if (p_71000_3_ > 0.0D)
                {
                    this.addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0D));
                }
            }
            else if (this.onGround)
            {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 0)
                {
                    this.addStat(StatList.distanceWalkedStat, var7);

                    if (this.isSprinting())
                    {
                        this.addExhaustion(0.099999994F * (float)var7 * 0.01F);
                    }
                    else
                    {
                        this.addExhaustion(0.01F * (float)var7 * 0.01F);
                    }
                }
            }
            else
            {
                var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

                if (var7 > 25)
                {
                    this.addStat(StatList.distanceFlownStat, var7);
                }
            }
        }
    }

    private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_)
    {
        if (this.ridingEntity != null)
        {
            int var7 = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);

            if (var7 > 0)
            {
                if (this.ridingEntity instanceof EntityMinecart)
                {
                    this.addStat(StatList.distanceByMinecartStat, var7);

                    if (this.startMinecartRidingCoordinate == null)
                    {
                        this.startMinecartRidingCoordinate = new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
                    }
                    else if ((double)this.startMinecartRidingCoordinate.getDistanceSquared(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0D)
                    {
                        this.addStat(AchievementList.onARail, 1);
                    }
                }
                else if (this.ridingEntity instanceof EntityBoat)
                {
                    this.addStat(StatList.distanceByBoatStat, var7);
                }
                else if (this.ridingEntity instanceof EntityPig)
                {
                    this.addStat(StatList.distanceByPigStat, var7);
                }
                else if (this.ridingEntity instanceof EntityHorse)
                {
                    this.addStat(StatList.field_151185_q, var7);
                }
            }
        }
    }

    protected void fall(float distance)
    {
        if (!this.capabilities.allowFlying)
        {
            if (distance >= 2.0F)
            {
                this.addStat(StatList.distanceFallenStat, (int)Math.round((double)distance * 100.0D));
            }

            super.fall(distance);
        }
    }

    protected String func_146067_o(int p_146067_1_)
    {
        return p_146067_1_ > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
    }

    public void onKillEntity(EntityLivingBase entityLivingIn)
    {
        if (entityLivingIn instanceof IMob)
        {
            this.triggerAchievement(AchievementList.killEnemy);
        }

        int var2 = EntityList.getEntityID(entityLivingIn);
        EntityList.EntityEggInfo var3 = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(var2));

        if (var3 != null)
        {
            this.addStat(var3.field_151512_d, 1);
        }
    }

    public void setInWeb()
    {
        if (!this.capabilities.isFlying)
        {
            super.setInWeb();
        }
    }

    public IIcon getItemIcon(ItemStack itemStackIn, int p_70620_2_)
    {
        IIcon var3 = super.getItemIcon(itemStackIn, p_70620_2_);

        if (itemStackIn.getItem() == Items.fishing_rod && this.fishEntity != null)
        {
            var3 = Items.fishing_rod.func_94597_g();
        }
        else
        {
            if (itemStackIn.getItem().requiresMultipleRenderPasses())
            {
                return itemStackIn.getItem().getIconFromDamageForRenderPass(itemStackIn.getMetadata(), p_70620_2_);
            }

            if (this.itemInUse != null && itemStackIn.getItem() == Items.bow)
            {
                int var4 = itemStackIn.getMaxItemUseDuration() - this.itemInUseCount;

                if (var4 >= 18)
                {
                    return Items.bow.getItemIconForUseDuration(2);
                }

                if (var4 > 13)
                {
                    return Items.bow.getItemIconForUseDuration(1);
                }

                if (var4 > 0)
                {
                    return Items.bow.getItemIconForUseDuration(0);
                }
            }
        }

        return var3;
    }

    public ItemStack getCurrentArmor(int p_82169_1_)
    {
        return this.inventory.armorItemInSlot(p_82169_1_);
    }

    public void addExperience(int p_71023_1_)
    {
        this.addScore(p_71023_1_);
        int var2 = Integer.MAX_VALUE - this.experienceTotal;

        if (p_71023_1_ > var2)
        {
            p_71023_1_ = var2;
        }

        this.experience += (float)p_71023_1_ / (float)this.xpBarCap();

        for (this.experienceTotal += p_71023_1_; this.experience >= 1.0F; this.experience /= (float)this.xpBarCap())
        {
            this.experience = (this.experience - 1.0F) * (float)this.xpBarCap();
            this.addExperienceLevel(1);
        }
    }

    public void addExperienceLevel(int p_82242_1_)
    {
        this.experienceLevel += p_82242_1_;

        if (this.experienceLevel < 0)
        {
            this.experienceLevel = 0;
            this.experience = 0.0F;
            this.experienceTotal = 0;
        }

        if (p_82242_1_ > 0 && this.experienceLevel % 5 == 0 && (float)this.field_82249_h < (float)this.ticksExisted - 100.0F)
        {
            float var2 = this.experienceLevel > 30 ? 1.0F : (float)this.experienceLevel / 30.0F;
            this.worldObj.playSoundAtEntity(this, "random.levelup", var2 * 0.75F, 1.0F);
            this.field_82249_h = this.ticksExisted;
        }
    }

    public int xpBarCap()
    {
        return this.experienceLevel >= 30 ? 62 + (this.experienceLevel - 30) * 7 : (this.experienceLevel >= 15 ? 17 + (this.experienceLevel - 15) * 3 : 17);
    }

    public void addExhaustion(float p_71020_1_)
    {
        if (!this.capabilities.disableDamage)
        {
            if (!this.worldObj.isRemote)
            {
                this.foodStats.addExhaustion(p_71020_1_);
            }
        }
    }

    public FoodStats getFoodStats()
    {
        return this.foodStats;
    }

    public boolean canEat(boolean p_71043_1_)
    {
        return (p_71043_1_ || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }

    public boolean shouldHeal()
    {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_)
    {
        if (p_71008_1_ != this.itemInUse)
        {
            this.itemInUse = p_71008_1_;
            this.itemInUseCount = p_71008_2_;

            if (!this.worldObj.isRemote)
            {
                this.setEating(true);
            }
        }
    }

    public boolean isCurrentToolAdventureModeExempt(int p_82246_1_, int p_82246_2_, int p_82246_3_)
    {
        if (this.capabilities.allowEdit)
        {
            return true;
        }
        else
        {
            Block var4 = this.worldObj.getBlock(p_82246_1_, p_82246_2_, p_82246_3_);

            if (var4.getMaterial() != Material.air)
            {
                if (var4.getMaterial().isAdventureModeExempt())
                {
                    return true;
                }

                if (this.getCurrentEquippedItem() != null)
                {
                    ItemStack var5 = this.getCurrentEquippedItem();

                    if (var5.canItemHarvestBlock(var4) || var5.getStrVsBlock(var4) > 1.0F)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean canPlayerEdit(int p_82247_1_, int p_82247_2_, int p_82247_3_, int p_82247_4_, ItemStack p_82247_5_)
    {
        return this.capabilities.allowEdit ? true : (p_82247_5_ != null ? p_82247_5_.canEditBlocks() : false);
    }

    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            return 0;
        }
        else
        {
            int var2 = this.experienceLevel * 7;
            return var2 > 100 ? 100 : var2;
        }
    }

    protected boolean isPlayer()
    {
        return true;
    }

    public boolean getAlwaysRenderNameTagForRender()
    {
        return true;
    }

    public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_)
    {
        if (p_71049_2_)
        {
            this.inventory.copyInventory(p_71049_1_.inventory);
            this.setHealth(p_71049_1_.getHealth());
            this.foodStats = p_71049_1_.foodStats;
            this.experienceLevel = p_71049_1_.experienceLevel;
            this.experienceTotal = p_71049_1_.experienceTotal;
            this.experience = p_71049_1_.experience;
            this.setScore(p_71049_1_.getScore());
            this.teleportDirection = p_71049_1_.teleportDirection;
        }
        else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            this.inventory.copyInventory(p_71049_1_.inventory);
            this.experienceLevel = p_71049_1_.experienceLevel;
            this.experienceTotal = p_71049_1_.experienceTotal;
            this.experience = p_71049_1_.experience;
            this.setScore(p_71049_1_.getScore());
        }

        this.theInventoryEnderChest = p_71049_1_.theInventoryEnderChest;
    }

    protected boolean canTriggerWalking()
    {
        return !this.capabilities.isFlying;
    }

    public void sendPlayerAbilities() {}

    public void setGameType(WorldSettings.GameType gameType) {}

    public String getCommandSenderName()
    {
        return this.gameProfile.getName();
    }

    public World getEntityWorld()
    {
        return this.worldObj;
    }

    public InventoryEnderChest getInventoryEnderChest()
    {
        return this.theInventoryEnderChest;
    }

    public ItemStack getEquipmentInSlot(int p_71124_1_)
    {
        return p_71124_1_ == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[p_71124_1_ - 1];
    }

    public ItemStack getHeldItem()
    {
        return this.inventory.getCurrentItem();
    }

    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn)
    {
        this.inventory.armorInventory[slotIn] = itemStackIn;
    }

    public boolean isInvisibleToPlayer(EntityPlayer player)
    {
        if (!this.isInvisible())
        {
            return false;
        }
        else
        {
            Team var2 = this.getTeam();
            return var2 == null || player == null || player.getTeam() != var2 || !var2.func_98297_h();
        }
    }

    public ItemStack[] getInventory()
    {
        return this.inventory.armorInventory;
    }

    public boolean getHideCape()
    {
        return this.getHideCape(1);
    }

    public boolean isPushedByWater()
    {
        return !this.capabilities.isFlying;
    }

    public Scoreboard getWorldScoreboard()
    {
        return this.worldObj.getScoreboard();
    }

    public Team getTeam()
    {
        return this.getWorldScoreboard().getPlayersTeam(this.getCommandSenderName());
    }

    public IChatComponent getFormattedCommandSenderName()
    {
        ChatComponentText var1 = new ChatComponentText(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getCommandSenderName()));
        var1.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getCommandSenderName() + " "));
        return var1;
    }

    public void setAbsorptionAmount(float p_110149_1_)
    {
        if (p_110149_1_ < 0.0F)
        {
            p_110149_1_ = 0.0F;
        }

        this.getDataWatcher().updateObject(17, Float.valueOf(p_110149_1_));
    }

    public float getAbsorptionAmount()
    {
        return this.getDataWatcher().getWatchableObjectFloat(17);
    }

    public static UUID getUUID(GameProfile p_146094_0_)
    {
        UUID var1 = p_146094_0_.getId();

        if (var1 == null)
        {
            var1 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_146094_0_.getName()).getBytes(Charsets.UTF_8));
        }

        return var1;
    }

    public static enum EnumChatVisibility
    {
        FULL("FULL", 0, 0, "options.chat.visibility.full"),
        SYSTEM("SYSTEM", 1, 1, "options.chat.visibility.system"),
        HIDDEN("HIDDEN", 2, 2, "options.chat.visibility.hidden");
        private static final EntityPlayer.EnumChatVisibility[] field_151432_d = new EntityPlayer.EnumChatVisibility[values().length];
        private final int chatVisibility;
        private final String resourceKey;

        private static final EntityPlayer.EnumChatVisibility[] $VALUES = new EntityPlayer.EnumChatVisibility[]{FULL, SYSTEM, HIDDEN};
        private static final String __OBFID = "CL_00001714";

        private EnumChatVisibility(String p_i45323_1_, int p_i45323_2_, int p_i45323_3_, String p_i45323_4_)
        {
            this.chatVisibility = p_i45323_3_;
            this.resourceKey = p_i45323_4_;
        }

        public int getChatVisibility()
        {
            return this.chatVisibility;
        }

        public static EntityPlayer.EnumChatVisibility getEnumChatVisibility(int p_151426_0_)
        {
            return field_151432_d[p_151426_0_ % field_151432_d.length];
        }

        public String getResourceKey()
        {
            return this.resourceKey;
        }

        static {
            EntityPlayer.EnumChatVisibility[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                EntityPlayer.EnumChatVisibility var3 = var0[var2];
                field_151432_d[var3.chatVisibility] = var3;
            }
        }
    }

    public static enum EnumStatus
    {
        OK("OK", 0),
        NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1),
        NOT_POSSIBLE_NOW("NOT_POSSIBLE_NOW", 2),
        TOO_FAR_AWAY("TOO_FAR_AWAY", 3),
        OTHER_PROBLEM("OTHER_PROBLEM", 4),
        NOT_SAFE("NOT_SAFE", 5);

        private static final EntityPlayer.EnumStatus[] $VALUES = new EntityPlayer.EnumStatus[]{OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE};
        private static final String __OBFID = "CL_00001712";

        private EnumStatus(String p_i1751_1_, int p_i1751_2_) {}
    }
}
