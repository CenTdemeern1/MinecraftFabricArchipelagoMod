package net.deadlydiamond98.archipelago.compat.create;

import net.minecraft.block.entity.BlockEntity;

public class CreateCraftingAPRequirement extends CreateSingleAPRequirement {
    public CreateCraftingAPRequirement(BlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected String getKind() {
        return "crafting";
    }
}
