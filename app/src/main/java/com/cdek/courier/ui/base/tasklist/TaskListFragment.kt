package com.cdek.courier.ui.base.tasklist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.databinding.FragmentTasklistBinding
import com.cdek.courier.ui.base.task.mapDialog.MapDialog
import com.cdek.courier.ui.base.tasklist.adapter.SimpleItemTouchHelperCallback
import com.cdek.courier.ui.base.tasklist.adapter.TaskAdapter
import com.cdek.courier.utils.TASK_NUMBER
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding2.widget.textChanges
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_tasklist.*
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TaskListFragment : DaggerFragment(), TaskAdapter.OnItemClickListener,
    AppBarLayout.OnOffsetChangedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val adapter = TaskAdapter(this@TaskListFragment)
    lateinit var toolbar: Toolbar
    lateinit var appBar: AppBarLayout
    lateinit var mItemTouchHelper: ItemTouchHelper
    lateinit var fab: FloatingActionButton

    private val taskListViewModel by lazy {
        ViewModelProviders.of(
            this@TaskListFragment,
            viewModelFactory
        )[TaskListViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        Log.d("activity1", "onResume")
        fab = activity?.findViewById(R.id.fab_map)!!
        fab.show()

    }

    override fun onPause() {
        super.onPause()
        Log.d("activity1", "onPause")
        fab = activity?.findViewById(R.id.fab_map)!!
        fab.hide()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding1: FragmentTasklistBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.fragment_tasklist, container, false)

        binding1.taskListViewModel = taskListViewModel
        return binding1.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
        appBar = activity!!.findViewById<AppBarLayout>(R.id.appBar_activity_base)
        tasksRecyclerView.adapter = adapter
        //tasksRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        val callback: ItemTouchHelper.Callback =
            SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        appBar.setExpanded(true, false)

        tasksRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("test2", "addOnScrollListener " + dy.toString())
                if (dy == 0) {
                    Log.d("test2", "addOnScrollListener " + dy.toString())
                    val l = tasksRecyclerView.layoutManager?.childCount
                    val pp = adapter.itemCount
                    if (pp > l!!) {
                        val lp: AppBarLayout.LayoutParams =
                            toolbar.layoutParams as AppBarLayout.LayoutParams
                        lp.scrollFlags =
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                        toolbar.layoutParams = lp
                        appBar.addOnOffsetChangedListener(this@TaskListFragment)

                    } else {
                        val lp: AppBarLayout.LayoutParams =
                            toolbar.layoutParams as AppBarLayout.LayoutParams
                        lp.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
                        toolbar.layoutParams = lp
                        toolbar.alpha = 1f
                        appBar.removeOnOffsetChangedListener(this@TaskListFragment)
                    }
                }
            }
        })

        taskListViewModel.message.observe(this@TaskListFragment, Observer {

            // отключаем скролл тулбара если получили ошибку (тут нужно обмозговать!!!)
            /*val layoutParams: AppBarLayout.LayoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
            layoutParams. setScrollFlags(0)
            appBar.removeOnOffsetChangedListener(this@TaskListFragment)*/

            if (it.isNotEmpty()) {
                showMessage(it)
                taskListViewModel.messageHandled()
            }
        })

        tasksRecyclerView.layoutManager = LinearLayoutManager(activity)

        taskListViewModel.getTasks().observe(this@TaskListFragment, Observer {
            if (it.isNotEmpty()) {
                adapter.setData(it)
                adapter.filterData(et_task_search.text.toString())
                //tasksRecyclerView.scrollTo(0,0) посмотрим нужно ли вызывать метод onScroll принудительно
            } else {
                taskListViewModel.getAll()
            }
        })

        iv_task_clean.setOnClickListener(View.OnClickListener {
            et_task_search.text?.clear()
        })

        et_task_search
            .textChanges()
            .skip(1)
            .map { it.toString() }
            .doOnNext {
                adapter.filterData(it) //"и́"
                //mainProgressbar.visibility = View.VISIBLE
                //mainContent.visibility = View.GONE
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.map {
                if (it.isNotBlank()) {
                    adapter.filterData(it)
                    //searchService.search(it).subscribeOn(Schedulers.io())
                } else {
                    Observable.just("Error")
                }
            }*/
            //.observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                //mainProgressbar.visibility = View.GONE
                //mainContent.visibility = View.VISIBLE
            }
            .doOnError { showMessage("Ошибка применения фильтра") }
            .retry()
            .subscribe({
                //mainContent.text = it.text
                //Log.d("MainActivity", it.text)
            }, {
                //Log.e("MainActivity", it.toString())
            })
    }

    private fun showMessage(text: String?) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(task: Task) {
        val bundle = bundleOf(TASK_NUMBER to task.basisNumber)
        findNavController().navigate(R.id.action_tasksFragment_to_taskItem, bundle)
        /*fab.animate().alpha(0f).setDuration(300).withEndAction({
            activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.action_tasksFragment_to_taskItem)
        }).start()*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        Log.d("test2", "1. scrollY " + verticalOffset.toString())
        if (Math.abs(verticalOffset) > 50)
            return
        toolbar.alpha = getAlphaForActionBar(verticalOffset)
    }

    fun getAlphaForActionBar(scrollY: Int): Float {
        if (scrollY == 0) {
            return 1f
        } else if (scrollY == -50) {
            return 0f
        }

        var alpha = 1f - ((1.0f / 50f) * Math.abs(scrollY).toFloat())
        Log.d("test2", "alpha " + alpha.toString())
        return alpha
    }
}