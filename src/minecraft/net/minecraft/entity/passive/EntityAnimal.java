package net.minecraft.entity.passive;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals
{
    private int inLove;
    private int breeding;
    private EntityPlayer playerInLove;
    private static final String __OBFID = "CL_00001638";

    public EntityAnimal(World p_i1681_1_)
    {
        super(p_i1681_1_);
    }

    protected void updateAITick()
    {
        if (this.getGrowingAge() != 0)
        {
            this.inLove = 0;
        }

        super.updateAITick();
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.getGrowingAge() != 0)
        {
            this.inLove = 0;
        }

        if (this.inLove > 0)
        {
            --this.inLove;
            String var1 = "heart";

            if (this.inLove % 10 == 0)
            {
                double var2 = this.rand.nextGaussian() * 0.02D;
                double var4 = this.rand.nextGaussian() * 0.02D;
                double var6 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(var1, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var2, var4, var6);
            }
        }
        else
        {
            this.breeding = 0;
        }
    }

    protected void attackEntity(Entity p_70785_1_, float p_70785_2_)
    {
        if (p_70785_1_ instanceof EntityPlayer)
        {
            if (p_70785_2_ < 3.0F)
            {
                double var3 = p_70785_1_.posX - this.posX;
                double var5 = p_70785_1_.posZ - this.posZ;
                this.rotationYaw = (float)(Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
                this.hasAttacked = true;
            }

            EntityPlayer var7 = (EntityPlayer)p_70785_1_;

            if (var7.getCurrentEquippedItem() == null || !this.isBreedingItem(var7.getCurrentEquippedItem()))
            {
                this.entityToAttack = null;
            }
        }
        else if (p_70785_1_ instanceof EntityAnimal)
        {
            EntityAnimal var8 = (EntityAnimal)p_70785_1_;

            if (this.getGrowingAge() > 0 && var8.getGrowingAge() < 0)
            {
                if ((double)p_70785_2_ < 2.5D)
                {
                    this.hasAttacked = true;
                }
            }
            else if (this.inLove > 0 && var8.inLove > 0)
            {
                if (var8.entityToAttack == null)
                {
                    var8.entityToAttack = this;
                }

                if (var8.entityToAttack == this && (double)p_70785_2_ < 3.5D)
                {
                    ++var8.inLove;
                    ++this.inLove;
                    ++this.breeding;

                    if (this.breeding % 4 == 0)
                    {
                        this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D);
                    }

                    if (this.breeding == 60)
                    {
                        this.procreate((EntityAnimal)p_70785_1_);
                    }
                }
                else
                {
                    this.breeding = 0;
                }
            }
            else
            {
                this.breeding = 0;
                this.entityToAttack = null;
            }
        }
    }

    private void procreate(EntityAnimal p_70876_1_)
    {
        EntityAgeable var2 = this.createChild(p_70876_1_);

        if (var2 != null)
        {
            if (this.playerInLove == null && p_70876_1_.func_146083_cb() != null)
            {
                this.playerInLove = p_70876_1_.func_146083_cb();
            }

            if (this.playerInLove != null)
            {
                this.playerInLove.triggerAchievement(StatList.animalsBredStat);

                if (this instanceof EntityCow)
                {
                    this.playerInLove.triggerAchievement(AchievementList.breedCow);
                }
            }

            this.setGrowingAge(6000);
            p_70876_1_.setGrowingAge(6000);
            this.inLove = 0;
            this.breeding = 0;
            this.entityToAttack = null;
            p_70876_1_.entityToAttack = null;
            p_70876_1_.breeding = 0;
            p_70876_1_.inLove = 0;
            var2.setGrowingAge(-24000);
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);

            for (int var3 = 0; var3 < 7; ++var3)
            {
                double var4 = this.rand.nextGaussian() * 0.02D;
                double var6 = this.rand.nextGaussian() * 0.02D;
                double var8 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
            }

            this.worldObj.spawnEntityInWorld(var2);
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            this.fleeingTick = 60;

            if (!this.isAIEnabled())
            {
                IAttributeInstance var3 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

                if (var3.getModifier(field_110179_h) == null)
                {
                    var3.applyModifier(field_110181_i);
                }
            }

            this.entityToAttack = null;
            this.inLove = 0;
            return super.attackEntityFrom(source, amount);
        }
    }

    public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_)
    {
        return this.worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) == Blocks.grass ? 10.0F : this.worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_) - 0.5F;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("InLove", this.inLove);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.inLove = tagCompund.getInteger("InLove");
    }

    protected Entity findPlayerToAttack()
    {
        if (this.fleeingTick > 0)
        {
            return null;
        }
        else
        {
            float var1 = 8.0F;
            List var2;
            int var3;
            EntityAnimal var4;

            if (this.inLove > 0)
            {
                var2 = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand((double)var1, (double)var1, (double)var1));

                for (var3 = 0; var3 < var2.size(); ++var3)
                {
                    var4 = (EntityAnimal)var2.get(var3);

                    if (var4 != this && var4.inLove > 0)
                    {
                        return var4;
                    }
                }
            }
            else if (this.getGrowingAge() == 0)
            {
                var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand((double)var1, (double)var1, (double)var1));

                for (var3 = 0; var3 < var2.size(); ++var3)
                {
                    EntityPlayer var5 = (EntityPlayer)var2.get(var3);

                    if (var5.getCurrentEquippedItem() != null && this.isBreedingItem(var5.getCurrentEquippedItem()))
                    {
                        return var5;
                    }
                }
            }
            else if (this.getGrowingAge() > 0)
            {
                var2 = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand((double)var1, (double)var1, (double)var1));

                for (var3 = 0; var3 < var2.size(); ++var3)
                {
                    var4 = (EntityAnimal)var2.get(var3);

                    if (var4 != this && var4.getGrowingAge() < 0)
                    {
                        return var4;
                    }
                }
            }

            return null;
        }
    }

    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.worldObj.getBlock(var1, var2 - 1, var3) == Blocks.grass && this.worldObj.getFullBlockLightValue(var1, var2, var3) > 8 && super.getCanSpawnHere();
    }

    public int getTalkInterval()
    {
        return 120;
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    public boolean isBreedingItem(ItemStack p_70877_1_)
    {
        return p_70877_1_.getItem() == Items.wheat;
    }

    public boolean interact(EntityPlayer p_70085_1_)
    {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (var2 != null && this.isBreedingItem(var2) && this.getGrowingAge() == 0 && this.inLove <= 0)
        {
            if (!p_70085_1_.capabilities.isCreativeMode)
            {
                --var2.stackSize;

                if (var2.stackSize <= 0)
                {
                    p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack)null);
                }
            }

            this.setInLove(p_70085_1_);
            return true;
        }
        else
        {
            return super.interact(p_70085_1_);
        }
    }

    public void setInLove(EntityPlayer p_146082_1_)
    {
        this.inLove = 600;
        this.playerInLove = p_146082_1_;
        this.entityToAttack = null;
        this.worldObj.setEntityState(this, (byte)18);
    }

    public EntityPlayer func_146083_cb()
    {
        return this.playerInLove;
    }

    public boolean isInLove()
    {
        return this.inLove > 0;
    }

    public void resetInLove()
    {
        this.inLove = 0;
    }

    public boolean canMateWith(EntityAnimal p_70878_1_)
    {
        return p_70878_1_ == this ? false : (p_70878_1_.getClass() != this.getClass() ? false : this.isInLove() && p_70878_1_.isInLove());
    }

    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 18)
        {
            for (int var2 = 0; var2 < 7; ++var2)
            {
                double var3 = this.rand.nextGaussian() * 0.02D;
                double var5 = this.rand.nextGaussian() * 0.02D;
                double var7 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle("heart", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var3, var5, var7);
            }
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
}
