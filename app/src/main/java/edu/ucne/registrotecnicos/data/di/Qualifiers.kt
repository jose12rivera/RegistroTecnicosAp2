package edu.ucne.registrotecnicos.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoshiCliente

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoshiMedicinas

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpClientCliente

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpClientMedicinas
