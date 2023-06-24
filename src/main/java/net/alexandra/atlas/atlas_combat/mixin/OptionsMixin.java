package net.alexandra.atlas.atlas_combat.mixin;

import net.alexandra.atlas.atlas_combat.AtlasClient;
import net.alexandra.atlas.atlas_combat.config.ShieldIndicatorStatus;
import net.alexandra.atlas.atlas_combat.extensions.IOptions;
import net.minecraft.client.Options;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsMixin implements IOptions {

	@Inject(method = "processOptions", at = @At(value = "HEAD"))
	public void injectOptions(Options.FieldAccess visitor, CallbackInfo ci) {
		AtlasClient.autoAttack = visitor.process("autoAttack", AtlasClient.autoAttack);
		AtlasClient.shieldCrouch = visitor.process("shieldCrouch", AtlasClient.shieldCrouch);
		AtlasClient.rhythmicAttacks = visitor.process("rhythmicAttacks", AtlasClient.rhythmicAttacks);
		AtlasClient.protectionIndicator = visitor.process("protIndicator", AtlasClient.protectionIndicator);
		AtlasClient.fishingRodLegacy = visitor.process("fishingRodLegacy", AtlasClient.fishingRodLegacy);
		AtlasClient.attackIndicatorValue = Mth.clamp(visitor.process("attackIndicatorValue", AtlasClient.attackIndicatorValue), 0.1, 2.0);
		AtlasClient.shieldIndicator = visitor.process("shieldIndicator", AtlasClient.shieldIndicator, ShieldIndicatorStatus::byId, ShieldIndicatorStatus::getId);
	}

	@Override
	public Boolean autoAttack() {
		return AtlasClient.autoAttack;
	}
	@Override
	public Boolean shieldCrouch() {
		return AtlasClient.shieldCrouch;
	}

	@Override
	public Boolean rhythmicAttacks() {
		return AtlasClient.rhythmicAttacks;
	}
	@Override
	public Boolean protIndicator() {
		return AtlasClient.protectionIndicator;
	}
	@Override
	public Boolean fishingRodLegacy() {
		return AtlasClient.fishingRodLegacy;
	}
	@Override
	public ShieldIndicatorStatus shieldIndicator() {
		return AtlasClient.shieldIndicator;
	}
	@Override
	public Double attackIndicatorValue() {
		return AtlasClient.attackIndicatorValue;
	}
}
