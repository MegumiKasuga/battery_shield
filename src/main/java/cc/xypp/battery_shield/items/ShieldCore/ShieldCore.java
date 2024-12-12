package cc.xypp.battery_shield.items.ShieldCore;

import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.utils.TypeBinding;
import net.minecraft.world.item.Item;

public class ShieldCore extends Item implements IShieldCore {

    protected final TypeBinding type;
    public ShieldCore(Properties prop, TypeBinding type) {
        super(prop);
        this.type = type;
    }

    public TypeBinding getType() {
        return type;
    }

    @Override
    public ShieldType getCoreLevel() {
        return type.shield;
    }

    @Override
    public boolean isRequired(ShieldType type) {
        return this.type.required.test(type);
    }
}
