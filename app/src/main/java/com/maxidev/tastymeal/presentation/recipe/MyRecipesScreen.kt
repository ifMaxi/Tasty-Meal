@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.recipe

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.maxidev.tastymeal.R
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.presentation.components.CustomCenteredTopBar
import com.maxidev.tastymeal.presentation.components.CustomFab
import com.maxidev.tastymeal.presentation.components.CustomLazyVerticalStaggeredGrid
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme

@Composable
fun MyRecipesScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    navigateToCreate: () -> Unit,
    navigateToDetail: (Long) -> Unit
) {
    val state by viewModel.recipes.collectAsStateWithLifecycle()

    ScreenContent(
        recipes = state.recipes,
        navigateToCreate = navigateToCreate,
        navigateToDetail = navigateToDetail
    )
}

@Composable
private fun ScreenContent(
    recipes: List<Recipe>,
    navigateToCreate: () -> Unit,
    navigateToDetail: (Long) -> Unit
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        topBar = {
            CustomCenteredTopBar(
                title = {
                    Text(text = "My recipes")
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            CustomFab(
                onClick = navigateToCreate,
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Create recipe"
                    )
                },
                shape = RoundedCornerShape(10.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            )
        }
    ) { innerPadding ->
        Box {
            CustomLazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                itemsContent = recipes,
                key = { it.id },
                columns = StaggeredGridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(10.dp),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                lazyState = rememberLazyStaggeredGridState(),
                content = {
                    RecipeItem(
                        title = it.title,
                        imageUri = it.image,
                        cameraUri = it.cameraImage,
                        onClick = { navigateToDetail(it.id) }
                    )
                }
            )
        }
    }
}

@Composable
private fun RecipeItem(
    cameraUri: Uri?,
    imageUri: Uri?,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        val photo = if (imageUri == Uri.EMPTY) cameraUri else imageUri

        Image(
            painter = rememberAsyncImagePainter(
                model = photo,
                // TODO: Replace with another image
                error = painterResource(R.drawable.not_found)
            ),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        if (title.isNotEmpty()) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun RecipeItemPreview() {
    TastyMealTheme {
        RecipeItem(
            title = "Lorem impsum",
            imageUri = Uri.EMPTY,
            cameraUri = Uri.EMPTY,
            onClick = {}
        )
    }
}