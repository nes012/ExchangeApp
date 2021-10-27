package nesty.anzhy.exchangeapp.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import nesty.anzhy.exchangeapp.data.database.LocalDataSource
import nesty.anzhy.exchangeapp.data.network.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}