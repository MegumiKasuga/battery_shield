package cc.xypp.battery_shield.items;

import cc.xypp.battery_shield.BatteryShield;
import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.ILivingEntityA;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.data.UsageEvent;
import cc.xypp.battery_shield.items.ShieldCore.*;
import cc.xypp.battery_shield.utils.MiscUtil;
import cc.xypp.battery_shield.utils.TypeBinding;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public class Register {

    public static final HashMap<ShieldType, TypeBinding> TYPE = new HashMap<>();
    public static final HashMap<ResourceLocation, RegistryObject<Battery>> BATTERIES = new HashMap<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BatteryShield.MODID);
    // public static final RegistryObject<Item> smallBattery = ITEMS.register("small_battery", SmallBattery::new);
    // public static final RegistryObject<Item> battery = ITEMS.register("battery", Battery::new);
    // public static final RegistryObject<Item> phoenixBattery = ITEMS.register("phoenix_battery", PhoenixBattery::new);

    public static final RegistryObject<Item> steel = ITEMS.register("steel", () -> new Item(new Item.Properties().stacksTo(16)));
    // public static final RegistryObject<Item> shield_core_b = ITEMS.register("shield_core_b", ShieldCoreB::new);

    public static final RegistryObject<Battery> BATTERY = registerBattery("battery", 80, (item, level, entity) -> {
        ILivingEntityA e = (ILivingEntityA) entity;
        return Math.min(e.battery_shield$getShield() + Config.battery_value, e.battery_shield$getMaxShield());
    }, UsageEvent.CHARGE_DURATION, UsageEvent.CHARGE_START, MiscUtil::getMessage);

    public static final RegistryObject<Battery> SMALL_BATTERY = registerBattery("small_battery", 30, ((item, level, entity) -> {
        ILivingEntityA e = (ILivingEntityA) entity;
        return Math.min(e.battery_shield$getShield() + Config.small_battery_value, e.battery_shield$getMaxShield());
    }), UsageEvent.CHARGE_DURATION_SMALL, UsageEvent.CHARGE_START_SMALL, MiscUtil::getMessage);

    public static final RegistryObject<Battery> PHOENIX_BATTERY = registerBattery("phoenix_battery", 180, ((item, level, entity) -> {
        ILivingEntityA e = (ILivingEntityA) entity;
        return e.battery_shield$getMaxShield();
    }), UsageEvent.CHARGE_DURATION_PHOENIX, UsageEvent.CHARGE_START_PHOENIX, MiscUtil::getMessagePhoenix);

    public static final TypeBinding WHITE_BINDING = new TypeBinding(
            ShieldType.SHIELD_WHITE,
            (st) -> st == null || st == ShieldType.RAW,
            1,
            byPath("battery_fill_w"),
            () -> Config.shield_pre_level*2
    );

    public static final TypeBinding BLUE_BINDING = new TypeBinding(
            ShieldType.SHIELD_BLUE,
            (st) -> st == ShieldType.SHIELD_WHITE,
            2,
            byPath("battery_fill_b"),
            () -> Config.shield_pre_level*3
    );

    public static final TypeBinding PURPLE_BINDING = new TypeBinding(
            ShieldType.SHIELD_PERP,
            st -> st == ShieldType.SHIELD_BLUE,
            3,
            byPath("battery_fill_p"),
            () -> Config.shield_pre_level*4
    );

    public static final TypeBinding RED_BINDING = new TypeBinding(
            ShieldType.SHIELD_RED,
            st -> st == ShieldType.SHIELD_PERP,
            4,
            byPath("battery_fill_r"),
            () -> Config.shield_pre_level*5
    );

    public static final RegistryObject<ShieldCore> SHIELD_CORE_W = registerShield("shield_core_w",
            new Item.Properties().stacksTo(1), WHITE_BINDING);

    public static final RegistryObject<ShieldCore> SHIELD_CORE_B = registerShield("shield_core_b",
            new Item.Properties().stacksTo(1), BLUE_BINDING);

    public static final RegistryObject<ShieldCore> SHIELD_CORE_P = registerShield("shield_core_p",
            new Item.Properties().stacksTo(1), PURPLE_BINDING);

    public static final RegistryObject<ShieldCore> SHIELD_CORE_R = registerShield("shield_core_r",
            new Item.Properties().stacksTo(1), RED_BINDING);
    // public static final RegistryObject<Item> shield_core_r = ITEMS.register("shield_core_r", ShieldCoreR::new);
    // public static final RegistryObject<Item> shield_core_p = ITEMS.register("shield_core_p", ShieldCoreP::new);
    // public static final RegistryObject<Item> shield_core_w = ITEMS.register("shield_core_w", ShieldCoreW::new);

    public static final DeferredRegister<CreativeModeTab> TAB_DEFERRED_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BatteryShield.MODID);
    public static final RegistryObject<CreativeModeTab> BATTERY_SHIELD_TAB = TAB_DEFERRED_REGISTER.register("example", () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("item_group." + BatteryShield.MODID + ".main"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(BATTERY.get()))
            // Add default items to tab
            .displayItems((params, output) -> {
                output.accept(BATTERY.get());
                output.accept(SMALL_BATTERY.get());
                output.accept(PHOENIX_BATTERY.get());
                output.accept(steel.get());
                output.accept(SHIELD_CORE_B.get());
                output.accept(SHIELD_CORE_R.get());
                output.accept(SHIELD_CORE_P.get());
                output.accept(SHIELD_CORE_W.get());
            })
            .build()
    );

    public static ResourceLocation byPath(String path) {
        return new ResourceLocation(BatteryShield.MODID, path);
    }

    public static RegistryObject<ShieldCore> registerShield(String name, Item.Properties prop, TypeBinding type) {
        RegistryObject<ShieldCore> registryObject = ITEMS.register(name, () -> new ShieldCore(prop, type));
        TYPE.put(type.shield, type);
        return registryObject;
    }

    public static RegistryObject<Battery> registerBattery(String name, int useDuration, Battery.ChargeFunction func,
                                                          UsageEvent chargeDuration, UsageEvent startEvent, Supplier<String> msg) {
        RegistryObject<Battery> registryObject = ITEMS.register(name, () -> new Battery(useDuration, func, chargeDuration, startEvent, msg));
        BATTERIES.put(registryObject.getId(), registryObject);
        return registryObject;
    }
}
