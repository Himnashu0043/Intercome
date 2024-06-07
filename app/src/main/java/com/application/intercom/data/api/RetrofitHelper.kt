package com.application.intercom.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object RetrofitHelper {

    //  private const val BASE_URL = "http://3.130.15.227:5500/api/v1/" //development

    private lateinit var httpClient: OkHttpClient.Builder
   // private const val BASE_URL = "https://intercomapp.xyz:4000/api/v1/" //production
     private const val BASE_URL = "http://54.84.219.152:5500/api/v1/" //test
    fun getInstance(): Retrofit {
        val logging = HttpLoggingInterceptor() // logs HTTP request and response data.
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // set your desired log level
        httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unsafeOkHttpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .client(httpClient.build())
            .build()


    }

 /*   @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initSSL(context: Context) {
        var sslContext: SSLContext? = null
        try {
            sslContext = createCertificate(
                context.getResources()
                    .openRawResource(com.application.intercom.R.raw.intercomapp_xyz)
            )
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        if (sslContext != null) {
            systemDefaultTrustManager()?.let {
                httpClient.sslSocketFactory(
                    sslContext.socketFactory,
                    it
                )
            }
        }
    }

    @Throws(
        CertificateException::class,
        IOException::class,
        KeyStoreException::class,
        KeyManagementException::class,
        NoSuchAlgorithmException::class
    )
    private fun createCertificate(trustedCertificateIS: InputStream): SSLContext? {
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        val ca: Certificate
        ca = try {
            cf.generateCertificate(trustedCertificateIS)
        } finally {
            trustedCertificateIS.close()
        }

        // creating a KeyStore containing our trusted CAs
        val keyStoreType: String = KeyStore.getDefaultType()
        val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // creating a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

    private fun systemDefaultTrustManager(): X509TrustManager? {
        return try {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + Arrays.toString(
                    trustManagers
                )
            }
            trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError() // The system has no TLS. Just give up.
        }
    }*/

    private val unsafeOkHttpClient: OkHttpClient.Builder
        get() = try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) //
            val httpClient = OkHttpClient.Builder()
            httpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            httpClient.addInterceptor(logging)
            httpClient.hostnameVerifier(HostnameVerifier { hostname, session -> true })
            httpClient
        } catch (e: Exception) {
            throw RuntimeException(e)
        }


}