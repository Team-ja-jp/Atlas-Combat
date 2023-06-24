package net.alexandra.atlas.atlas_combat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.alexandra.atlas.atlas_combat.extensions.PlayerExtensions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
	@Final
	@Shadow
	protected ServerPlayer player;
	@ModifyExpressionValue(
			method = "handleBlockBreakAction",
			require = 1, allow = 1, at = @At(value = "CONSTANT", args = "doubleValue=36.0"))
	public double getActualReachDistance(double constant) {
		return ((PlayerExtensions)player).getSquaredReach(player, constant);
	}
}
