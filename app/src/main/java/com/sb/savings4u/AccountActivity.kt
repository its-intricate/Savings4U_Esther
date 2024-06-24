package com.sb.savings4u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sb.savings4u.ui.AccountScreen
import com.sb.savings4u.ui.AccountViewModel
import com.sb.savings4u.ui.theme.Savings4UTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : ComponentActivity() {
    private val viewModel: AccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Savings4UTheme {
                AccountScreen(viewModel)
            }
        }

        viewModel.initialize()
    }
}