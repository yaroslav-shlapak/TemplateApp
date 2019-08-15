package com.company.app.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.company.app.R
import com.company.app.core.AppViewModelFactory
import com.company.app.presentation.BaseFragment
import com.company.app.presentation.BaseViewModelContract
import com.company.app.presentation.MainActivity
import com.company.app.presentation.extensions.viewModelOf
import com.company.domain.model.view.main.MainViewAction
import com.company.domain.model.view.main.MainViewState
import com.company.app.core.navigation.FragmentNavigation
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.include_toolbar.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created on 2019/01/27.
 */
class MainFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    @Inject
    lateinit var navigation: FragmentNavigation
    private lateinit var viewModel: BaseViewModelContract<MainViewState, MainViewAction>

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        initViewModel()
        viewModel.apply(MainViewAction.DoSomeWork)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Timber.d("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            observeViewState(viewLifecycleOwner)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        MainActivity.setUpToolbar(view, activity, "", true)
        view?.toolbar?.setupWithNavController(findNavController(this))
        setupUi(view)
        return view
    }

    private fun setupUi(view: View) {

    }

    private fun initViewModel() {
        viewModel = this.viewModelOf(viewModelFactory, MainFragmentViewModel::class.java)
    }

    private fun observeViewState(viewLifecycleOwner: LifecycleOwner) {
        viewModel
            .viewState()
            .observe(
                viewLifecycleOwner,
                Observer { state ->
                    when (state) {

                    }

                }
            )
    }
}