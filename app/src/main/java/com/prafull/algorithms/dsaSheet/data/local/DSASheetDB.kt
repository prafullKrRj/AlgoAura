package com.prafull.algorithms.dsaSheet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TopicEntity::class, QuestionEntity::class], version = 1, exportSchema = false)
abstract class DSASheetDB : RoomDatabase() {
    abstract fun dsasheetDao(): DSASheetDao
}