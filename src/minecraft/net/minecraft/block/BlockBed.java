package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockBed extends BlockDirectional
{
    public static final int[][] bedDirections = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};
    private IIcon[] iconEnd;
    private IIcon[] iconSide;
    private IIcon[] iconTop;
    private static final String __OBFID = "CL_00000198";

    public BlockBed()
    {
        super(Material.cloth);
        this.setBedBounds();
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            int var10 = worldIn.getBlockMetadata(x, y, z);

            if (!isBlockHeadOfBed(var10))
            {
                int var11 = getDirection(var10);
                x += bedDirections[var11][0];
                z += bedDirections[var11][1];

                if (worldIn.getBlock(x, y, z) != this)
                {
                    return true;
                }

                var10 = worldIn.getBlockMetadata(x, y, z);
            }

            if (worldIn.provider.canRespawnHere() && worldIn.getBiomeGenForCoords(x, z) != BiomeGenBase.hell)
            {
                if (isBedOccupied(var10))
                {
                    EntityPlayer var19 = null;
                    Iterator var12 = worldIn.playerEntities.iterator();

                    while (var12.hasNext())
                    {
                        EntityPlayer var21 = (EntityPlayer)var12.next();

                        if (var21.isPlayerSleeping())
                        {
                            ChunkCoordinates var14 = var21.playerLocation;

                            if (var14.posX == x && var14.posY == y && var14.posZ == z)
                            {
                                var19 = var21;
                            }
                        }
                    }

                    if (var19 != null)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                        return true;
                    }

                    setBedOccupied(worldIn, x, y, z, false);
                }

                EntityPlayer.EnumStatus var20 = player.sleepInBedAt(x, y, z);

                if (var20 == EntityPlayer.EnumStatus.OK)
                {
                    setBedOccupied(worldIn, x, y, z, true);
                    return true;
                }
                else
                {
                    if (var20 == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
                    }
                    else if (var20 == EntityPlayer.EnumStatus.NOT_SAFE)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
                    }

                    return true;
                }
            }
            else
            {
                double var18 = (double)x + 0.5D;
                double var13 = (double)y + 0.5D;
                double var15 = (double)z + 0.5D;
                worldIn.setBlockToAir(x, y, z);
                int var17 = getDirection(var10);
                x += bedDirections[var17][0];
                z += bedDirections[var17][1];

                if (worldIn.getBlock(x, y, z) == this)
                {
                    worldIn.setBlockToAir(x, y, z);
                    var18 = (var18 + (double)x + 0.5D) / 2.0D;
                    var13 = (var13 + (double)y + 0.5D) / 2.0D;
                    var15 = (var15 + (double)z + 0.5D) / 2.0D;
                }

                worldIn.newExplosion((Entity)null, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 5.0F, true, true);
                return true;
            }
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        if (side == 0)
        {
            return Blocks.planks.getBlockTextureFromSide(side);
        }
        else
        {
            int var3 = getDirection(meta);
            int var4 = Direction.bedDirection[var3][side];
            int var5 = isBlockHeadOfBed(meta) ? 1 : 0;
            return (var5 != 1 || var4 != 2) && (var5 != 0 || var4 != 3) ? (var4 != 5 && var4 != 4 ? this.iconTop[var5] : this.iconSide[var5]) : this.iconEnd[var5];
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.iconTop = new IIcon[] {reg.registerIcon(this.getTextureName() + "_feet_top"), reg.registerIcon(this.getTextureName() + "_head_top")};
        this.iconEnd = new IIcon[] {reg.registerIcon(this.getTextureName() + "_feet_end"), reg.registerIcon(this.getTextureName() + "_head_end")};
        this.iconSide = new IIcon[] {reg.registerIcon(this.getTextureName() + "_feet_side"), reg.registerIcon(this.getTextureName() + "_head_side")};
    }

    public int getRenderType()
    {
        return 14;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.setBedBounds();
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);
        int var7 = getDirection(var6);

        if (isBlockHeadOfBed(var6))
        {
            if (worldIn.getBlock(x - bedDirections[var7][0], y, z - bedDirections[var7][1]) != this)
            {
                worldIn.setBlockToAir(x, y, z);
            }
        }
        else if (worldIn.getBlock(x + bedDirections[var7][0], y, z + bedDirections[var7][1]) != this)
        {
            worldIn.setBlockToAir(x, y, z);

            if (!worldIn.isRemote)
            {
                this.dropBlockAsItem(worldIn, x, y, z, var6, 0);
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return isBlockHeadOfBed(meta) ? Item.getItemById(0) : Items.bed;
    }

    private void setBedBounds()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
    }

    public static boolean isBlockHeadOfBed(int meta)
    {
        return (meta & 8) != 0;
    }

    public static boolean isBedOccupied(int meta)
    {
        return (meta & 4) != 0;
    }

    public static void setBedOccupied(World worldIn, int x, int y, int z, boolean occupied)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);

        if (occupied)
        {
            var5 |= 4;
        }
        else
        {
            var5 &= -5;
        }

        worldIn.setBlockMetadataWithNotify(x, y, z, var5, 4);
    }

    public static ChunkCoordinates getSafeExitLocation(World worldIn, int x, int y, int z, int safeIndex)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        int var6 = BlockDirectional.getDirection(var5);

        for (int var7 = 0; var7 <= 1; ++var7)
        {
            int var8 = x - bedDirections[var6][0] * var7 - 1;
            int var9 = z - bedDirections[var6][1] * var7 - 1;
            int var10 = var8 + 2;
            int var11 = var9 + 2;

            for (int var12 = var8; var12 <= var10; ++var12)
            {
                for (int var13 = var9; var13 <= var11; ++var13)
                {
                    if (World.doesBlockHaveSolidTopSurface(worldIn, var12, y - 1, var13) && !worldIn.getBlock(var12, y, var13).getMaterial().isOpaque() && !worldIn.getBlock(var12, y + 1, var13).getMaterial().isOpaque())
                    {
                        if (safeIndex <= 0)
                        {
                            return new ChunkCoordinates(var12, y, var13);
                        }

                        --safeIndex;
                    }
                }
            }
        }

        return null;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        if (!isBlockHeadOfBed(meta))
        {
            super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, 0);
        }
    }

    public int getMobilityFlag()
    {
        return 1;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.bed;
    }

    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && isBlockHeadOfBed(meta))
        {
            int var7 = getDirection(meta);
            x -= bedDirections[var7][0];
            z -= bedDirections[var7][1];

            if (worldIn.getBlock(x, y, z) == this)
            {
                worldIn.setBlockToAir(x, y, z);
            }
        }
    }
}
