package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.component.WrappedComponent;

public enum PlacardProvider implements IBlockComponentProvider {
	INSTANCE;

	public void register(IRegistrar registrar) {
		registrar.addComponent(this, TooltipPosition.BODY, PlacardBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		var placard = (PlacardBlockEntity) accessor.getBlockEntity();

		if (!placard.getHeldItem().isEmpty()) {
			return;
		}

		tooltip.addLine(new WrappedComponent(placard.getHeldItem().getHoverName()));
	}

}
