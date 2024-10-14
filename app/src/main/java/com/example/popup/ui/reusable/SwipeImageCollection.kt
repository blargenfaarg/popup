package com.example.popup.ui.reusable

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.popup.R
import com.example.popup.ui.theme.Poiple
import com.example.popup.ui.util.UiConstants
import kotlinx.coroutines.Dispatchers

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
    val context = LocalContext.current

    Box(
        modifier = modifier
    ) {
        if (images.isNullOrEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(13.dp)),
                model = buildCoilImageRequest(
                    imageUrl = UiConstants.DEFAULT_IMAGE_URL,
                    context = context
                ),
                contentDescription = "No images available",
                contentScale = ContentScale.Crop
            )
        } else {
            val imagePagerState = rememberPagerState(pageCount = {images.size})

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalPager(
                    state = imagePagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(13.dp))
                ) { page ->
                    AsyncImage(
                        model = buildCoilImageRequest(
                            imageUrl = images[page],
                            context = context
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // only show the dots if we have more than one image
                if (images.size > 1) {
                    Row(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(images.size) { imgNum ->
                           val color = when(imagePagerState.currentPage) {
                               imgNum -> Poiple
                               else -> Color.Gray
                           }
                            val width = when(imagePagerState.currentPage) {
                                imgNum -> 20.dp
                                else -> 8.dp
                            }

                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color)
                                    .width(width)
                                    .height(8.dp)

                            )
                        }
                    }
                }
            }
        }
    }
}

fun buildCoilImageRequest(
    imageUrl: String?,
    context: Context
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .dispatcher(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.loading_failed)
        .fallback(R.drawable.placeholder)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            SwipeImageCollection(
                images = testImages,
                modifier = Modifier
            )
        }
    }
}