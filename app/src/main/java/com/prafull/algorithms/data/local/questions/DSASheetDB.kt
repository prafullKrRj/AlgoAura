package com.prafull.algorithms.data.local.questions

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TopicEntity::class, QuestionEntity::class], version = 1, exportSchema = false)
abstract class DSASheetDB : RoomDatabase() {
    abstract fun dsasheetDao(): DSASheetDao
}