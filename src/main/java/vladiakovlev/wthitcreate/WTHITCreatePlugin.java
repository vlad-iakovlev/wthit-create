package vladiakovlev.wthitcreate;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import vladiakovlev.wthitcreate.provider.BacktankProvider;

public class WTHITCreatePlugin implements IWailaPlugin {

	@Override
	public void register(IRegistrar registrar) {
		BacktankProvider.INSTANCE.register(registrar);
	}

}
