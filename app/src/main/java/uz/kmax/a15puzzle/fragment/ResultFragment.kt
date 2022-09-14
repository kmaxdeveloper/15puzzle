package uz.kmax.a15puzzle.fragment

import android.view.View
import uz.kmax.a15puzzle.databinding.LayoutResultBinding
import uz.kmax.a15puzzle.util.SharedHelper

class ResultFragment:BaseFragment<LayoutResultBinding>(LayoutResultBinding::inflate) {
    private val shared by lazy{
        SharedHelper(requireContext())
    }
    override fun onViewCreated() {
        val lastScore = shared.getWinScore()
        val lastTime = shared.getWinTime()

        binding.back.setOnClickListener {
            backFragment()
        }

        if (shared.getResult()){
            binding.lastResultNot.visibility = View.GONE
            binding.resultScore.text = lastScore.toString()
            binding.resultTimer.text = lastTime.toString()
        }else{
            binding.lastResultNot.visibility = View.VISIBLE
            binding.scoreLayout.visibility = View.GONE
            binding.timerLayout.visibility = View.GONE
        }
    }
}