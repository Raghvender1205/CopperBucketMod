package org.rc.copperbucket;

import net.fabricmc.api.ModInitializer;

public class Copperbucket implements ModInitializer {

    public static final String MOD_ID = "copperbucket";

    @Override
    public void onInitialize() {
        ModItems.register();
    }
}
