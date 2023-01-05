package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block
{
    private static final String __OBFID = "CL_00000309";

    protected BlockSnow()
    {
        super(Material.snow);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.func_150154_b(0);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("snow");
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z) & 7;
        float var6 = 0.125F;
        return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)((float)y + (float)var5 * var6), (double)z + this.maxZ);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsForItemRender()
    {
        this.func_150154_b(0);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.func_150154_b(worldIn.getBlockMetadata(x, y, z));
    }

    protected void func_150154_b(int p_150154_1_)
    {
        int var2 = p_150154_1_ & 7;
        float var3 = (float)(2 * (1 + var2)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        Block var5 = worldIn.getBlock(x, y - 1, z);
        return var5 != Blocks.ice && var5 != Blocks.packed_ice ? (var5.getMaterial() == Material.leaves ? true : (var5 == this && (worldIn.getBlockMetadata(x, y - 1, z) & 7) == 7 ? true : var5.isOpaqueCube() && var5.blockMaterial.blocksMovement())) : false;
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.func_150155_m(worldIn, x, y, z);
    }

    private boolean func_150155_m(World p_150155_1_, int p_150155_2_, int p_150155_3_, int p_150155_4_)
    {
        if (!this.canPlaceBlockAt(p_150155_1_, p_150155_2_, p_150155_3_, p_150155_4_))
        {
            this.dropBlockAsItem(p_150155_1_, p_150155_2_, p_150155_3_, p_150155_4_, p_150155_1_.getBlockMetadata(p_150155_2_, p_150155_3_, p_150155_4_), 0);
            p_150155_1_.setBlockToAir(p_150155_2_, p_150155_3_, p_150155_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        int var7 = meta & 7;
        this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(Items.snowball, var7 + 1, 0));
        worldIn.setBlockToAir(x, y, z);
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.snowball;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11)
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 1 ? true : super.shouldSideBeRendered(worldIn, x, y, z, side);
    }
}
