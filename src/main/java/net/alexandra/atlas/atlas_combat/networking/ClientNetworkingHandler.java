package net.alexandra.atlas.atlas_combat.networking;

import net.alexandra.atlas.atlas_combat.config.AtlasConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.TextComponent;

import static net.alexandra.atlas.atlas_combat.AtlasCombat.modDetectionNetworkChannel;

public class ClientNetworkingHandler {
	public ClientNetworkingHandler() {

		ClientPlayNetworking.registerGlobalReceiver(modDetectionNetworkChannel,(client, handler, buf, responseSender) -> {
			int booleans = 0;
			int ints = 0;
			AtlasConfig.toolsAreWeapons = buf.getBoolean(booleans);
			booleans++;
			if(AtlasConfig.bedrockBlockReach != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.bedrockBlockReach;
				AtlasConfig.bedrockBlockReach = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.bedrockBlockReach = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			AtlasConfig.refinedCoyoteTime = buf.getBoolean(booleans);
			booleans++;
			AtlasConfig.midairKB = buf.getBoolean(booleans);
			booleans++;
			AtlasConfig.fishingHookKB = buf.getBoolean(booleans);
			booleans++;
			if(AtlasConfig.fistDamage != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.fistDamage;
				AtlasConfig.fistDamage = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.fistDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			AtlasConfig.swordBlocking = buf.getBoolean(booleans);
			booleans++;
			AtlasConfig.sprintCritsEnabled = buf.getBoolean(booleans);
			booleans++;
			AtlasConfig.saturationHealing = buf.getBoolean(booleans);
			booleans++;
			AtlasConfig.autoAttackAllowed = buf.getBoolean(booleans);
			booleans++;
			if(AtlasConfig.configOnlyWeapons != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.configOnlyWeapons;
				AtlasConfig.configOnlyWeapons = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.configOnlyWeapons = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			if(AtlasConfig.axeReachBuff != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.axeReachBuff;
				AtlasConfig.axeReachBuff = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.axeReachBuff = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			if(AtlasConfig.blockReach != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.blockReach;
				AtlasConfig.blockReach = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.blockReach = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			if(AtlasConfig.attackReach != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.attackReach;
				AtlasConfig.attackReach = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.attackReach = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			AtlasConfig.attackSpeed = buf.getBoolean(booleans);
			booleans++;
			if(AtlasConfig.ctsAttackBalancing != buf.getBoolean(booleans)) {
				boolean oldValue = AtlasConfig.ctsAttackBalancing;
				AtlasConfig.ctsAttackBalancing = buf.getBoolean(booleans);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.ctsAttackBalancing = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			booleans++;
			AtlasConfig.eatingInterruption = buf.getBoolean(booleans);
			AtlasConfig.swordProtectionEfficacy = buf.getInt(ints);
			ints++;
			AtlasConfig.potionUseDuration = buf.getInt(ints);
			ints++;
			AtlasConfig.honeyBottleUseDuration = buf.getInt(ints);
			ints++;
			AtlasConfig.milkBucketUseDuration = buf.getInt(ints);
			ints++;
			AtlasConfig.stewUseDuration = buf.getInt(ints);
			ints++;
			AtlasConfig.instantHealthBonus = buf.getInt(ints);
			ints++;
			AtlasConfig.eggItemCooldown = buf.getInt(ints);
			ints++;
			AtlasConfig.snowballItemCooldown = buf.getInt(ints);
			AtlasConfig.snowballDamage = buf.getFloat(0);
			AtlasConfig.eggDamage = buf.getFloat(1);
			AtlasConfig.bowUncertainty = buf.getFloat(2);
			if(AtlasConfig.swordAttackDamage != buf.getFloat(3)) {
				float oldValue = AtlasConfig.swordAttackDamage;
				AtlasConfig.swordAttackDamage = buf.getFloat(3);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.swordAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.axeAttackDamage != buf.getFloat(4)) {
				float oldValue = AtlasConfig.axeAttackDamage;
				AtlasConfig.axeAttackDamage = buf.getFloat(4);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.axeAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.knifeAttackDamage != buf.getFloat(5)) {
				float oldValue = AtlasConfig.knifeAttackDamage;
				AtlasConfig.knifeAttackDamage = buf.getFloat(5);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.knifeAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.baseHoeAttackDamage != buf.getFloat(6)) {
				float oldValue = AtlasConfig.baseHoeAttackDamage;
				AtlasConfig.baseHoeAttackDamage = buf.getFloat(6);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.baseHoeAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.ironDiaHoeAttackDamage != buf.getFloat(7)) {
				float oldValue = AtlasConfig.ironDiaHoeAttackDamage;
				AtlasConfig.ironDiaHoeAttackDamage = buf.getFloat(7);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.ironDiaHoeAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.netheriteHoeAttackDamage != buf.getFloat(8)) {
				float oldValue = AtlasConfig.netheriteHoeAttackDamage;
				AtlasConfig.netheriteHoeAttackDamage = buf.getFloat(8);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.netheriteHoeAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.tridentAttackDamage != buf.getFloat(9)) {
				float oldValue = AtlasConfig.tridentAttackDamage;
				AtlasConfig.tridentAttackDamage = buf.getFloat(9);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.tridentAttackDamage = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.swordAttackSpeed != buf.getFloat(10)) {
				float oldValue = AtlasConfig.swordAttackSpeed;
				AtlasConfig.swordAttackSpeed = buf.getFloat(10);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.swordAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.axeAttackSpeed != buf.getFloat(11)) {
				float oldValue = AtlasConfig.axeAttackSpeed;
				AtlasConfig.axeAttackSpeed = buf.getFloat(11);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.axeAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.woodenHoeAttackSpeed != buf.getFloat(12)) {
				float oldValue = AtlasConfig.woodenHoeAttackSpeed;
				AtlasConfig.woodenHoeAttackSpeed = buf.getFloat(12);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.woodenHoeAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.stoneHoeAttackSpeed != buf.getFloat(13)) {
				float oldValue = AtlasConfig.stoneHoeAttackSpeed;
				AtlasConfig.stoneHoeAttackSpeed = buf.getFloat(13);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.stoneHoeAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.ironHoeAttackSpeed != buf.getFloat(14)) {
				float oldValue = AtlasConfig.ironHoeAttackSpeed;
				AtlasConfig.ironHoeAttackSpeed = buf.getFloat(14);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.ironHoeAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.goldDiaNethHoeAttackSpeed != buf.getFloat(15)) {
				float oldValue = AtlasConfig.goldDiaNethHoeAttackSpeed;
				AtlasConfig.goldDiaNethHoeAttackSpeed = buf.getFloat(15);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.goldDiaNethHoeAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.knifeAttackSpeed != buf.getFloat(16)) {
				float oldValue = AtlasConfig.knifeAttackSpeed;
				AtlasConfig.knifeAttackSpeed = buf.getFloat(16);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.knifeAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.tridentAttackSpeed != buf.getFloat(17)) {
				float oldValue = AtlasConfig.tridentAttackSpeed;
				AtlasConfig.tridentAttackSpeed = buf.getFloat(17);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.tridentAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.defaultAttackSpeed != buf.getFloat(18)) {
				float oldValue = AtlasConfig.defaultAttackSpeed;
				AtlasConfig.defaultAttackSpeed = buf.getFloat(18);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.defaultAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.slowestToolAttackSpeed != buf.getFloat(19)) {
				float oldValue = AtlasConfig.slowestToolAttackSpeed;
				AtlasConfig.slowestToolAttackSpeed = buf.getFloat(19);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.slowestToolAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.slowToolAttackSpeed != buf.getFloat(20)) {
				float oldValue = AtlasConfig.slowToolAttackSpeed;
				AtlasConfig.slowToolAttackSpeed = buf.getFloat(20);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.slowToolAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.fastToolAttackSpeed != buf.getFloat(21)) {
				float oldValue = AtlasConfig.fastToolAttackSpeed;
				AtlasConfig.fastToolAttackSpeed = buf.getFloat(21);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.fastToolAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
			if(AtlasConfig.fastestToolAttackSpeed != buf.getFloat(22)) {
				float oldValue = AtlasConfig.fastestToolAttackSpeed;
				AtlasConfig.fastestToolAttackSpeed = buf.getFloat(22);
				AtlasConfig.write("atlas-combat");
				AtlasConfig.fastestToolAttackSpeed = oldValue;
				handler.getConnection().disconnect(new TextComponent("Cannot connect to this server without restarting due to a config mismatch!"));
				return;
			}
		});
	}
}
