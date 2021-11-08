package com.ivyclub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "FriendData")
data class FriendData(
    @PrimaryKey
    val phoneNumber: String, // 전화번호, pk
    val name: String, // 이름
    val birthday: String, // 생년월일
    val groupName: String, // 속한 그룹명
    val planList: List<String>, // 약속 리스트
    val isFavorite: Boolean, // 즐겨찾기 포함 유무
    val extraInfo: Map<String, String> // 성격
)

@Entity(tableName = "PlanData")
data class PlanData(
    @PrimaryKey
    val id: Long, // 약속 ID, pk
    val participant: List<String> = emptyList(), // 만나는 사람들(번호)
    val date: Date, // datetime
    val title: String = "", // 제목
    val place: String = "", // 장소
    val content: String = "", // 내용
    val color: String // 고유색, HexCode
)

data class GroupData(
    val name: String // 이름, pk
)

data class PhoneContactData(
    val name: String,
    val phoneNumber: String
)