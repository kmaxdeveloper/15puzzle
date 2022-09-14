package uz.kmax.a15puzzle.fragment

import android.media.MediaPlayer
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import uz.kmax.a15puzzle.R
import uz.kmax.a15puzzle.coordinate.Coordinate
import uz.kmax.a15puzzle.databinding.LayoutGameBinding
import uz.kmax.a15puzzle.dialog.WinDialog
import uz.kmax.a15puzzle.util.SharedHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

class GameFragment : BaseFragment<LayoutGameBinding>(LayoutGameBinding::inflate) {
    private var buttons = ArrayList<ArrayList<AppCompatButton>>()
    private var numbers = ArrayList<Int>()
    private var scoreCounter = 0
    private var timeCounter = 0
    private lateinit var passiveCoordinate: Coordinate
    private val timer by lazy { Timer() }
    private val shared by lazy { SharedHelper(requireContext()) }
    private lateinit var sound: MediaPlayer
    private val winDialog by lazy { WinDialog() }
    private var mInterstitialAd: InterstitialAd? = null
    var adType = 0

    override fun onViewCreated() {
        MobileAds.initialize(requireContext()) {}
        sound = MediaPlayer.create(requireContext(), R.raw.btn_music)
        onClick()
        loadAllViews()
        if (shared.getResume()) {
            resumeLoadNumbers()
            resumeRestart()
        }else{
            loadNumbers()
            restart()
        }
    }

    private fun onClick() {
        binding.back.setOnClickListener {
            sound.start()
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
                adType = 1
                onStop()
            } else {
                backFragment()
            }
        }

        binding.restartButton.setOnClickListener {
            sound.start()
            if (mInterstitialAd != null){
                adType = 2
                mInterstitialAd?.show(requireActivity())
            }else{
                numbers = ArrayList()
                timeCounter = 0
                loadAllViews()
                loadNumbers()
                shuffle()
                loadDataToView()
            }
        }
    }

    private fun resumeRestart() {
        resumeLoadDataToView()
        setTimer()
    }

    private fun restart() {
        shuffle()
        loadDataToView()
        setTimer()
    }

    private fun loadAllViews() {
        var list = ArrayList<AppCompatButton>()
        for (i in 0 until binding.btnParent.childCount) {
            val b = binding.btnParent.getChildAt(i)
            b.setOnClickListener {
                check(it as AppCompatButton)
            }
            b.tag = Coordinate(i / 4, i % 4)
            list.add(b as AppCompatButton)
            if ((i + 1) % 4 == 0) {
                buttons.add(list)
                list = ArrayList()
            }
        }
        /////
        passiveCoordinate = Coordinate(3, 3)
        //////
    }

    private fun loadNumbers() {
        for (i in 1..15) {
            numbers.add(i)
        }
    }

    private fun resumeLoadNumbers(){ numbers.addAll(shared.getAllNumbers()) }

    private fun loadDataToView() {
        var t = 0
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (i == 3 && j == 3) {
                    buttons[i][j].text = ""
                    buttons[i][j].setBackgroundResource(R.drawable.bg_passive_btn)
                } else {
                    buttons[i][j].text = "${numbers[t++]}"
                    buttons[i][j].setBackgroundResource(R.drawable.bg_active_btn)
                }
            }
        }
        scoreCounter = 0
        timeCounter = 0
        binding.score.text = "$scoreCounter"
        binding.time.text = timeFormat(timeCounter)
    }

    private fun resumeLoadDataToView() {
        var index = 0
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val t = numbers[index++]
                if (t == -1) {
                    buttons[i][j].text = ""
                    buttons[i][j].setBackgroundResource(R.drawable.bg_passive_btn)
                } else {
                    buttons[i][j].text = "$t"
                    buttons[i][j].setBackgroundResource(R.drawable.bg_active_btn)
                }
            }
        }
        scoreCounter = shared.getScoreCount()
        timeCounter = shared.getTimerCount()
        binding.score.text = "$scoreCounter"
        binding.time.text = timeFormat(timeCounter) // ????
    }

    private fun shuffle() {
        numbers.shuffle()
    }

    private fun setTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    binding.time.text = timeFormat(++timeCounter)
                }
            }

        }, 1000, 1000)
    }

    fun timeFormat(time: Int): String {
        val minute = time / 60
        val second = time % 60
        val hour = time / 3600
        val hourFormat = if (hour < 10) "0${hour}" else "$hour"
        val secondFormat = if (second < 10) "0${second}" else "$second"
        val minuteFormat = if (minute < 10) "0${minute}" else "$minute"
        return "${hourFormat}:${minuteFormat}:${secondFormat}"
    }

    private fun check(activeBtn: AppCompatButton) {
        sound.start()
        val activeCoordinate = activeBtn.tag as Coordinate
        if (
            (activeCoordinate.x - passiveCoordinate.x).absoluteValue
            + (activeCoordinate.y - passiveCoordinate.y).absoluteValue == 1
        ) {
            val passiveBtn = buttons[passiveCoordinate.x][passiveCoordinate.y]
            passiveBtn.text = activeBtn.text
            activeBtn.text = ""
            passiveBtn.setBackgroundResource(R.drawable.bg_active_btn)
            activeBtn.setBackgroundResource(R.drawable.bg_passive_btn)
            passiveCoordinate.x = activeCoordinate.x
            passiveCoordinate.y = activeCoordinate.y
            ////////////////////////////////////////
            binding.score.text = "${++scoreCounter}"
            ///////////
            if (isWin()) {
                Toast.makeText(requireContext(), "You won ! ! !", Toast.LENGTH_SHORT).show()
                shared.setResult(true)
                shared.setWinScore(scoreCounter)
                shared.setTimerCount(timeCounter)
                timer.cancel()
                winDialog.show(requireContext(),scoreCounter,timeCounter)
                winDialog.setOnClickListener {
                    if (mInterstitialAd != null){
                        adType = 3
                        mInterstitialAd?.show(requireActivity())
                    }else{
                        backFragment()
                    }
                }
            }
        }
    }

    private fun isWin(): Boolean {
        if (passiveCoordinate.x != 3 && passiveCoordinate.y != 3) return false
        var isTrue = true
        for (i in 0..14) {
            isTrue = isTrue && "${i + 1}" == buttons[(i) / 4][(i) % 4].text.toString()
        }
        return isTrue
    }

    override fun onStop() {
        val numbers = ArrayList<Int>()
        for (i in 0 until binding.btnParent.childCount) {
            val d = (binding.btnParent.getChildAt(i) as AppCompatButton).text.toString()
            if (d.isEmpty()) {
                numbers.add(-1)
            } else {
                numbers.add(d.toInt())
            }
        }
        shared.setX(passiveCoordinate.x)
        shared.setY(passiveCoordinate.y)
        shared.setAllNumbers(numbers)
        shared.setTimerCount(timeCounter)
        shared.setScoreCount(scoreCounter)

        shared.setResume(true)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        // ca-app-pub-3940256099942544/1033173712 simple code
        // ca-app-pub-4664801446868642/5705342964 my code
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-4664801446868642/5705342964",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    ad()
                }
            })
    }

    private fun ad() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                when (adType) {
                    1 -> {
                        backFragment()
                    }
                    2 -> {
                        numbers.clear()
                        numbers = ArrayList()
                        timeCounter = 0
                        loadAllViews()
                        loadNumbers()
                        shuffle()
                        loadDataToView()
                    }
                    3 -> {
                        backFragment()
                    }
                }
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
            }
        }
    }
}