package com.yatsenko.testhelper.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.yatsenko.testhelper.R

open class BaseFragment : Fragment() {

    @LayoutRes
    protected open var layoutRes: Int = R.layout.view_empty

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun navigateTo(directions: NavDirections) {
        findNavController().navigate(directions)
    }

    protected fun navigateUp() {
        findNavController().navigateUp()
    }

    protected fun setupNavigateUpView(view: View?) {
        view?.setOnClickListener {
            navigateUp()
        }
    }
}