package com.hfad.buddha

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.buddha.databinding.FragmentQuoteBinding
import com.hfad.buddha.viewmodels.QuoteViewModel
import com.hfad.buddha.viewmodels.QuoteViewModelFactory
import kotlinx.coroutines.launch

class QuoteFragment : Fragment() {

    private val viewModel: QuoteViewModel by viewModels {
        QuoteViewModelFactory(
            (activity?.application as BuddhaApplication).database.quotesDao(),
            (activity?.application as BuddhaApplication).database.settingsDao()
        )
    }

    private var _binding: FragmentQuoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val quoteAdapter = QuoteAdapter({
            val intent = Intent(Intent(context, FullQuoteActivity::class.java))
            intent.putExtra("quote", it.text)
            startActivity(intent)
        }, {
                it ->
            val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
            if(it.contains(" ")) {
                val str = it.split(" ")
                builder?.setTitle("Delete quote ${str[0]} ${str[1]}...?")
            } else
                builder?.setTitle("Delete quote $it?")
            builder?.setPositiveButton("OK") { _, _ ->
                viewModel.deleteQuote(it)
            }
            builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder?.show()
        },viewModel
        )
        recyclerView.adapter = quoteAdapter
        lifecycle.coroutineScope.launch {
            viewModel.allQuotes().collect {
                quoteAdapter.submitList(it)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}