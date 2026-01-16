package com.example.moneyapp.ui.category.manage

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType

data class CategoryManageState(
    val categories: List<Category> = emptyList(),       // 카테고리 목록
    val type: TransactionType = TransactionType.EXPENSE // 출력할 카테고리 분류
)