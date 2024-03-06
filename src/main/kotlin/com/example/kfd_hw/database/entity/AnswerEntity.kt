package com.example.kfd_hw.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.util.UUID

@Entity
class AnswerEntity(
    @Column(nullable = false, length = 200)
    var body: String = "",
    @Column(nullable = false, length = 40)
    var authorEmail: String = "",
    @Column(nullable = false)
    var questionnaireId: UUID = UUID.randomUUID()
) : AbstractEntity()