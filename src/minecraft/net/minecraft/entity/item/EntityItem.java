package net.minecraft.entity.item;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity
{
    private static final Logger logger = LogManager.getLogger();
    public int age;
    public int delayBeforeCanPickup;
    private int health;
    private String field_145801_f;
    private String field_145802_g;
    public float hoverStart;
    private static final String __OBFID = "CL_00001669";

    public EntityItem(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    public EntityItem(World worldIn, double x, double y, double z, ItemStack stack)
    {
        this(worldIn, x, y, z);
        this.setEntityItemStack(stack);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityItem(World p_i1711_1_)
    {
        super(p_i1711_1_);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }

    protected void entityInit()
    {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }

    public void onUpdate()
    {
        if (this.getEntityItem() == null)
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();

            if (this.delayBeforeCanPickup > 0)
            {
                --this.delayBeforeCanPickup;
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            boolean var1 = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;

            if (var1 || this.ticksExisted % 25 == 0)
            {
                if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava)
                {
                    this.motionY = 0.20000000298023224D;
                    this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }

                if (!this.worldObj.isRemote)
                {
                    this.searchForOtherItemsNearby();
                }
            }

            float var2 = 0.98F;

            if (this.onGround)
            {
                var2 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.98F;
            }

            this.motionX *= (double)var2;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double)var2;

            if (this.onGround)
            {
                this.motionY *= -0.5D;
            }

            ++this.age;

            if (!this.worldObj.isRemote && this.age >= 6000)
            {
                this.setDead();
            }
        }
    }

    private void searchForOtherItemsNearby()
    {
        Iterator var1 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();

        while (var1.hasNext())
        {
            EntityItem var2 = (EntityItem)var1.next();
            this.combineItems(var2);
        }
    }

    public boolean combineItems(EntityItem p_70289_1_)
    {
        if (p_70289_1_ == this)
        {
            return false;
        }
        else if (p_70289_1_.isEntityAlive() && this.isEntityAlive())
        {
            ItemStack var2 = this.getEntityItem();
            ItemStack var3 = p_70289_1_.getEntityItem();

            if (var3.getItem() != var2.getItem())
            {
                return false;
            }
            else if (var3.hasTagCompound() ^ var2.hasTagCompound())
            {
                return false;
            }
            else if (var3.hasTagCompound() && !var3.getTagCompound().equals(var2.getTagCompound()))
            {
                return false;
            }
            else if (var3.getItem() == null)
            {
                return false;
            }
            else if (var3.getItem().getHasSubtypes() && var3.getMetadata() != var2.getMetadata())
            {
                return false;
            }
            else if (var3.stackSize < var2.stackSize)
            {
                return p_70289_1_.combineItems(this);
            }
            else if (var3.stackSize + var2.stackSize > var3.getMaxStackSize())
            {
                return false;
            }
            else
            {
                var3.stackSize += var2.stackSize;
                p_70289_1_.delayBeforeCanPickup = Math.max(p_70289_1_.delayBeforeCanPickup, this.delayBeforeCanPickup);
                p_70289_1_.age = Math.min(p_70289_1_.age, this.age);
                p_70289_1_.setEntityItemStack(var3);
                this.setDead();
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public void setAgeToCreativeDespawnTime()
    {
        this.age = 4800;
    }

    public boolean handleWaterMovement()
    {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
    }

    protected void dealFireDamage(int amount)
    {
        this.attackEntityFrom(DamageSource.inFire, (float)amount);
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (this.getEntityItem() != null && this.getEntityItem().getItem() == Items.nether_star && source.isExplosion())
        {
            return false;
        }
        else
        {
            this.setBeenAttacked();
            this.health = (int)((float)this.health - amount);

            if (this.health <= 0)
            {
                this.setDead();
            }

            return false;
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setShort("Health", (short)((byte)this.health));
        tagCompound.setShort("Age", (short)this.age);

        if (this.getThrower() != null)
        {
            tagCompound.setString("Thrower", this.field_145801_f);
        }

        if (this.getOwner() != null)
        {
            tagCompound.setString("Owner", this.field_145802_g);
        }

        if (this.getEntityItem() != null)
        {
            tagCompound.setTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        this.health = tagCompund.getShort("Health") & 255;
        this.age = tagCompund.getShort("Age");

        if (tagCompund.hasKey("Owner"))
        {
            this.field_145802_g = tagCompund.getString("Owner");
        }

        if (tagCompund.hasKey("Thrower"))
        {
            this.field_145801_f = tagCompund.getString("Thrower");
        }

        NBTTagCompound var2 = tagCompund.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(var2));

        if (this.getEntityItem() == null)
        {
            this.setDead();
        }
    }

    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.worldObj.isRemote)
        {
            ItemStack var2 = this.getEntityItem();
            int var3 = var2.stackSize;

            if (this.delayBeforeCanPickup == 0 && (this.field_145802_g == null || 6000 - this.age <= 200 || this.field_145802_g.equals(entityIn.getCommandSenderName())) && entityIn.inventory.addItemStackToInventory(var2))
            {
                if (var2.getItem() == Item.getItemFromBlock(Blocks.log))
                {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }

                if (var2.getItem() == Item.getItemFromBlock(Blocks.log2))
                {
                    entityIn.triggerAchievement(AchievementList.mineWood);
                }

                if (var2.getItem() == Items.leather)
                {
                    entityIn.triggerAchievement(AchievementList.killCow);
                }

                if (var2.getItem() == Items.diamond)
                {
                    entityIn.triggerAchievement(AchievementList.diamonds);
                }

                if (var2.getItem() == Items.blaze_rod)
                {
                    entityIn.triggerAchievement(AchievementList.blazeRod);
                }

                if (var2.getItem() == Items.diamond && this.getThrower() != null)
                {
                    EntityPlayer var4 = this.worldObj.getPlayerEntityByName(this.getThrower());

                    if (var4 != null && var4 != entityIn)
                    {
                        var4.triggerAchievement(AchievementList.field_150966_x);
                    }
                }

                this.worldObj.playSoundAtEntity(entityIn, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityIn.onItemPickup(this, var3);

                if (var2.stackSize <= 0)
                {
                    this.setDead();
                }
            }
        }
    }

    public String getCommandSenderName()
    {
        return StatCollector.translateToLocal("item." + this.getEntityItem().getUnlocalizedName());
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    public void travelToDimension(int dimensionId)
    {
        super.travelToDimension(dimensionId);

        if (!this.worldObj.isRemote)
        {
            this.searchForOtherItemsNearby();
        }
    }

    public ItemStack getEntityItem()
    {
        ItemStack var1 = this.getDataWatcher().getWatchableObjectItemStack(10);
        return var1 == null ? new ItemStack(Blocks.stone) : var1;
    }

    public void setEntityItemStack(ItemStack p_92058_1_)
    {
        this.getDataWatcher().updateObject(10, p_92058_1_);
        this.getDataWatcher().setObjectWatched(10);
    }

    public String getOwner()
    {
        return this.field_145802_g;
    }

    public void setOwner(String p_145797_1_)
    {
        this.field_145802_g = p_145797_1_;
    }

    public String getThrower()
    {
        return this.field_145801_f;
    }

    public void setThrower(String p_145799_1_)
    {
        this.field_145801_f = p_145799_1_;
    }
}
