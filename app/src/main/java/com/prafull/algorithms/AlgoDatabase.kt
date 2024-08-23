package com.prafull.algorithms

import androidx.annotation.DrawableRes
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


@Database(entities = [AlgorithmEntity::class], version = 1, exportSchema = true)

abstract class AlgoDatabase : RoomDatabase() {
    abstract fun algoDao(): AlgoDao
}

interface AlgoDao {
    @Query("SELECT * FROM AlgorithmEntity")
    fun getAllAlgorithms(): Flow<List<AlgorithmEntity>>

    @Insert
    suspend fun insert(algo: AlgorithmEntity)

    @Delete
    suspend fun delete(algo: AlgorithmEntity)
}

@Entity
data class AlgorithmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: String,
    val language: String,
    val extension: String,
    val title: String = ""
) {
    fun toAlgorithms(): Algorithms {
        return Algorithms(
            code = code,
            language = ProgrammingLanguage.valueOf(language),
            langName = ProgrammingLanguage.valueOf(language).fileName,
            extension = extension,
            title = title
        )
    }
}

data class Algorithms(
    val code: String,
    val language: ProgrammingLanguage,
    val langName: String = language.fileName,
    val extension: String = language.extension,
    val title: String
) {
    fun toEntity(): AlgorithmEntity {
        return AlgorithmEntity(
            code = code,
            language = language.name,
            extension = language.extension,
            title = title
        )
    }
}

data class FileInfo(
    val name: String,
    val path: String,
    val language: ProgrammingLanguage
)

data class FolderInfo(
    val name: String,
    val path: String
)

enum class ProgrammingLanguage(
    val extension: String,
    val fileName: String,
    val languageGenerics: String,
    @DrawableRes val logo: Int
) {
    C_PLUS_PLUS("cpp", "C-Plus-Plus", "C++", R.drawable.c_plus_plus),
    C_SHARP("cs", "C-Sharp", "C#", R.drawable.c_sharp),
    C("c", "C", "C", R.drawable.c),
    DART("dart", "Dart", "Dart", R.drawable.dart),
    GO("go", "Go", "Go", R.drawable.go),
    JAVA("java", "Java", "Java", R.drawable.java),
    JAVASCRIPT("js", "JavaScript", "JavaScript", R.drawable.javascript),
    JULIA("jl", "Julia", "Julia", R.drawable.julia),
    KOTLIN("kt", "Kotlin", "Kotlin", R.drawable.kotlin),
    PHP_SORT("php", "PHP", "PHP", R.drawable.php),
    PYTHON("py", "Python", "Python", R.drawable.python),
    UNKNOWN("unknown", "Unknown", "Unknown", R.drawable.ic_launcher_background);
}

