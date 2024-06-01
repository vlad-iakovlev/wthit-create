package vladiakovlev.wthitcreate.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.logistics.filter.FilterItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Mixin(FilterItem.class)
public interface FilterItemMixin {

	@Invoker("makeSummary")
	List<Component> wthitcreate$makeSummary(ItemStack filter);

}
