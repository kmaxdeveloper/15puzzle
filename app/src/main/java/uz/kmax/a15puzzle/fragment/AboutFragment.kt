package uz.kmax.a15puzzle.fragment

import uz.kmax.a15puzzle.databinding.LayoutAboutBinding

class AboutFragment : BaseFragment<LayoutAboutBinding>(LayoutAboutBinding::inflate) {
    override fun onViewCreated() {
        binding.back.setOnClickListener {
            backFragment()
        }
    }
}