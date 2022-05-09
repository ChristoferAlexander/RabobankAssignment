package com.example.rabobankassignment.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val Typography = Typography()

val Typography.headerTextStyle: TextStyle
    @Composable
    get() = TextStyle(
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )

val Typography.stringTextStyle: TextStyle
    @Composable
    get() = TextStyle(
        fontSize = 10.sp,
        textAlign = TextAlign.Center
    )


val Typography.intTextStyle: TextStyle
    @Composable
    get() = TextStyle(
        color = Color.Blue,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )

val Typography.dateTextStyle: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 10.sp,
        textAlign = TextAlign.Center
    )