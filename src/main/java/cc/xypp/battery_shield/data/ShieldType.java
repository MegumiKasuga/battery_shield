package cc.xypp.battery_shield.data;

import cc.xypp.battery_shield.items.Register;
import cc.xypp.battery_shield.utils.TypeBinding;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum ShieldType implements StringRepresentable {
    RAW(0, () -> null),
    SHIELD_WHITE(1, () -> Register.WHITE_BINDING),
    SHIELD_BLUE(2, () -> Register.BLUE_BINDING),
    SHIELD_PERP(3, () -> Register.PURPLE_BINDING),
    SHIELD_RED(4, () -> Register.RED_BINDING);

    public final int level;
    public final Supplier<TypeBinding> binding;

    ShieldType(int level, Supplier<TypeBinding> binding) {
        this.level = level;
        this.binding = binding;
    }

    public int getMaxValue() {
        return binding != null ? binding.get().getMaxValue() : 0;
    }

    public @Nullable TypeBinding getBinding() {
        return binding.get();
    }

    @Override
    public @NotNull String getSerializedName() {
        return switch (this) {
            case RAW -> "raw";
            case SHIELD_RED -> "shield_red";
            case SHIELD_BLUE -> "shield_blue";
            case SHIELD_PERP -> "shield_perp";
            case SHIELD_WHITE -> "shield_white";
        };
    }

    public static @Nullable ShieldType fromString(String str) {
        return switch (str) {
            case "raw" -> RAW;
            case "shield_red" -> SHIELD_RED;
            case "shield_blue" -> SHIELD_BLUE;
            case "shield_perp" -> SHIELD_PERP;
            case "shield_white" -> SHIELD_WHITE;
            default -> null;
        };
    }
}
