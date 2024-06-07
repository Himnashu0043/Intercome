package com.application.intercom.baseActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    protected lateinit var callback: OnBackPressedCallback
    protected lateinit var binding: B


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        lstnr()
        observer()

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                try {
                    // handle code here
                } catch (e: Exception) {
                    requireActivity().finish()
                }
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    abstract fun lstnr()

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun init()
    abstract fun observer()
}