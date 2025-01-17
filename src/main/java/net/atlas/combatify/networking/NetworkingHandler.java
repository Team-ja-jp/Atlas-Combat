package net.atlas.combatify.networking;

import com.google.common.collect.ArrayListMultimap;
import net.atlas.combatify.Combatify;
import net.atlas.combatify.config.ConfigurableItemData;
import net.atlas.combatify.config.ItemConfig;
import net.atlas.combatify.extensions.*;
import net.atlas.combatify.item.NewAttributes;
import net.atlas.combatify.item.WeaponType;
import net.atlas.combatify.util.MethodHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static net.atlas.combatify.Combatify.*;

public class NetworkingHandler {

	public NetworkingHandler() {
		ServerPlayNetworking.registerGlobalReceiver(modDetectionNetworkChannel,(server, player, handler, buf, responseSender) -> {
		});
		ServerPlayConnectionEvents.DISCONNECT.register(modDetectionNetworkChannel, (handler, server) -> {
			if (unmoddedPlayers.contains(handler.player.getUUID())) {
				Timer timer = scheduleHitResult.get(handler.getPlayer().getUUID());
				timer.cancel();
				timer.purge();
			}
		});
		ServerPlayNetworking.registerGlobalReceiver(ServerboundMissPacket.TYPE, (packet, player, responseSender) -> {
			final ServerLevel serverLevel = player.serverLevel();
			player.resetLastActionTime();
			if (!serverLevel.getWorldBorder().isWithinBounds(player.blockPosition())) {
				return;
			}
			double d = MethodHandler.getCurrentAttackReach(player, 1.0F) + 1;
			d *= d;
			if(!player.hasLineOfSight(player)) {
				d = 6.25;
			}

			AABB aABB = player.getBoundingBox();
			Vec3 eyePos = player.getEyePosition(0.0F);
			eyePos.distanceToSqr(MethodHandler.getNearestPointTo(aABB, eyePos));
			double dist = 0;
			if (dist < d) {
				((PlayerExtensions)player).attackAir();
			}

		});
		ServerPlayConnectionEvents.JOIN.register(modDetectionNetworkChannel,(handler, sender, server) -> {
			boolean bl = CONFIG.configOnlyWeapons() || CONFIG.defender() || CONFIG.piercer() || !CONFIG.letVanillaConnect();
			if(!ServerPlayNetworking.canSend(handler.player, ItemConfigPacket.TYPE)) {
				if(bl) {
					handler.player.connection.disconnect(Component.literal("Combatify needs to be installed on the client to join this server!"));
					return;
				}
				Combatify.unmoddedPlayers.add(handler.player.getUUID());
				Combatify.isPlayerAttacking.put(handler.player.getUUID(), true);
				Combatify.finalizingAttack.put(handler.player.getUUID(), true);
				scheduleHitResult.put(handler.player.getUUID(), new Timer());
				Combatify.LOGGER.info("Unmodded player joined: " + handler.player.getUUID());
				return;
			}
			if (unmoddedPlayers.contains(handler.player.getUUID())) {
				unmoddedPlayers.remove(handler.player.getUUID());
				isPlayerAttacking.remove(handler.player.getUUID());
				finalizingAttack.remove(handler.player.getUUID());
			}
			ServerPlayNetworking.send(handler.player, new ItemConfigPacket(ITEMS));
			Combatify.LOGGER.info("Config packet sent to client.");
		});
		ModifyItemAttributeModifiersCallback.EVENT.register(modDetectionNetworkChannel, (stack, slot, attributeModifiers) -> {
			Item item = stack.getItem();
			if (ITEMS.configuredItems.containsKey(item) && slot == EquipmentSlot.MAINHAND) {
				ConfigurableItemData configurableItemData = ITEMS.configuredItems.get(item);
				if (configurableItemData.type != null) {
					if (attributeModifiers.containsKey(Attributes.ATTACK_DAMAGE)) {
						List<Integer> indexes = new ArrayList<>();
						List<AttributeModifier> modifiers = attributeModifiers.get(Attributes.ATTACK_DAMAGE).stream().toList();
						for (AttributeModifier modifier : modifiers)
							if (modifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID || modifier.getId() == WeaponType.BASE_ATTACK_DAMAGE_UUID)
								indexes.add(modifiers.indexOf(modifier));
						if (!indexes.isEmpty())
							for (Integer index : indexes)
								attributeModifiers.remove(Attributes.ATTACK_DAMAGE, modifiers.get(index));
					}
					if (attributeModifiers.containsKey(Attributes.ATTACK_SPEED)) {
						List<Integer> indexes = new ArrayList<>();
						List<AttributeModifier> modifiers = attributeModifiers.get(Attributes.ATTACK_SPEED).stream().toList();
						for (AttributeModifier modifier : modifiers)
							if (modifier.getId() == Item.BASE_ATTACK_SPEED_UUID || modifier.getId() == WeaponType.BASE_ATTACK_SPEED_UUID)
								indexes.add(modifiers.indexOf(modifier));
						if (!indexes.isEmpty())
							for (Integer index : indexes)
								attributeModifiers.remove(Attributes.ATTACK_SPEED, modifiers.get(index));
					}
					if (attributeModifiers.containsKey(NewAttributes.ATTACK_REACH)) {
						List<Integer> indexes = new ArrayList<>();
						List<AttributeModifier> modifiers = attributeModifiers.get(NewAttributes.ATTACK_REACH).stream().toList();
						for (AttributeModifier modifier : modifiers)
							if (modifier.getId() == WeaponType.BASE_ATTACK_REACH_UUID)
								indexes.add(modifiers.indexOf(modifier));
						if (!indexes.isEmpty())
							for (Integer index : indexes)
								attributeModifiers.remove(NewAttributes.ATTACK_REACH, modifiers.get(index));
					}
					ArrayListMultimap<Attribute, AttributeModifier> modMap = ArrayListMultimap.create();
					configurableItemData.type.addCombatAttributes(item instanceof TieredItem tieredItem ? tieredItem.getTier() : item instanceof Tierable tierable ? tierable.getTier() : Tiers.NETHERITE, modMap);
					attributeModifiers.putAll(modMap);
				}
				if (configurableItemData.damage != null) {
					if (attributeModifiers.containsKey(Attributes.ATTACK_DAMAGE)) {
						List<Integer> indexes = new ArrayList<>();
						List<AttributeModifier> modifiers = attributeModifiers.get(Attributes.ATTACK_DAMAGE).stream().toList();
						for (AttributeModifier modifier : modifiers)
							if (modifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID || modifier.getId() == WeaponType.BASE_ATTACK_DAMAGE_UUID)
								indexes.add(modifiers.indexOf(modifier));
						if (!indexes.isEmpty())
							for (Integer index : indexes)
								attributeModifiers.remove(Attributes.ATTACK_DAMAGE, modifiers.get(index));
					}
					attributeModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(WeaponType.BASE_ATTACK_DAMAGE_UUID, "Config modifier", configurableItemData.damage - (CONFIG.fistDamage() ? 1 : 2), AttributeModifier.Operation.ADDITION));
				}
				if (configurableItemData.speed != null) {
					if (attributeModifiers.containsKey(Attributes.ATTACK_SPEED)) {
						List<Integer> indexes = new ArrayList<>();
						List<AttributeModifier> modifiers = attributeModifiers.get(Attributes.ATTACK_SPEED).stream().toList();
						for (AttributeModifier modifier : modifiers)
							if (modifier.getId() == Item.BASE_ATTACK_SPEED_UUID || modifier.getId() == WeaponType.BASE_ATTACK_SPEED_UUID)
								indexes.add(modifiers.indexOf(modifier));
						if (!indexes.isEmpty())
							for (Integer index : indexes)
								attributeModifiers.remove(Attributes.ATTACK_SPEED, modifiers.get(index));
					}
					attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(WeaponType.BASE_ATTACK_SPEED_UUID, "Config modifier", configurableItemData.speed - CONFIG.baseHandAttackSpeed(), AttributeModifier.Operation.ADDITION));
				}
				if (configurableItemData.reach != null) {
					if (attributeModifiers.containsKey(NewAttributes.ATTACK_REACH)) {
						List<Integer> indexes = new ArrayList<>();
						List<AttributeModifier> modifiers = attributeModifiers.get(NewAttributes.ATTACK_REACH).stream().toList();
						for (AttributeModifier modifier : modifiers)
							if (modifier.getId() == WeaponType.BASE_ATTACK_REACH_UUID)
								indexes.add(modifiers.indexOf(modifier));
						if (!indexes.isEmpty())
							for (Integer index : indexes)
								attributeModifiers.remove(NewAttributes.ATTACK_REACH, modifiers.get(index));
					}
					attributeModifiers.put(NewAttributes.ATTACK_REACH, new AttributeModifier(WeaponType.BASE_ATTACK_REACH_UUID, "Config modifier", configurableItemData.reach - 2.5, AttributeModifier.Operation.ADDITION));
				}
			}
		});
		AttackBlockCallback.EVENT.register(modDetectionNetworkChannel, (player, world, hand, pos, direction) -> {
			if (Combatify.unmoddedPlayers.contains(player.getUUID()) && finalizingAttack.get(player.getUUID()) && player instanceof ServerPlayer serverPlayer) {
				Map<HitResult, Float[]> hitResultToRotationMap = ((ServerPlayerExtensions)serverPlayer).getHitResultToRotationMap();
				((ServerPlayerExtensions) serverPlayer).getPresentResult();
				for (HitResult hitResultToChoose : ((ServerPlayerExtensions)serverPlayer).getOldHitResults()) {
					if(hitResultToChoose == null)
						continue;
					Float[] rotations = null;
					if (hitResultToRotationMap.containsKey(hitResultToChoose))
						rotations = hitResultToRotationMap.get(hitResultToChoose);
					float xRot = serverPlayer.getXRot() % 360;
					float yRot = serverPlayer.getYHeadRot() % 360;
					if(rotations != null) {
						float xDiff = Math.abs(xRot - rotations[1]);
						float yDiff = Math.abs(yRot - rotations[0]);
						if(xDiff > 20 || yDiff > 20)
							continue;
					}
					if (hitResultToChoose.getType() == HitResult.Type.ENTITY) {
						return InteractionResult.FAIL;
					}
				}
			}
			return InteractionResult.PASS;
		});
		UseBlockCallback.EVENT.register(modDetectionNetworkChannel, (player, world, hand, hitResult) -> {
			if(Combatify.unmoddedPlayers.contains(player.getUUID()))
				Combatify.isPlayerAttacking.put(player.getUUID(), false);
			return InteractionResult.PASS;
		});
		UseEntityCallback.EVENT.register(modDetectionNetworkChannel, (player, world, hand, entity, hitResult) -> {
			if(Combatify.unmoddedPlayers.contains(player.getUUID()))
				Combatify.isPlayerAttacking.put(player.getUUID(), false);
			return InteractionResult.PASS;
		});
		UseItemCallback.EVENT.register(modDetectionNetworkChannel, (player, world, hand) -> {
			if(Combatify.unmoddedPlayers.contains(player.getUUID()))
				Combatify.isPlayerAttacking.put(player.getUUID(), false);
			return InteractionResultHolder.pass(player.getItemInHand(hand));
		});
		ServerLifecycleEvents.SERVER_STARTED.register(modDetectionNetworkChannel, server -> {
			ITEMS = new ItemConfig();

			List<Item> items = BuiltInRegistries.ITEM.stream().toList();

			for(Item item : items) {
				((ItemExtensions) item).modifyAttributeModifiers();
			}
			for(Item item : ITEMS.configuredItems.keySet()) {
				ConfigurableItemData configurableItemData = ITEMS.configuredItems.get(item);
				if (configurableItemData.stackSize != null)
					((ItemExtensions) item).setStackSize(configurableItemData.stackSize);
			}
		});
	}
	public record ItemConfigPacket(ItemConfig config) implements FabricPacket {
		public static final PacketType<ItemConfigPacket> TYPE = PacketType.create(Combatify.id("item_config"), ItemConfigPacket::new);

		public ItemConfigPacket(FriendlyByteBuf buf) {
			this(ITEMS.loadFromNetwork(buf));
		}

		/**
		 * Writes the contents of this packet to the buffer.
		 *
		 * @param buf the output buffer
		 */
		@Override
		public void write(FriendlyByteBuf buf) {
			ITEMS.saveToNetwork(buf);
		}

		/**
		 * Returns the packet type of this packet.
		 *
		 * <p>Implementations should store the packet type instance in a {@code static final}
		 * field and return that here, instead of creating a new instance.
		 *
		 * @return the type of this packet
		 */
		@Override
		public PacketType<?> getType() {
			return TYPE;
		}
	}
	public record ServerboundMissPacket() implements FabricPacket {
		public static final PacketType<ServerboundMissPacket> TYPE = PacketType.create(Combatify.id("miss_attack"), ServerboundMissPacket::new);

		public ServerboundMissPacket(FriendlyByteBuf buf) {
			this();
		}

		/**
		 * Writes the contents of this packet to the buffer.
		 *
		 * @param buf the output buffer
		 */
		@Override
		public void write(FriendlyByteBuf buf) {

		}

		/**
		 * Returns the packet type of this packet.
		 *
		 * <p>Implementations should store the packet type instance in a {@code static final}
		 * field and return that here, instead of creating a new instance.
		 *
		 * @return the type of this packet
		 */
		@Override
		public PacketType<?> getType() {
			return TYPE;
		}
	}
}
