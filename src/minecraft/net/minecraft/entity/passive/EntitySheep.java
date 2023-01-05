package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySheep extends EntityAnimal
{
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container()
    {
        private static final String __OBFID = "CL_00001649";
        public boolean canInteractWith(EntityPlayer player)
        {
            return false;
        }
    }, 2, 1);
    public static final float[][] fleeceColorTable = new float[][] {{1.0F, 1.0F, 1.0F}, {0.85F, 0.5F, 0.2F}, {0.7F, 0.3F, 0.85F}, {0.4F, 0.6F, 0.85F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.5F, 0.65F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.5F, 0.6F}, {0.5F, 0.25F, 0.7F}, {0.2F, 0.3F, 0.7F}, {0.4F, 0.3F, 0.2F}, {0.4F, 0.5F, 0.2F}, {0.6F, 0.2F, 0.2F}, {0.1F, 0.1F, 0.1F}};
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);
    private static final String __OBFID = "CL_00001648";

    public EntitySheep(World p_i1691_1_)
    {
        super(p_i1691_1_);
        this.setSize(0.9F, 1.3F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.wheat, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.dye, 1, 0));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.dye, 1, 0));
    }

    protected boolean isAIEnabled()
    {
        return true;
    }

    protected void updateAITasks()
    {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    public void onLivingUpdate()
    {
        if (this.worldObj.isRemote)
        {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        if (!this.getSheared())
        {
            this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor()), 0.0F);
        }
    }

    protected Item getDropItem()
    {
        return Item.getItemFromBlock(Blocks.wool);
    }

    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 10)
        {
            this.sheepTimer = 40;
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public float getHeadRotationPointY(float p_70894_1_)
    {
        return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0F : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0F));
    }

    public float getHeadRotationAngleX(float p_70890_1_)
    {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36)
        {
            float var2 = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(var2 * 28.7F);
        }
        else
        {
            return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
        }
    }

    public boolean interact(EntityPlayer p_70085_1_)
    {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.shears && !this.getSheared() && !this.isChild())
        {
            if (!this.worldObj.isRemote)
            {
                this.setSheared(true);
                int var3 = 1 + this.rand.nextInt(3);

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    EntityItem var5 = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, this.getFleeceColor()), 1.0F);
                    var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
                    var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                }
            }

            var2.damageItem(1, p_70085_1_);
            this.playSound("mob.sheep.shear", 1.0F, 1.0F);
        }

        return super.interact(p_70085_1_);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Sheared", this.getSheared());
        tagCompound.setByte("Color", (byte)this.getFleeceColor());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setSheared(tagCompund.getBoolean("Sheared"));
        this.setFleeceColor(tagCompund.getByte("Color"));
    }

    protected String getLivingSound()
    {
        return "mob.sheep.say";
    }

    protected String getHurtSound()
    {
        return "mob.sheep.say";
    }

    protected String getDeathSound()
    {
        return "mob.sheep.say";
    }

    protected void playStepSound(int x, int y, int z, Block blockIn)
    {
        this.playSound("mob.sheep.step", 0.15F, 1.0F);
    }

    public int getFleeceColor()
    {
        return this.dataWatcher.getWatchableObjectByte(16) & 15;
    }

    public void setFleeceColor(int p_70891_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & 240 | p_70891_1_ & 15)));
    }

    public boolean getSheared()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
    }

    public void setSheared(boolean p_70893_1_)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70893_1_)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 16)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -17)));
        }
    }

    public static int getRandomFleeceColor(Random p_70895_0_)
    {
        int var1 = p_70895_0_.nextInt(100);
        return var1 < 5 ? 15 : (var1 < 10 ? 7 : (var1 < 15 ? 8 : (var1 < 18 ? 12 : (p_70895_0_.nextInt(500) == 0 ? 6 : 0))));
    }

    public EntitySheep createChild(EntityAgeable p_90011_1_)
    {
        EntitySheep var2 = (EntitySheep)p_90011_1_;
        EntitySheep var3 = new EntitySheep(this.worldObj);
        int var4 = this.getDyeBasedOnParents(this, var2);
        var3.setFleeceColor(15 - var4);
        return var3;
    }

    public void eatGrassBonus()
    {
        this.setSheared(false);

        if (this.isChild())
        {
            this.addGrowth(60);
        }
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
        this.setFleeceColor(getRandomFleeceColor(this.worldObj.rand));
        return p_110161_1_;
    }

    private int getDyeBasedOnParents(EntityAnimal p_90014_1_, EntityAnimal p_90014_2_)
    {
        int var3 = this.getDyeFromFleeceColor(p_90014_1_);
        int var4 = this.getDyeFromFleeceColor(p_90014_2_);
        this.inventoryCrafting.getStackInSlot(0).setMetadata(var3);
        this.inventoryCrafting.getStackInSlot(1).setMetadata(var4);
        ItemStack var5 = CraftingManager.getInstance().findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)p_90014_1_).worldObj);
        int var6;

        if (var5 != null && var5.getItem() == Items.dye)
        {
            var6 = var5.getMetadata();
        }
        else
        {
            var6 = this.worldObj.rand.nextBoolean() ? var3 : var4;
        }

        return var6;
    }

    private int getDyeFromFleeceColor(EntityAnimal p_90013_1_)
    {
        return 15 - ((EntitySheep)p_90013_1_).getFleeceColor();
    }
}
