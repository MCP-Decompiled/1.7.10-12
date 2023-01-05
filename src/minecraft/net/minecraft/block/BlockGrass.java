package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockGrass extends Block implements IGrowable
{
    private static final Logger logger = LogManager.getLogger();
    private IIcon field_149991_b;
    private IIcon field_149993_M;
    private IIcon field_149994_N;
    private static final String __OBFID = "CL_00000251";

    protected BlockGrass()
    {
        super(Material.grass);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_149991_b : (side == 0 ? Blocks.dirt.getBlockTextureFromSide(side) : this.blockIcon);
    }

    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (side == 1)
        {
            return this.field_149991_b;
        }
        else if (side == 0)
        {
            return Blocks.dirt.getBlockTextureFromSide(side);
        }
        else
        {
            Material var6 = worldIn.getBlock(x, y + 1, z).getMaterial();
            return var6 != Material.snow && var6 != Material.craftedSnow ? this.blockIcon : this.field_149993_M;
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.field_149991_b = reg.registerIcon(this.getTextureName() + "_top");
        this.field_149993_M = reg.registerIcon(this.getTextureName() + "_side_snowed");
        this.field_149994_N = reg.registerIcon(this.getTextureName() + "_side_overlay");
    }

    public int getBlockColor()
    {
        double var1 = 0.5D;
        double var3 = 1.0D;
        return ColorizerGrass.getGrassColor(var1, var3);
    }

    public int getRenderColor(int meta)
    {
        return this.getBlockColor();
    }

    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;

        for (int var8 = -1; var8 <= 1; ++var8)
        {
            for (int var9 = -1; var9 <= 1; ++var9)
            {
                int var10 = worldIn.getBiomeGenForCoords(x + var9, z + var8).getBiomeGrassColor(x + var9, y, z + var8);
                var5 += (var10 & 16711680) >> 16;
                var6 += (var10 & 65280) >> 8;
                var7 += var10 & 255;
            }
        }

        return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.getBlockLightValue(x, y + 1, z) < 4 && worldIn.getBlock(x, y + 1, z).getLightOpacity() > 2)
            {
                worldIn.setBlock(x, y, z, Blocks.dirt);
            }
            else if (worldIn.getBlockLightValue(x, y + 1, z) >= 9)
            {
                for (int var6 = 0; var6 < 4; ++var6)
                {
                    int var7 = x + random.nextInt(3) - 1;
                    int var8 = y + random.nextInt(5) - 3;
                    int var9 = z + random.nextInt(3) - 1;
                    Block var10 = worldIn.getBlock(var7, var8 + 1, var9);

                    if (worldIn.getBlock(var7, var8, var9) == Blocks.dirt && worldIn.getBlockMetadata(var7, var8, var9) == 0 && worldIn.getBlockLightValue(var7, var8 + 1, var9) >= 4 && var10.getLightOpacity() <= 2)
                    {
                        worldIn.setBlock(var7, var8, var9, Blocks.grass);
                    }
                }
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Blocks.dirt.getItemDropped(0, random, fortune);
    }

    public static IIcon getIconSideOverlay()
    {
        return Blocks.grass.field_149994_N;
    }

    public boolean canFertilize(World worldIn, int x, int y, int z, boolean isClient)
    {
        return true;
    }

    public boolean shouldFertilize(World worldIn, Random random, int x, int y, int z)
    {
        return true;
    }

    public void fertilize(World worldIn, Random random, int x, int y, int z)
    {
        int var6 = 0;

        while (var6 < 128)
        {
            int var7 = x;
            int var8 = y + 1;
            int var9 = z;
            int var10 = 0;

            while (true)
            {
                if (var10 < var6 / 16)
                {
                    var7 += random.nextInt(3) - 1;
                    var8 += (random.nextInt(3) - 1) * random.nextInt(3) / 2;
                    var9 += random.nextInt(3) - 1;

                    if (worldIn.getBlock(var7, var8 - 1, var9) == Blocks.grass && !worldIn.getBlock(var7, var8, var9).isNormalCube())
                    {
                        ++var10;
                        continue;
                    }
                }
                else if (worldIn.getBlock(var7, var8, var9).blockMaterial == Material.air)
                {
                    if (random.nextInt(8) != 0)
                    {
                        if (Blocks.tallgrass.canBlockStay(worldIn, var7, var8, var9))
                        {
                            worldIn.setBlock(var7, var8, var9, Blocks.tallgrass, 1, 3);
                        }
                    }
                    else
                    {
                        String var13 = worldIn.getBiomeGenForCoords(var7, var9).func_150572_a(random, var7, var8, var9);
                        logger.debug("Flower in " + worldIn.getBiomeGenForCoords(var7, var9).biomeName + ": " + var13);
                        BlockFlower var11 = BlockFlower.func_149857_e(var13);

                        if (var11 != null && var11.canBlockStay(worldIn, var7, var8, var9))
                        {
                            int var12 = BlockFlower.func_149856_f(var13);
                            worldIn.setBlock(var7, var8, var9, var11, var12, 3);
                        }
                    }
                }

                ++var6;
                break;
            }
        }
    }
}
