package net.deadlydiamond98.archipelago.compat.create;

import net.minecraft.block.entity.BlockEntity;

public class CreateBreakingAPRequirement extends CreateSingleAPRequirement {
    public CreateBreakingAPRequirement(BlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected String getKind() {
        return "breaking";
    }
}
