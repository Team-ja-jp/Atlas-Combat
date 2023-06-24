package net.alexandra.atlas.atlas_combat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.alexandra.atlas.atlas_combat.AtlasCombat;
import net.alexandra.atlas.atlas_combat.extensions.PlayerExtensions;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketMixin {
	@Shadow
	public ServerPlayer player;

	@Inject(method = "handleInteract", at = @At(value = "HEAD"))
	public void injectPlayer(ServerboundInteractPacket packet, CallbackInfo ci) {
		AtlasCombat.player = player;
	}

	@ModifyExpressionValue(
		method = "handleInteract",
		require = 1, allow = 1, at = @At(value = "CONSTANT", args = "doubleValue=36.0"))
	private double getActualAttackRange(final double reachDistance) {
		return ((PlayerExtensions)player).getSquaredReach(player, Mth.square(Math.sqrt(reachDistance) - 1));
	}
	@ModifyExpressionValue(
			method = "handleUseItemOn",
			require = 1, allow = 1, at = @At(value = "CONSTANT", args = "doubleValue=64.0"))
	private double getActualReachDistance(final double reachDistance) {
		return ((PlayerExtensions)player).getSquaredReach(player, reachDistance);
	}
}
