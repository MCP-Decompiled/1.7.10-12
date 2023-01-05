package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.ResourceLocation;

public class SoundEventAccessorComposite implements ISoundEventAccessor
{
    private final List soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation field_148735_c;
    private final SoundCategory field_148732_d;
    private double eventPitch;
    private double eventVolume;
    private static final String __OBFID = "CL_00001146";

    public SoundEventAccessorComposite(ResourceLocation p_i45120_1_, double p_i45120_2_, double p_i45120_4_, SoundCategory p_i45120_6_)
    {
        this.field_148735_c = p_i45120_1_;
        this.eventVolume = p_i45120_4_;
        this.eventPitch = p_i45120_2_;
        this.field_148732_d = p_i45120_6_;
    }

    public int func_148721_a()
    {
        int var1 = 0;
        ISoundEventAccessor var3;

        for (Iterator var2 = this.soundPool.iterator(); var2.hasNext(); var1 += var3.func_148721_a())
        {
            var3 = (ISoundEventAccessor)var2.next();
        }

        return var1;
    }

    public SoundPoolEntry func_148720_g()
    {
        int var1 = this.func_148721_a();

        if (!this.soundPool.isEmpty() && var1 != 0)
        {
            int var2 = this.rnd.nextInt(var1);
            Iterator var3 = this.soundPool.iterator();
            ISoundEventAccessor var4;

            do
            {
                if (!var3.hasNext())
                {
                    return SoundHandler.missing_sound;
                }

                var4 = (ISoundEventAccessor)var3.next();
                var2 -= var4.func_148721_a();
            }
            while (var2 >= 0);

            SoundPoolEntry var5 = (SoundPoolEntry)var4.func_148720_g();
            var5.setPitch(var5.getPitch() * this.eventPitch);
            var5.setVolume(var5.getVolume() * this.eventVolume);
            return var5;
        }
        else
        {
            return SoundHandler.missing_sound;
        }
    }

    public void addSoundToEventPool(ISoundEventAccessor p_148727_1_)
    {
        this.soundPool.add(p_148727_1_);
    }

    public ResourceLocation getSoundEventLocation()
    {
        return this.field_148735_c;
    }

    public SoundCategory getSoundCategory()
    {
        return this.field_148732_d;
    }
}
