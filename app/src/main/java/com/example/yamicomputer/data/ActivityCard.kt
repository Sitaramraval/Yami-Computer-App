package com.example.yamicomputer.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yamicomputer.R

@Composable
fun ActivityCardItems(
    activityCardData: ActivityCardData,
    size: Size = Size(width = 155f, height = 160f),
    imageCard: Dp = 60.dp,
    cardCorner: Dp = 30.dp,
    enableIcon: Boolean = true,
    imageScale: ContentScale = ContentScale.FillBounds,
    iconCardTopPadding: Dp = 16.dp,
    onClick: (key: String) -> Unit,
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Card(
        modifier = Modifier
            .width(size.width.dp)
            .height(size.height.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    onClick(activityCardData.key)
                }
            ),
        backgroundColor = activityCardData.cardColor,
        elevation = 10.dp,
        shape = RoundedCornerShape(cardCorner),
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.transaction_activity),
                contentDescription = "",
                colorFilter = ColorFilter.tint(activityCardData.bgColor),
                contentScale = imageScale
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = iconCardTopPadding)
                    .background(color = Color.Transparent),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Card(
                        modifier = Modifier.size(imageCard),
                        shape = CircleShape,
                        backgroundColor = if (enableIcon) Color.White  else Color.Transparent,
                        elevation = if (enableIcon) 10.dp else 0.dp,

                        ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(text = activityCardData.icon.toString(), fontSize = 20.sp, color = if (enableIcon) Color.Black  else Color.Transparent)
                        }
                    }



                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                        .padding(start = 15.dp, bottom = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Text(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        text = activityCardData.text,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }

            }
        }

    }

}