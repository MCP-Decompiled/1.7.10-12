package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntity implements IHopper
{
    private ItemStack[] field_145900_a = new ItemStack[5];
    private String field_145902_i;
    private int transferCooldown = -1;
    private static final String __OBFID = "CL_00000359";

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.field_145900_a = new ItemStack[this.getSizeInventory()];

        if (compound.hasKey("CustomName", 8))
        {
            this.field_145902_i = compound.getString("CustomName");
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.field_145900_a.length)
            {
                this.field_145900_a[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_145900_a.length; ++var3)
        {
            if (this.field_145900_a[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.field_145900_a[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);
        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.isCustomInventoryName())
        {
            compound.setString("CustomName", this.field_145902_i);
        }
    }

    public void markDirty()
    {
        super.markDirty();
    }

    public int getSizeInventory()
    {
        return this.field_145900_a.length;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return this.field_145900_a[slotIn];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.field_145900_a[index] != null)
        {
            ItemStack var3;

            if (this.field_145900_a[index].stackSize <= count)
            {
                var3 = this.field_145900_a[index];
                this.field_145900_a[index] = null;
                return var3;
            }
            else
            {
                var3 = this.field_145900_a[index].splitStack(count);

                if (this.field_145900_a[index].stackSize == 0)
                {
                    this.field_145900_a[index] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.field_145900_a[index] != null)
        {
            ItemStack var2 = this.field_145900_a[index];
            this.field_145900_a[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.field_145900_a[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    public String getInventoryName()
    {
        return this.isCustomInventoryName() ? this.field_145902_i : "container.hopper";
    }

    public boolean isCustomInventoryName()
    {
        return this.field_145902_i != null && this.field_145902_i.length() > 0;
    }

    public void func_145886_a(String p_145886_1_)
    {
        this.field_145902_i = p_145886_1_;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public void updateEntity()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.transferCooldown;

            if (!this.isOnTransferCooldown())
            {
                this.setTransferCooldown(0);
                this.func_145887_i();
            }
        }
    }

    public boolean func_145887_i()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            if (!this.isOnTransferCooldown() && BlockHopper.getActiveStateFromMetadata(this.getBlockMetadata()))
            {
                boolean var1 = false;

                if (!this.func_152104_k())
                {
                    var1 = this.func_145883_k();
                }

                if (!this.func_152105_l())
                {
                    var1 = func_145891_a(this) || var1;
                }

                if (var1)
                {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    private boolean func_152104_k()
    {
        ItemStack[] var1 = this.field_145900_a;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            ItemStack var4 = var1[var3];

            if (var4 != null)
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_152105_l()
    {
        ItemStack[] var1 = this.field_145900_a;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            ItemStack var4 = var1[var3];

            if (var4 == null || var4.stackSize != var4.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_145883_k()
    {
        IInventory var1 = this.func_145895_l();

        if (var1 == null)
        {
            return false;
        }
        else
        {
            int var2 = Facing.oppositeSide[BlockHopper.getDirectionFromMetadata(this.getBlockMetadata())];

            if (this.func_152102_a(var1, var2))
            {
                return false;
            }
            else
            {
                for (int var3 = 0; var3 < this.getSizeInventory(); ++var3)
                {
                    if (this.getStackInSlot(var3) != null)
                    {
                        ItemStack var4 = this.getStackInSlot(var3).copy();
                        ItemStack var5 = func_145889_a(var1, this.decrStackSize(var3, 1), var2);

                        if (var5 == null || var5.stackSize == 0)
                        {
                            var1.markDirty();
                            return true;
                        }

                        this.setInventorySlotContents(var3, var4);
                    }
                }

                return false;
            }
        }
    }

    private boolean func_152102_a(IInventory p_152102_1_, int p_152102_2_)
    {
        if (p_152102_1_ instanceof ISidedInventory && p_152102_2_ > -1)
        {
            ISidedInventory var7 = (ISidedInventory)p_152102_1_;
            int[] var8 = var7.getSlotsForFace(p_152102_2_);

            for (int var9 = 0; var9 < var8.length; ++var9)
            {
                ItemStack var6 = var7.getStackInSlot(var8[var9]);

                if (var6 == null || var6.stackSize != var6.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int var3 = p_152102_1_.getSizeInventory();

            for (int var4 = 0; var4 < var3; ++var4)
            {
                ItemStack var5 = p_152102_1_.getStackInSlot(var4);

                if (var5 == null || var5.stackSize != var5.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean func_152103_b(IInventory p_152103_0_, int p_152103_1_)
    {
        if (p_152103_0_ instanceof ISidedInventory && p_152103_1_ > -1)
        {
            ISidedInventory var5 = (ISidedInventory)p_152103_0_;
            int[] var6 = var5.getSlotsForFace(p_152103_1_);

            for (int var4 = 0; var4 < var6.length; ++var4)
            {
                if (var5.getStackInSlot(var6[var4]) != null)
                {
                    return false;
                }
            }
        }
        else
        {
            int var2 = p_152103_0_.getSizeInventory();

            for (int var3 = 0; var3 < var2; ++var3)
            {
                if (p_152103_0_.getStackInSlot(var3) != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean func_145891_a(IHopper p_145891_0_)
    {
        IInventory var1 = func_145884_b(p_145891_0_);

        if (var1 != null)
        {
            byte var2 = 0;

            if (func_152103_b(var1, var2))
            {
                return false;
            }

            if (var1 instanceof ISidedInventory && var2 > -1)
            {
                ISidedInventory var7 = (ISidedInventory)var1;
                int[] var8 = var7.getSlotsForFace(var2);

                for (int var5 = 0; var5 < var8.length; ++var5)
                {
                    if (func_145892_a(p_145891_0_, var1, var8[var5], var2))
                    {
                        return true;
                    }
                }
            }
            else
            {
                int var3 = var1.getSizeInventory();

                for (int var4 = 0; var4 < var3; ++var4)
                {
                    if (func_145892_a(p_145891_0_, var1, var4, var2))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            EntityItem var6 = func_145897_a(p_145891_0_.getWorld(), p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0D, p_145891_0_.getZPos());

            if (var6 != null)
            {
                return func_145898_a(p_145891_0_, var6);
            }
        }

        return false;
    }

    private static boolean func_145892_a(IHopper p_145892_0_, IInventory p_145892_1_, int p_145892_2_, int p_145892_3_)
    {
        ItemStack var4 = p_145892_1_.getStackInSlot(p_145892_2_);

        if (var4 != null && func_145890_b(p_145892_1_, var4, p_145892_2_, p_145892_3_))
        {
            ItemStack var5 = var4.copy();
            ItemStack var6 = func_145889_a(p_145892_0_, p_145892_1_.decrStackSize(p_145892_2_, 1), -1);

            if (var6 == null || var6.stackSize == 0)
            {
                p_145892_1_.markDirty();
                return true;
            }

            p_145892_1_.setInventorySlotContents(p_145892_2_, var5);
        }

        return false;
    }

    public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_)
    {
        boolean var2 = false;

        if (p_145898_1_ == null)
        {
            return false;
        }
        else
        {
            ItemStack var3 = p_145898_1_.getEntityItem().copy();
            ItemStack var4 = func_145889_a(p_145898_0_, var3, -1);

            if (var4 != null && var4.stackSize != 0)
            {
                p_145898_1_.setEntityItemStack(var4);
            }
            else
            {
                var2 = true;
                p_145898_1_.setDead();
            }

            return var2;
        }
    }

    public static ItemStack func_145889_a(IInventory p_145889_0_, ItemStack p_145889_1_, int p_145889_2_)
    {
        if (p_145889_0_ instanceof ISidedInventory && p_145889_2_ > -1)
        {
            ISidedInventory var6 = (ISidedInventory)p_145889_0_;
            int[] var7 = var6.getSlotsForFace(p_145889_2_);

            for (int var5 = 0; var5 < var7.length && p_145889_1_ != null && p_145889_1_.stackSize > 0; ++var5)
            {
                p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, var7[var5], p_145889_2_);
            }
        }
        else
        {
            int var3 = p_145889_0_.getSizeInventory();

            for (int var4 = 0; var4 < var3 && p_145889_1_ != null && p_145889_1_.stackSize > 0; ++var4)
            {
                p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, var4, p_145889_2_);
            }
        }

        if (p_145889_1_ != null && p_145889_1_.stackSize == 0)
        {
            p_145889_1_ = null;
        }

        return p_145889_1_;
    }

    private static boolean func_145885_a(IInventory p_145885_0_, ItemStack p_145885_1_, int p_145885_2_, int p_145885_3_)
    {
        return !p_145885_0_.isItemValidForSlot(p_145885_2_, p_145885_1_) ? false : !(p_145885_0_ instanceof ISidedInventory) || ((ISidedInventory)p_145885_0_).canInsertItem(p_145885_2_, p_145885_1_, p_145885_3_);
    }

    private static boolean func_145890_b(IInventory p_145890_0_, ItemStack p_145890_1_, int p_145890_2_, int p_145890_3_)
    {
        return !(p_145890_0_ instanceof ISidedInventory) || ((ISidedInventory)p_145890_0_).canExtractItem(p_145890_2_, p_145890_1_, p_145890_3_);
    }

    private static ItemStack func_145899_c(IInventory p_145899_0_, ItemStack p_145899_1_, int p_145899_2_, int p_145899_3_)
    {
        ItemStack var4 = p_145899_0_.getStackInSlot(p_145899_2_);

        if (func_145885_a(p_145899_0_, p_145899_1_, p_145899_2_, p_145899_3_))
        {
            boolean var5 = false;

            if (var4 == null)
            {
                p_145899_0_.setInventorySlotContents(p_145899_2_, p_145899_1_);
                p_145899_1_ = null;
                var5 = true;
            }
            else if (func_145894_a(var4, p_145899_1_))
            {
                int var6 = p_145899_1_.getMaxStackSize() - var4.stackSize;
                int var7 = Math.min(p_145899_1_.stackSize, var6);
                p_145899_1_.stackSize -= var7;
                var4.stackSize += var7;
                var5 = var7 > 0;
            }

            if (var5)
            {
                if (p_145899_0_ instanceof TileEntityHopper)
                {
                    ((TileEntityHopper)p_145899_0_).setTransferCooldown(8);
                    p_145899_0_.markDirty();
                }

                p_145899_0_.markDirty();
            }
        }

        return p_145899_1_;
    }

    private IInventory func_145895_l()
    {
        int var1 = BlockHopper.getDirectionFromMetadata(this.getBlockMetadata());
        return func_145893_b(this.getWorld(), (double)(this.xCoord + Facing.offsetsXForSide[var1]), (double)(this.yCoord + Facing.offsetsYForSide[var1]), (double)(this.zCoord + Facing.offsetsZForSide[var1]));
    }

    public static IInventory func_145884_b(IHopper p_145884_0_)
    {
        return func_145893_b(p_145884_0_.getWorld(), p_145884_0_.getXPos(), p_145884_0_.getYPos() + 1.0D, p_145884_0_.getZPos());
    }

    public static EntityItem func_145897_a(World p_145897_0_, double p_145897_1_, double p_145897_3_, double p_145897_5_)
    {
        List var7 = p_145897_0_.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(p_145897_1_, p_145897_3_, p_145897_5_, p_145897_1_ + 1.0D, p_145897_3_ + 1.0D, p_145897_5_ + 1.0D), IEntitySelector.selectAnything);
        return var7.size() > 0 ? (EntityItem)var7.get(0) : null;
    }

    public static IInventory func_145893_b(World p_145893_0_, double p_145893_1_, double p_145893_3_, double p_145893_5_)
    {
        IInventory var7 = null;
        int var8 = MathHelper.floor_double(p_145893_1_);
        int var9 = MathHelper.floor_double(p_145893_3_);
        int var10 = MathHelper.floor_double(p_145893_5_);
        TileEntity var11 = p_145893_0_.getTileEntity(var8, var9, var10);

        if (var11 != null && var11 instanceof IInventory)
        {
            var7 = (IInventory)var11;

            if (var7 instanceof TileEntityChest)
            {
                Block var12 = p_145893_0_.getBlock(var8, var9, var10);

                if (var12 instanceof BlockChest)
                {
                    var7 = ((BlockChest)var12).getInventory(p_145893_0_, var8, var9, var10);
                }
            }
        }

        if (var7 == null)
        {
            List var13 = p_145893_0_.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(p_145893_1_, p_145893_3_, p_145893_5_, p_145893_1_ + 1.0D, p_145893_3_ + 1.0D, p_145893_5_ + 1.0D), IEntitySelector.selectInventories);

            if (var13 != null && var13.size() > 0)
            {
                var7 = (IInventory)var13.get(p_145893_0_.rand.nextInt(var13.size()));
            }
        }

        return var7;
    }

    private static boolean func_145894_a(ItemStack p_145894_0_, ItemStack p_145894_1_)
    {
        return p_145894_0_.getItem() != p_145894_1_.getItem() ? false : (p_145894_0_.getMetadata() != p_145894_1_.getMetadata() ? false : (p_145894_0_.stackSize > p_145894_0_.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(p_145894_0_, p_145894_1_)));
    }

    public double getXPos()
    {
        return (double)this.xCoord;
    }

    public double getYPos()
    {
        return (double)this.yCoord;
    }

    public double getZPos()
    {
        return (double)this.zCoord;
    }

    public void setTransferCooldown(int p_145896_1_)
    {
        this.transferCooldown = p_145896_1_;
    }

    public boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }
}
