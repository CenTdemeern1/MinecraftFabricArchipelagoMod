package net.deadlydiamond98.archipelago.util.mixinterfaces;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.archipelago.items.SavedArchipelagoItems;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.List;

public interface IDisplaysMissingAPItemsForCreate extends IMissingAPItemsAccessor {
    @Unique
    private static LangBuilder langBuilder() {
        return new LangBuilder(APMod.MOD_ID);
    }

    @Unique
    private static void cutAndAddTextComponent(Text text, List<Text> tooltip) {
        List<Text> cutTexts = TooltipHelper.cutTextComponent(text, FontHelper.Palette.GRAY_AND_WHITE);
        for (Text cutText : cutTexts) {
            langBuilder()
                    .add(cutText)
                    .forGoggles(tooltip);
        }
    }

    default boolean archipelago$addMissingAPItemTooltip(List<Text> tooltip, boolean originalOutput) {
        APMissingItem[] missingItems = archipelago$getMissingAPItems();
        if (missingItems.length == 0) return originalOutput;
        else if (missingItems.length == 1) {
            APMissingItem missingItem = missingItems[0];
            String itemName = SavedArchipelagoItems.ID_TO_NAME_MAP.get(missingItem.name);

            langBuilder()
                    .translate("check.requires_check", itemName)
                    .style(Formatting.GOLD)
                    .forGoggles(tooltip);

            String translationKey = String.format(
                    "archipelago.compat.create.%s.requires_check%s",
                    ((Object) this instanceof BlockBreakingKineticBlockEntity) ? "breaking" : "crafting",
                    missingItem.amount == 0 ? "" : ".progressive"
            );
            MutableText description = Text.translatable(translationKey, missingItem.attemptedCraft, itemName, missingItem.amount);
            cutAndAddTextComponent(description, tooltip);

            return true;
        } else {
            long unlockedItems = Arrays.stream(missingItems).filter(i -> !i.isStillMissing()).count();
            if (unlockedItems == missingItems.length) return originalOutput; // State has changed in the meantime

            langBuilder()
                    .translate("compat.create.crafting.some_missing.heading")
                    .style(Formatting.GOLD)
                    .forGoggles(tooltip);

            MutableText description = Text.translatable(
                    "archipelago.compat.create.crafting.some_missing.description",
                    unlockedItems,
                    missingItems.length
            );
            cutAndAddTextComponent(description, tooltip);

            for (APMissingItem missingItem : missingItems) {
                String translationKey = String.format(
                        "compat.create.crafting.some_missing.item.%s",
                        missingItem.isStillMissing()
                                ? "locked"
                                : "unlocked"
                );
                String reqTranslationKey = String.format(
                        "compat.create.crafting.some_missing.requirement%s",
                        missingItem.amount == 0 ? "" : ".progressive"
                );
                String itemName = SavedArchipelagoItems.ID_TO_NAME_MAP.get(missingItem.name);
                Formatting formatting = missingItem.isStillMissing() ? Formatting.RED : Formatting.GREEN;

                tooltip.add(Text.empty());
                langBuilder()
                        .translate(translationKey, missingItem.attemptedCraft)
                        .style(formatting)
                        .forGoggles(tooltip, 2);
                langBuilder()
                        .translate(reqTranslationKey, itemName, missingItem.amount)
                        .style(formatting)
                        .forGoggles(tooltip, 4);
            }
            return true;
        }
    }
}
