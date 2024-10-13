package com.example.popup.ui.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.popup.ui.util.UiConstants

/**
 * Re-usable composable for showing a pictures in a horizontal swiping fashion
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 10/12/2024
 */
@Composable
fun SwipeImageCollection(
    images: List<String>?,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (images.isNullOrEmpty()) {
            AsyncImage(
                model = UiConstants.DEFAULT_IMAGE_URL,
                contentDescription = "No images available",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val imagePagerState = rememberPagerState(pageCount = {images.size})

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalPager(
                    state = imagePagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = images[page],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // only show the dots if we have more than one image
                if (images.size > 1) {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(images.size) { imgNum ->
                           val color = when(imagePagerState.currentPage) {
                               imgNum -> Color.White
                               else -> Color.Transparent
                           }

                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = 1.dp,
                                        color = Color.White
                                    )
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SwipeImageCollectionPreview(

) {
    val testImages = listOf(
        "https://pop-up.s3.us-west-1.amazonaws.com/UID2/PID1/crowd.jpg",
        "https://pop-up.s3.us-west-1.amazonaws.com/UID2/PID1/guitar.jpg",
        "https://pop-up.s3.us-west-1.amazonaws.com/UID2/PID1/music-event-1.jpg"
    )

    SwipeImageCollection(
        images = testImages,
        modifier = Modifier
    )
}