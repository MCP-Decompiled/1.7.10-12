package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFarmland extends Block
{
    private IIcon iconDry;
    private IIcon iconWet;
    private static final String __OBFID = "CL_00000241";

    protected BlockFarmland()
    {
        super(Material.ground);
        this.setTickRandomly(true);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)(x + 0), (double)(y + 0), (double)(z + 0), (double)(x + 1), (double)(y + 1), (double)(z + 1));
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? (meta > 0 ? this.iconDry : this.iconWet) : Blocks.dirt.getBlockTextureFromSide(side);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!this.func_149821_m(worldIn, x, y, z) && !worldIn.isRainingAt(x, y + 1, z))
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if (var6 > 0)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, var6 - 1, 2);
            }
            else if (!this.func_149822_e(worldIn, x, y, z))
            {
                worldIn.setBlock(x, y, z, Blocks.dirt);
            }
        }
        else
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 7, 2);
        }
    }

    public void onFallenUpon(World worldIn, int x, int y, int z, Entity entityIn, float fallDistance)
    {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F)
        {
            if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getGameRuleBooleanValue("mobGriefing"))
            {
                return;
            }

            worldIn.setBlock(x, y, z, Blocks.dirt);
        }
    }

    private boolean func_149822_e(World p_149822_1_, int p_149822_2_, int p_149822_3_, int p_149822_4_)
    {
        byte var5 = 0;

        for (int var6 = p_149822_2_ - var5; var6 <= p_149822_2_ + var5; ++var6)
        {
            for (int var7 = p_149822_4_ - var5; var7 <= p_149822_4_ + var5; ++var7)
            {
                Block var8 = p_149822_1_.getBlock(var6, p_149822_3_ + 1, var7);

                if (var8 == Blocks.wheat || var8 == Blocks.melon_stem || var8 == Blocks.pumpkin_stem || var8 == Blocks.potatoes || var8 == Blocks.carrots)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean func_149821_m(World p_149821_1_, int p_149821_2_, int p_149821_3_, int p_149821_4_)
    {
        for (int var5 = p_149821_2_ - 4; var5 <= p_149821_2_ + 4; ++var5)
        {
            for (int var6 = p_149821_3_; var6 <= p_149821_3_ + 1; ++var6)
            {
                for (int var7 = p_149821_4_ - 4; var7 <= p_149821_4_ + 4; ++var7)
                {
                    if (p_149821_1_.getBlock(var5, var6, var7).getMaterial() == Material.water)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        Material var6 = worldIn.getBlock(x, y + 1, z).getMaterial();

        if (var6.isSolid())
        {
            worldIn.setBlock(x, y, z, Blocks.dirt);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Blocks.dirt.getItemDropped(0, random, fortune);
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.iconDry = reg.registerIcon(this.getTextureName() + "_wet");
        this.iconWet = reg.registerIcon(this.getTextureName() + "_dry");
    }
}
