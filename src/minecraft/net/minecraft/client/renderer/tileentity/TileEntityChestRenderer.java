package net.minecraft.client.renderer.tileentity;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
    private static final ResourceLocation textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
    private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
    private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
    private static final ResourceLocation textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
    private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    private ModelChest simpleChest = new ModelChest();
    private ModelChest largeChest = new ModelLargeChest();
    private boolean isChristams;
    private static final String __OBFID = "CL_00000965";

    public TileEntityChestRenderer()
    {
        Calendar var1 = Calendar.getInstance();

        if (var1.get(2) + 1 == 12 && var1.get(5) >= 24 && var1.get(5) <= 26)
        {
            this.isChristams = true;
        }
    }

    public void renderTileEntityAt(TileEntityChest p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        int var9;

        if (!p_147500_1_.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            Block var10 = p_147500_1_.getBlockType();
            var9 = p_147500_1_.getBlockMetadata();

            if (var10 instanceof BlockChest && var9 == 0)
            {
                ((BlockChest)var10).initMetadata(p_147500_1_.getWorld(), p_147500_1_.xCoord, p_147500_1_.yCoord, p_147500_1_.zCoord);
                var9 = p_147500_1_.getBlockMetadata();
            }

            p_147500_1_.checkForAdjacentChests();
        }

        if (p_147500_1_.adjacentChestZNeg == null && p_147500_1_.adjacentChestXNeg == null)
        {
            ModelChest var14;

            if (p_147500_1_.adjacentChestXPos == null && p_147500_1_.adjacentChestZPos == null)
            {
                var14 = this.simpleChest;

                if (p_147500_1_.getChestType() == 1)
                {
                    this.bindTexture(textureTrapped);
                }
                else if (this.isChristams)
                {
                    this.bindTexture(textureChristmas);
                }
                else
                {
                    this.bindTexture(textureNormal);
                }
            }
            else
            {
                var14 = this.largeChest;

                if (p_147500_1_.getChestType() == 1)
                {
                    this.bindTexture(textureTrappedDouble);
                }
                else if (this.isChristams)
                {
                    this.bindTexture(textureChristmasDouble);
                }
                else
                {
                    this.bindTexture(textureNormalDouble);
                }
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)p_147500_2_, (float)p_147500_4_ + 1.0F, (float)p_147500_6_ + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short var11 = 0;

            if (var9 == 2)
            {
                var11 = 180;
            }

            if (var9 == 3)
            {
                var11 = 0;
            }

            if (var9 == 4)
            {
                var11 = 90;
            }

            if (var9 == 5)
            {
                var11 = -90;
            }

            if (var9 == 2 && p_147500_1_.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && p_147500_1_.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = p_147500_1_.prevLidAngle + (p_147500_1_.lidAngle - p_147500_1_.prevLidAngle) * p_147500_8_;
            float var13;

            if (p_147500_1_.adjacentChestZNeg != null)
            {
                var13 = p_147500_1_.adjacentChestZNeg.prevLidAngle + (p_147500_1_.adjacentChestZNeg.lidAngle - p_147500_1_.adjacentChestZNeg.prevLidAngle) * p_147500_8_;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (p_147500_1_.adjacentChestXNeg != null)
            {
                var13 = p_147500_1_.adjacentChestXNeg.prevLidAngle + (p_147500_1_.adjacentChestXNeg.lidAngle - p_147500_1_.adjacentChestXNeg.prevLidAngle) * p_147500_8_;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            var12 = 1.0F - var12;
            var12 = 1.0F - var12 * var12 * var12;
            var14.chestLid.rotateAngleX = -(var12 * (float)Math.PI / 2.0F);
            var14.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        this.renderTileEntityAt((TileEntityChest)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
    }
}
