package com.imceits.android.dbsbeeceptortask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.imceits.android.dbsbeeceptortask.R

class ArticleUpdateFragment : Fragment() {

    private lateinit var updateViewModel: UpdateViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        updateViewModel =
                ViewModelProviders.of(this).get(UpdateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_update, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        updateViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
