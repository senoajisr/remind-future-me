package org.d3if3109.mobpro1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if3109.mobpro1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}