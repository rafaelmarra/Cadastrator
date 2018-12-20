package com.marrapps.cadastrator.fragments


import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.marrapps.cadastrator.MainActivity
import com.marrapps.cadastrator.R
import com.marrapps.cadastrator.adapters.AdapterListEntry
import com.marrapps.cadastrator.database.DbWorkerThread
import com.marrapps.cadastrator.database.EntryDatabase
import com.marrapps.cadastrator.interfaces.ListClickListener
import com.marrapps.cadastrator.model.Entry
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment(), ListClickListener {

    private lateinit var onFabClickListener: OnFabClickListener
    private var entryDatabase: EntryDatabase? = null
    lateinit var fab: FloatingActionButton
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val uiHandler = Handler()
    private val listAdapter = AdapterListEntry(emptyList(), this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val aContext: Context = this.context ?: requireContext()

        entryDatabase = EntryDatabase.getInstance(aContext)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        fab = view.fab
        fab.setOnClickListener { onFabClickListener.onFabClick() }

        val viewManager = LinearLayoutManager(aContext)
        view.recyclerViewList.apply {
            layoutManager = viewManager
            adapter = listAdapter
            setHasFixedSize(true)
        }

        return view
    }

    interface OnFabClickListener {

        fun onFabClick()
    }


    fun setOnFabClickListener(activity: Activity) {
        onFabClickListener = activity as OnFabClickListener
    }

    override fun onItemClick(view: View, position: Int) {
        val activity = activity as MainActivity
        activity.onListClick(listAdapter.entryList[position])
    }

    override fun onResume() {
        /*val task = Runnable {
            val entryList = entryDatabase?.entryDao()?.getAll()
            uiHandler.post {
                if (entryList != null) {
                    listAdapter.update(entryList)
                }
            }
        }
        mDbWorkerThread.postTask(task)*/

        Thread(Runnable {
            entryDatabase?.entryDao()?.getAllLiveData()?.observe(this,
                Observer<List<Entry>> { entryList ->
                    activity?.runOnUiThread {
                        if (entryList != null) {
                            listAdapter.update(entryList)
                        }
                    }
                })
        }).start()

        super.onResume()
    }

    override fun onDestroy() {
        EntryDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }
}
