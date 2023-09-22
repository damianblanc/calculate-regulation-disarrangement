package com.bymatech.calculateregulationdisarrangement.service.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class impl {
    public static class BymaAPIServiceGenerator {

            private static final String BASE_URL = "https://open.bymadata.com.ar";

            private static Retrofit.Builder builder
                    = new Retrofit.Builder()
                    .client(createTrustingOkHttpClient())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            private static Retrofit retrofit = builder.build();

            private static OkHttpClient.Builder httpClient
                    = new OkHttpClient.Builder();

            public static <S> S createService(Class<S> serviceClass) {
                return retrofit.create(serviceClass);
            }


        private static OkHttpClient createTrustingOkHttpClient() {
            try {
                X509TrustManager x509TrustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                };
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        x509TrustManager
                };
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                return new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                        .hostnameVerifier((hostname, session) -> true)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class PrimaryAPIServiceGenerator {

        private static final String BASE_URL = "https://api.remarkets.primary.com.ar";

        private static Retrofit.Builder builder
                = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        private static Retrofit retrofit = builder.build();

    }
}
