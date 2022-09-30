package lu.nowina.nexu.api.resurceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Utf8ResourceBundle extends ResourceBundle{

    private static Locale locale;

    private Utf8ResourceBundle() {
    }

    public static ResourceBundle getUTF8Bundle(String baseName){
        if (locale == null){
            return ResourceBundle.getBundle(baseName, new UTF8Control());
        }else {
            return ResourceBundle.getBundle(baseName, locale, new UTF8Control());
        }
    }

    public static void setLocale(Locale locale) {
        Utf8ResourceBundle.locale = locale;
    }
}

