package org.rc.copperbucket;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.rc.copperbucket.item.CopperBucketItem;
import org.rc.copperbucket.item.CopperWaterBucketItem;

public class ModItems {

    // Keys
    public static final Identifier COPPER_BUCKET_ID =
            Identifier.of(Copperbucket.MOD_ID, "copper_bucket");
    public static final RegistryKey<Item> COPPER_BUCKET_KEY =
            RegistryKey.of(RegistryKeys.ITEM, COPPER_BUCKET_ID);

    public static final Identifier COPPER_WATER_BUCKET_ID =
            Identifier.of(Copperbucket.MOD_ID, "copper_water_bucket");
    public static final RegistryKey<Item> COPPER_WATER_BUCKET_KEY =
            RegistryKey.of(RegistryKeys.ITEM, COPPER_WATER_BUCKET_ID);

    // Items
    public static Item COPPER_BUCKET;
    public static Item COPPER_WATER_BUCKET;

    public static void register() {
        COPPER_BUCKET = new CopperBucketItem(
                new Item.Settings()
                        .maxCount(1)
                        .registryKey(COPPER_BUCKET_KEY)
        );
        Registry.register(Registries.ITEM, COPPER_BUCKET_KEY, COPPER_BUCKET);

        COPPER_WATER_BUCKET = new CopperWaterBucketItem(
                Fluids.WATER,
                new Item.Settings()
                        .maxCount(1)
                        .registryKey(COPPER_WATER_BUCKET_KEY),
                COPPER_BUCKET
        );
        Registry.register(Registries.ITEM, COPPER_WATER_BUCKET_KEY, COPPER_WATER_BUCKET);
    }
}
