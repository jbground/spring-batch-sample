package com.jbground.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public class CheckTrustCertificateTasklet implements Tasklet, StepExecutionListener {

    private final static Logger logger = LoggerFactory.getLogger(CheckTrustCertificateTasklet.class);
    private final char[] HEXDIGITS = "0123456789abcdef".toCharArray();


    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //히스토리 조회 후 PKIX 에러가 발생한 경우 인증서 등록
        checkCertificate(new URL(""));

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    private String checkCertificate(URL url) throws Exception {
        return checkCertificate(url.getHost(), null);
    }

    private String checkCertificate(String url, String password) throws Exception {

        char[] passphrase;

        if(url == null || "".equals(url))
            return "Usage: java InstallCert <host>[:port] [passphrase]";

        String[] c = url.split(":");
        String host = c[0];
        int port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
        String p = password == null ? "changeit" : password;
        passphrase = p.toCharArray();

//        File file = new File("jssecacerts");
//        if (!file.isFile()) {
//            char SEP = File.separatorChar;
//            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
//            file = new File(dir, "jssecacerts");
//            if (!file.isFile()) {
//                file = new File(dir, "cacerts");
//            }
//        }

        char SEP = File.separatorChar;
        File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
        File file = new File(dir, "cacerts");

        logger.info("Loading KeyStore {}...", file);

        InputStream in = Files.newInputStream(file.toPath());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager stm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{stm}, null);
        SSLSocketFactory factory = context.getSocketFactory();
        logger.info("Opening connection to {}:{}...", host, port);
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            logger.info("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            logger.info("No errors, certificate is already trusted");
            return "success";
        } catch (SSLException e) {
            e.printStackTrace();
        }

        X509Certificate[] chain = stm.chain;
        if (chain == null)
            return "Could not obtain server certificate chain";

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        logger.info("Server sent {} certificate(s):", chain.length);

        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            logger.info(" {} Subject {}", i + 1, cert.getSubjectDN());
            logger.info("   Issuer  {}", cert.getIssuerDN());

            sha1.update(cert.getEncoded());
            logger.info("   sha1    {}", toHexString(sha1.digest()));

            md5.update(cert.getEncoded());
            logger.info("   md5     {}", toHexString(md5.digest()));
        }

        logger.info("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
//        String line = reader.readLine().trim();
        int k = 0;
//        try {
//            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
//        } catch (NumberFormatException e) {
//            return "KeyStore not changed";
//        }

        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);
        OutputStream out = Files.newOutputStream(file.toPath());
        ks.store(out, passphrase);
        out.close();
        logger.info(String.valueOf(cert));
        logger.info("Added certificate to keystore 'cacerts' using alias '{}'", alias);

        return "success";
    }

    private String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }
    private static class SavingTrustManager implements X509TrustManager {
        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }
}
