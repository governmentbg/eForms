/**
 * © Nowina Solutions, 2015-2015
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
package lu.nowina.nexu;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract base class for test classes that need to configure logging.
 *
 * @author Jean Lepropre (jean.lepropre@nowina.lu)
 */
public abstract class AbstractConfigureLoggerTest {

	private static final Logger LOG = LogManager.getLogger(AbstractConfigureLoggerTest.class.getName());
	
	public AbstractConfigureLoggerTest() {
		super();
	}

	@BeforeClass
	public static void configureLogger() {
		final String PATTERN = "%d [%p|%c|%C{1}] %m%n";

		ConfigurationBuilder<BuiltConfiguration> builder = new DefaultConfigurationBuilder<>();
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout","CONSOLE")
				.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout").
				addAttribute("pattern", PATTERN));
		appenderBuilder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
				.addAttribute("level", Level.DEBUG));
		builder.add(appenderBuilder);
		builder.add(builder.newRootLogger().add(builder.newAppenderRef("Stdout")));

		builder.add(builder.newLogger("org", Level.INFO));
		builder.add(builder.newLogger("httpclient", Level.INFO));
		builder.add(builder.newLogger("freemarker", Level.INFO));
		builder.add(builder.newLogger("lu.nowina", Level.DEBUG));
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			LOG.info("Starting test: " + description.getMethodName());
		}
	}; 
}
