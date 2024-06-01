package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.Lang;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import vladiakovlev.wthitcreate.WTHITCreate;
import vladiakovlev.wthitcreate.mixin.FilterItemMixin;

public enum FilterProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final ResourceLocation CONFIG_FILTER_SETTINGS = new ResourceLocation(WTHITCreate.MOD_ID,
			"filter-settings");

	public void register(IRegistrar registrar) {
		registrar.addFeatureConfig(CONFIG_FILTER_SETTINGS, true);
		registrar.addComponent(this, TooltipPosition.BODY, SmartBlockEntity.class, 2000);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		var target = (SmartBlockEntity) accessor.getBlockEntity();
		var behaviour = target.getBehaviour(FilteringBehaviour.TYPE);
		if (behaviour == null) {
			return;
		}

		var filter = behaviour.getFilter(accessor.getSide());
		if (filter == null || filter.isEmpty()) {
			return;
		}

		if (filter.getItem() instanceof FilterItem item) {
			var filterItem = (FilterItem & FilterItemMixin) item;
			var createTooltip = filterItem.wthitcreate$makeSummary(filter);

			createTooltip.forEach(tooltip::addLine);
		} else {
			tooltip.addLine(Lang.translateDirect("gui.filter.allow_list").withStyle(ChatFormatting.GOLD));
			tooltip.addLine(Component.literal("- ").append(filter.getHoverName()));
		}
	}

}
