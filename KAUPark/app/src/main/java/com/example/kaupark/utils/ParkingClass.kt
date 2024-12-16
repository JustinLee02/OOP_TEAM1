package com.example.kaupark.data

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class ParkingClass {
    fun getMarker(): List<Marker> {
        return listOf(
            Marker().apply {
                position = LatLng(37.6020819, 126.8657351)
                captionText = "과학관 주차장"
                tag = "여유"
                isHideCollidedSymbols = true // Hiding 겹치는 심볼
                icon = MarkerIcons.RED
                width = 50
                height = 80
            },
            Marker().apply {
                position = LatLng(37.6009608, 126.8658735)
                captionText = "운동장 옆 주차장"
                tag = "운동장 옆 주차장"
                isHideCollidedSymbols = true
                icon = MarkerIcons.RED
                width = 50
                height = 80
            },
            Marker().apply {
                position = LatLng(37.6001840, 126.8644001)
                captionText = "학생회관 주차장"
                tag = "학생회관 주차장"
                isHideCollidedSymbols = true
                icon = MarkerIcons.RED
                width = 50
                height = 80
            },
            Marker().apply {
                position = LatLng(37.5977161, 126.8645011)
                captionText = "연구동 주차장"
                tag = "연구동 주차장"
                isHideCollidedSymbols = true
                icon = MarkerIcons.RED
                width = 50
                height = 80
            },
            Marker().apply {
                position = LatLng(37.5984458, 126.8641054)
                captionText = "도서관 주차장"
                tag = "도서관 주차장"
                isHideCollidedSymbols = true
                icon = MarkerIcons.RED
                width = 50
                height = 80
            },
            Marker().apply {
                position = LatLng(37.5981439, 126.8653157)
                captionText = "산학협력관 주차장"
                tag = "산학협력관 주차장"
                isHideCollidedSymbols = true
                icon = MarkerIcons.RED
                width = 50
                height = 80
            }
        )
    }
}