package net.deadlydiamond98.archipelago.compat.create;

import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;

import java.util.List;

@FieldsAreNonnullByDefault
public abstract class CreateDisplayableAPRequirement implements IHaveHoveringInformation {
    protected APMissingItem[] missingAPItems = new APMissingItem[0];
    protected BlockEntity blockEntity;

    protected CreateDisplayableAPRequirement(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    protected static LangBuilder langBuilder() {
        return new LangBuilder(APMod.MOD_ID);
    }

    protected static void cutAndAddTextComponent(Text text, List<Text> tooltip) {
        List<Text> cutTexts = TooltipHelper.cutTextComponent(text, FontHelper.Palette.GRAY_AND_WHITE);
        for (Text cutText : cutTexts) {
            langBuilder()
                    .add(cutText)
                    .forGoggles(tooltip);
        }
    }

    protected abstract boolean renderTooltip(List<Text> tooltip);

    @Override
    public boolean addToTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        return renderTooltip(tooltip);
    }
}
