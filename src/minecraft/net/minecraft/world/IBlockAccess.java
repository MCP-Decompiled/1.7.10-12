package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;

public interface IBlockAccess
{
    Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_);

    TileEntity getTileEntity(int x, int y, int z);

    int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_);

    int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_);

    boolean isAirBlock(int x, int y, int z);

    BiomeGenBase getBiomeGenForCoords(int x, int z);

    int getHeight();

    boolean extendedLevelsInChunkCache();

    int isBlockProvidingPowerTo(int x, int y, int z, int directionIn);
}
