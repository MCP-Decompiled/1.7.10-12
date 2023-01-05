package net.minecraft.entity.item;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity
{
    private Block blockObj;
    public int metadata;
    public int fallTime;
    public boolean shouldDropItem;
    private boolean field_145808_f;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NBTTagCompound tileEntityData;
    private static final String __OBFID = "CL_00001668";

    public EntityFallingBlock(World p_i1706_1_)
    {
        super(p_i1706_1_);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
    }

    public EntityFallingBlock(World p_i45318_1_, double p_i45318_2_, double p_i45318_4_, double p_i45318_6_, Block p_i45318_8_)
    {
        this(p_i45318_1_, p_i45318_2_, p_i45318_4_, p_i45318_6_, p_i45318_8_, 0);
    }

    public EntityFallingBlock(World p_i45319_1_, double p_i45319_2_, double p_i45319_4_, double p_i45319_6_, Block p_i45319_8_, int p_i45319_9_)
    {
        super(p_i45319_1_);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
        this.blockObj = p_i45319_8_;
        this.metadata = p_i45319_9_;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i45319_2_;
        this.prevPosY = p_i45319_4_;
        this.prevPosZ = p_i45319_6_;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void onUpdate()
    {
        if (this.blockObj.getMaterial() == Material.air)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.fallTime;
            this.motionY -= 0.03999999910593033D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;

            if (!this.worldObj.isRemote)
            {
                int var1 = MathHelper.floor_double(this.posX);
                int var2 = MathHelper.floor_double(this.posY);
                int var3 = MathHelper.floor_double(this.posZ);

                if (this.fallTime == 1)
                {
                    if (this.worldObj.getBlock(var1, var2, var3) != this.blockObj)
                    {
                        this.setDead();
                        return;
                    }

                    this.worldObj.setBlockToAir(var1, var2, var3);
                }

                if (this.onGround)
                {
                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlock(var1, var2, var3) != Blocks.piston_extension)
                    {
                        this.setDead();

                        if (!this.field_145808_f && this.worldObj.canPlaceEntityOnSide(this.blockObj, var1, var2, var3, true, 1, (Entity)null, (ItemStack)null) && !BlockFalling.canFallBelow(this.worldObj, var1, var2 - 1, var3) && this.worldObj.setBlock(var1, var2, var3, this.blockObj, this.metadata, 3))
                        {
                            if (this.blockObj instanceof BlockFalling)
                            {
                                ((BlockFalling)this.blockObj).playSoundWhenFallen(this.worldObj, var1, var2, var3, this.metadata);
                            }

                            if (this.tileEntityData != null && this.blockObj instanceof ITileEntityProvider)
                            {
                                TileEntity var4 = this.worldObj.getTileEntity(var1, var2, var3);

                                if (var4 != null)
                                {
                                    NBTTagCompound var5 = new NBTTagCompound();
                                    var4.writeToNBT(var5);
                                    Iterator var6 = this.tileEntityData.getKeySet().iterator();

                                    while (var6.hasNext())
                                    {
                                        String var7 = (String)var6.next();
                                        NBTBase var8 = this.tileEntityData.getTag(var7);

                                        if (!var7.equals("x") && !var7.equals("y") && !var7.equals("z"))
                                        {
                                            var5.setTag(var7, var8.copy());
                                        }
                                    }

                                    var4.readFromNBT(var5);
                                    var4.markDirty();
                                }
                            }
                        }
                        else if (this.shouldDropItem && !this.field_145808_f)
                        {
                            this.entityDropItem(new ItemStack(this.blockObj, 1, this.blockObj.damageDropped(this.metadata)), 0.0F);
                        }
                    }
                }
                else if (this.fallTime > 100 && !this.worldObj.isRemote && (var2 < 1 || var2 > 256) || this.fallTime > 600)
                {
                    if (this.shouldDropItem)
                    {
                        this.entityDropItem(new ItemStack(this.blockObj, 1, this.blockObj.damageDropped(this.metadata)), 0.0F);
                    }

                    this.setDead();
                }
            }
        }
    }

    protected void fall(float distance)
    {
        if (this.hurtEntities)
        {
            int var2 = MathHelper.ceiling_float_int(distance - 1.0F);

            if (var2 > 0)
            {
                ArrayList var3 = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
                boolean var4 = this.blockObj == Blocks.anvil;
                DamageSource var5 = var4 ? DamageSource.anvil : DamageSource.fallingBlock;
                Iterator var6 = var3.iterator();

                while (var6.hasNext())
                {
                    Entity var7 = (Entity)var6.next();
                    var7.attackEntityFrom(var5, (float)Math.min(MathHelper.floor_float((float)var2 * this.fallHurtAmount), this.fallHurtMax));
                }

                if (var4 && (double)this.rand.nextFloat() < 0.05000000074505806D + (double)var2 * 0.05D)
                {
                    int var8 = this.metadata >> 2;
                    int var9 = this.metadata & 3;
                    ++var8;

                    if (var8 > 2)
                    {
                        this.field_145808_f = true;
                    }
                    else
                    {
                        this.metadata = var9 | var8 << 2;
                    }
                }
            }
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setByte("Tile", (byte)Block.getIdFromBlock(this.blockObj));
        tagCompound.setInteger("TileID", Block.getIdFromBlock(this.blockObj));
        tagCompound.setByte("Data", (byte)this.metadata);
        tagCompound.setByte("Time", (byte)this.fallTime);
        tagCompound.setBoolean("DropItem", this.shouldDropItem);
        tagCompound.setBoolean("HurtEntities", this.hurtEntities);
        tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        tagCompound.setInteger("FallHurtMax", this.fallHurtMax);

        if (this.tileEntityData != null)
        {
            tagCompound.setTag("TileEntityData", this.tileEntityData);
        }
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        if (tagCompund.hasKey("TileID", 99))
        {
            this.blockObj = Block.getBlockById(tagCompund.getInteger("TileID"));
        }
        else
        {
            this.blockObj = Block.getBlockById(tagCompund.getByte("Tile") & 255);
        }

        this.metadata = tagCompund.getByte("Data") & 255;
        this.fallTime = tagCompund.getByte("Time") & 255;

        if (tagCompund.hasKey("HurtEntities", 99))
        {
            this.hurtEntities = tagCompund.getBoolean("HurtEntities");
            this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
            this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
        }
        else if (this.blockObj == Blocks.anvil)
        {
            this.hurtEntities = true;
        }

        if (tagCompund.hasKey("DropItem", 99))
        {
            this.shouldDropItem = tagCompund.getBoolean("DropItem");
        }

        if (tagCompund.hasKey("TileEntityData", 10))
        {
            this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
        }

        if (this.blockObj.getMaterial() == Material.air)
        {
            this.blockObj = Blocks.sand;
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public World getWorldObj()
    {
        return this.worldObj;
    }

    public void setHurtEntities(boolean p_145806_1_)
    {
        this.hurtEntities = p_145806_1_;
    }

    public boolean canRenderOnFire()
    {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category)
    {
        super.addEntityCrashInfo(category);
        category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(this.blockObj)));
        category.addCrashSection("Immitating block data", Integer.valueOf(this.metadata));
    }

    public Block getBlock()
    {
        return this.blockObj;
    }
}
