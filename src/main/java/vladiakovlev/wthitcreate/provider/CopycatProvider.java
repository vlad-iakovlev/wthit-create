package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.component.WrappedComponent;

public enum CopycatProvider implements IBlockComponentProvider {
	INSTANCE;

	public void register(IRegistrar registrar) {
		registrar.addComponent(this, TooltipPosition.BODY, CopycatBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		var copycat = (CopycatBlockEntity) accessor.getBlockEntity();
		var material = copycat.getMaterial();

		if (material == AllBlocks.COPYCAT_BASE.getDefaultState()) {
			return;
		}

		// tooltip.addLine(new ItemComponent(material.getBlock()));
		tooltip.addLine(new WrappedComponent(material.getBlock().getName()));
	}

}
