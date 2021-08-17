package com.winphyoethu.ultimaxmusic.util.contentresolver

import android.content.Context
import android.provider.MediaStore
import com.winphyoethu.ultimaxmusic.data.entity.Song
import com.winphyoethu.ultimaxmusic.util.getLongFromCursor
import com.winphyoethu.ultimaxmusic.util.getStringFromCursor
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ContentResolverUtil @Inject constructor(@ApplicationContext val context: Context) {

    private val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
    private val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
    private val calendar = Calendar.getInstance()

    fun getSongFromContentProvider(): Observable<List<Song>> {
        return Observable.create {
            val contentResolver = context.contentResolver

            val songList: MutableList<Song> = ArrayList()

            val cursor = contentResolver.query(uri, null, selection, null, sortOrder)

            try {
                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val data = cursor.getStringFromCursor(MediaStore.Audio.Media.DATA)
                        val title = cursor.getStringFromCursor(MediaStore.Audio.Media.TITLE)
                        val artist = cursor.getStringFromCursor(MediaStore.Audio.Media.DISPLAY_NAME)
                        val album = cursor.getStringFromCursor(MediaStore.Audio.Media.DATE_MODIFIED)
                        val duration = cursor.getLongFromCursor(MediaStore.Audio.Media.DURATION)

                        songList.add(
                            Song(
                                id = 0, title = title, data = data, artist = artist, album = album,
                                duration = duration, createdDate = calendar.timeInMillis,
                                modifiedDate = calendar.timeInMillis,
                            )
                        )

                    }
                }
                it.onNext(songList)
            } catch (e: Exception) {
                it.onError(e)
            } finally {
                cursor?.close()
                it.onComplete()
            }
        }
    }


}