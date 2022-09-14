package uz.kmax.a15puzzle.fragment

import android.media.MediaPlayer
import android.widget.Toast
import uz.kmax.a15puzzle.R
import uz.kmax.a15puzzle.databinding.LayoutSettingBinding
import uz.kmax.a15puzzle.util.SharedHelper

class SettingFragment:BaseFragment<LayoutSettingBinding>(LayoutSettingBinding::inflate) {
    private val shared by lazy { SharedHelper(requireContext()) }

    var musicModeValue = true
    var themeModeValue = true

    override fun onViewCreated() {
        binding.back.setOnClickListener { backFragment() }

        musicMode()
        themeMode()
        lang()
    }

    private fun lang() {
        binding.engLang.setOnClickListener {
            Toast.makeText(requireContext(), "Eng Lang", Toast.LENGTH_SHORT).show()
        }

        binding.uzbLang.setOnClickListener {
            Toast.makeText(requireContext(), "Uzb Lang", Toast.LENGTH_SHORT).show()
        }
    }

    private fun musicMode(){
        binding.musicMode.setOnClickListener {

        }
    }

    private fun themeMode(){
        binding.themeMode.setOnClickListener {
            if (themeModeValue){
                themeModeValue = !themeModeValue
                binding.themeMode.setImageResource(R.drawable.night)
            }else{
                themeModeValue = !themeModeValue
                binding.themeMode.setImageResource(R.drawable.light)
            }
        }
    }
}