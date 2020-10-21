package com.cdek.courier.ui.start.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.cdek.courier.R
import com.cdek.courier.databinding.FragmentAuthBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AuthFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var list: List<String> = emptyList()

    private val viewModel by lazy {
        ViewModelProviders.of(this@AuthFragment, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAuthBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Если есть прошлая удачная авторизация, вносим логин
        viewModel.checkLastAuth()

        //change focus
        editTextLogin.setOnItemClickListener { parent, view, position, id -> editTextPassword.requestFocus() }

        viewModel.getUsers()?.observe(this@AuthFragment, Observer { users ->

            editTextLogin.setAdapter(
                activity?.let {
                    ArrayAdapter<String>(
                        it,
                        android.R.layout.simple_dropdown_item_1line,
                        users
                    )
                })

            if (users.contains(editTextLogin.text.toString())) {
                editTextLogin.dismissDropDown()
            } else {
                editTextLogin.showDropDown()
            }
        })

        initLoginListener()

        initObserve()
    }

    @SuppressLint("CheckResult")
    private fun initLoginListener() {
        RxTextView.textChangeEvents(editTextLogin)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { textViewTextChangeEvent -> textViewTextChangeEvent.text().toString() }
            .filter { text -> text.length >= 3 }
            .subscribe { viewModel.doSearchUsers() }
    }

    private fun initObserve() {
        viewModel.navigateToBase.observe(this@AuthFragment, Observer {
            if (it) {
                viewModel.navigateToBaseHandled()
                startBaseActivity()
            }
        })

        viewModel.message.observe(this@AuthFragment, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    private fun startBaseActivity() {
        findNavController().navigate(R.id.action_authFragment_to_baseActivity)
        activity?.finish()
    }
}