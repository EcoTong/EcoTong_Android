package id.ac.istts.ecotong.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.istts.ecotong.data.repository.AuthRepository
import id.ac.istts.ecotong.data.repository.AuthRepositoryImpl
import id.ac.istts.ecotong.data.repository.MLRepository
import id.ac.istts.ecotong.data.repository.MLRepositoryImpl
import id.ac.istts.ecotong.data.repository.PostRepository
import id.ac.istts.ecotong.data.repository.PostRepositoryImpl
import id.ac.istts.ecotong.data.repository.UserRepository
import id.ac.istts.ecotong.data.repository.UserRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindMLRepository(mlRepositoryImpl: MLRepositoryImpl): MLRepository
}