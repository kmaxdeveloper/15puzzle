package uz.kmax.a15puzzle.fragment

import android.media.MediaPlayer
import uz.kmax.a15puzzle.R
import uz.kmax.a15puzzle.databinding.LayoutMenuBinding

class MenuFragment : BaseFragment<LayoutMenuBinding>(LayoutMenuBinding::inflate) {
    override fun onViewCreated() {
        val btnSound = MediaPlayer.create(requireContext(), R.raw.btn_music)

        binding.playGame.setOnClickListener {
            //btnSound.start()
            replaceFragment(GameFragment())
        }

        binding.setting.setOnClickListener {
            btnSound.start()
            replaceFragment(SettingFragment())
        }

        binding.about.setOnClickListener {
            btnSound.start()
            replaceFragment(AboutFragment())
        }

        binding.result.setOnClickListener {
            btnSound.start()
            replaceFragment(ResultFragment())
        }

        binding.exit.setOnClickListener {
            btnSound.start()
            activity?.finish()
        }
    }
}