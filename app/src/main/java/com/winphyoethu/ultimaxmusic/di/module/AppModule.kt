package com.winphyoethu.ultimaxmusic.di.module

import android.content.Context
import androidx.room.Room
import com.winphyoethu.ultimaxmusic.data.dao.PlaylistDao
import com.winphyoethu.ultimaxmusic.data.dao.SongDao
import com.winphyoethu.ultimaxmusic.data.database.MusicDatabase
import com.winphyoethu.ultimaxmusic.data.repositoryimpl.PlaylistRepositoryImpl
import com.winphyoethu.ultimaxmusic.data.repositoryimpl.SongRepositoryImpl
import com.winphyoethu.ultimaxmusic.domain.player.MusicNotification
import com.winphyoethu.ultimaxmusic.domain.player.MusicPlayer
import com.winphyoethu.ultimaxmusic.domain.repository.PlaylistRepository
import com.winphyoethu.ultimaxmusic.domain.repository.SongRepository
import com.winphyoethu.ultimaxmusic.player.MusicNotificationImpl
import com.winphyoethu.ultimaxmusic.player.MusicPlayerImpl
import com.winphyoethu.ultimaxmusic.util.rxview.ViewHelper
import com.winphyoethu.ultimaxmusic.util.rxview.ViewHelperImpl
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [AppModule.RepositoryModule::class, AppModule.MusicPlayerModule::class, AppModule.ViewHelperModule::class, AppModule.SharedPreferenceModule::class])
class AppModule {

    @Provides
    @Singleton
    fun providesMusicDatabase(@ApplicationContext context: Context): MusicDatabase {
        return Room.databaseBuilder(context, MusicDatabase::class.java, "music_database").build()
    }

    @Provides
    fun providesSongDao(musicDatabase: MusicDatabase): SongDao {
        return musicDatabase.songDao()
    }

    @Provides
    fun providesPlaylistDao(musicDatabase: MusicDatabase): PlaylistDao {
        return musicDatabase.playlistDao()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface MusicPlayerModule {

        @Binds
        fun bindsMusicPlayer(musicPlayer: MusicPlayerImpl): MusicPlayer

    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface RepositoryModule {

        @Binds
        fun bindsSongRepository(impl: SongRepositoryImpl): SongRepository

        @Binds
        fun bindsPlaylistRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface NotificationBuilderModule {

        @Binds
        fun bindsMusicNotification(impl: MusicNotificationImpl): MusicNotification

    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface ViewHelperModule {

        @Binds
        fun bindsViewHelper(viewHelperImpl: ViewHelperImpl): ViewHelper

    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface SharedPreferenceModule {

        @Binds
        fun bindsSharedPreference(preferenceHelper: PreferenceHelperImpl): PreferenceHelper

    }

}