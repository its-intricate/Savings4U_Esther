package com.sb.savings4u.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideStarlingBankApiService() : StarlingBankApiService {
        val baseUrl = "https://api-sandbox.starlingbank.com/"

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(StarlingBankApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAccessToken(): String {
        return "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_21Ty27jMAz8lcLnsvBL8ePW2_7AfgAt0olQWzIkOd1isf--cmTHcZBL4Jkhh6TI_E2Uc0mb4KSAeDQfzqMdlD53qL8-pBmT98TNXYigNM960RVQlZRBeTpJQFHz8tMQMVWUyxDMf6akzaqsKTJRVOI9Uegjked1sxAopZm1_2UGYvtb0erdFZxDXRYnKKuUoc4lg-i6JuOs7CVi8Pbmi3XMKOq-zkSDUNViyegQsG4KKPO0xyr4pVKEjDDWp5Ts3F6n4aKGKm0qKFMhoJEiC1AWhNz0omqWgaWZeHmU2Clcbq2CxpFby0hvT4L_mZ4ERay96hXbIz8o5w_MCohsaLJlUv4OouI9ysvI98gdf1vl-Q1nfzFWubAyUJrUVdGMQwzucEAt19YkWgJptLdmiIUWZtWM7pUd0SujwfTQz5rWBuTsvBm3OXhEtWaPqAk9t8QDhz42eAsb2WNA2MoAF3HDt8wJf5g3KYLVJII9CNSI59UzavsneIvaoVx6vtMwGBmm370jAWZ5hmd2zbKmV8NWKtY-ULcoy5LV5A_AHaW4D4fXsAoHZ7P3ceDWUQ_czeeRicP14dlfWOziC69djKbywjQPTBDG3s_IsfdhwHla4YTbmYT_f7iicEzG0kP5I7vVPbIv8sF86zvveWkApLs-UxP1kXrc6W0Vz0tO_v0H_dg15rIEAAA.UMZ7kh3obaQALc679YOL1tsCosXW_ix1VVhQ-T4xAS6NlHynTmznxvRUjuCa-xVtQe5qnsmUkJ0t6QCtlXjJYUV-qFEpjF1_NKU4IysFQqUxk_9Xv7vbbfGI1ac7Rchu4v-o5-xjDrLoYLiIv8_zJI9NacXn4QG4TUz0jYdIUyL_NMxT-n5RjjnIeJNL7PSFe5tob7D8a9lzXkm8bJi0ZeLOZAN9qdRTB85rCtf5i2acGfNkrL0CxH50Ac8QqL3SN2D90c88EgNe8QFw7-7g3wXF1oE2x59GtEZaP9M4cIAn7spIYoKhR4VjVsDQR6jFpLPJtkiS1o7a5M43bB2R1P0zy160YrNXeyZXKGWHQbWfSiUP4aIaZ7qd137Cx3-ecF5OQHavMwfrLZLzXhOfOQQilj4hng8kDy8whjedDmcWj3SG3Mg4GBN_1rShY8ZF7fbqlt94oxcqec_9PEqBjjW43SZDznqVw4ZSoQPzrWJWS5TAOeGpthrJzP-Fc1x80OZlsLz8y9tfjZitZZyBvqs7xkzk_Y1Ox0RoiuALUUAIfcDYY1e63fexeu7Gm__x6unXR1VvDUmtgHiIPiN-V03D9Zzz3oww4e_1t0A1CNxcgb2f4m80UJzdRWN4sENQ0uhzugqPNdyL7aTmBrj2B6HzIuAO89hqYYAaeMGPyjY"
    }
}