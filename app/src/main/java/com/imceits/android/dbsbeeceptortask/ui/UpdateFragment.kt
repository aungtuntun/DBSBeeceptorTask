package com.imceits.android.dbsbeeceptortask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.imceits.android.dbsbeeceptortask.data.AutoClearedValue
import com.imceits.android.dbsbeeceptortask.databinding.FragmentUpdateBinding
import com.imceits.android.dbsbeeceptortask.di.Injectable
import javax.inject.Inject

class UpdateFragment : Fragment(), Injectable {

    private lateinit var updateViewModel: UpdateViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by AutoClearedValue<FragmentUpdateBinding>(this)
    private val args : UpdateFragmentArgs by navArgs()
    private var articleId = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        articleId = args.arcId
        binding.lifecycleOwner = this
        val directions = UpdateFragmentDirections.actionNavUpdateToNavDetail()
        directions.articleId = this.articleId
        binding.btnCancel.setOnClickListener(Navigation.createNavigateOnClickListener(directions))
        binding.btnSave.setOnClickListener{ view ->
            updateViewModel.update(binding.data!!).observe(viewLifecycleOwner, Observer {
                if (it > 0) {
                    Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_LONG).show()
                }
                view.findNavController().navigate(directions)
            })
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateViewModel = ViewModelProvider(this, viewModelFactory).get(UpdateViewModel::class.java)
        updateViewModel.updateParam(articleId)
        updateViewModel.article.observe(viewLifecycleOwner, Observer {
            binding.data = it
        })

    }
}
