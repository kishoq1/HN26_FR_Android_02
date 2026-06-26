package com.example.assignment4.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.assignment4.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.assignment4.utils.DateUtils
import kotlinx.coroutines.CoroutineScope

@Database(entities = [
    Client::class,
    Expense::class,
    Occupation::class,
    com.example.assignment4.data.model.Room::class,
    RoomPhoto::class,
    RoomType::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao() : ClientDao
    abstract fun expenseDao() : ExpenseDao
    abstract fun occupationDao() : OccupationDao
    abstract fun roomDao() : RoomDao
    abstract fun roomPhotoDao() : RoomPhotoDao
    abstract fun roomTypeDao() : RoomTypeDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase{
            return INSTANCE ?:synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hotel_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(db: AppDatabase) {
            val clientDao = db.clientDao()
            val roomTypeDao = db.roomTypeDao()
            val roomDao = db.roomDao()
            val photoDao = db.roomPhotoDao()

            // Nạp 8 khách hàng
            val clients = listOf(
                Client(
                    name = "Nguyễn Văn A",
                    gender = "Nam",
                    country = "Việt Nam",
                    phoneNumber = "0901234567"
                ),
                Client(
                    name = "Trần Thị B",
                    gender = "Nữ",
                    country = "Việt Nam",
                    phoneNumber = "0912345678"
                ),
                Client(
                    name = "John Smith",
                    gender = "Nam",
                    country = "USA",
                    phoneNumber = "+1234567890"
                ),
                Client(
                    name = "Maria Garcia",
                    gender = "Nữ",
                    country = "Tây Ban Nha",
                    phoneNumber = "+349876543"
                ),
                Client(
                    name = "Tanaka Taro",
                    gender = "Nam",
                    country = "Nhật Bản",
                    phoneNumber = "+8190123456"
                ),
                Client(
                    name = "Kim Ji Won",
                    gender = "Nữ",
                    country = "Hàn Quốc",
                    phoneNumber = "+8210987654"
                ),
                Client(
                    name = "Lê Hoàng C",
                    gender = "Nam",
                    country = "Việt Nam",
                    phoneNumber = "0987654321"
                ),
                Client(
                    name = "Jane Doe",
                    gender = "Nữ",
                    country = "Anh Quốc",
                    phoneNumber = "+4479123456"
                )
            )
            clients.forEach { clientDao.insertClient(it) }

            val idSingle = roomTypeDao.insertRoomType(
                RoomType(
                    typeName = "Single",
                    facilities = "TV, WiFi, Quạt",
                    price = 30.0
                )
            ).toInt()
            val idDouble = roomTypeDao.insertRoomType(
                RoomType(
                    typeName = "Double",
                    facilities = "TV, WiFi, Điều hòa, Tủ lạnh",
                    price = 60.0
                )
            ).toInt()
            val idSuite = roomTypeDao.insertRoomType(
                RoomType(
                    typeName = "Suite",
                    facilities = "TV, WiFi, Điều hòa, Bồn tắm, View biển",
                    price = 150.0
                )
            ).toInt()

            // Nạp 7 Phòng vật lý
            val rooms = listOf(
                Room(roomTypeId = idSingle, roomNumber = 101),
                Room(roomTypeId = idSingle, roomNumber = 102),
                Room(roomTypeId = idDouble, roomNumber = 201),
                Room(roomTypeId = idDouble, roomNumber = 202),
                Room(roomTypeId = idDouble, roomNumber = 203),
                Room(roomTypeId = idSuite, roomNumber = 301),
                Room(roomTypeId = idSuite, roomNumber = 302)
            )
            rooms.forEach { roomDao.insertRoom(it) }

            // Nạp ảnh mặc định
            photoDao.insertPhoto(RoomPhoto(roomTypeId = idSingle, isDefault = true))
            photoDao.insertPhoto(RoomPhoto(roomTypeId = idDouble, isDefault = true))
            photoDao.insertPhoto(RoomPhoto(roomTypeId = idSuite, isDefault = true))

            val occupationDao = db.occupationDao()
            val expenseDao = db.expenseDao()

            // 1. Tạo một đơn đặt phòng vào năm 2023 (Để test Task 4)
            // Khách hàng ID = 1 thuê Phòng ID = 1 (Phòng 101 - Single - 30.0$)
            val checkInDate1 = DateUtils.convertStringToLong("10/05/2023")
            val checkOutDate1 = DateUtils.convertStringToLong("15/05/2023") // Ở 5 ngày

            val occupationId1 = occupationDao.insertOccupation(
                Occupation(
                    clientId = 1,
                    roomId = 1,
                    dateTake = checkInDate1,
                    dateReturn = checkOutDate1
                )
            ).toInt()

            // 2. Thêm chi phí phát sinh cho đơn đặt phòng trên
            expenseDao.insertExpense(
                Expense(occupationId = occupationId1, describeFee = "Gọi đồ ăn tối", amount = 25.5)
            )
            expenseDao.insertExpense(
                Expense(occupationId = occupationId1, describeFee = "Giặt là", amount = 10.0)
            )

            // 3. Tạo thêm một đơn đặt phòng khác vào năm 2024
            val checkInDate2 = DateUtils.convertStringToLong("20/01/2024")
            val checkOutDate2 = DateUtils.convertStringToLong("22/01/2024")
            val occupationId2 = occupationDao.insertOccupation(
                Occupation(
                    clientId = 2,
                    roomId = 6,
                    dateTake = checkInDate2,
                    dateReturn = checkOutDate2
                )
            ).toInt()

            expenseDao.insertExpense(
                Expense(occupationId = occupationId2, describeFee = "Dịch vụ Massage", amount = 50.0)
            )
        }
    }
}