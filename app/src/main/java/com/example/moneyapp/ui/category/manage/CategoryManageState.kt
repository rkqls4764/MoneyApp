package com.example.moneyapp.ui.category.manage

import com.example.moneyapp.data.entity.Category

data class CategoryManageState(
    val categories: List<Category> = emptyList()  // 카테고리 목록
)