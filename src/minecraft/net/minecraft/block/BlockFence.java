package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block
{
    private final String field_149827_a;
    private static final String __OBFID = "CL_00000242";

    public BlockFence(String p_i45406_1_, Material p_i45406_2_)
    {
        super(p_i45406_2_);
        this.field_149827_a = p_i45406_1_;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        boolean var8 = this.canConnectFenceTo(worldIn, x, y, z - 1);
        boolean var9 = this.canConnectFenceTo(worldIn, x, y, z + 1);
        boolean var10 = this.canConnectFenceTo(worldIn, x - 1, y, z);
        boolean var11 = this.canConnectFenceTo(worldIn, x + 1, y, z);
        float var12 = 0.375F;
        float var13 = 0.625F;
        float var14 = 0.375F;
        float var15 = 0.625F;

        if (var8)
        {
            var14 = 0.0F;
        }

        if (var9)
        {
            var15 = 1.0F;
        }

        if (var8 || var9)
        {
            this.setBlockBounds(var12, 0.0F, var14, var13, 1.5F, var15);
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }

        var14 = 0.375F;
        var15 = 0.625F;

        if (var10)
        {
            var12 = 0.0F;
        }

        if (var11)
        {
            var13 = 1.0F;
        }

        if (var10 || var11 || !var8 && !var9)
        {
            this.setBlockBounds(var12, 0.0F, var14, var13, 1.5F, var15);
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }

        if (var8)
        {
            var14 = 0.0F;
        }

        if (var9)
        {
            var15 = 1.0F;
        }

        this.setBlockBounds(var12, 0.0F, var14, var13, 1.0F, var15);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        boolean var5 = this.canConnectFenceTo(worldIn, x, y, z - 1);
        boolean var6 = this.canConnectFenceTo(worldIn, x, y, z + 1);
        boolean var7 = this.canConnectFenceTo(worldIn, x - 1, y, z);
        boolean var8 = this.canConnectFenceTo(worldIn, x + 1, y, z);
        float var9 = 0.375F;
        float var10 = 0.625F;
        float var11 = 0.375F;
        float var12 = 0.625F;

        if (var5)
        {
            var11 = 0.0F;
        }

        if (var6)
        {
            var12 = 1.0F;
        }

        if (var7)
        {
            var9 = 0.0F;
        }

        if (var8)
        {
            var10 = 1.0F;
        }

        this.setBlockBounds(var9, 0.0F, var11, var10, 1.0F, var12);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        return false;
    }

    public int getRenderType()
    {
        return 11;
    }

    public boolean canConnectFenceTo(IBlockAccess p_149826_1_, int p_149826_2_, int p_149826_3_, int p_149826_4_)
    {
        Block var5 = p_149826_1_.getBlock(p_149826_2_, p_149826_3_, p_149826_4_);
        return var5 != this && var5 != Blocks.fence_gate ? (var5.blockMaterial.isOpaque() && var5.renderAsNormalBlock() ? var5.blockMaterial != Material.gourd : false) : true;
    }

    public static boolean isFence(Block p_149825_0_)
    {
        return p_149825_0_ == Blocks.fence || p_149825_0_ == Blocks.nether_brick_fence;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return true;
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.field_149827_a);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        return worldIn.isRemote ? true : ItemLead.func_150909_a(player, worldIn, x, y, z);
    }
}
