package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar
{
    protected IIcon[] field_150167_a;
    protected IIcon[] field_150166_b;
    private static final String __OBFID = "CL_00000266";

    public BlockLog()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
    }

    public static int func_150165_c(int p_150165_0_)
    {
        return p_150165_0_ & 3;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        byte var7 = 4;
        int var8 = var7 + 1;

        if (worldIn.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
        {
            for (int var9 = -var7; var9 <= var7; ++var9)
            {
                for (int var10 = -var7; var10 <= var7; ++var10)
                {
                    for (int var11 = -var7; var11 <= var7; ++var11)
                    {
                        if (worldIn.getBlock(x + var9, y + var10, z + var11).getMaterial() == Material.leaves)
                        {
                            int var12 = worldIn.getBlockMetadata(x + var9, y + var10, z + var11);

                            if ((var12 & 8) == 0)
                            {
                                worldIn.setBlockMetadataWithNotify(x + var9, y + var10, z + var11, var12 | 8, 4);
                            }
                        }
                    }
                }
            }
        }
    }

    protected IIcon getSideIcon(int p_150163_1_)
    {
        return this.field_150167_a[p_150163_1_ % this.field_150167_a.length];
    }

    protected IIcon getTopIcon(int p_150161_1_)
    {
        return this.field_150166_b[p_150161_1_ % this.field_150166_b.length];
    }
}
