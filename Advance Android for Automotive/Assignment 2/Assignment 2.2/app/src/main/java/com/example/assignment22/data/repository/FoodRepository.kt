package com.example.assignment22.data.repository

import com.example.assignment22.data.model.FoodItem

class FoodRepository {

    suspend fun getMockFoodList(): List<FoodItem> {
        return listOf(
            FoodItem(1, "Phở Bò Nam Định", "Nước dùng đậm đà, thịt bò mềm ngọt", 3.5),
            FoodItem(2, "Bún Chả Hà Nội", "Chả nướng than hoa thơm lừng", 3.0),
            FoodItem(3, "Bánh Mì Thịt Nướng", "Vỏ giòn rụm, nhân thịt đậm vị", 1.5),
            FoodItem(4, "Gỏi Cuốn Tôm Thịt", "Thanh mát, chấm tương đậu phộng", 2.0),
            FoodItem(5, "Cơm Tấm Sườn Bì", "Sườn nướng mật ong, nước mắm kẹo", 4.0),
            FoodItem(6, "Trà Sữa Trân Châu", "Đậm vị trà, trân châu dai giòn", 2.5)
        )
    }
}