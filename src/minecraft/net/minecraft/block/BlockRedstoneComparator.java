package net.minecraft.block;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
{
    private static final String __OBFID = "CL_00000220";

    public BlockRedstoneComparator(boolean p_i45399_1_)
    {
        super(p_i45399_1_);
        this.isBlockContainer = true;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.comparator;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.comparator;
    }

    protected int func_149901_b(int p_149901_1_)
    {
        return 2;
    }

    protected BlockRedstoneDiode getBlockPowered()
    {
        return Blocks.powered_comparator;
    }

    protected BlockRedstoneDiode getBlockUnpowered()
    {
        return Blocks.unpowered_comparator;
    }

    public int getRenderType()
    {
        return 37;
    }

    public IIcon getIcon(int side, int meta)
    {
        boolean var3 = this.isRepeaterPowered || (meta & 8) != 0;
        return side == 0 ? (var3 ? Blocks.redstone_torch.getBlockTextureFromSide(side) : Blocks.unlit_redstone_torch.getBlockTextureFromSide(side)) : (side == 1 ? (var3 ? Blocks.powered_comparator.blockIcon : this.blockIcon) : Blocks.double_stone_slab.getBlockTextureFromSide(1));
    }

    protected boolean func_149905_c(int p_149905_1_)
    {
        return this.isRepeaterPowered || (p_149905_1_ & 8) != 0;
    }

    protected int func_149904_f(IBlockAccess p_149904_1_, int p_149904_2_, int p_149904_3_, int p_149904_4_, int p_149904_5_)
    {
        return this.getTileEntityComparator(p_149904_1_, p_149904_2_, p_149904_3_, p_149904_4_).getOutputSignal();
    }

    private int getOutputStrength(World p_149970_1_, int p_149970_2_, int p_149970_3_, int p_149970_4_, int p_149970_5_)
    {
        return !this.func_149969_d(p_149970_5_) ? this.getInputStrength(p_149970_1_, p_149970_2_, p_149970_3_, p_149970_4_, p_149970_5_) : Math.max(this.getInputStrength(p_149970_1_, p_149970_2_, p_149970_3_, p_149970_4_, p_149970_5_) - this.func_149902_h(p_149970_1_, p_149970_2_, p_149970_3_, p_149970_4_, p_149970_5_), 0);
    }

    public boolean func_149969_d(int p_149969_1_)
    {
        return (p_149969_1_ & 4) == 4;
    }

    protected boolean isGettingInput(World p_149900_1_, int p_149900_2_, int p_149900_3_, int p_149900_4_, int p_149900_5_)
    {
        int var6 = this.getInputStrength(p_149900_1_, p_149900_2_, p_149900_3_, p_149900_4_, p_149900_5_);

        if (var6 >= 15)
        {
            return true;
        }
        else if (var6 == 0)
        {
            return false;
        }
        else
        {
            int var7 = this.func_149902_h(p_149900_1_, p_149900_2_, p_149900_3_, p_149900_4_, p_149900_5_);
            return var7 == 0 ? true : var6 >= var7;
        }
    }

    protected int getInputStrength(World p_149903_1_, int p_149903_2_, int p_149903_3_, int p_149903_4_, int p_149903_5_)
    {
        int var6 = super.getInputStrength(p_149903_1_, p_149903_2_, p_149903_3_, p_149903_4_, p_149903_5_);
        int var7 = getDirection(p_149903_5_);
        int var8 = p_149903_2_ + Direction.offsetX[var7];
        int var9 = p_149903_4_ + Direction.offsetZ[var7];
        Block var10 = p_149903_1_.getBlock(var8, p_149903_3_, var9);

        if (var10.hasComparatorInputOverride())
        {
            var6 = var10.getComparatorInputOverride(p_149903_1_, var8, p_149903_3_, var9, Direction.rotateOpposite[var7]);
        }
        else if (var6 < 15 && var10.isNormalCube())
        {
            var8 += Direction.offsetX[var7];
            var9 += Direction.offsetZ[var7];
            var10 = p_149903_1_.getBlock(var8, p_149903_3_, var9);

            if (var10.hasComparatorInputOverride())
            {
                var6 = var10.getComparatorInputOverride(p_149903_1_, var8, p_149903_3_, var9, Direction.rotateOpposite[var7]);
            }
        }

        return var6;
    }

    public TileEntityComparator getTileEntityComparator(IBlockAccess p_149971_1_, int p_149971_2_, int p_149971_3_, int p_149971_4_)
    {
        return (TileEntityComparator)p_149971_1_.getTileEntity(p_149971_2_, p_149971_3_, p_149971_4_);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        int var10 = worldIn.getBlockMetadata(x, y, z);
        boolean var11 = this.isRepeaterPowered | (var10 & 8) != 0;
        boolean var12 = !this.func_149969_d(var10);
        int var13 = var12 ? 4 : 0;
        var13 |= var11 ? 8 : 0;
        worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, var12 ? 0.55F : 0.5F);
        worldIn.setBlockMetadataWithNotify(x, y, z, var13 | var10 & 3, 2);
        this.func_149972_c(worldIn, x, y, z, worldIn.rand);
        return true;
    }

    protected void func_149897_b(World p_149897_1_, int p_149897_2_, int p_149897_3_, int p_149897_4_, Block p_149897_5_)
    {
        if (!p_149897_1_.isBlockTickScheduledThisTick(p_149897_2_, p_149897_3_, p_149897_4_, this))
        {
            int var6 = p_149897_1_.getBlockMetadata(p_149897_2_, p_149897_3_, p_149897_4_);
            int var7 = this.getOutputStrength(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6);
            int var8 = this.getTileEntityComparator(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_).getOutputSignal();

            if (var7 != var8 || this.func_149905_c(var6) != this.isGettingInput(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6))
            {
                if (this.func_149912_i(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6))
                {
                    p_149897_1_.scheduleBlockUpdateWithPriority(p_149897_2_, p_149897_3_, p_149897_4_, this, this.func_149901_b(0), -1);
                }
                else
                {
                    p_149897_1_.scheduleBlockUpdateWithPriority(p_149897_2_, p_149897_3_, p_149897_4_, this, this.func_149901_b(0), 0);
                }
            }
        }
    }

    private void func_149972_c(World p_149972_1_, int p_149972_2_, int p_149972_3_, int p_149972_4_, Random p_149972_5_)
    {
        int var6 = p_149972_1_.getBlockMetadata(p_149972_2_, p_149972_3_, p_149972_4_);
        int var7 = this.getOutputStrength(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_, var6);
        int var8 = this.getTileEntityComparator(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_).getOutputSignal();
        this.getTileEntityComparator(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_).setOutputSignal(var7);

        if (var8 != var7 || !this.func_149969_d(var6))
        {
            boolean var9 = this.isGettingInput(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_, var6);
            boolean var10 = this.isRepeaterPowered || (var6 & 8) != 0;

            if (var10 && !var9)
            {
                p_149972_1_.setBlockMetadataWithNotify(p_149972_2_, p_149972_3_, p_149972_4_, var6 & -9, 2);
            }
            else if (!var10 && var9)
            {
                p_149972_1_.setBlockMetadataWithNotify(p_149972_2_, p_149972_3_, p_149972_4_, var6 | 8, 2);
            }

            this.func_149911_e(p_149972_1_, p_149972_2_, p_149972_3_, p_149972_4_);
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (this.isRepeaterPowered)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            worldIn.setBlock(x, y, z, this.getBlockUnpowered(), var6 | 8, 4);
        }

        this.func_149972_c(worldIn, x, y, z, random);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        worldIn.setTileEntity(x, y, z, this.createNewTileEntity(worldIn, 0));
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
        worldIn.removeTileEntity(x, y, z);
        this.func_149911_e(worldIn, x, y, z);
    }

    public boolean onBlockEventReceived(World worldIn, int x, int y, int z, int eventId, int eventData)
    {
        super.onBlockEventReceived(worldIn, x, y, z, eventId, eventData);
        TileEntity var7 = worldIn.getTileEntity(x, y, z);
        return var7 != null ? var7.receiveClientEvent(eventId, eventData) : false;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityComparator();
    }
}
