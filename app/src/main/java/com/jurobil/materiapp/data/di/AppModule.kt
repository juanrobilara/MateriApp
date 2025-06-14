package com.jurobil.materiapp.data.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jurobil.materiapp.data.local.AppDatabase
import com.jurobil.materiapp.data.local.AsignaturaDao
import com.jurobil.materiapp.data.local.CarreraDao
import com.jurobil.materiapp.data.network.GoogleAuthUiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}


@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {

    @Provides
    fun provideOneTapClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
        oneTapClient: SignInClient,
        auth: FirebaseAuth
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(context, oneTapClient, auth)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "carrera_db"
        ).build()
    }

    @Provides
    fun provideCarreraDao(db: AppDatabase): CarreraDao = db.carreraDao()

    @Provides
    fun provideAsignaturaDao(db: AppDatabase): AsignaturaDao = db.asignaturaDao()
}