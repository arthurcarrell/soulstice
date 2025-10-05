package com.asteristired.soulstice.Items;

import com.asteristired.soulstice.Items.ItemClasses.Soul;
import com.asteristired.soulstice.Items.ItemClasses.Soulcleave;
import com.asteristired.soulstice.Items.ItemClasses.Soulsparker;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static com.asteristired.soulstice.Soulstice.MOD_ID;

public class ModItems {
    public static Item SOULCLEAVE = Register(new Soulcleave(ToolMaterials.DIAMOND, 3, -3f, new Item.Settings()
    ), "soulcleave");
    public static Item SOUL = Register(new Soul(new Item.Settings().rarity(Rarity.UNCOMMON)
    ), "soul");

    public static Item SOULSPARKER = Register(new Soulsparker(new Item.Settings()
            .rarity(Rarity.UNCOMMON)
            .maxCount(1)
    ), "soulsparker");

    public static Item Register(Item item, String id) {
        // create the identifier
        Identifier ID = new Identifier(MOD_ID, id);

        // register the item
        Item registeredItem = Registry.register(Registries.ITEM, ID, item);

        // return it
        return registeredItem;
    }

    public static void AddToCreativeMenu() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(itemGroup -> {
         itemGroup.addAfter(Items.NETHERITE_AXE, ModItems.SOULCLEAVE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
            itemGroup.addAfter(Items.BRUSH, ModItems.SOULSPARKER);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> {
            itemGroup.addAfter(Items.BRUSH, ModItems.SOULCLEAVE);
        });


    }

    public static void Initialise() {}
}
