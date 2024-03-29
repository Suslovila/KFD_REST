package com.example.kfd_hw.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class QuestionnaireEntity(
    @Column(nullable = false, length = 200)
    var title: String = "",
    @Column(nullable = false, length = 200)
    var question: String = ""
) : AbstractEntity()