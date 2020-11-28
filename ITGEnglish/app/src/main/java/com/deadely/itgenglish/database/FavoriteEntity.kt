package com.deadely.itgenglish.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deadely.itgenglish.model.Word
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorites_table")
@Parcelize
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    val word: String,
    val tr: String,
    val translate: String,
    val isFavorite: Boolean
) : Parcelable

fun Word.mapToDBEntity(): FavoriteEntity {
    return FavoriteEntity(id, word, tr, translate, favorite)
}

fun FavoriteEntity.mapToModelEntity(): Word {
    return Word(id, word, tr, translate, isFavorite)
}

fun List<FavoriteEntity>.mapToModelList(): List<Word> {
    return map { it.mapToModelEntity() }
}