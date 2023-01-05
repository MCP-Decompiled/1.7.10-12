package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockRedstoneLight extends Block
{
    private final boolean field_150171_a;
    private static final String __OBFID = "CL_00000297";

    public BlockRedstoneLight(boolean p_i45421_1_)
    {
        super(Material.redstoneLight);
        this.field_150171_a = p_i45421_1_;

        if (p_i45421_1_)
        {
            this.setLightLevel(1.0F);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        if (!worldIn.isRemote)
        {
            if (this.field_150171_a && !worldIn.isBlockIndirectlyGettingPowered(x, y, z))
            {
                worldIn.scheduleBlockUpdate(x, y, z, this, 4);
            }
            else if (!this.field_150171_a && worldIn.isBlockIndirectlyGettingPowered(x, y, z))
            {
                worldIn.setBlock(x, y, z, Blocks.lit_redstone_lamp, 0, 2);
            }
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            if (this.field_150171_a && !worldIn.isBlockIndirectlyGettingPowered(x, y, z))
            {
                worldIn.scheduleBlockUpdate(x, y, z, this, 4);
            }
            else if (!this.field_150171_a && worldIn.isBlockIndirectlyGettingPowered(x, y, z))
            {
                worldIn.setBlock(x, y, z, Blocks.lit_redstone_lamp, 0, 2);
            }
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote && this.field_150171_a && !worldIn.isBlockIndirectlyGettingPowered(x, y, z))
        {
            worldIn.setBlock(x, y, z, Blocks.redstone_lamp, 0, 2);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Blocks.redstone_lamp);
    }
}
