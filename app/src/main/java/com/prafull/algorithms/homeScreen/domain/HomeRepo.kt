package com.prafull.algorithms.homeScreen.domain

import com.prafull.algorithms.commons.models.FileInfo
import com.prafull.algorithms.commons.models.FolderInfo
import com.prafull.algorithms.commons.models.ProgrammingLanguage
import com.prafull.algorithms.commons.utils.BaseClass
import kotlinx.coroutines.flow.Flow

interface HomeRepo {


    // get algorithms into a particular folder in home screen like C++ -> Bit Manipulation -> algos
    fun getAlgorithms(path: String): Flow<BaseClass<List<FileInfo>>>        // Get all the algorithms in a group
    fun getAlgoGroups(language: ProgrammingLanguage): Flow<BaseClass<List<FolderInfo>>> // Get all the groups of algorithm

}