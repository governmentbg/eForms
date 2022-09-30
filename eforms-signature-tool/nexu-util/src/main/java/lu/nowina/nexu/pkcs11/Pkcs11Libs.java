package lu.nowina.nexu.pkcs11;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pkcs11Libs {

    private static final Logger LOGGER = LogManager.getLogger(Pkcs11Libs.class);

    private static Map<String, List<ProviderLibs>> providerLibsMap = new HashMap<>();

    static {
        try {
            InputStream jsonStream = Pkcs11Libs.class
                    .getClassLoader().getResourceAsStream("pkcs11Libs.json");

            if (jsonStream != null) {
                String json = IOUtils.toString(jsonStream, StandardCharsets.UTF_8);

                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, List<ProviderLibs>>> typeRef
                        = new TypeReference<HashMap<String, List<ProviderLibs>>>() {};
                providerLibsMap = mapper.readValue(json, typeRef);
            } else {
                LOGGER.warn("Missing pkcs11Libs.json resource");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

    }

    public static String getProviderAbsolutePath(String os, String terminalLabel, File nexuHome) {
        if (StringUtils.isEmpty(terminalLabel)) {
            return "";
        }
        if (providerLibsMap.containsKey(os)) {
            for (ProviderLibs libs : providerLibsMap.get(os)) {
                for (String libLabel : libs.getTerminalLabel()) {
                    if (terminalLabel.toLowerCase().contains(libLabel.toLowerCase())) {
                        for (String path : libs.getPkcs11Lib()) {
                            String fullPath = nexuHome.getAbsolutePath() + "/" +
                                    path.substring(path.lastIndexOf("/") + 1);
                            InputStream driverStream = Pkcs11Libs.class.getClassLoader()
                                    .getResourceAsStream("drivers/" + os.toLowerCase() + "/" + path);
                            if (driverStream != null) {
                                if (loadDriver(driverStream, fullPath)) {
                                    return fullPath;
                                } else {
                                    LOGGER.info("Missing bundled library for " + terminalLabel);
                                }
                            }
                        }

                        for (String path : libs.getPkcs11LibAbsolutePath()) {
                            if (Files.exists(Paths.get(path))) {
                                return path;
                            }
                        }
                        break;
                    }
                }
            }
        }
        LOGGER.info("Missing library for " + terminalLabel);
        return "";
    }

    private static boolean loadDriver(InputStream driverStream, String path) {
        byte[] buffer = new byte[2048];
        try (FileOutputStream fos = new FileOutputStream(path);
             BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length)) {
            int len;
            while ((len = driverStream.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return true;
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        return false;
    }

    static class ProviderLibs {
        private List<String> terminalLabel;
        private List<String> pkcs11Lib;
        private List<String> pkcs11LibAbsolutePath;

        public ProviderLibs() {
            pkcs11LibAbsolutePath = new ArrayList<String>();
        }

        public List<String> getTerminalLabel() {
            return terminalLabel;
        }

        public void setTerminalLabel(List<String> terminalLabel) {
            this.terminalLabel = terminalLabel;
        }

        public List<String>  getPkcs11Lib() {
            return pkcs11Lib;
        }

        public void setPkcs11Lib(List<String>  pkcs11Lib) {
            this.pkcs11Lib = pkcs11Lib;
        }

        public List<String> getPkcs11LibAbsolutePath() {
            return pkcs11LibAbsolutePath;
        }

        public void setPkcs11LibAbsolutePath(List<String> pkcs11LibAbsolutePath) {
            this.pkcs11LibAbsolutePath = pkcs11LibAbsolutePath;
        }
    }
}
