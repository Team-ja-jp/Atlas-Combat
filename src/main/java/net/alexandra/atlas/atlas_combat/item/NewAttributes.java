package net.alexandra.atlas.atlas_combat.item;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class NewAttributes extends Attributes{

	private static Attribute register(String id, Attribute attribute) {
		return Registry.register(Registry.ATTRIBUTE, id, attribute);
	}

	public static final Attribute NEW_ATTACK_SPEED = register("generic.new_attack_speed", (new RangedAttribute("attribute.name.generic.new_attack_speed", 4.0, 0.10000000149011612, 1024.0)).setSyncable(true));

	public static final Attribute ATTACK_REACH = register("generic.attack_reach", (new RangedAttribute("attribute.name.generic.attack_reach", 2.5, 0.0, 6.0)).setSyncable(true));
}
