package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class BlockMobSpawner extends BlockContainer
{
    private static final String __OBFID = "CL_00000269";

    protected BlockMobSpawner()
    {
        super(Material.rock);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMobSpawner();
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);
        int var8 = 15 + worldIn.rand.nextInt(15) + worldIn.rand.nextInt(15);
        this.dropXpOnBlockBreak(worldIn, x, y, z, var8);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemById(0);
    }
}
