package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block
{
    private static final String __OBFID = "CL_00000330";

    public BlockVine()
    {
        super(Material.vine);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int getRenderType()
    {
        return 20;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        float var5 = 0.0625F;
        int var6 = worldIn.getBlockMetadata(x, y, z);
        float var7 = 1.0F;
        float var8 = 1.0F;
        float var9 = 1.0F;
        float var10 = 0.0F;
        float var11 = 0.0F;
        float var12 = 0.0F;
        boolean var13 = var6 > 0;

        if ((var6 & 2) != 0)
        {
            var10 = Math.max(var10, 0.0625F);
            var7 = 0.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var9 = 0.0F;
            var12 = 1.0F;
            var13 = true;
        }

        if ((var6 & 8) != 0)
        {
            var7 = Math.min(var7, 0.9375F);
            var10 = 1.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var9 = 0.0F;
            var12 = 1.0F;
            var13 = true;
        }

        if ((var6 & 4) != 0)
        {
            var12 = Math.max(var12, 0.0625F);
            var9 = 0.0F;
            var7 = 0.0F;
            var10 = 1.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var13 = true;
        }

        if ((var6 & 1) != 0)
        {
            var9 = Math.min(var9, 0.9375F);
            var12 = 1.0F;
            var7 = 0.0F;
            var10 = 1.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var13 = true;
        }

        if (!var13 && this.func_150093_a(worldIn.getBlock(x, y + 1, z)))
        {
            var8 = Math.min(var8, 0.9375F);
            var11 = 1.0F;
            var7 = 0.0F;
            var10 = 1.0F;
            var9 = 0.0F;
            var12 = 1.0F;
        }

        this.setBlockBounds(var7, var8, var9, var10, var11, var12);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side)
    {
        switch (side)
        {
            case 1:
                return this.func_150093_a(worldIn.getBlock(x, y + 1, z));

            case 2:
                return this.func_150093_a(worldIn.getBlock(x, y, z + 1));

            case 3:
                return this.func_150093_a(worldIn.getBlock(x, y, z - 1));

            case 4:
                return this.func_150093_a(worldIn.getBlock(x + 1, y, z));

            case 5:
                return this.func_150093_a(worldIn.getBlock(x - 1, y, z));

            default:
                return false;
        }
    }

    private boolean func_150093_a(Block p_150093_1_)
    {
        return p_150093_1_.renderAsNormalBlock() && p_150093_1_.blockMaterial.blocksMovement();
    }

    private boolean func_150094_e(World p_150094_1_, int p_150094_2_, int p_150094_3_, int p_150094_4_)
    {
        int var5 = p_150094_1_.getBlockMetadata(p_150094_2_, p_150094_3_, p_150094_4_);
        int var6 = var5;

        if (var5 > 0)
        {
            for (int var7 = 0; var7 <= 3; ++var7)
            {
                int var8 = 1 << var7;

                if ((var5 & var8) != 0 && !this.func_150093_a(p_150094_1_.getBlock(p_150094_2_ + Direction.offsetX[var7], p_150094_3_, p_150094_4_ + Direction.offsetZ[var7])) && (p_150094_1_.getBlock(p_150094_2_, p_150094_3_ + 1, p_150094_4_) != this || (p_150094_1_.getBlockMetadata(p_150094_2_, p_150094_3_ + 1, p_150094_4_) & var8) == 0))
                {
                    var6 &= ~var8;
                }
            }
        }

        if (var6 == 0 && !this.func_150093_a(p_150094_1_.getBlock(p_150094_2_, p_150094_3_ + 1, p_150094_4_)))
        {
            return false;
        }
        else
        {
            if (var6 != var5)
            {
                p_150094_1_.setBlockMetadataWithNotify(p_150094_2_, p_150094_3_, p_150094_4_, var6, 2);
            }

            return true;
        }
    }

    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int getRenderColor(int meta)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return worldIn.getBiomeGenForCoords(x, z).getBiomeFoliageColor(x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote && !this.func_150094_e(worldIn, x, y, z))
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0)
        {
            byte var6 = 4;
            int var7 = 5;
            boolean var8 = false;
            int var9;
            int var10;
            int var11;
            label134:

            for (var9 = x - var6; var9 <= x + var6; ++var9)
            {
                for (var10 = z - var6; var10 <= z + var6; ++var10)
                {
                    for (var11 = y - 1; var11 <= y + 1; ++var11)
                    {
                        if (worldIn.getBlock(var9, var11, var10) == this)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                var8 = true;
                                break label134;
                            }
                        }
                    }
                }
            }

            var9 = worldIn.getBlockMetadata(x, y, z);
            var10 = worldIn.rand.nextInt(6);
            var11 = Direction.facingToDirection[var10];
            int var13;

            if (var10 == 1 && y < 255 && worldIn.isAirBlock(x, y + 1, z))
            {
                if (var8)
                {
                    return;
                }

                int var15 = worldIn.rand.nextInt(16) & var9;

                if (var15 > 0)
                {
                    for (var13 = 0; var13 <= 3; ++var13)
                    {
                        if (!this.func_150093_a(worldIn.getBlock(x + Direction.offsetX[var13], y + 1, z + Direction.offsetZ[var13])))
                        {
                            var15 &= ~(1 << var13);
                        }
                    }

                    if (var15 > 0)
                    {
                        worldIn.setBlock(x, y + 1, z, this, var15, 2);
                    }
                }
            }
            else
            {
                Block var12;
                int var14;

                if (var10 >= 2 && var10 <= 5 && (var9 & 1 << var11) == 0)
                {
                    if (var8)
                    {
                        return;
                    }

                    var12 = worldIn.getBlock(x + Direction.offsetX[var11], y, z + Direction.offsetZ[var11]);

                    if (var12.blockMaterial == Material.air)
                    {
                        var13 = var11 + 1 & 3;
                        var14 = var11 + 3 & 3;

                        if ((var9 & 1 << var13) != 0 && this.func_150093_a(worldIn.getBlock(x + Direction.offsetX[var11] + Direction.offsetX[var13], y, z + Direction.offsetZ[var11] + Direction.offsetZ[var13])))
                        {
                            worldIn.setBlock(x + Direction.offsetX[var11], y, z + Direction.offsetZ[var11], this, 1 << var13, 2);
                        }
                        else if ((var9 & 1 << var14) != 0 && this.func_150093_a(worldIn.getBlock(x + Direction.offsetX[var11] + Direction.offsetX[var14], y, z + Direction.offsetZ[var11] + Direction.offsetZ[var14])))
                        {
                            worldIn.setBlock(x + Direction.offsetX[var11], y, z + Direction.offsetZ[var11], this, 1 << var14, 2);
                        }
                        else if ((var9 & 1 << var13) != 0 && worldIn.isAirBlock(x + Direction.offsetX[var11] + Direction.offsetX[var13], y, z + Direction.offsetZ[var11] + Direction.offsetZ[var13]) && this.func_150093_a(worldIn.getBlock(x + Direction.offsetX[var13], y, z + Direction.offsetZ[var13])))
                        {
                            worldIn.setBlock(x + Direction.offsetX[var11] + Direction.offsetX[var13], y, z + Direction.offsetZ[var11] + Direction.offsetZ[var13], this, 1 << (var11 + 2 & 3), 2);
                        }
                        else if ((var9 & 1 << var14) != 0 && worldIn.isAirBlock(x + Direction.offsetX[var11] + Direction.offsetX[var14], y, z + Direction.offsetZ[var11] + Direction.offsetZ[var14]) && this.func_150093_a(worldIn.getBlock(x + Direction.offsetX[var14], y, z + Direction.offsetZ[var14])))
                        {
                            worldIn.setBlock(x + Direction.offsetX[var11] + Direction.offsetX[var14], y, z + Direction.offsetZ[var11] + Direction.offsetZ[var14], this, 1 << (var11 + 2 & 3), 2);
                        }
                        else if (this.func_150093_a(worldIn.getBlock(x + Direction.offsetX[var11], y + 1, z + Direction.offsetZ[var11])))
                        {
                            worldIn.setBlock(x + Direction.offsetX[var11], y, z + Direction.offsetZ[var11], this, 0, 2);
                        }
                    }
                    else if (var12.blockMaterial.isOpaque() && var12.renderAsNormalBlock())
                    {
                        worldIn.setBlockMetadataWithNotify(x, y, z, var9 | 1 << var11, 2);
                    }
                }
                else if (y > 1)
                {
                    var12 = worldIn.getBlock(x, y - 1, z);

                    if (var12.blockMaterial == Material.air)
                    {
                        var13 = worldIn.rand.nextInt(16) & var9;

                        if (var13 > 0)
                        {
                            worldIn.setBlock(x, y - 1, z, this, var13, 2);
                        }
                    }
                    else if (var12 == this)
                    {
                        var13 = worldIn.rand.nextInt(16) & var9;
                        var14 = worldIn.getBlockMetadata(x, y - 1, z);

                        if (var14 != (var14 | var13))
                        {
                            worldIn.setBlockMetadataWithNotify(x, y - 1, z, var14 | var13, 2);
                        }
                    }
                }
            }
        }
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        byte var10 = 0;

        switch (side)
        {
            case 2:
                var10 = 1;
                break;

            case 3:
                var10 = 4;
                break;

            case 4:
                var10 = 8;
                break;

            case 5:
                var10 = 2;
        }

        return var10 != 0 ? var10 : meta;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears)
        {
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            this.dropBlockAsItem(worldIn, x, y, z, new ItemStack(Blocks.vine, 1, 0));
        }
        else
        {
            super.harvestBlock(worldIn, player, x, y, z, meta);
        }
    }
}
