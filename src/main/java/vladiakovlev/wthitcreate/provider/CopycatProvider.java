package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.resources.ResourceLocation;
import vladiakovlev.wthitcreate.WTHITCreate;
import vladiakovlev.wthitcreate.component.NamedItemComponent;

public enum CopycatProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final ResourceLocation CONFIG_COPYCAT_MATERIAL = new ResourceLocation(WTHITCreate.MOD_ID,
			"copycat-material");

	public void register(IRegistrar registrar) {
		registrar.addFeatureConfig(CONFIG_COPYCAT_MATERIAL, true);
		registrar.addComponent(this, TooltipPosition.BODY, CopycatBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(CONFIG_COPYCAT_MATERIAL)) {
			return;
		}

		var copycat = (CopycatBlockEntity) accessor.getBlockEntity();
		var material = copycat.getMaterial();

		if (material == AllBlocks.COPYCAT_BASE.getDefaultState()) {
			return;
		}

		tooltip.addLine(new NamedItemComponent(material.getBlock()));
	}

}
