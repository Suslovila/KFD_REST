package com.example.kfd_hw.database.dao

import com.example.kfd_hw.database.entity.AnswerEntity
import java.util.UUID


interface AnswerDao : CommonDao<AnswerEntity> {
    fun findByEmailAndQuestionnaireId(authorEmail: String, questionnaireId: UUID): AnswerEntity?
    fun findAllByAuthorEmail(authorEmail: String): Iterable<AnswerEntity>
}