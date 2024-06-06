package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.component.NamedItemComponent;
import net.minecraft.resources.ResourceLocation;
import vladiakovlev.wthitcreate.WTHITCreate;

public enum PlacardProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final ResourceLocation CONFIG_PLACARD_ITEM = new ResourceLocation(WTHITCreate.MOD_ID,
			"placard-item");

	public void register(IRegistrar registrar) {
		registrar.addFeatureConfig(CONFIG_PLACARD_ITEM, true);
		registrar.addComponent(this, TooltipPosition.BODY, PlacardBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(CONFIG_PLACARD_ITEM)) {
			return;
		}

		var placard = (PlacardBlockEntity) accessor.getBlockEntity();
		var itemStack = placard.getHeldItem();

		if (itemStack.isEmpty()) {
			return;
		}

		tooltip.addLine(new NamedItemComponent(itemStack));
	}

}
