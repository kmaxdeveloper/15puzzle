package uz.kmax.a15puzzle.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import uz.kmax.a15puzzle.R
import uz.kmax.a15puzzle.controller.FragmentController

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    private val controller = FragmentController.controller

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    abstract fun onViewCreated()

    fun replaceFragment(setFragment: Fragment) {
        controller?.replaceFragment(setFragment)
    }

    fun startMainFragment(setFragment: Fragment){
        controller?.startMainFragment(setFragment)
    }

    fun backFragment(){
        controller?.back()
    }

}
