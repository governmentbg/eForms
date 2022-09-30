package lu.nowina.nexu;

import lu.nowina.nexu.api.AppConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.io.File;

public class CustomConfigurationFactory extends ConfigurationFactory {

    static Configuration createConfiguration(ConfigurationBuilder<BuiltConfiguration> builder, AppConfig config){

        AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout","CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "%d [%p|%c|%C{1}|%t] %m%n"));
        appenderBuilder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
                .addAttribute("level", config.isDebug() ? Level.DEBUG : Level.INFO));
        builder.add(appenderBuilder);

        File nexuHome = config.getNexuHome();
        File fileName = new File(nexuHome, "nexu.log");
        File filePattern = new File(nexuHome, "nexu-%d{yyyy-MM-dd}-%i.log");
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %-5p [%c{1}] %m%n");
        ComponentBuilder componentBuilder = builder.newComponent("Policies")
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", config.getRollingLogMaxFileSize()));
        appenderBuilder = builder.newAppender("FileLogger", "RollingFile")
                .addAttribute("fileName", fileName.getAbsolutePath())
                .addAttribute("filePattern", filePattern.getAbsolutePath())
                .addAttribute("append", true)
                .add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
                        .addAttribute("level", config.isDebug() ? Level.DEBUG : Level.INFO))
                .add(layoutBuilder)
                .addComponent(componentBuilder)
                .addComponent(builder.newComponent("DefaultRolloverStrategy")
                        .addAttribute("max", config.getRollingLogMaxFileNumber()));
        builder.add(appenderBuilder);

        builder.add(builder.newRootLogger()
                .add(builder.newAppenderRef("Stdout"))
                .add(builder.newAppenderRef("FileLogger")));


        builder.add(builder.newLogger("org", Level.INFO));
        builder.add(builder.newLogger("httpclient", Level.INFO));
        builder.add(builder.newLogger("freemarker", Level.INFO));
        builder.add(builder.newLogger("lu.nowina", Level.DEBUG));
        // Disable warnings for java.util.prefs: when loading userRoot on Windows,
        // JRE will also try to load/create systemRoot which is under HKLM. This last
        // operation will not be permitted unless user is Administrator. If it is not
        // the case, a warning will be issued but we can ignore it safely as we only
        // use userRoot which is under HKCU.
        builder.add(builder.newLogger("java.util.prefs", Level.ERROR));

        return builder.build();
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[0];
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }
}
