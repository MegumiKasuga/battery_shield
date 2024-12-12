package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.helper.AssetsManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class TypeBinding {
    public final ShieldType shield;
    public final Predicate<ShieldType> required;
    public final int coreLevel;
    public final ResourceLocation imageLocation;
    private final Supplier<Integer> maxValue;
    public TypeBinding(ShieldType type, Predicate<ShieldType> required, int coreLevel, ResourceLocation imageLocation, Supplier<Integer> maxValue) {
        this.shield = type;
        this.coreLevel = coreLevel;
        this.imageLocation = imageLocation;
        this.maxValue = maxValue;
        this.required = required;
    }

    public Integer getMaxValue() {
        return maxValue.get();
    }

    @OnlyIn(Dist.CLIENT)
    public AssetsManager.ImageAssets getImage() {
        return AssetsManager.IMAGES.get(this.imageLocation);
    }
}
