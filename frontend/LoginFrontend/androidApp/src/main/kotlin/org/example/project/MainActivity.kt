package org.example.project

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
