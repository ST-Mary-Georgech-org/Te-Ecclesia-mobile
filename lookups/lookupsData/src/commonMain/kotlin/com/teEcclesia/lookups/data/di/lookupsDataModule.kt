package com.teEcclesia.lookups.data.di

import com.teEcclesia.lookups.data.dataSource.remote.LookupRepositoryImpl
import com.teEcclesia.lookups.domain.repository.LookupRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val lookupsDataModule = module {
    singleOf(::LookupRepositoryImpl) bind LookupRepository::class
}
