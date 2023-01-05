package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityChest extends TileEntity implements IInventory
{
    private ItemStack[] chestContents = new ItemStack[36];
    public boolean adjacentChestChecked;
    public TileEntityChest adjacentChestZNeg;
    public TileEntityChest adjacentChestXPos;
    public TileEntityChest adjacentChestXNeg;
    public TileEntityChest adjacentChestZPos;
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private int cachedChestType;
    private String customName;
    private static final String __OBFID = "CL_00000346";

    public TileEntityChest()
    {
        this.cachedChestType = -1;
    }

    public TileEntityChest(int p_i2350_1_)
    {
        this.cachedChestType = p_i2350_1_;
    }

    public int getSizeInventory()
    {
        return 27;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return this.chestContents[slotIn];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.chestContents[index] != null)
        {
            ItemStack var3;

            if (this.chestContents[index].stackSize <= count)
            {
                var3 = this.chestContents[index];
                this.chestContents[index] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.chestContents[index].splitStack(count);

                if (this.chestContents[index].stackSize == 0)
                {
                    this.chestContents[index] = null;
                }

                this.markDirty();
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
        if (this.chestContents[index] != null)
        {
            ItemStack var2 = this.chestContents[index];
            this.chestContents[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.chestContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public String getInventoryName()
    {
        return this.isCustomInventoryName() ? this.customName : "container.chest";
    }

    public boolean isCustomInventoryName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String p_145976_1_)
    {
        this.customName = p_145976_1_;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.chestContents.length)
            {
                this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.chestContents.length; ++var3)
        {
            if (this.chestContents[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.chestContents[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (this.isCustomInventoryName())
        {
            compound.setString("CustomName", this.customName);
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    private void func_145978_a(TileEntityChest p_145978_1_, int p_145978_2_)
    {
        if (p_145978_1_.isInvalid())
        {
            this.adjacentChestChecked = false;
        }
        else if (this.adjacentChestChecked)
        {
            switch (p_145978_2_)
            {
                case 0:
                    if (this.adjacentChestZPos != p_145978_1_)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;

                case 1:
                    if (this.adjacentChestXNeg != p_145978_1_)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;

                case 2:
                    if (this.adjacentChestZNeg != p_145978_1_)
                    {
                        this.adjacentChestChecked = false;
                    }

                    break;

                case 3:
                    if (this.adjacentChestXPos != p_145978_1_)
                    {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    public void checkForAdjacentChests()
    {
        if (!this.adjacentChestChecked)
        {
            this.adjacentChestChecked = true;
            this.adjacentChestZNeg = null;
            this.adjacentChestXPos = null;
            this.adjacentChestXNeg = null;
            this.adjacentChestZPos = null;

            if (this.isSameTypeChestAt(this.xCoord - 1, this.yCoord, this.zCoord))
            {
                this.adjacentChestXNeg = (TileEntityChest)this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
            }

            if (this.isSameTypeChestAt(this.xCoord + 1, this.yCoord, this.zCoord))
            {
                this.adjacentChestXPos = (TileEntityChest)this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
            }

            if (this.isSameTypeChestAt(this.xCoord, this.yCoord, this.zCoord - 1))
            {
                this.adjacentChestZNeg = (TileEntityChest)this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
            }

            if (this.isSameTypeChestAt(this.xCoord, this.yCoord, this.zCoord + 1))
            {
                this.adjacentChestZPos = (TileEntityChest)this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
            }

            if (this.adjacentChestZNeg != null)
            {
                this.adjacentChestZNeg.func_145978_a(this, 0);
            }

            if (this.adjacentChestZPos != null)
            {
                this.adjacentChestZPos.func_145978_a(this, 2);
            }

            if (this.adjacentChestXPos != null)
            {
                this.adjacentChestXPos.func_145978_a(this, 1);
            }

            if (this.adjacentChestXNeg != null)
            {
                this.adjacentChestXNeg.func_145978_a(this, 3);
            }
        }
    }

    private boolean isSameTypeChestAt(int p_145977_1_, int p_145977_2_, int p_145977_3_)
    {
        if (this.worldObj == null)
        {
            return false;
        }
        else
        {
            Block var4 = this.worldObj.getBlock(p_145977_1_, p_145977_2_, p_145977_3_);
            return var4 instanceof BlockChest && ((BlockChest)var4).chestType == this.getChestType();
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        this.checkForAdjacentChests();
        ++this.ticksSinceSync;
        float var1;

        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            var1 = 5.0F;
            List var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)((float)this.xCoord - var1), (double)((float)this.yCoord - var1), (double)((float)this.zCoord - var1), (double)((float)(this.xCoord + 1) + var1), (double)((float)(this.yCoord + 1) + var1), (double)((float)(this.zCoord + 1) + var1)));
            Iterator var3 = var2.iterator();

            while (var3.hasNext())
            {
                EntityPlayer var4 = (EntityPlayer)var3.next();

                if (var4.openContainer instanceof ContainerChest)
                {
                    IInventory var5 = ((ContainerChest)var4.openContainer).getLowerChestInventory();

                    if (var5 == this || var5 instanceof InventoryLargeChest && ((InventoryLargeChest)var5).isPartOfLargeChest(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        var1 = 0.1F;
        double var11;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
        {
            double var8 = (double)this.xCoord + 0.5D;
            var11 = (double)this.zCoord + 0.5D;

            if (this.adjacentChestZPos != null)
            {
                var11 += 0.5D;
            }

            if (this.adjacentChestXPos != null)
            {
                var8 += 0.5D;
            }

            this.worldObj.playSoundEffect(var8, (double)this.yCoord + 0.5D, var11, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float var9 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += var1;
            }
            else
            {
                this.lidAngle -= var1;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float var10 = 0.5F;

            if (this.lidAngle < var10 && var9 >= var10 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
            {
                var11 = (double)this.xCoord + 0.5D;
                double var6 = (double)this.zCoord + 0.5D;

                if (this.adjacentChestZPos != null)
                {
                    var6 += 0.5D;
                }

                if (this.adjacentChestXPos != null)
                {
                    var11 += 0.5D;
                }

                this.worldObj.playSoundEffect(var11, (double)this.yCoord + 0.5D, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openChest()
    {
        if (this.numPlayersUsing < 0)
        {
            this.numPlayersUsing = 0;
        }

        ++this.numPlayersUsing;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
    }

    public void closeChest()
    {
        if (this.getBlockType() instanceof BlockChest)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        }
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    public int getChestType()
    {
        if (this.cachedChestType == -1)
        {
            if (this.worldObj == null || !(this.getBlockType() instanceof BlockChest))
            {
                return 0;
            }

            this.cachedChestType = ((BlockChest)this.getBlockType()).chestType;
        }

        return this.cachedChestType;
    }
}
