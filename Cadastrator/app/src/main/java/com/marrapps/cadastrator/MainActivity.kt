package com.marrapps.cadastrator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.marrapps.cadastrator.fragments.EditFragment
import com.marrapps.cadastrator.fragments.EntryFragment
import com.marrapps.cadastrator.fragments.ListFragment
import com.marrapps.cadastrator.fragments.newEditFragmentInstance
import com.marrapps.cadastrator.model.Entry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), ListFragment.OnFabClickListener, EntryFragment.OnGoClickListener,
    EditFragment.OnGoClickListener {


    private val listFragment = ListFragment()
    private val entryFragment = EntryFragment()
    private val fragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fragmentManager.beginTransaction()
            .replace(container.id, listFragment)
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        if (fragment is ListFragment) {
            fragment.setOnFabClickListener(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)


        } else if (fragment is EntryFragment) {
            fragment.setOnGoClickListener(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        } else if (fragment is EditFragment) {
            fragment.setOnGoClickListener(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onFabClick() {
        fragmentManager.beginTransaction()
            .replace(container.id, entryFragment)
            .commit()
    }

    override fun onGoClick() {
        fragmentManager.beginTransaction()
            .replace(container.id, listFragment)
            .commit()
    }

    override fun onGoBackClick() {
        fragmentManager.beginTransaction()
            .replace(container.id, listFragment)
            .commit()
    }

    fun onListClick(entry: Entry) {
        val editFragment = newEditFragmentInstance(entry)
        fragmentManager.beginTransaction()
            .replace(container.id, editFragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home) {
            fragmentManager.beginTransaction()
                .replace(container.id, listFragment)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }
}
