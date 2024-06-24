package com.sb.savings4u.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AccountScreen(viewModel: AccountViewModel) {
    val uiState = viewModel.uiState.value

    Surface(color = Color.White) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.isError -> ErrorDialog(viewModel)
                else -> Content(uiState, viewModel)
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Content(uiState: AccountContract.AccountUIState, viewModel: AccountViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            item { AccountInformation(uiState) }
            item { SavingsSection(uiState) }
        }

        BottomButtons(
            viewModel = viewModel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        )
    }
}

@Composable
fun AccountInformation(uiState: AccountContract.AccountUIState) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
    ) {
        Text(
            text = "Welcome, ${uiState.accountHolderName}!",
            textAlign = TextAlign.Center,
            color = Color(0xFF262637),
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Here's this week's round up total: ${uiState.roundUpTotal}",
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SavingsSection(uiState: AccountContract.AccountUIState) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Text(
                text = "Total Savings: ${uiState.totalSaved}",
                color = Color(0xFF262637),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = "Target Savings: ${uiState.targetSavings}",
                color = Color(0xFF262637),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PieChart(uiState = uiState)
        }
    }
}

@Composable
fun BottomButtons(viewModel: AccountViewModel, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = { viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Refresh")
        }

        Button(
            onClick = { viewModel.handleEvent(AccountContract.AccountUIEvent.TransferToSavings) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Transfer")
        }
    }
}

@Composable
fun PieChart(uiState: AccountContract.AccountUIState, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val percentageSaved = uiState.percentageSaved.coerceIn(0f, 100f)
        val data = listOf(percentageSaved, 100f - percentageSaved)
        val colors = listOf(Color.Green, Color.Gray)
        var startAngle = 270f

        Canvas(modifier = modifier.size(200.dp)) {
            val canvasSize = size.minDimension

            data.forEachIndexed { index, value ->
                val sweepAngle = (value / 100f) * 360f
                drawArc(
                    color = colors.getOrElse(index) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(canvasSize, canvasSize)
                )
                startAngle += sweepAngle
            }
        }

        Text(
            text = "${"%.2f".format(uiState.percentageSaved)}%",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun ErrorDialog(viewModel: AccountViewModel, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = { viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen) },
        properties = DialogProperties(dismissOnClickOutside = false),
        confirmButton = {
            Button(onClick = { viewModel.handleEvent(AccountContract.AccountUIEvent.RefreshScreen) }) {
                Text(text = "Retry")
            }
        },
        text = { Text(text = "Sorry, there seems to be a problem fetching your account data. Please try again later.") },
        modifier = modifier
    )
}
