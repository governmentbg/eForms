package com.bulpros.eforms.signature.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.validator.routines.UrlValidator;

import com.bulpros.eforms.signature.exception.SignatureRequestException;
import com.ctc.wstx.util.URLUtil;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.utils.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DocumentUtils {
    
    private static UrlValidator urlValidator = UrlValidator.getInstance();

    private DocumentUtils() {
    }
    
    public static DSSDocument toDSSDocument(String file, DigestAlgorithm digestAlgorithm) {
        if (Utils.isBase64Encoded(file)) {
            return bytesToDSSDocument(Utils.fromBase64(file), digestAlgorithm);
        } else if (urlValidator.isValid(file)) {
            return urlToDSSDocument(file, digestAlgorithm);
        } else {
            throw new SignatureRequestException("Unsupported document format. The document should be either base64 encoded content or url.");
        }
    }
    
    public static DSSDocument toDSSDocument(String file, String originalFileName) {
        if (Utils.isBase64Encoded(file)) {
            return bytesToDSSDocument(Utils.fromBase64(file), originalFileName);
        } else if (urlValidator.isValid(file)) {
            return urlToDSSDocument(file, originalFileName);
        } else {
            throw new SignatureRequestException("Unsupported document format. The document should be either base64 encoded content or url.");
        }
    }

    private static DSSDocument urlToDSSDocument(String fileUrl, DigestAlgorithm digestAlgorithm) {
        DigestDocument digestDocument = null;
        InputStream is = null;
        try {
            final URL url = new URL(fileUrl);
            is = URLUtil.inputStreamFromURL(url);
            digestDocument = toDigestDocument(is, digestAlgorithm);
        } catch (MalformedURLException e) {
            log.error(String.format("URL %1$s seems to be malformed. %2$s", fileUrl, e.getMessage()));
        } catch (IOException e) {
            log.error(String.format("Failed to read from URL: %1$s. %2$s", fileUrl, e.getMessage()));
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.warn("Couldn't close input stream: " + e.getMessage());
            }
        }
        return digestDocument;
    }

    private static DSSDocument bytesToDSSDocument(byte[] fileContents, DigestAlgorithm digestAlgorithm) {
        return toDigestDocument(new ByteArrayInputStream(fileContents), digestAlgorithm);
    }

    private static DSSDocument urlToDSSDocument(String fileUrl, String originalFileName) {
        InMemoryDocument inMemoryDocument = null;
        InputStream is = null;
        try {
            final URL url = new URL(fileUrl);
            is = URLUtil.inputStreamFromURL(url);
            inMemoryDocument = toInMemoryDocument(is.readAllBytes(), originalFileName);
        } catch (MalformedURLException e) {
            log.error(String.format("URL %1$s seems to be malformed. %2$s", fileUrl, e.getMessage()));
        } catch (IOException e) {
            log.error(String.format("Failed to read from URL: %1$s. %2$s", fileUrl, e.getMessage()));
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.warn("Couldn't close input stream: " + e.getMessage());
            }
        }
        return inMemoryDocument;
    }

    private static DSSDocument bytesToDSSDocument(byte[] fileContents, String originalFileName) {
        return toInMemoryDocument(fileContents, originalFileName);
    }

    private static DigestDocument toDigestDocument(InputStream is, DigestAlgorithm digestAlgorithm) {
        DigestDocument digestDocument = null;
        try {
            final MessageDigest md = MessageDigest.getInstance(digestAlgorithm.getJavaName());
            final byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                md.update(buffer, 0, count);
            }
            final byte[] digestBytes = md.digest();
            final String base64EncodeDigest = Base64.getEncoder().encodeToString(digestBytes);
            digestDocument = new DigestDocument(digestAlgorithm, base64EncodeDigest);
        } catch (IOException e) {
            log.error(String.format("Failed to read from input stream: %1$s.", e.getMessage()));
        } catch (NoSuchAlgorithmException e) {
            log.error(String.format("Digest algorithm %1$s is not recognized. %2$s", digestAlgorithm.getJavaName(),
                    e.getMessage()));
        }
        return digestDocument;
    }

    private static InMemoryDocument toInMemoryDocument(byte[] fileContents, String originalFileName) {
        return new InMemoryDocument(fileContents, originalFileName);
    }

}
