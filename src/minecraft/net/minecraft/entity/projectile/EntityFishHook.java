package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook extends Entity
{
    private static final List JUNK = Arrays.asList(new WeightedRandomFishable[] {(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10)});
    private static final List VALUABLES = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).setMaxDamagePercent(0.25F).setEnchantable(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).setMaxDamagePercent(0.25F).setEnchantable(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).setEnchantable()});
    private static final List FISH = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getItemDamage()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getItemDamage()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getItemDamage()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getItemDamage()), 13)});
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public int shake;
    public EntityPlayer angler;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private int fishPosRotationIncrements;
    private double fishX;
    private double fishY;
    private double fishZ;
    private double fishYaw;
    private double fishPitch;
    private double clientMotionX;
    private double clientMotionY;
    private double clientMotionZ;
    private static final String __OBFID = "CL_00001663";

    public EntityFishHook(World p_i1764_1_)
    {
        super(p_i1764_1_);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(0.25F, 0.25F);
        this.ignoreFrustumCheck = true;
    }

    public EntityFishHook(World p_i1765_1_, double p_i1765_2_, double p_i1765_4_, double p_i1765_6_, EntityPlayer p_i1765_8_)
    {
        this(p_i1765_1_);
        this.setPosition(p_i1765_2_, p_i1765_4_, p_i1765_6_);
        this.ignoreFrustumCheck = true;
        this.angler = p_i1765_8_;
        p_i1765_8_.fishEntity = this;
    }

    public EntityFishHook(World p_i1766_1_, EntityPlayer p_i1766_2_)
    {
        super(p_i1766_1_);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.ignoreFrustumCheck = true;
        this.angler = p_i1766_2_;
        this.angler.fishEntity = this;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(p_i1766_2_.posX, p_i1766_2_.posY + 1.62D - (double)p_i1766_2_.yOffset, p_i1766_2_.posZ, p_i1766_2_.rotationYaw, p_i1766_2_.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float var3 = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
        this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    protected void entityInit() {}

    public boolean isInRangeToRenderDist(double distance)
    {
        double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return distance < var3 * var3;
    }

    public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_)
    {
        float var9 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
        p_146035_1_ /= (double)var9;
        p_146035_3_ /= (double)var9;
        p_146035_5_ /= (double)var9;
        p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_1_ *= (double)p_146035_7_;
        p_146035_3_ *= (double)p_146035_7_;
        p_146035_5_ *= (double)p_146035_7_;
        this.motionX = p_146035_1_;
        this.motionY = p_146035_3_;
        this.motionZ = p_146035_5_;
        float var10 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_146035_1_, p_146035_5_) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_146035_3_, (double)var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int rotationIncrements)
    {
        this.fishX = x;
        this.fishY = y;
        this.fishZ = z;
        this.fishYaw = (double)yaw;
        this.fishPitch = (double)pitch;
        this.fishPosRotationIncrements = rotationIncrements;
        this.motionX = this.clientMotionX;
        this.motionY = this.clientMotionY;
        this.motionZ = this.clientMotionZ;
    }

    public void setVelocity(double x, double y, double z)
    {
        this.clientMotionX = this.motionX = x;
        this.clientMotionY = this.motionY = y;
        this.clientMotionZ = this.motionZ = z;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.fishPosRotationIncrements > 0)
        {
            double var27 = this.posX + (this.fishX - this.posX) / (double)this.fishPosRotationIncrements;
            double var28 = this.posY + (this.fishY - this.posY) / (double)this.fishPosRotationIncrements;
            double var29 = this.posZ + (this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements;
            double var7 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.fishPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(var27, var28, var29);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
            if (!this.worldObj.isRemote)
            {
                ItemStack var1 = this.angler.getCurrentEquippedItem();

                if (this.angler.isDead || !this.angler.isEntityAlive() || var1 == null || var1.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.angler) > 1024.0D)
                {
                    this.setDead();
                    this.angler.fishEntity = null;
                    return;
                }

                if (this.caughtEntity != null)
                {
                    if (!this.caughtEntity.isDead)
                    {
                        this.posX = this.caughtEntity.posX;
                        this.posY = this.caughtEntity.boundingBox.minY + (double)this.caughtEntity.height * 0.8D;
                        this.posZ = this.caughtEntity.posZ;
                        return;
                    }

                    this.caughtEntity = null;
                }
            }

            if (this.shake > 0)
            {
                --this.shake;
            }

            if (this.inGround)
            {
                if (this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.inTile)
                {
                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else
            {
                ++this.ticksInAir;
            }

            Vec3 var26 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var26, var2);
            var26 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var3 != null)
            {
                var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
            }

            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;
            double var13;

            for (int var8 = 0; var8 < var5.size(); ++var8)
            {
                Entity var9 = (Entity)var5.get(var8);

                if (var9.canBeCollidedWith() && (var9 != this.angler || this.ticksInAir >= 5))
                {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
                    MovingObjectPosition var12 = var11.calculateIntercept(var26, var2);

                    if (var12 != null)
                    {
                        var13 = var26.distanceTo(var12.hitVec);

                        if (var13 < var6 || var6 == 0.0D)
                        {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            if (var4 != null)
            {
                var3 = new MovingObjectPosition(var4);
            }

            if (var3 != null)
            {
                if (var3.entityHit != null)
                {
                    if (var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0F))
                    {
                        this.caughtEntity = var3.entityHit;
                    }
                }
                else
                {
                    this.inGround = true;
                }
            }

            if (!this.inGround)
            {
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float var30 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

                for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var30) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
                {
                    ;
                }

                while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
                {
                    this.prevRotationPitch += 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw < -180.0F)
                {
                    this.prevRotationYaw -= 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
                {
                    this.prevRotationYaw += 360.0F;
                }

                this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
                this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
                float var31 = 0.92F;

                if (this.onGround || this.isCollidedHorizontally)
                {
                    var31 = 0.5F;
                }

                byte var32 = 5;
                double var33 = 0.0D;

                for (int var34 = 0; var34 < var32; ++var34)
                {
                    double var14 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var34 + 0) / (double)var32 - 0.125D + 0.125D;
                    double var16 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var34 + 1) / (double)var32 - 0.125D + 0.125D;
                    AxisAlignedBB var18 = AxisAlignedBB.getBoundingBox(this.boundingBox.minX, var14, this.boundingBox.minZ, this.boundingBox.maxX, var16, this.boundingBox.maxZ);

                    if (this.worldObj.isAABBInMaterial(var18, Material.water))
                    {
                        var33 += 1.0D / (double)var32;
                    }
                }

                if (!this.worldObj.isRemote && var33 > 0.0D)
                {
                    WorldServer var35 = (WorldServer)this.worldObj;
                    int var36 = 1;

                    if (this.rand.nextFloat() < 0.25F && this.worldObj.isRainingAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ)))
                    {
                        var36 = 2;
                    }

                    if (this.rand.nextFloat() < 0.5F && !this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ)))
                    {
                        --var36;
                    }

                    if (this.ticksCatchable > 0)
                    {
                        --this.ticksCatchable;

                        if (this.ticksCatchable <= 0)
                        {
                            this.ticksCaughtDelay = 0;
                            this.ticksCatchableDelay = 0;
                        }
                    }
                    else
                    {
                        float var15;
                        float var17;
                        double var20;
                        double var22;
                        float var37;
                        double var38;

                        if (this.ticksCatchableDelay > 0)
                        {
                            this.ticksCatchableDelay -= var36;

                            if (this.ticksCatchableDelay <= 0)
                            {
                                this.motionY -= 0.20000000298023224D;
                                this.playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                                var15 = (float)MathHelper.floor_double(this.boundingBox.minY);
                                var35.func_147487_a("bubble", this.posX, (double)(var15 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                var35.func_147487_a("wake", this.posX, (double)(var15 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                            }
                            else
                            {
                                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                                var15 = this.fishApproachAngle * 0.017453292F;
                                var37 = MathHelper.sin(var15);
                                var17 = MathHelper.cos(var15);
                                var38 = this.posX + (double)(var37 * (float)this.ticksCatchableDelay * 0.1F);
                                var20 = (double)((float)MathHelper.floor_double(this.boundingBox.minY) + 1.0F);
                                var22 = this.posZ + (double)(var17 * (float)this.ticksCatchableDelay * 0.1F);

                                if (this.rand.nextFloat() < 0.15F)
                                {
                                    var35.func_147487_a("bubble", var38, var20 - 0.10000000149011612D, var22, 1, (double)var37, 0.1D, (double)var17, 0.0D);
                                }

                                float var24 = var37 * 0.04F;
                                float var25 = var17 * 0.04F;
                                var35.func_147487_a("wake", var38, var20, var22, 0, (double)var25, 0.01D, (double)(-var24), 1.0D);
                                var35.func_147487_a("wake", var38, var20, var22, 0, (double)(-var25), 0.01D, (double)var24, 1.0D);
                            }
                        }
                        else if (this.ticksCaughtDelay > 0)
                        {
                            this.ticksCaughtDelay -= var36;
                            var15 = 0.15F;

                            if (this.ticksCaughtDelay < 20)
                            {
                                var15 = (float)((double)var15 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
                            }
                            else if (this.ticksCaughtDelay < 40)
                            {
                                var15 = (float)((double)var15 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
                            }
                            else if (this.ticksCaughtDelay < 60)
                            {
                                var15 = (float)((double)var15 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
                            }

                            if (this.rand.nextFloat() < var15)
                            {
                                var37 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
                                var17 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
                                var38 = this.posX + (double)(MathHelper.sin(var37) * var17 * 0.1F);
                                var20 = (double)((float)MathHelper.floor_double(this.boundingBox.minY) + 1.0F);
                                var22 = this.posZ + (double)(MathHelper.cos(var37) * var17 * 0.1F);
                                var35.func_147487_a("splash", var38, var20, var22, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                            }

                            if (this.ticksCaughtDelay <= 0)
                            {
                                this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
                                this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                            }
                        }
                        else
                        {
                            this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                            this.ticksCaughtDelay -= EnchantmentHelper.func_151387_h(this.angler) * 20 * 5;
                        }
                    }

                    if (this.ticksCatchable > 0)
                    {
                        this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
                    }
                }

                var13 = var33 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * var13;

                if (var33 > 0.0D)
                {
                    var31 = (float)((double)var31 * 0.9D);
                    this.motionY *= 0.8D;
                }

                this.motionX *= (double)var31;
                this.motionY *= (double)var31;
                this.motionZ *= (double)var31;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        tagCompound.setByte("inTile", (byte)Block.getIdFromBlock(this.inTile));
        tagCompound.setByte("shake", (byte)this.shake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        this.shake = tagCompund.getByte("shake") & 255;
        this.inGround = tagCompund.getByte("inGround") == 1;
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public int handleHookRetraction()
    {
        if (this.worldObj.isRemote)
        {
            return 0;
        }
        else
        {
            byte var1 = 0;

            if (this.caughtEntity != null)
            {
                double var2 = this.angler.posX - this.posX;
                double var4 = this.angler.posY - this.posY;
                double var6 = this.angler.posZ - this.posZ;
                double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
                double var10 = 0.1D;
                this.caughtEntity.motionX += var2 * var10;
                this.caughtEntity.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08D;
                this.caughtEntity.motionZ += var6 * var10;
                var1 = 3;
            }
            else if (this.ticksCatchable > 0)
            {
                EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.func_146033_f());
                double var3 = this.angler.posX - this.posX;
                double var5 = this.angler.posY - this.posY;
                double var7 = this.angler.posZ - this.posZ;
                double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
                double var11 = 0.1D;
                var13.motionX = var3 * var11;
                var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08D;
                var13.motionZ = var7 * var11;
                this.worldObj.spawnEntityInWorld(var13);
                this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
                var1 = 1;
            }

            if (this.inGround)
            {
                var1 = 2;
            }

            this.setDead();
            this.angler.fishEntity = null;
            return var1;
        }
    }

    private ItemStack func_146033_f()
    {
        float var1 = this.worldObj.rand.nextFloat();
        int var2 = EnchantmentHelper.func_151386_g(this.angler);
        int var3 = EnchantmentHelper.func_151387_h(this.angler);
        float var4 = 0.1F - (float)var2 * 0.025F - (float)var3 * 0.01F;
        float var5 = 0.05F + (float)var2 * 0.01F - (float)var3 * 0.01F;
        var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
        var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);

        if (var1 < var4)
        {
            this.angler.addStat(StatList.field_151183_A, 1);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).getItemStack(this.rand);
        }
        else
        {
            var1 -= var4;

            if (var1 < var5)
            {
                this.angler.addStat(StatList.field_151184_B, 1);
                return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, VALUABLES)).getItemStack(this.rand);
            }
            else
            {
                float var10000 = var1 - var5;
                this.angler.addStat(StatList.fishCaughtStat, 1);
                return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, FISH)).getItemStack(this.rand);
            }
        }
    }

    public void setDead()
    {
        super.setDead();

        if (this.angler != null)
        {
            this.angler.fishEntity = null;
        }
    }
}
