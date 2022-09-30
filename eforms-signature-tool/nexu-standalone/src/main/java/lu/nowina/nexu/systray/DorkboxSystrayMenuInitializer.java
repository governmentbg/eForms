/**
 * © Nowina Solutions, 2015-2017
 *
 * Concédée sous licence EUPL, version 1.1 ou – dès leur approbation par la Commission européenne - versions ultérieures de l’EUPL (la «Licence»).
 * Vous ne pouvez utiliser la présente œuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous la Licence est distribué «en l’état»,
 * SANS GARANTIES OU CONDITIONS QUELLES QU’ELLES SOIENT, expresses ou implicites.
 * Consultez la Licence pour les autorisations et les restrictions linguistiques spécifiques relevant de la Licence.
 */
package lu.nowina.nexu.systray;

import java.net.URL;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import lu.nowina.nexu.api.SystrayMenuItem;
import lu.nowina.nexu.api.flow.OperationFactory;

/**
 * Implementation of {@link SystrayMenuInitializer} using
 * <a href="https://github.com/dorkbox/SystemTray">SystemTray from Dorkbox</a>.
 * 
 * @author Jean Lepropre (jean.lepropre@nowina.lu)
 */
public class DorkboxSystrayMenuInitializer implements SystrayMenuInitializer {

	private static final Logger LOGGER = LogManager.getLogger(DorkboxSystrayMenuInitializer.class.getName());
	private Menu menu;

	public DorkboxSystrayMenuInitializer() {
		super();
	}

	@Override
	public void init(final String tooltip, final URL trayIconURL, final OperationFactory operationFactory,
			final SystrayMenuItem exitMenuItem, final SystrayMenuItem... systrayMenuItems) {
		final SystemTray systemTray = SystemTray.getNative();
		if (systemTray == null) {
			LOGGER.warn("System tray is currently not supported.");
			return;
		}

		systemTray.setImage(trayIconURL);

		menu = systemTray.getMenu();
		for(final SystrayMenuItem systrayMenuItem : systrayMenuItems) {
			menu.add(new MenuItem(systrayMenuItem.getLabel(),
					(e) -> systrayMenuItem.getFutureOperationInvocation().call(operationFactory)));
		}
		
		menu.add(new MenuItem(exitMenuItem.getLabel(),
				(e) -> exitMenuItem.getFutureOperationInvocation().call(operationFactory)));
	}

	@Override
	public void reloadMenuItemLabels(ArrayList<String> labels) {
		for (int i = 0; i < labels.size(); i++) {
			MenuItem item = (MenuItem) menu.get(i);
			item.setText(labels.get(i));
		}
	}

}
